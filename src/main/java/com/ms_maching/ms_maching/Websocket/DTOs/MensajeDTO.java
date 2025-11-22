package com.ms_maching.ms_maching.Websocket.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeDTO {
    private Long id;
    private Long conversacionId;
    private Long userId;
    private String username;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEdicion;
    private Boolean leido;
}