package com.vozdelciudadano.service.proxy;

import com.vozdelciudadano.model.flyweight.FirmaContexto;

//la interfaz en comun
public interface FirmaService {
    FirmaContexto registrarFirma(Long ciudadanoId, Long propuestaId, String comentario);    
}
