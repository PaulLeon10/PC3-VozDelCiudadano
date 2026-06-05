package com.vozdelciudadano.repository;

import com.vozdelciudadano.model.composite.ExpedienteDigital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropuestaRepository extends JpaRepository<ExpedienteDigital, Long> {
    List<ExpedienteDigital> findByEstado(String estado);
}