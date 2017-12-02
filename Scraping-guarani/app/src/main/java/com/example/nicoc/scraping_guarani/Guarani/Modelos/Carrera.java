package com.example.nicoc.scraping_guarani.Guarani.Modelos;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Carrera implements Parcelable {
    private String codigo;
    private String nombre;
    private String plan;
    private ArrayList<Materia> materias;
    private ArrayList<Correlatividad> correlatividades;

    public Carrera()
    {
        this.codigo = "";
        this.nombre = "";
        this.plan = "";
        this.materias = new ArrayList<Materia>();
        this.correlatividades = new ArrayList<Correlatividad>();
    }

    protected Carrera(Parcel in) {
        codigo = in.readString();
        nombre = in.readString();
        plan = in.readString();
        //materias = in.readArrayList(null);
        materias = in.readArrayList(Materia.class.getClassLoader());
        //correlatividades = in.readArrayList(null);
        materias = in.readArrayList(Correlatividad.class.getClassLoader());


    }

    public static final Creator<Carrera> CREATOR = new Creator<Carrera>() {
        @Override
        public Carrera createFromParcel(Parcel in) {
            return new Carrera(in);
        }

        @Override
        public Carrera[] newArray(int size) {
            return new Carrera[size];
        }
    };

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

    public ArrayList<Materia> getMaterias() {
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
    public void setMaterias(ArrayList<Materia> materias) {
        for (Materia materia : materias){
            materia.setCarrera(this);
        }
        this.materias = materias;
    }

    public ArrayList<Correlatividad> getCorrelatividades() {
        return correlatividades;
    }

    public void setCorrelatividades(ArrayList<Correlatividad> correlatividades) {
        this.correlatividades = correlatividades;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(codigo);
        parcel.writeString(nombre);
        parcel.writeString(plan);
        parcel.writeList(materias);
        parcel.writeList(correlatividades);

    }
}