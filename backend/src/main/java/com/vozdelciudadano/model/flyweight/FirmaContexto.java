package com.vozdelciudadano.model.flyweight;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "firmas")
public class FirmaContexto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ciudadanoId;
    private Long propuestaId;
    private LocalDateTime fechaFirma;

    @Transient
    private MetadataPropuestaFlyweight flyweight;

    public FirmaContexto() {}

    public FirmaContexto(Long ciudadanoId, Long propuestaId) {
        this.ciudadanoId = ciudadanoId;
        this.propuestaId = propuestaId;
        this.fechaFirma = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCiudadanoId() { return ciudadanoId; }
    public void setCiudadanoId(Long ciudadanoId) { this.ciudadanoId = ciudadanoId; }
    public Long getPropuestaId() { return propuestaId; }
    public void setPropuestaId(Long propuestaId) { this.propuestaId = propuestaId; }
    public LocalDateTime getFechaFirma() { return fechaFirma; }
    public void setFechaFirma(LocalDateTime fechaFirma) { this.fechaFirma = fechaFirma; }
    public MetadataPropuestaFlyweight getFlyweight() { return flyweight; }
    public void setFlyweight(MetadataPropuestaFlyweight flyweight) { this.flyweight = flyweight; }
}