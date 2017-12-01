package com.example.nicoc.scraping_guarani.Mesa.Listado;

import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Login.AsyncLogin;
import com.example.nicoc.scraping_guarani.Mesa.Servicio.AsyncGetMesas;
import com.example.nicoc.scraping_guarani.Mesa.Servicio.AsyncInscribirse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicoc on 26/09/17.
 */

public class ListadoModel implements IListado.Model, AsyncGetMesas.IGetMesas, AsyncInscribirse.IInscribirse {

    private IListado.Presenter presenter;
    private ManagerGuarani manager;

    public ListadoModel(IListado.Presenter presenter){

        this.presenter = presenter;
    }

    @Override
    public void getMesas() {

        new AsyncGetMesas(this).execute();
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
