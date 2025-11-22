package com.ms_maching.ms_maching.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ms_maching.ms_maching.Dto.VacanteRequest;
import com.ms_maching.ms_maching.repository.VacanteRepository;

@Service
public class VacanteService {

    @Autowired
    private VacanteRepository vacanteRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Transactional
    public JsonNode getVacantes(VacanteRequest request) {
        // Normalizar valores vacíos como null
        String modalidad = (request.getModalidad() == null || request.getModalidad().isEmpty()) ? null : request.getModalidad();
        String area = (request.getArea() == null || request.getArea().isEmpty()) ? null : request.getArea();
        String titulo = (request.getTitulo() == null || request.getTitulo().isEmpty()) ? null : request.getTitulo();
        String horario = (request.getHorario() == null || request.getHorario().isEmpty()) ? null : request.getHorario();

        String json = vacanteRepository.getVacantes(
                request.getTipo(),
                request.getClienteId(),
                request.getSalario(),
                modalidad,
                area,
                titulo,
                horario
        );

        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir el JSON devuelto por la función", e);
        }
    }

}
