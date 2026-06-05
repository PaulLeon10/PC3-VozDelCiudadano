package com.vozdelciudadano.repository;

import com.vozdelciudadano.model.composite.ElementoTexto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementoTextoRepository extends JpaRepository<ElementoTexto, Long> {
    List<ElementoTexto> findByPropuestaId(Long propuestaId);
}