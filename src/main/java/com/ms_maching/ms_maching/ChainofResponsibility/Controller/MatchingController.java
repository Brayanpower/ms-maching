package com.ms_maching.ms_maching.ChainofResponsibility.Controller;

import com.ms_maching.ms_maching.ChainofResponsibility.Repository.MatchingRepository;
import com.ms_maching.ms_maching.ChainofResponsibility.Service.MatchService;
import com.ms_maching.ms_maching.Dto.MatchDataResponse;
import com.ms_maching.ms_maching.Dto.VacanteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matching")
public class MatchingController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchingRepository repo;

    @GetMapping("/chain")
    public List<Map<String, Object>> matchUsuario(@RequestParam Long clienteId) {

        MatchDataResponse data = repo.obtenerDatosMatching(clienteId);

        List<Map<String, Object>> result = new ArrayList<>();

        for (VacanteDto vacante : data.vacantes) {
            double score = matchService.calcularMatch(data.usuario, vacante);

            result.add(Map.of(
                    "vacante", vacante,
                    "score", score
            ));
        }

        return result;
    }
}

