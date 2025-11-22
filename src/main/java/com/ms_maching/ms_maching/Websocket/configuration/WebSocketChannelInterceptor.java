package com.ms_maching.ms_maching.Websocket.configuration;


import com.ms_maching.ms_maching.auth.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Obtener el header de Authorization
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);

                try {
                    // Validar el token
                    if (jwtUtil.validateToken(token)) {
                        // Extraer username del token
                        String username = jwtUtil.extractUsername(token);
                        List<String> roles = jwtUtil.extractRoles(token);

                        // Crear lista de autoridades
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        if (roles != null) {
                            roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                        }

                        // Crear autenticación
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                authorities
                        );

                        // Establecer en el contexto de seguridad
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(authentication);

                        log.info("Usuario {} autenticado en WebSocket", username);
                    } else {
                        log.warn("Token JWT inválido");
                    }
                } catch (Exception e) {
                    log.error("Error validando token JWT en WebSocket: {}", e.getMessage());
                }
            } else {
                log.warn("No se encontró header Authorization en WebSocket");
            }
        }

        return message;
    }
}