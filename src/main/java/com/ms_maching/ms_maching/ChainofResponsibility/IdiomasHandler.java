package com.ms_maching.ms_maching.ChainofResponsibility;

import java.util.Collections;
import java.util.List;

public class IdiomasHandler implements MatchHandler {

    @Override
    public void handle(MatchContext context) {

        // Asegurar que NO sean null
        List<String> idiomasVacante =
                context.vacante.idiomas != null ? context.vacante.idiomas : Collections.emptyList();

        List<String> idiomasUsuario =
                context.usuario.idiomas != null ? context.usuario.idiomas : Collections.emptyList();

        int total = idiomasVacante.size();

        if (total == 0) return; // si la vacante no pide idiomas no suma

        long match = idiomasVacante.stream()
                .filter(idiomasUsuario::contains)
                .count();

        context.score += (match / (double) total) * 30; // 30 = tu peso configurado
    }
}
