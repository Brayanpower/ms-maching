package com.ms_maching.ms_maching.Websocket.service;

import com.ms_maching.ms_maching.Websocket.DTOs.MensajeDTO;
import com.ms_maching.ms_maching.Websocket.model.Conversacion;
import com.ms_maching.ms_maching.Websocket.model.Mensaje;
import com.ms_maching.ms_maching.Websocket.repository.ConversacionRepository;
import com.ms_maching.ms_maching.Websocket.repository.MensajeRepository;
import com.ms_maching.ms_maching.auth.model.User;
import com.ms_maching.ms_maching.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final ConversacionRepository conversacionRepository;
    private final UserRepository userRepository;

    public MensajeDTO guardarMensaje(Long conversacionId, Long userId, String contenido) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mensaje mensaje = new Mensaje();
        mensaje.setConversacion(conversacion);
        mensaje.setUser(user);
        mensaje.setContenido(contenido);
        mensaje.setLeido(false);

        Mensaje guardado = mensajeRepository.save(mensaje);
        conversacion.setFechaActualizacion(LocalDateTime.now());
        conversacionRepository.save(conversacion);

        return mensajeToDTO(guardado);
    }

    public MensajeDTO editarMensaje(Long mensajeId, String nuevoContenido) {
        Mensaje mensaje = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));

        mensaje.setContenido(nuevoContenido);
        mensaje.setFechaEdicion(LocalDateTime.now());

        Mensaje actualizado = mensajeRepository.save(mensaje);
        return mensajeToDTO(actualizado);
    }

    public void marcarComoLeido(Long mensajeId) {
        Mensaje mensaje = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        mensaje.setLeido(true);
        mensajeRepository.save(mensaje);
    }

    private MensajeDTO mensajeToDTO(Mensaje mensaje) {
        MensajeDTO dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        dto.setConversacionId(mensaje.getConversacion().getId());
        dto.setUserId(mensaje.getUser().getId());
        dto.setUsername(mensaje.getUser().getUsername());
        dto.setContenido(mensaje.getContenido());
        dto.setFechaEnvio(mensaje.getFechaEnvio());
        dto.setFechaEdicion(mensaje.getFechaEdicion());
        dto.setLeido(mensaje.getLeido());
        return dto;
    }
}