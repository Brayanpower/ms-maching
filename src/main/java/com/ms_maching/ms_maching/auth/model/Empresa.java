package com.ms_maching.ms_maching.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nombre",columnDefinition = "TEXT", unique = true)
    private String nombre;

    @Column(name = "Cp",nullable = false, length = 150)
    private int cp;

    @Column(name ="Edo",columnDefinition = "TEXT")
    private String edo;

    @Column(name ="Municipio",columnDefinition = "TEXT")
    private String municipio;

    @Column(name ="Colonia",columnDefinition = "TEXT")
    private String colonia;

    public Empresa() {}

    public Empresa(Long id) { this.id = id; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getEdo() {
        return edo;
    }

    public void setEdo(String edo) {
        this.edo = edo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
}