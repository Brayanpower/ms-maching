package com.ms_maching.ms_maching.controllers;


import com.ms_maching.ms_maching.Dto.VacanteRequest;
import com.ms_maching.ms_maching.services.VacanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
@RestController
@RequestMapping("/api/ms_maching")
public class VacanteController {

    @Autowired
    private VacanteService vacanteService;

    @PostMapping("/maching")
    public JsonNode getVacantes(@RequestBody VacanteRequest request) {
        return vacanteService.getVacantes(request);
    }
}