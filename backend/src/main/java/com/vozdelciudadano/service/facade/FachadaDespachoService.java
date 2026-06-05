package com.vozdelciudadano.service.facade;

import com.vozdelciudadano.model.composite.ElementoTexto;
import com.vozdelciudadano.model.composite.ExpedienteDigital;
import com.vozdelciudadano.repository.ElementoTextoRepository;
import com.vozdelciudadano.repository.PropuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FachadaDespachoService {

    @Autowired
    private PropuestaRepository propuestaRepository;

    @Autowired
    private ElementoTextoRepository elementoTextoRepository;

    @Transactional
    public ExpedienteDigital ejecutarCierreYDistribucion(Long propuestaId) {
        // 1. Se recupera la propuesta base
        ExpedienteDigital propuesta = propuestaRepository.findById(propuestaId)
                .orElseThrow(() -> new RuntimeException("Propuesta no encontrada"));
                
                
        // Caso de uso 4

        // Paso 2: Bloqueo de seguridad (Cambio de estado inmediato a CONGELADA)
        propuesta.setEstado("CONGELADA");
        propuestaRepository.saveAndFlush(propuesta);

        // Paso 3: Reconstrucción de Composite cargando sus hojas (Anexos, comentarios)
        List<ElementoTexto> hojas = elementoTextoRepository.findByPropuestaId(propuestaId);
        for (ElementoTexto hoja : hojas) {
            propuesta.agregarElemento(hoja);
        }

        // Se extrae la cadena de texto lineal unificada desde el Composite
        String contenidoCompleto = propuesta.obtenerContenido();

        // Paso 4 y 5: Cierre criptográfico y persistencia del sello
        String hashGenerado = UtilidadCriptografica.calcularSHA256(contenidoCompleto);
        propuesta.setHashSeguridad(hashGenerado); // Se guarda la huella digital inmutable

        // Paso 6 y 7: Distribución y actualización final
        propuesta.setEstado("DISTRIBUIDA_A_COMISIONES");
        
        return propuestaRepository.save(propuesta);
    }
}