package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import java.util.ArrayList;

public class Alumno{

    private String nombre;
    private boolean regular;
    private ArrayList<Carrera> carreras;
    private transient ArrayList<Inscripcion> inscripciones;
    private transient ArrayList<Mesa> mesas;

    public Alumno(){
        this.nombre = "";
        this.mesas = new ArrayList<Mesa>();
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

    /**
     * Recorremos las inscripciones del alumno y verificamos que coincida con la mesa recibida
     * por parametro. Coinciden en codigo_materia y codigo_carrera.
     * @param mesa mesa habilitada para inscripcion.
     * @return true si el alumno esta inscripto a la mesa.
     */
    public Boolean estaInscripto(Mesa mesa){
        Boolean result = false;
        for (Inscripcion inscripcion : this.getInscripciones()){
            if (inscripcion.getMateria().equals(mesa.getMateria()) &&
                    inscripcion.getCarrera().equals(mesa.getCarrera())
                    ){
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Retornamos la carrera del alumno.
     * @param cod_carrera codigo de la carrera buscada.
     * @return Carrera instancia o null si no existe.
     */
    public Carrera getCarreraById(String cod_carrera) {
        Carrera result = null;
        for (Carrera carrera : this.carreras){
            if (carrera.getCodigo().equals(cod_carrera)){
                result = carrera;
                break;
            }
        }
        return result;
    }

    /**
     * Seteamos las mesas de examen como atributo volatil del alumno. No se almacenan localmente.
     * @param mesas listado de mesas habilitadas para inscripcion.
     */
    public void loadMesas(ArrayList<Mesa> mesas) {
        this.mesas = mesas;
    }

    /**
     * Seteamos las inscripciones a examen como atributo volatil del alumno. No se almacenan localmente.
     * @param inscripciones Listado de inscripciones a examen del alumno.
     */
    public void loadInscripciones(ArrayList<Inscripcion> inscripciones) {
        this.inscripciones= inscripciones;
    }
    public ArrayList<Mesa> getMesas(){
        return this.mesas;
    }

    /**
     * Retornamos la carrera del alumno con el nombre buscado. Se comparan cadenas previamente
     * convertidas a lower case
     * @param nombre_carrera String a buscar.
     * @return Carrera o null si no existe.
     */
    public Carrera getCarreraByName(String nombre_carrera){
        String buscadr = nombre_carrera.toLowerCase();
        Carrera result = null;
        for (Carrera carrera : this.getCarreras()){
            if (carrera.getNombre().toLowerCase().equals(nombre_carrera)){
                result = carrera;
                break;
            }
        }
        return result;
    }
}
