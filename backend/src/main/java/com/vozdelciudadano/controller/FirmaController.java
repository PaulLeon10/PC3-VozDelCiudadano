package com.vozdelciudadano.controller;

import com.vozdelciudadano.model.flyweight.FirmaContexto;
import com.vozdelciudadano.service.proxy.FirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/firmas")
@CrossOrigin(origins = "*")
public class FirmaController {

    @Autowired
    @Qualifier("firmaServiceProxy") //para que se haga uso del Patrón Proxy
    private FirmaService firmaService;

    //CU-02: Registrar participación y firma ciudadana
    @PostMapping
    public ResponseEntity<?> registrarFirma(@RequestBody Map<String, Object> payload) {
        try {
            Long ciudadanoId = Long.valueOf(payload.get("ciudadanoId").toString());
            Long propuestaId = Long.valueOf(payload.get("propuestaId").toString());
            String comentario = (String) payload.get("comentario");

            FirmaContexto firma = firmaService.registrarFirma(ciudadanoId, propuestaId, comentario);
            return ResponseEntity.status(HttpStatus.CREATED).body(firma);
            
        } catch (IllegalArgumentException e) {
            // se captura el bloqueo del Proxy (en caso de Firma Duplicada)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", 400, "error", e.getMessage()));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}