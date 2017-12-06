package com.example.nicoc.scraping_guarani.Alumno;

import android.content.SharedPreferences;

/**
 * Created by nicoc on 06/12/17.
 */

class AlumnoPresenter implements IAlumno.Presenter {

    IAlumno.View view;
    IAlumno.Model model;
    SharedPreferences sharedPreferences;
    public AlumnoPresenter(IAlumno.View view, SharedPreferences loginPreferences) {
        this.view = view;
        this.sharedPreferences = loginPreferences;
        this.model = new AlumnoModel(this);
    }

    @Override
    public void desloguearse() {
        this.model.desloguearse();
    }

    @Override
    public void deslogueado() {
        this.view.mostrarError("Deslogueado");
    }
}
