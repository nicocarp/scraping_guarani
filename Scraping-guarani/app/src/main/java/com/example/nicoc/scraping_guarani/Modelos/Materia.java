package com.example.nicoc.scraping_guarani.Modelos;

import java.io.Serializable;

/**
 * Created by nicoc on 24/11/17.
 */

public class Materia implements Serializable {
    private String codigo;
    private String nombre;
    private Carrera carrera;
    private String  año;
    private String periodoLectivo;
    private String correlatividad;

    public String getCorrelatividad() {
        return correlatividad;
    }

    public void setCorrelatividad(String correlatividad) {
        this.correlatividad = correlatividad;
    }



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

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }
    public String getPeriodoLectivo() {
        return this.periodoLectivo;
    }
}

