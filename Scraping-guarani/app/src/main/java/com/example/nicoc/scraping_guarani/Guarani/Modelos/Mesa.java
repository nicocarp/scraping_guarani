package com.example.nicoc.scraping_guarani.Guarani.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Mesa implements Parcelable {

    private Long id;
    private String fecha;
    private String sede;
    private TipoMesa tipoMesa; // libre o regular
    private ArrayList<Profesor> profesores;

    private String carrera;
    private String materia;
    private ArrayList<String> materias_necesarias;
    private String turno; // esto es necesario para lanzar post de inscripcion

    public Mesa(){
        this.materias_necesarias = new ArrayList<String>();
    }


    //Desde aca
    protected Mesa(Parcel in) {
        fecha = in.readString();
        sede = in.readString();
        tipoMesa = (TipoMesa) in.readSerializable();
        profesores = in.readArrayList(null);
        //carrera = (Carrera) in.readSerializable();
        //materia = (Materia) in.readSerializable();
        carrera = in.readParcelable(Carrera.class.getClassLoader());
        materia = in.readParcelable(Materia.class.getClassLoader());
        materias_necesarias = in.readArrayList(null);
        turno = in.readString();

    }



    public static final Creator<Mesa> CREATOR = new Creator<Mesa>() {
        @Override
        public Mesa createFromParcel(Parcel in) {
            return new Mesa(in);
        }

        @Override
        public Mesa[] newArray(int size) {
            return new Mesa[size];
        }
    };//hasta aca

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fecha);
        parcel.writeString(sede);
        parcel.writeSerializable(tipoMesa);
        parcel.writeList(profesores);
        parcel.writeList(materias_necesarias);
        parcel.writeString(turno);
    }


}
