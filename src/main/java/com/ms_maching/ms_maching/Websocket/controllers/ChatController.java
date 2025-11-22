package com.ms_maching.ms_maching.Websocket.controllers;

import com.ms_maching.ms_maching.Websocket.DTOs.ConversacionDTO;
import com.ms_maching.ms_maching.Websocket.service.ConversacionService;
import com.ms_maching.ms_maching.Websocket.service.MensajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final ConversacionService conversacionService;
    private final MensajeService mensajeService;

    @GetMapping("/conversaciones")
    public ResponseEntity<List<ConversacionDTO>> obtenerConversaciones(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = obtenerUserIdDelUserDetails(userDetails);
        return ResponseEntity.ok(conversacionService.obtenerConversacionesDelUsuario(userId));
    }

    @PostMapping("/conversaciones")
    public ResponseEntity<ConversacionDTO> crearConversacion(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String nombre) {
        Long userId = obtenerUserIdDelUserDetails(userDetails);
        ConversacionDTO nueva = conversacionService.crearConversacion(userId, nombre);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @GetMapping("/conversaciones/{id}")
    public ResponseEntity<ConversacionDTO> obtenerConversacion(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = obtenerUserIdDelUserDetails(userDetails);
        return ResponseEntity.ok(conversacionService.obtenerConversacion(id, userId));
    }

    @DeleteMapping("/conversaciones/{id}")
    public ResponseEntity<Void> eliminarConversacion(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = obtenerUserIdDelUserDetails(userDetails);
        conversacionService.eliminarConversacion(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/conversaciones/{id}")
    public ResponseEntity<ConversacionDTO> actualizarNombre(
            @PathVariable Long id,
            @RequestParam String nombre,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = obtenerUserIdDelUserDetails(userDetails);
        ConversacionDTO actualizada = conversacionService.actualizarNombre(id, userId, nombre);
        return ResponseEntity.ok(actualizada);
    }

    private Long obtenerUserIdDelUserDetails(UserDetails userDetails) {
        // Implementar según tu sistema de autenticación
        return 1L; // Placeholder
    }
}