package com.example.nicoc.scraping_guarani.Modelos;

import java.io.Serializable;
import java.util.List;

public class Carrera implements Serializable{
    private String codigo;
    private String nombre;
    private String plan;
    private List<Materia> materias;
    private List<Correlatividad> correlatividades;

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    private Boolean activo;

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

    public List<Materia> getMaterias() {
        return materias;
    }

    public Materia getMateriaById(String id_materia){
        Materia result = null;
        for (Materia m : this.materias){
            if (m.getCodigo().equals(id_materia)){
                result = m;
                break;
            }
        }
        return result;

    }
    public void setMaterias(List<Materia> materias) {
        for (Materia materia : materias){
            materia.setCarrera(this);
        }
        this.materias = materias;
    }

    public List<Correlatividad> getCorrelatividades() {
        return correlatividades;
    }

    public void setCorrelatividades(List<Correlatividad> correlatividades) {
        this.correlatividades = correlatividades;
    }
}

