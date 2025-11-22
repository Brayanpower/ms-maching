package com.ms_maching.ms_maching.Websocket.service;

import com.ms_maching.ms_maching.Websocket.DTOs.ConversacionDTO;
import com.ms_maching.ms_maching.Websocket.DTOs.MensajeDTO;
import com.ms_maching.ms_maching.Websocket.model.Conversacion;
import com.ms_maching.ms_maching.Websocket.model.Mensaje;
import com.ms_maching.ms_maching.Websocket.repository.ConversacionRepository;
import com.ms_maching.ms_maching.Websocket.repository.MensajeRepository;
import com.ms_maching.ms_maching.auth.model.User;
import com.ms_maching.ms_maching.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversacionService {

    private final ConversacionRepository conversacionRepository;
    private final UserRepository userRepository;

    public List<ConversacionDTO> obtenerConversacionesDelUsuario(Long userId) {
        return conversacionRepository.findByUserIdOrderByFechaActualizacionDesc(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ConversacionDTO crearConversacion(Long userId, String nombre) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Conversacion conversacion = new Conversacion();
        conversacion.setNombre(nombre);
        conversacion.setUser(user);

        Conversacion guardada = conversacionRepository.save(conversacion);
        return toDTO(guardada);
    }

    public ConversacionDTO obtenerConversacion(Long conversacionId, Long userId) {
        Conversacion conversacion = conversacionRepository.findByIdAndUserId(conversacionId, userId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));
        return toDTO(conversacion);
    }

    public void eliminarConversacion(Long conversacionId, Long userId) {
        Conversacion conversacion = conversacionRepository.findByIdAndUserId(conversacionId, userId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));
        conversacionRepository.delete(conversacion);
    }

    public ConversacionDTO actualizarNombre(Long conversacionId, Long userId, String nuevoNombre) {
        Conversacion conversacion = conversacionRepository.findByIdAndUserId(conversacionId, userId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));
        conversacion.setNombre(nuevoNombre);
        Conversacion actualizada = conversacionRepository.save(conversacion);
        return toDTO(actualizada);
    }

    private ConversacionDTO toDTO(Conversacion conversacion) {
        ConversacionDTO dto = new ConversacionDTO();
        dto.setId(conversacion.getId());
        dto.setNombre(conversacion.getNombre());
        dto.setFechaCreacion(conversacion.getFechaCreacion());
        dto.setFechaActualizacion(conversacion.getFechaActualizacion());
        dto.setMensajes(conversacion.getMensajes().stream()
                .map(this::mensajeToDTO)
                .collect(Collectors.toList()));
        return dto;
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