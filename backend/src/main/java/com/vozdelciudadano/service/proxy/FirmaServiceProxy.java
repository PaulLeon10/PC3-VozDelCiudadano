package com.vozdelciudadano.service.proxy;

import com.vozdelciudadano.model.flyweight.FirmaContexto;
import com.vozdelciudadano.repository.FirmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("firmaServiceProxy")
public class FirmaServiceProxy implements FirmaService {

    // El Proxy guarda una referencia al objeto real
    @Autowired
    @Qualifier("firmaServiceReal")
    private FirmaService firmaServiceReal;

    @Autowired
    private FirmaRepository firmaRepository;

    @Override
    public FirmaContexto registrarFirma(Long ciudadanoId, Long propuestaId, String comentario) {
        
        // 1. Control de seguridad del Proxy pregunta si ya hay esa combinacion en la base de datos
        boolean yaFirmo = firmaRepository.existsByCiudadanoIdAndPropuestaId(ciudadanoId, propuestaId);
        
        if (yaFirmo) {
            // El flujo se detiene si es un duplicado
            throw new IllegalArgumentException("Acceso Denegado: El ciudadano " + ciudadanoId + " ya firmó esta propuesta.");
        }

        // 2. Si es una firma nueva, el Proxy delega la ejecución al objeto real
        return firmaServiceReal.registrarFirma(ciudadanoId, propuestaId, comentario);
    }
}