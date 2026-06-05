package com.vozdelciudadano.repository;

import com.vozdelciudadano.model.flyweight.FirmaContexto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmaRepository extends JpaRepository<FirmaContexto, Long> {
    boolean existsByCiudadanoIdAndPropuestaId(Long ciudadanoId, Long propuestaId);
}