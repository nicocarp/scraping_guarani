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
    private String anio_acedemico;
    private Boolean inscripto;
    private Boolean habilitada;
    public Boolean getInscripto() {
        return inscripto;
    }

    public void setInscripto(Boolean inscripto) {
        this.inscripto = inscripto;
    }

    public Boolean getHabilitada() {
        return habilitada;
    }

    public void setHabilitada(Boolean habilitada) {
        this.habilitada = habilitada;
    }


    public Mesa(){
        this.inscripto = false;
        this.habilitada = true;
        this.materias_necesarias = new ArrayList<String>();
    }

    /**
     * Forma de identificar a una mesa: codigo_carrera+codigo_materia
     * @return String correspondiente al idetnficiador de la mesa.
     */
    public String getId(){
        return this.carrera+this.materia;
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
        if (this.fecha == null)
            return "";
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

    public String getSoloFecha(){
        String[] result = this.getFecha().split(" ");
        return result[0];
    }
    public String getHora(){
        String[] result = this.getFecha().split(" ");
        return result[1];
    }

    public String getAnio_acedemico() {
        return anio_acedemico;
    }

    public void setAnio_acedemico(String anio_acedemico) {
        this.anio_acedemico = anio_acedemico;
    }

    public void setInscripcion(ArrayList<Inscripcion> inscripciones) {
        Boolean inscripto = false;
        for (Inscripcion inscripcion : inscripciones){
            if (inscripcion.getId().equals(this.getId())){
                inscripto = true;
                break;
            }
        }
        this.setInscripto(inscripto);
    }

    public void setHabilitada(ArrayList<Mesa> mesas) {
        Boolean habilitada = false;
        for (Mesa mesa:mesas){
            if (mesa.getId().equals(this.getId())){
                habilitada = true;
                break;
            }
        }
        this.setHabilitada(habilitada);
    }
}
