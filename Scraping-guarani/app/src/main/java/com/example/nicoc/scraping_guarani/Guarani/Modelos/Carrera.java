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

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    private String legajo;

    private ArrayList<Correlatividad> correlatividades;
    private Boolean activo;

    public Carrera()
    {
        this.codigo = "";
        this.nombre = "";
        this.plan = "";

        this.correlatividades = new ArrayList<Correlatividad>();
    }

    protected Carrera(Parcel in) {
        codigo = in.readString();
        nombre = in.readString();
        plan = in.readString();
        //materias = in.readArrayList(null);

        //correlatividades = in.readArrayList(null);
        correlatividades = in.readArrayList(Correlatividad.class.getClassLoader());


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
        for (Materia materia : materias){
            materia.setCarrera(this);
        }
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
        parcel.writeList(correlatividades);

    }
}