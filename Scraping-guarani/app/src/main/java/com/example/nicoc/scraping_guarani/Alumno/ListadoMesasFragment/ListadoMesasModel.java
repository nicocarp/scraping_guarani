package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import android.content.SharedPreferences;

import com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment.IListadoMesasFragment;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicoc on 08/10/17.
 */

class ListadoMesasModel implements IListadoMesasFragment.Model {

    private IListadoMesasFragment.Presenter presenter;
    private SharedPreferences preferences;

    public ListadoMesasModel(IListadoMesasFragment.Presenter presenter, SharedPreferences loginPrefs) {
        this.presenter = presenter;
        this.preferences = loginPrefs;
    }

    /**
     * Obtenemos de SharedPreferences los datos relacionados con las mesas a inscripciones     *
     */
    @Override
    public void getMesasEInscripciones() {
        ArrayList<Mesa> mesas= new  ArrayList<Mesa>();
        ArrayList<Inscripcion> inscripciones= new  ArrayList<Inscripcion>();

        String mesas_string = preferences.getString("mesas", "");
        String inscripciones_json = preferences.getString("inscripciones", "");

        Type collectionType;
        if (!mesas_string.isEmpty()){
            collectionType = new TypeToken<ArrayList<Mesa>>(){}.getType();
            mesas= new Gson().fromJson(mesas_string, collectionType);
        }

        if (!inscripciones_json.isEmpty()){
            collectionType = new TypeToken<ArrayList<Inscripcion>>(){}.getType();
            inscripciones = new  Gson().fromJson(inscripciones_json, collectionType);
        }
        this.presenter.setItems(mesas, inscripciones);

    }
}
