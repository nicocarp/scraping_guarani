package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import android.content.SharedPreferences;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;

/**
 * Created by nicoc on 08/10/17.
 */

class ListadoMesasPresenter implements IListadoMesasFragment.Presenter {

    private IListadoMesasFragment.View view;
    private IListadoMesasFragment.Model model;


    public ListadoMesasPresenter(IListadoMesasFragment.View view, SharedPreferences loginPrefs) {
        this.view = view;
        this.model = new ListadoMesasModel(this, loginPrefs);
    }

    @Override
    public void getItems() {
        this.model.getItems();
    }

    @Override
    public void setItems(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones) {
        this.view.setItems(mesas, inscripciones);
    }

    @Override
    public void mostrarError(String error) {
        this.view.mostrarError(error);

    }
}
