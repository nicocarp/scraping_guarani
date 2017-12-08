package com.example.nicoc.scraping_guarani.Mesa.Listado;

import android.content.SharedPreferences;

import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Login.AsyncLogin;
import com.example.nicoc.scraping_guarani.Mesa.Servicio.AsyncGetMesas;
import com.example.nicoc.scraping_guarani.Mesa.Servicio.AsyncInscribirse;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicoc on 26/09/17.
 */

public class ListadoModel implements IListado.Model, AsyncInscribirse.IInscribirse {

    private IListado.Presenter presenter;
    private ManagerGuarani manager;
    private SharedPreferences preferences;

    public ListadoModel(IListado.Presenter presenter, SharedPreferences loginPrefs){
        this.presenter = presenter;
        this.preferences = loginPrefs;
    }

    @Override
    public void getMesas() {
        ArrayList<Mesa> mesas= new  ArrayList<Mesa>();
        ArrayList<Inscripcion> inscripciones= new  ArrayList<Inscripcion>();

        String mesas_string = preferences.getString("mesas", "");
        String inscripciones_json = preferences.getString("inscripciones", "");

        Type collectionType;
        if (!mesas_string.isEmpty()){
            collectionType = new TypeToken<ArrayList<Mesa>>(){}.getType();
            mesas= new  Gson().fromJson(mesas_string, collectionType);
        }

        if (!inscripciones_json.isEmpty()){
            collectionType = new TypeToken<ArrayList<Inscripcion>>(){}.getType();
            inscripciones = new  Gson().fromJson(inscripciones_json, collectionType);
        }
        this.presenter.setItems(mesas, inscripciones);
    }

    @Override
    public void inscribirse(Mesa mesa, Alumno alumno, String tipo) {

        new AsyncInscribirse(this).execute(alumno, mesa, tipo);
    }

    @Override
    public void onError(String error) {
        this.presenter.mostrarError(error);
    }

    @Override
    public void onInscripcion() {
        this.presenter.mostrarError("Inscripto correctamente");
    }


}
