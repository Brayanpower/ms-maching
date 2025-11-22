package com.ms_maching.ms_maching.auth.repository;

import com.ms_maching.ms_maching.auth.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresasRepositor  extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByNombre(String nombre);
}