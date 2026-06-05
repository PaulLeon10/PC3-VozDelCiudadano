package com.vozdelciudadano.service.proxy;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vozdelciudadano.model.composite.ElementoTexto;
import com.vozdelciudadano.model.composite.ExpedienteDigital;
import com.vozdelciudadano.model.flyweight.FirmaContexto;
import com.vozdelciudadano.model.flyweight.FirmaFlyweightCache;
import com.vozdelciudadano.model.flyweight.MetadataPropuestaFlyweight;
import com.vozdelciudadano.repository.FirmaRepository;
import com.vozdelciudadano.repository.PropuestaRepository;
import com.vozdelciudadano.repository.ElementoTextoRepository;

//una implementacion tipica de la intefaz

@Service("firmaServiceReal")
public class FirmaServiceImpl implements FirmaService{
    
    @Autowired
    private PropuestaRepository propuestaRepository;

    @Autowired
    private ElementoTextoRepository elementoTextoRepository;

    @Autowired
    private FirmaRepository firmaRepository;

    @Override
    @Transactional
    public FirmaContexto registrarFirma(Long ciudadanoId, Long propuestaId, String comentario) {
        ExpedienteDigital propuesta = propuestaRepository.findById(propuestaId)
            .orElseThrow(() -> new RuntimeException("Propuesta no encontrada"));

    //1. La aplicacion de flyweight: se extrae el estado compartido de la cache
    MetadataPropuestaFlyweight flyweight = FirmaFlyweightCache.obtenerFlyweight(propuesta.getId(), propuesta.getTitulo());
    
    FirmaContexto nuevaFirma = new FirmaContexto(ciudadanoId, propuestaId);
    nuevaFirma.setFlyweight(flyweight);

    //2. la persistencia de la firma
    firmaRepository.save(nuevaFirma);

    //3. la aplicacion de composite, donde el comentario es una hoja que se agrega al expediente
    if (comentario != null && !comentario.trim().isEmpty()) {
            ElementoTexto hojaComentario = new ElementoTexto("COMENTARIO", comentario, "Ciudadano_" + ciudadanoId, propuestaId);
            elementoTextoRepository.save(hojaComentario);
        }

    //4. El contador se actualiza
    propuesta.setContadorFirmas(propuesta.getContadorFirmas() + 1);
    propuestaRepository.save(propuesta);

    return nuevaFirma;
    }


}
