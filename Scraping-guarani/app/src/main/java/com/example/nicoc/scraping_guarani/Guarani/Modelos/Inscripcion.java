package com.example.nicoc.scraping_guarani.Guarani.Modelos;

/**
 * Created by nicoc on 30/11/17.
 */

public class Inscripcion {

    private String carrera;

    private String  fecha;
    private String tipo;
    private String materia;

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
