package com.vozdelciudadano.model.composite;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "propuestas")
public class ExpedienteDigital implements ComponentePropuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String estado; // "ACTIVA", "EXPIRADA", "CONGELADA", "DISTRIBUIDA_A_COMISIONES"
    private int contadorFirmas;
    private LocalDateTime fechaExpiracion;
    private String hashSeguridad;

    @Transient // JPA ignora este campo en  mapeos simples, asi que se cargara por repositorio
    private List<ComponentePropuesta> elementos = new ArrayList<>();

    public ExpedienteDigital() {}

    public ExpedienteDigital(String titulo) {
        this.titulo = titulo;
        this.estado = "ACTIVA";
        this.contadorFirmas = 0;
        this.fechaExpiracion = LocalDateTime.now().plusDays(90);
    }

    public void agregarElemento(ComponentePropuesta componente) {
        this.elementos.add(componente);
    }

    @Override
    public String obtenerContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("EXPEDIENTE: ").append(titulo).append("\n");
        sb.append("ID REGISTRO: ").append(id).append("\n");
        sb.append("- CONTENIDO -").append("\n");
        for (ComponentePropuesta elemento : elementos) {
            sb.append(elemento.obtenerContenido());
        }
        return sb.toString();
    }

    // Getters y Setters para JPA
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getContadorFirmas() { return contadorFirmas; }
    public void setContadorFirmas(int contadorFirmas) { this.contadorFirmas = contadorFirmas; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public String getHashSeguridad() { return hashSeguridad; }
    public void setHashSeguridad(String hashSeguridad) { this.hashSeguridad = hashSeguridad; }
    public List<ComponentePropuesta> getElementos() { return elementos; }
    public void setElementos(List<ComponentePropuesta> elementos) { this.elementos = elementos; }
}