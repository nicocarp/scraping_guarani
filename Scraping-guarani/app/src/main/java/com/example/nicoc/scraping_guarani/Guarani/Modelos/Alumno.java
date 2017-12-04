package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class Alumno{

    private String nombre;

    private ArrayList<Carrera> carreras;
    private boolean regular;
    private ArrayList<Inscripcion> inscripciones;

    public Alumno(){

        this.carreras = new ArrayList<Carrera>();
        this.inscripciones= new ArrayList<Inscripcion>();
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

        if (!regular)
            for (Carrera carrera :this.getCarreras()){
                carrera.setActivo(false);
        }
        this.regular = regular;
    }

    public void addInscripcion(Inscripcion inscripcion){
        this.inscripciones.add(inscripcion);
    }
    public void addCarrera(Carrera carrera) {
        this.carreras.add(carrera);
    }

    public ArrayList<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Guardamos listado de inscripciones como atributo del alumno.
     * @param inscripciones
     */
    public void setInscripciones(ArrayList<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }


    public Boolean estaInscripto(Materia materia){
        Boolean result = false;
        for (Inscripcion inscripcion : this.getInscripciones()){
            if (inscripcion.getMateria().equals(materia)){
                result = true;
                break;
            }
        }
        return result;
    }

}
