package com.example.nicoc.scraping_guarani.Mesa.Listado;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.List;

/**
 * Created by nicoc on 26/09/17.
 */

public class ListadoPresenter implements IListado.Presenter{
    private IListado.View view;
    private IListado.Model model;

    public ListadoPresenter(IListado.View view) {
        this.view = view;
        this.model = new ListadoModel(this);
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
    public void setItems(List<Mesa> items) {
        this.view.setItems(items);
    }

    @Override
    public void inscribirse(Mesa mesa, Alumno alumno, String tipo) {
        if (tipo.equals("libre") || tipo.equals("regular"))
            this.model.inscribirse(mesa, alumno, tipo);
        else
            this.view.mostrarError("Validacion: error de tipo");
    }



}
