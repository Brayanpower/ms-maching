package com.ms_maching.ms_maching.ChainofResponsibility;

import com.ms_maching.ms_maching.ChainofResponsibility.model.MatchDetalle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IdiomasHandler implements MatchHandler {

    @Override
    public void handle(MatchContext context) {

        List<String> idiomasVacante =
                context.vacante.idiomas != null ? context.vacante.idiomas : Collections.emptyList();

        List<String> idiomasUsuario =
                context.usuario.idiomas != null ? context.usuario.idiomas : Collections.emptyList();

        List<MatchDetalle> detalles = new ArrayList<>();

        for (String idioma : idiomasVacante) {
            MatchDetalle d = new MatchDetalle();
            d.nombre = idioma;
            d.status = idiomasUsuario.contains(idioma);
            detalles.add(d);
        }

        // Guardar el detalle en el contexto
        context.idiomasMatch = detalles;

        int total = idiomasVacante.size();
        if (total == 0) return;

        long match = detalles.stream().filter(d -> Boolean.TRUE.equals(d.status)).count();

        context.score += (match / (double) total) * 30;
    }
}
