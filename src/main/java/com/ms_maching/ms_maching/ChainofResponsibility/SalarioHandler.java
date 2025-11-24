package com.ms_maching.ms_maching.ChainofResponsibility;

import com.ms_maching.ms_maching.ChainofResponsibility.Dto.SalarioMatch;

public class SalarioHandler implements MatchHandler {

    @Override
    public void handle(MatchContext context) {

        if (context.vacante.salario == null || context.usuario.salario == null) return;

        double sv = context.vacante.salario;
        double su = context.usuario.salario;

        // Nueva regla: si la vacante paga más o igual, es match
        boolean status = sv >= su;

        // Guardar detalle del match salarial
        context.salarioMatch = new SalarioMatch(sv, su, status);

        // Si cumple → sumar 10 puntos
        if (status) {
            context.score += 10;
        }
    }
}
