package com.example.nicoc.scraping_guarani.Alumno;

import android.content.SharedPreferences;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Alumno.Servicio.AsyncDesloguear;
import com.example.nicoc.scraping_guarani.Alumno.Servicio.AsyncInscribirse;
import com.google.gson.Gson;

/**
 * Created by nicoc on 06/12/17.
 */

class AlumnoModel implements IAlumno.Model, AsyncDesloguear.IDesloguear, AsyncInscribirse.IInscribirse {
     private IAlumno.Presenter presenter;
    private SharedPreferences preferences;

    public AlumnoModel(IAlumno.Presenter presenter, SharedPreferences loginPreferences) {
        this.presenter = presenter;
        this.preferences = loginPreferences;
    }

    @Override
    public void getAlumno() {
        String obj_json = preferences.getString("alumno_json", "");
        if (obj_json.isEmpty())
            this.presenter.mostrarError("Sin alumno guardado.");
        Alumno alumno = new Gson().fromJson(obj_json, Alumno.class);
        this.presenter.onAlumno(alumno);
    }

    @Override
    public void inscribirseAMesa(Alumno alumno, Mesa mesa, String tipo) {
        new AsyncInscribirse(this).execute(alumno, mesa, tipo);
    }


    @Override
    public void desloguearse() {
        new AsyncDesloguear(this).execute(); // no me anda el postExecute, ejecute onSuccess a mano...
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("alumno_json", "");
        edit.putString("mesas", "");
        edit.putString("username", "");
        edit.putString("password", "");
        edit.commit();
        onSuccess();
    }

    @Override
    public void onSuccess() {
        this.presenter.deslogueado();

    }

    @Override
    public void onError(String error) {
        this.presenter.mostrarError(error);
    }

    @Override
    public void onInscripcion(String mensaje) {
        this.presenter.onInscripcion(mensaje);
    }
}
