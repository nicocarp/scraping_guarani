package com.example.nicoc.scraping_guarani.Modelos;

import java.util.ArrayList;
import java.util.Date;

public class Mesa{
    private String fecha;
    private String sede;
    private TipoMesa tipoMesa; // libre o regular
    private ArrayList<Profesor> profesores;
    private Materia materia;
    private ArrayList<Materia> materias_necesarias;
    private String turno; // esto es necesario para lanzar post de inscripcion

    public Mesa(){
        this.materias_necesarias = new ArrayList<Materia>();
    }
    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public void addMateriaNecesariaById(Materia materia){
        this.materias_necesarias.add(materia);
    }
    /**
     * Materias que el alumno necesita aprobar para inscribirse a una mesa de la Materia
     * @return Listado de materias.
     */
    public ArrayList<Materia> getMaterias_necesarias() {
        return materias_necesarias;
    }

    public void setMaterias_necesarias(ArrayList<Materia> materias_necesarias) {
        this.materias_necesarias = materias_necesarias;
    }


    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
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


}
