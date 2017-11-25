package com.example.nicoc.scraping_guarani.Modelos;

/**
 * Created by nicoc on 24/11/17.
 */

public class Materia {
    private String codigo;
    private String nombre;
    private Carrera carrera;
    private Integer año;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

}

