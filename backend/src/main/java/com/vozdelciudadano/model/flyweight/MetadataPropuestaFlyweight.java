package com.vozdelciudadano.model.flyweight;

public class MetadataPropuestaFlyweight {
    private final Long propuestaId;
    private final String titulo;

    public MetadataPropuestaFlyweight(Long propuestaId, String titulo) {
        this.propuestaId = propuestaId;
        this.titulo = titulo;
    }

    public Long getPropuestaId() { return propuestaId; }
    public String getTitulo() { return titulo; }
}