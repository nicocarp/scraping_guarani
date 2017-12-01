package com.example.nicoc.scraping_guarani.Modelos;

import java.io.Serializable;

public class Profesor implements Serializable{
    private String nombre;

    public Profesor(String s) {
        this.nombre = s;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

