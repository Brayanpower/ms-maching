package com.ms_maching.ms_maching.ChainofResponsibility.Dto;

public class SalarioMatch {
    public Double vacante_salario;
    public Double usuario_salario;
    public Boolean status;

    public SalarioMatch(Double vacSal, Double usrSal, Boolean st) {
        this.vacante_salario = vacSal;
        this.usuario_salario = usrSal;
        this.status = st;
    }
}
