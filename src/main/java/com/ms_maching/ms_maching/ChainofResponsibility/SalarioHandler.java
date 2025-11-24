package com.ms_maching.ms_maching.ChainofResponsibility;

public class SalarioHandler implements MatchHandler {

    @Override
    public void handle(MatchContext context) {

        // Evitar NullPointerException
        if (context.vacante.salario == null || context.usuario.salario == null) {
            return; // no suma puntos si no hay datos
        }

        double salarioVacante = context.vacante.salario;
        double salarioUsuario = context.usuario.salario;

        double diff = Math.abs(salarioVacante - salarioUsuario);

        // Comparación dentro del 20%
        if (diff <= salarioUsuario * 0.20) {
            context.score += 10;
        }
    }
}
