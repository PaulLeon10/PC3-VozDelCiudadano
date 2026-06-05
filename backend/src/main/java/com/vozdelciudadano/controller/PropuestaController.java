package com.vozdelciudadano.controller;

import com.vozdelciudadano.model.composite.ElementoTexto;
import com.vozdelciudadano.model.composite.ExpedienteDigital;
import com.vozdelciudadano.repository.ElementoTextoRepository;
import com.vozdelciudadano.repository.PropuestaRepository;
import com.vozdelciudadano.service.facade.FachadaDespachoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/propuestas")
@CrossOrigin(origins = "*")
public class PropuestaController {

    @Autowired
    private PropuestaRepository propuestaRepository;

    @Autowired
    private ElementoTextoRepository elementoTextoRepository;

    @Autowired
    private FachadaDespachoService fachadaDespachoService;

    //CU-01: Registrar propuesta normativa y recursos
    @PostMapping
    public ResponseEntity<ExpedienteDigital> crearPropuesta(@RequestBody Map<String, String> payload) {
        String titulo = payload.get("titulo");
        String textoBase = payload.get("textoBase");

        if (titulo == null || textoBase == null || titulo.trim().isEmpty() || textoBase.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Crear la raíz del Composite
        ExpedienteDigital propuesta = new ExpedienteDigital(titulo);
        ExpedienteDigital guardada = propuestaRepository.save(propuesta);

        // Crear y anidar la primera hoja (Texto Legal Base)
        ElementoTexto elementoBase = new ElementoTexto("BASE", textoBase, "Colectivo Civil", guardada.getId());
        elementoTextoRepository.save(elementoBase);

        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    //Listar propuestas para el catálogo del ciudadano
    @GetMapping
    public ResponseEntity<List<ExpedienteDigital>> listarPropuestas() {
        return ResponseEntity.ok(propuestaRepository.findAll());
    }

    // CU-03 y CU-04: control de plazo o llegada a la meta de 25 mil  firmas
    @PostMapping("/{id}/simular-cierre")
    public ResponseEntity<?> simularCierreCriptografico(@PathVariable Long id) {
        try {
            ExpedienteDigital expedienteSellado = fachadaDespachoService.ejecutarCierreYDistribucion(id);
            return ResponseEntity.ok(expedienteSellado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}