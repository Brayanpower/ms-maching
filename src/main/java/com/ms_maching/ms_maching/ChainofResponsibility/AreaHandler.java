package com.ms_maching.ms_maching.ChainofResponsibility;

import com.ms_maching.ms_maching.ChainofResponsibility.model.MatchDetalle;

public class AreaHandler implements MatchHandler {

    @Override
    public void handle(MatchContext context) {

        MatchDetalle detalle = new MatchDetalle();
        detalle.nombre = context.vacante.area;
        detalle.status = context.vacante.area != null &&
                context.usuario.area != null &&
                context.vacante.area.equalsIgnoreCase(context.usuario.area);

        // Guardar en el contexto
        context.areaMatch = detalle;

        if (Boolean.TRUE.equals(detalle.status)) {
            context.score += 20;
        }
    }
}
