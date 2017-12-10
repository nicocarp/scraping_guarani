package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import java.util.ArrayList;

public class Carrera{
    private String codigo;
    private String nombre;
    private String plan;
    private String legajo;
    private Boolean activo;
    private ArrayList<Materia> materias;

    public Carrera(){
        this.codigo = "";
        this.nombre = "";
        this.plan = "";
        this.legajo= "";
        this.activo= true;
        this.materias= new ArrayList<Materia>();
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }


    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setMaterias(ArrayList<Materia> materias) {
        /*for (Materia materia : materias){
            materia.setCarrera(this);
        }*/
        this.materias = materias;
    }

    /**
     * Buscamos la materia asociada a la carrera.
     * @param cod_materia codigo de la materia buscada.
     * @return Materia instancia o null si no existe.
     */
    public Materia getMateriaById(String cod_materia) {
        Materia result=null;
        for (Materia materia: this.materias){
            if (materia.getCodigo().equals(cod_materia)){
                result = materia;
                break;
            }
        }
        return result;
    }
}