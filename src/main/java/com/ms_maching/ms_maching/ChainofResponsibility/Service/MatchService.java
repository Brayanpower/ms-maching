package com.ms_maching.ms_maching.ChainofResponsibility.Service;

import com.ms_maching.ms_maching.ChainofResponsibility.*;
import com.ms_maching.ms_maching.Dto.UsuarioDto;
import com.ms_maching.ms_maching.Dto.VacanteDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final List<MatchHandler> handlers = List.of(
            new HabilidadesHandler(),
            new IdiomasHandler(),
            new AreaHandler(),
            new SalarioHandler()
    );

    public MatchContext calcularMatch(UsuarioDto usuario, VacanteDto vacante) {

        MatchContext context = new MatchContext();
        context.usuario = usuario;
        context.vacante = vacante;

        for (MatchHandler handler : handlers) {
            handler.handle(context);
        }

        return context; // ← YA NO DOUBLE
    }
}


