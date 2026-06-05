package com.vozdelciudadano.model.composite;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "elementos_texto")
public class ElementoTexto implements ComponentePropuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo; // "BASE", "ANEXO", "COMENTARIO", "MODIFICACION"
    
    @Column(columnDefinition = "TEXT")
    private String contenido;
    
    private String autor;
    private LocalDateTime fechaCreacion;

    @Column(name = "propuesta_id")
    private Long propuestaId;

    public ElementoTexto() {}

    public ElementoTexto(String tipo, String contenido, String autor, Long propuestaId) {
        this.tipo = tipo;
        this.contenido = contenido;
        this.autor = autor;
        this.propuestaId = propuestaId;
        this.fechaCreacion = LocalDateTime.now();
    }

    @Override
    public String obtenerContenido() {
        return "[" + tipo + "] por " + autor + " (" + fechaCreacion + "): " + contenido + "\n";
    }

    // Getters y Setters para JPA
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public Long getPropuestaId() { return propuestaId; }
    public void setPropuestaId(Long propuestaId) { this.propuestaId = propuestaId; }
}