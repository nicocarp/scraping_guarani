package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nicoc on 24/11/17.
 */

public class Materia implements Parcelable {
    private String codigo;
    private String nombre;
    private Carrera carrera;
    private String  año;
    private String periodoLectivo;
    private String correlatividad;

    public Materia(){
        codigo = "";
        nombre = "";
        carrera = new Carrera();
        año = "";
        periodoLectivo = "";
        correlatividad = "";
    }

    protected Materia(Parcel in) {
        codigo = in.readString();
        nombre = in.readString();
        carrera = in.readParcelable(Carrera.class.getClassLoader());
        //carrera = (Carrera) in.readValue(Carrera.class.getClassLoader());
        año = in.readString();
        periodoLectivo = in.readString();
        correlatividad = in.readString();
    }

    public static final Creator<Materia> CREATOR = new Creator<Materia>() {
        @Override
        public Materia createFromParcel(Parcel in) {
            return new Materia(in);
        }

        @Override
        public Materia[] newArray(int size) {
            return new Materia[size];
        }
    };

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

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(codigo);
        parcel.writeString(nombre);
        parcel.writeParcelable(carrera,i);
        //parcel.writeValue(carrera);
        parcel.writeString(año);
        parcel.writeString(periodoLectivo);
        parcel.writeString(correlatividad);

    }
}


