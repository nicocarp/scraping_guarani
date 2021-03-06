package com.example.nicoc.scraping_guarani.Guarani.Modelos;


/**
 * Created by nicoc on 24/11/17.
 * Clase que representa una materia. Guardamos codigo y nombre de la materia, año de cursada,
 * periodo lectivo y sus correlatividades.
 */

public class Materia{
    private String codigo;
    private String nombre;
    private String  año;
    private String periodoLectivo;
    private String correlatividad;

    public Materia(){
        codigo = "";
        nombre = "";
        año = "";
        periodoLectivo = "";
        correlatividad = "";
    }

    /**
     * Devolvemos un texto con todas las materias necesarias para cursar y aprobar la materia.
     * @return
     */
    public String getCorrelatividad() {
        return correlatividad;
    }

    public void setCorrelatividad(String correlatividad) {
        this.correlatividad = correlatividad;
    }



    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }
    public String getPeriodoLectivo() {
        return this.periodoLectivo;
    }

}


