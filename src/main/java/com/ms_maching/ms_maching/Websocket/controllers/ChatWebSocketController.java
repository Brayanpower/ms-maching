package com.ms_maching.ms_maching.Websocket.controllers;

import com.ms_maching.ms_maching.Websocket.DTOs.ChatMessageRequest;
import com.ms_maching.ms_maching.Websocket.DTOs.MensajeDTO;
import com.ms_maching.ms_maching.Websocket.DTOs.WebSocketMessage;
import com.ms_maching.ms_maching.Websocket.service.MensajeService;
import com.ms_maching.ms_maching.auth.config.JwtUtil;
import com.ms_maching.ms_maching.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final MensajeService mensajeService;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Value("${jwt.secret:tu_clave_secreta}")
    private String jwtSecret;

    @MessageMapping("/chat/{conversacionId}/enviar")
    @SendTo("/topic/conversacion/{conversacionId}")
    public WebSocketMessage enviarMensaje(
            @DestinationVariable Long conversacionId,
            ChatMessageRequest request,
            Principal principal,
            StompHeaderAccessor headerAccessor) {

        try {
            Long userId = obtenerUserIdDelPrincipal(principal, headerAccessor);

            if (userId == null) {
                log.warn("UserId es null, no se puede enviar mensaje");
                return crearMensajeError("Usuario no autenticado");
            }

            String username = principal != null ? principal.getName() : "Usuario Anónimo";

            MensajeDTO mensaje = mensajeService.guardarMensaje(
                    conversacionId,
                    userId,
                    request.getContenido()
            );

            WebSocketMessage wsMessage = new WebSocketMessage();
            wsMessage.setTipo("NUEVO_MENSAJE");
            wsMessage.setMensaje(mensaje);
            wsMessage.setTimestamp(LocalDateTime.now());

            log.info("Mensaje enviado en conversación {} por usuario {}", conversacionId, username);
            return wsMessage;

        } catch (Exception e) {
            log.error("Error al enviar mensaje", e);
            return crearMensajeError("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    @MessageMapping("/chat/{conversacionId}/escribiendo")
    @SendTo("/topic/conversacion/{conversacionId}/escribiendo")
    public Map<String, Object> usuarioEscribiendo(
            @DestinationVariable Long conversacionId,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal != null) {
            response.put("usuario", principal.getName());
            response.put("escribiendo", true);
            response.put("timestamp", LocalDateTime.now());
        } else {
            log.warn("Principal es null en usuarioEscribiendo");
            response.put("usuario", "Usuario Anónimo");
            response.put("escribiendo", true);
            response.put("timestamp", LocalDateTime.now());
        }

        return response;
    }

    @MessageMapping("/chat/{conversacionId}/dejar-escribir")
    @SendTo("/topic/conversacion/{conversacionId}/escribiendo")
    public Map<String, Object> usuarioDejaEscribir(
            @DestinationVariable Long conversacionId,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal != null) {
            response.put("usuario", principal.getName());
            response.put("escribiendo", false);
            response.put("timestamp", LocalDateTime.now());
        } else {
            log.warn("Principal es null en usuarioDejaEscribir");
            response.put("usuario", "Usuario Anónimo");
            response.put("escribiendo", false);
            response.put("timestamp", LocalDateTime.now());
        }

        return response;
    }

    @MessageMapping("/chat/{conversacionId}/marcar-leido")
    public void marcarMensajeComoLeido(
            @DestinationVariable Long conversacionId,
            Long mensajeId) {

        try {
            mensajeService.marcarComoLeido(mensajeId);

            Map<String, Object> notificacion = new HashMap<>();
            notificacion.put("tipo", "MENSAJE_LEIDO");
            notificacion.put("mensajeId", mensajeId);
            notificacion.put("timestamp", LocalDateTime.now());

            messagingTemplate.convertAndSend(
                    "/topic/conversacion/" + conversacionId,
                    notificacion
            );
        } catch (Exception e) {
            log.error("Error marcando mensaje como leído", e);
        }
    }

    @MessageMapping("/chat/{conversacionId}/editar")
    @SendTo("/topic/conversacion/{conversacionId}")
    public WebSocketMessage editarMensaje(
            @DestinationVariable Long conversacionId,
            Map<String, Object> payload) {

        try {
            Long mensajeId = ((Number) payload.get("mensajeId")).longValue();
            String nuevoContenido = (String) payload.get("contenido");

            MensajeDTO mensajeEditado = mensajeService.editarMensaje(mensajeId, nuevoContenido);

            WebSocketMessage wsMessage = new WebSocketMessage();
            wsMessage.setTipo("MENSAJE_EDITADO");
            wsMessage.setMensaje(mensajeEditado);
            wsMessage.setTimestamp(LocalDateTime.now());

            return wsMessage;

        } catch (Exception e) {
            log.error("Error al editar mensaje", e);
            return crearMensajeError("Error al editar el mensaje");
        }
    }

    private WebSocketMessage crearMensajeError(String contenido) {
        WebSocketMessage error = new WebSocketMessage();
        error.setTipo("ERROR");
        error.setContent(contenido);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }

    private Long obtenerUserIdDelPrincipal(Principal principal, StompHeaderAccessor headerAccessor) {
        try {
            String username = null;

            // Opción 1: Extraer del header Authorization (recomendado para WebSocket)
            if (headerAccessor != null) {
                String authHeader = headerAccessor.getFirstNativeHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    username = extractUsernameFromJwt(token);
                    log.debug("Username extraído del JWT: {}", username);
                }
            }

            // Opción 2: Usar el principal si está disponible
            if (username == null && principal != null) {
                username = principal.getName();
                log.debug("Username extraído del Principal: {}", username);
            }

            if (username != null) {
                return userRepository.findByUsername(username)
                        .map(user -> {
                            log.debug("UserId encontrado: {}", user.getId());
                            return user.getId();
                        })
                        .orElse(null);
            }

            log.warn("No se pudo obtener userId - header: {}, principal: {}",
                    headerAccessor, principal);
            return null;

        } catch (Exception e) {
            log.error("Error obteniendo userId del principal", e);
            return null;
        }
    }

    private String extractUsernameFromJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            log.error("JWT firma inválida: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error extrayendo username del JWT: {}", e.getMessage());
            return null;
        }
    }
}