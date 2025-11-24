package com.ms_maching.ms_maching.ChainofResponsibility;

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

        if (total == 0) return; // Nada que evaluar

        long match = habilidadesVacante.stream()
                .filter(habilidadesUsuario::contains)
                .count();

        double porcentaje = (match / (double) total);

        // Peso asignado a habilidades = 40 puntos
        context.score += porcentaje * 40;
    }
}
