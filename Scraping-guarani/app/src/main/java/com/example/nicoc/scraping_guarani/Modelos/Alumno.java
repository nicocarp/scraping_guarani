package com.example.nicoc.scraping_guarani.Modelos;

import java.util.ArrayList;
import java.util.List;

public class Alumno{

    private String nombre;
    private String legajo;
    private ArrayList<Carrera> carreras;
    private boolean regular;

    public Alumno(){
        this.carreras = new ArrayList<Carrera>();
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public ArrayList<Carrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(ArrayList<Carrera> carreras) {
        this.carreras = carreras;
    }

    public boolean isRegular() {
        return regular;
    }

    public void setRegular(boolean regular) {
        this.regular = regular;
    }

    public void addCarrera(Carrera carrera) {
        this.carreras.add(carrera);
    }
}
