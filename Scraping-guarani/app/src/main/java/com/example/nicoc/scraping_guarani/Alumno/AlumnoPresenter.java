package com.example.nicoc.scraping_guarani.Alumno;

import android.content.SharedPreferences;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;

/**
 * Created by nicoc on 06/12/17.
 */

class AlumnoPresenter implements IAlumno.Presenter {

    IAlumno.View view;
    IAlumno.Model model;

    public AlumnoPresenter(IAlumno.View view, SharedPreferences loginPreferences) {
        this.view = view;
        this.model = new AlumnoModel(this, loginPreferences);
    }

    @Override
    public void mostrarError(String error) {
        this.view.mostrarError(error);
    }

    @Override
    public void desloguearse() {
        this.model.desloguearse();
    }

    @Override
    public void getAlumno() {
        this.model.getAlumno();
    }

    @Override
    public void onAlumno(Alumno alumno) {
        this.view.setAlumno(alumno);
    }

    @Override
    public void deslogueado() {
        this.view.mostrarError("Deslogueado");
    }

    @Override
    public void getMesasEInscripciones() {
        this.model.getMesasEInscripciones();
    }

    @Override
    public void onMesasEInscripciones(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones) {
        this.view.setMesasEInscripciones(mesas, inscripciones);
    }
}
