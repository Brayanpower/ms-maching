package com.ms_maching.ms_maching.ChainofResponsibility;

import com.ms_maching.ms_maching.ChainofResponsibility.model.MatchDetalle;
import com.ms_maching.ms_maching.ChainofResponsibility.Dto.SalarioMatch;
import com.ms_maching.ms_maching.ChainofResponsibility.Dto.UsuarioDto;
import com.ms_maching.ms_maching.ChainofResponsibility.Dto.VacanteDto;

import java.util.ArrayList;
import java.util.List;

public class MatchContext {

    public double score = 0;

    public UsuarioDto usuario;
    public VacanteDto vacante;

    public List<MatchDetalle> habilidadesMatch = new ArrayList<>();
    public List<MatchDetalle> idiomasMatch = new ArrayList<>();

    public MatchDetalle areaMatch;      // un solo campo
    public SalarioMatch salarioMatch;
}


