package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import java.util.ArrayList;

public class Mesa {

    private String carrera;
    private String materia;
    private String fecha;
    private String turno; // esto es necesario para lanzar post de inscripcion
    private ArrayList<String> materias_necesarias;
    private String anio_acedemico;
    private Boolean inscripto;
    private Boolean habilitada;
    public Boolean getInscripto() {
        return inscripto;
    }

    public Mesa(){
        this.inscripto = false;
        this.habilitada = true;
        this.materias_necesarias = new ArrayList<String>();
    }

    /**
     * Seteamos con un booleano si existe una inscripcion a la mesa.
     * @param inscripto
     */
    public void setInscripto(Boolean inscripto) {
        this.inscripto = inscripto;
    }

    /**
     * Retornamos si la mesa esta habilitada para inscripcion.
     * @return
     */
    public Boolean getHabilitada() {
        return habilitada;
    }

    /**
     * Seteamos si la mesa esta habiltiada para inscripcion o no.
     * @param habilitada
     */
    public void setHabilitada(Boolean habilitada) {
        this.habilitada = habilitada;
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

    /**
     * Agregamos un codigo de una materia que es necesaria aprobar para inscribirte en la mesa.
     * @param materia
     */
    public void addMateriaNecesariaById(String materia){
        this.materias_necesarias.add(materia);
    }

    /**
     * Materias que el alumno necesita aprobar para inscribirse a una mesa de la Materia.
     * @return Listado de Strngs que corresponden con el codigo de materia necesaria.
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

    /**
     * Retornamos la fecha de la mesa. Si no existe devolvemos.
     * @return fecha si existe si no null.
     */
    public String getFecha() {
        if (this.fecha == null)
            return "";
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Retornamos booleano si el alumno puede inscribirse a la mesa.
     * @return
     */
    public Boolean puedeInscribirse(){
        return (this.getMaterias_necesarias().isEmpty());
    }

    /**
     * Retornamos la fecha de la Mesa de examen-
     * @return String fecha dd/mm/aaaa
     */
    public String getSoloFecha(){
        String[] result = this.getFecha().split(" ");
        return result[0];
    }

    /**
     * Retornamos la hora de la mesa de examen-
     * @return String hora mm:ss
     */
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

    /**
     * Recorre listado de inscripciones y busca si tiene alguna inscripcion asociada.
     * Comparacion entre inscripcion y mesa es por codigo_carrera+codigo_materia.
     * Si la inscripcion asociada existe, se seta el atributo inscripta como true.
     * @param inscripciones
     */
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

    /**
     * Recorre un listado de mesas y verifica que esté dentro de ella.
     * Si la mesa existe dentro del listado se setea su atributo habilitada en true.
     * Se comparan con codigo_carrera+codigo_materia
     * @param mesas
     */
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
