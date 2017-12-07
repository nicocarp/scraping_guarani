package com.example.nicoc.scraping_guarani.Mesa.Listado;

import android.content.SharedPreferences;

import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
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

public class ListadoModel implements IListado.Model, AsyncGetMesas.IGetMesas, AsyncInscribirse.IInscribirse {

    private IListado.Presenter presenter;
    private ManagerGuarani manager;
    private SharedPreferences preferences;

    public ListadoModel(IListado.Presenter presenter, SharedPreferences loginPrefs){
        this.presenter = presenter;
        this.preferences = loginPrefs;
    }

    @Override
    public void getMesas() {
        //new AsyncGetMesas(this).execute();
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        String mesas_string = preferences.getString("mesas", "");
        if (!mesas_string.isEmpty()) {
            Type collectionType = new TypeToken<ArrayList<Mesa>>(){}.getType();
            mesas= new  Gson().fromJson(mesas_string, collectionType);
        }
        this.presenter.setItems(mesas);
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

    @Override
    public void onMesas(ArrayList<Mesa> mesas) {
        this.presenter.setItems(mesas);
    }
}
