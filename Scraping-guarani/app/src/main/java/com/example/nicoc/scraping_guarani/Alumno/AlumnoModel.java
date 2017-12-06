package com.example.nicoc.scraping_guarani.Alumno;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Login.AsyncLogin;
import com.example.nicoc.scraping_guarani.Mesa.Servicio.AsyncDesloguear;

/**
 * Created by nicoc on 06/12/17.
 */

class AlumnoModel implements IAlumno.Model, AsyncDesloguear.IDesloguear {
     private IAlumno.Presenter presenter;

    public AlumnoModel(IAlumno.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void desloguearse() {
        new AsyncDesloguear(this).execute();
        onSuccess();
    }

    @Override
    public void onSuccess() {
        this.presenter.deslogueado();

    }

    @Override
    public void onError(String error) {

    }
}
