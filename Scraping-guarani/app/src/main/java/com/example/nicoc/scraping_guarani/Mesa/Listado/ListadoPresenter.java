package com.example.nicoc.scraping_guarani.Mesa.Listado;

import android.content.SharedPreferences;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicoc on 26/09/17.
 */

public class ListadoPresenter implements IListado.Presenter{
    private IListado.View view;
    private IListado.Model model;

    public ListadoPresenter(IListado.View view, SharedPreferences loginPrefs) {
        this.view = view;
        this.model = new ListadoModel(this, loginPrefs);
    }

    @Override
    public void getItems() {
        this.model.getMesas();
    }

    @Override
    public void mostrarError(String error) {
        this.view.mostrarError(error);
    }

    @Override
    public void setItems(List<Mesa> mesas, ArrayList<Inscripcion> inscripciones) {
        this.view.setItems(mesas, inscripciones);
    }



    @Override
    public void inscribirse(Mesa mesa, Alumno alumno, String tipo) {
        if (tipo.equals("libre") || tipo.equals("regular"))
            this.model.inscribirse(mesa, alumno, tipo);
        else
            this.view.mostrarError("Validacion: error de tipo");
    }



}
