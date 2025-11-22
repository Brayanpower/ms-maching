package com.ms_maching.ms_maching.Websocket.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
    private String tipo; // "NUEVO_MENSAJE", "MENSAJE_EDITADO", "USUARIO_CONECTADO", etc
    private MensajeDTO mensaje;
    private String content;
    private LocalDateTime timestamp;
}