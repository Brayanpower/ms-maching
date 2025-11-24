package com.ms_maching.ms_maching.Dto;

import java.util.List;

public class MatchDetailResponse {
    public VacanteDto vacante;
    public double score;

    public List<MatchItem> habilidades;
    public List<MatchItem> idiomas;

    public MatchItem area_match;
    public SalarioMatch salario_match;
}
