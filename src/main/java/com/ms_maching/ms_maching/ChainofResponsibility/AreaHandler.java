package com.ms_maching.ms_maching.ChainofResponsibility;

public class AreaHandler implements MatchHandler {
    @Override
    public void handle(MatchContext context) {
        if (context.vacante.area.equalsIgnoreCase(context.usuario.area)) {
            context.score += 20;
        }
    }
}
