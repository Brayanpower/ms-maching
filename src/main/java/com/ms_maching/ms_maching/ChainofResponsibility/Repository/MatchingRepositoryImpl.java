package com.ms_maching.ms_maching.ChainofResponsibility.Repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_maching.ms_maching.Dto.MatchDataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MatchingRepositoryImpl implements MatchingRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(MatchingRepositoryImpl.class);

    public MatchingRepositoryImpl(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public MatchDataResponse obtenerDatosMatching(Long clienteId) {
        // SQL que llama a tu función. Asegúrate que el schema y nombre coincidan.
        // usamos p_tipo = 2 (según lo acordado) y solo pasamos p_cliente_id
        final String sql = "SELECT maching.vacantesmaching(6, ?, NULL, NULL, NULL, NULL, NULL)::text as resultado_json";

        try {
            String json = jdbcTemplate.queryForObject(sql, new Object[]{clienteId}, String.class);
            System.out.println(json);
            // Si empieza con "[" significa que es un array → lo envolvemos en un objeto
            if (json.trim().startsWith("[")) {

                json = "{ \"vacantes\": " + json + " }";

                // usuario queda null porque tu función actual NO devuelve usuario
            }


            // Tu función con p_tipo=6 retorna un json_build_object;
            // dependiendo de cómo lo estés devolviendo puede ser un array o un objeto.
            // Asumimos el objeto que contiene "usuario" y "vacantes".
            MatchDataResponse response = objectMapper.readValue(json, MatchDataResponse.class);
            return response;

        } catch (EmptyResultDataAccessException ex) {
            log.warn("No se encontraron datos para clienteId={}", clienteId);
            return new MatchDataResponse();
        } catch (JsonProcessingException ex) {
            log.error("Error parseando JSON devuelto por vacantesmaching para clienteId={}: {}", clienteId, ex.getMessage());
            throw new RuntimeException("Error parseando JSON de matching", ex);
        } catch (Exception ex) {
            log.error("Error al invocar vacantesmaching para clienteId={}: {}", clienteId, ex.getMessage());
            throw new RuntimeException("Error al obtener datos de matching", ex);
        }
    }
}

