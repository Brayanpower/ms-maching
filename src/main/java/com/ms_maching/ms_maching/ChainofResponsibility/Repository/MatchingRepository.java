package com.ms_maching.ms_maching.ChainofResponsibility.Repository;

import com.ms_maching.ms_maching.Dto.MatchDataResponse;

public interface MatchingRepository {
    /**
     * Llama a la función PL/pgSQL vacantesmaching con p_tipo = 6
     * y mapea el JSON resultante a MatchDataResponse.
     *
     * @param clienteId id_userauth del usuario
     * @return MatchDataResponse con usuario y lista de vacantes
     */
    MatchDataResponse obtenerDatosMatching(Long clienteId);
}
