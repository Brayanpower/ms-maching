    package com.ms_maching.ms_maching.ChainofResponsibility.Controller;

    import com.ms_maching.ms_maching.ChainofResponsibility.MatchContext;
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
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/api/matching")
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
                MatchContext context = matchService.calcularMatch(data.usuario, vacante);

                Map<String, Object> item = new HashMap<>();
                item.put("vacante", vacante);
                item.put("porcentaje_match", context.score);
                item.put("habilidades", context.habilidadesMatch);
                item.put("idiomas", context.idiomasMatch);
                item.put("area_match", context.areaMatch);
                item.put("salario_match", context.salarioMatch);

                item.put("match_competencia", vacante.match_competencia);

                result.add(item);
            }

            // ⬇⬇⬇ ORDENAR DE MAYOR A MENOR ⬇⬇⬇
            result.sort((a, b) -> {
                double scoreA = (double) a.get("porcentaje_match");
                double scoreB = (double) b.get("porcentaje_match");
                return Double.compare(scoreB, scoreA); // DESC
            });

            return result;
        }

    }

