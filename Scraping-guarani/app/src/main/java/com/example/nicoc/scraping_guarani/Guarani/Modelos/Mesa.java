package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import java.util.ArrayList;

public class Mesa {

    private String carrera;
    private String materia;
    private String fecha;
    private String sede;
    private TipoMesa tipoMesa; // libre o regular
    private String turno; // esto es necesario para lanzar post de inscripcion
    private ArrayList<Profesor> profesores;
    private ArrayList<String> materias_necesarias;

    public Mesa(){
        this.materias_necesarias = new ArrayList<String>();
    }

    /**
     * Forma de identificar a una mesa: codigo_carrera+codigo_materia
     * @return String correspondiente al idetnficiador de la mesa.
     */
    public String getId(){
        return this.carrera + this.materia;
    }
    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public void addMateriaNecesariaById(String materia){
        this.materias_necesarias.add(materia);
    }

    /**
     * Materias que el alumno necesita aprobar para inscribirse a una mesa de la Materia
     * @return Listado de materias.
     */
    public ArrayList<String> getMaterias_necesarias() {
        return materias_necesarias;
    }

    public void setMaterias_necesarias(ArrayList<String> materias_necesarias) {
        this.materias_necesarias = materias_necesarias;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public TipoMesa getTipoMesa() {
        return tipoMesa;
    }

    public void setTipoMesa(TipoMesa tipoMesa) {
        this.tipoMesa = tipoMesa;
    }

    public ArrayList<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(ArrayList<Profesor> profesores) {
        this.profesores = profesores;
    }

    public Boolean puedeInscribirse(){
        return (this.getMaterias_necesarias().isEmpty());
    }

}
