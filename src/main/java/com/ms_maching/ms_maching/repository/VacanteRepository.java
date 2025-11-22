package com.ms_maching.ms_maching.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class VacanteRepository {

    @PersistenceContext
    private EntityManager em;

    public String getVacantes(
            Long tipo,
            Long clienteId,
            Long salario,
            String modalidad,
            String area,
            String titulo,
            String horario
    ) {
        return (String) em.createNativeQuery(
                        "SELECT maching.vacantesmaching(:tipo,:clienteId, :salario, :modalidad, :area, :titulo, :horario)::text")
                .setParameter("tipo", tipo)
                .setParameter("clienteId", clienteId)
                .setParameter("salario", salario)
                .setParameter("modalidad", modalidad)
                .setParameter("area", area)
                .setParameter("titulo", titulo)
                .setParameter("horario", horario)
                .getSingleResult();
    }
}
