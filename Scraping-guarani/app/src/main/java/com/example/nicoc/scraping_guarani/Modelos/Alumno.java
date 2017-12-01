package com.example.nicoc.scraping_guarani.Modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Alumno{

    private String nombre;
    private String legajo;
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

        if (!regular)
            for (Carrera carrera :this.getCarreras()){
                carrera.setActivo(false);
        }
        this.regular = regular;
    }

    public void addInscripcion(Inscripcion inscripcion){
        inscripcion.setAlumno(this);
        this.inscripciones.add(inscripcion);
    }
    public void addCarrera(Carrera carrera) {
        this.carreras.add(carrera);
    }

    public ArrayList<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Seteamos a cada inscripcion el alumno con this
     * @param inscripciones
     */
    public void setInscripciones(ArrayList<Inscripcion> inscripciones) {
        for (Inscripcion inscripcion : inscripciones){
            inscripcion.setAlumno(this);
        }
        this.inscripciones = inscripciones;
    }
    public Carrera getCarreraById(String id){
        Carrera result = null;
        for (Carrera carrera : this.getCarreras()){
            if (carrera.getCodigo().equals(id)){
                result = carrera;
                break;
            }
        }
        return result;
    }
    public void createInscripcion(HashMap<String, String> insc_map){
        Carrera carrera = this.getCarreraById(insc_map.get("cod_carrera"));
        Materia materia = carrera.getMateriaById(insc_map.get("cod_materia"));

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setMateria(materia);
        inscripcion.setTipo(insc_map.get("tipo"));
        addInscripcion(inscripcion);
    }

}
