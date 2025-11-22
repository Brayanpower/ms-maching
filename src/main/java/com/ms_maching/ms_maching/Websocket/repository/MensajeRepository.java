package com.ms_maching.ms_maching.Websocket.repository;

import com.ms_maching.ms_maching.Websocket.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByConversacionIdOrderByFechaEnvioAsc(Long conversacionId);
    List<Mensaje> findByConversacionIdAndLeidoFalseAndUserIdNot(Long conversacionId, Long userId);
}