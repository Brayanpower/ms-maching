package com.ms_maching.ms_maching.ChainofResponsibility;

import com.ms_maching.ms_maching.ChainofResponsibility.model.MatchDetalle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HabilidadesHandler implements MatchHandler {

    @Override
    public void handle(MatchContext context) {

        // Asegurar que no sean null
        List<String> habilidadesVacante =
                context.vacante.habilidades != null ? context.vacante.habilidades : Collections.emptyList();

        List<String> habilidadesUsuario =
                context.usuario.habilidades != null ? context.usuario.habilidades : Collections.emptyList();

        int total = habilidadesVacante.size();

        // Crear lista de detalles
        List<MatchDetalle> detalles = new ArrayList<>();

        for (String habilidadVacante : habilidadesVacante) {

            MatchDetalle d = new MatchDetalle();
            d.nombre = habilidadVacante;
            d.status = habilidadesUsuario.contains(habilidadVacante);

            detalles.add(d);
        }

        // Guardar en el contexto
        context.habilidadesMatch = detalles;

        if (total == 0) return; // Nada que evaluar

        long match = detalles.stream()
                .filter(det -> Boolean.TRUE.equals(det.status))
                .count();

        double porcentaje = (match / (double) total);

        // Peso asignado a habilidades = 40 puntos
        context.score += porcentaje * 40;
    }
}
