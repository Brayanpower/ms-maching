package com.ms_maching.ms_maching.Websocket.repository;


import com.ms_maching.ms_maching.Websocket.model.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {
    List<Conversacion> findByUserIdOrderByFechaActualizacionDesc(Long userId);
    Optional<Conversacion> findByIdAndUserId(Long id, Long userId);
}