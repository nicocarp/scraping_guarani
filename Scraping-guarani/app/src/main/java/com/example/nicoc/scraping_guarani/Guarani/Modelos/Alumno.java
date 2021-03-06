package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import java.util.ArrayList;

/**
 * Representa a un alumno. Guardamos su nombre, si es regular o no y sus carreras.
 * Este objeto tiene atributos volatiles: mesas e inscripciones, no se almacenan localmente.
 * Esto es asi para evitar inconsistencias con el servidor Guarani. Se setean y se pierden una vez
 */
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

    public ArrayList<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    /**
     * Retornamos si el alumno esta inscripto a la mesa.
     * @param mesa mesa habilitada para inscripcion.
     * @return true si el alumno esta inscripto a la mesa.
     */
    public Boolean estaInscripto(Mesa mesa){
        return mesa.getInscripto();
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

    /**
     * Retornamos la mesa buscandola por su id: codigo_carrera+codigo_materia
     * @param carrera codigo
     * @param materia codigo
     * @return instancia de Mesa o null si no existe.
     */
    private Mesa getMesa(String carrera, String materia){
        Mesa result = null;
        for (Mesa mesa : this.getMesas()){
            if (mesa.getCarrera().equals(carrera) && mesa.getMateria().equals(materia)){
                result = mesa;
                break;
            }
        }
        return result;
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
        Carrera result = null;
        for (Carrera carrera : this.getCarreras()){
            if (carrera.getNombre().toLowerCase().equals(nombre_carrera.toLowerCase())){
                result = carrera;
                break;
            }
        }
        return result;
    }

    /**
     * Retornamos la inscripcion asociada a la mesa recibida por parametro.
     * @param mesa instancia
     * @return Inscripcion instancia o null si no existe.
     */
    public Inscripcion getInscripcionByMesa(Mesa mesa) {
        Inscripcion result = null;
        for (Inscripcion inscripcion : this.getInscripciones()){
            if (inscripcion.esDeLaMesa(mesa)){
                result = inscripcion;
                break;
            }
        }
        return result;
    }
}
