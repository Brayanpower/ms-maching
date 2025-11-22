package com.ms_maching.ms_maching.Websocket.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private Long conversacionId;
    private String contenido;
}
