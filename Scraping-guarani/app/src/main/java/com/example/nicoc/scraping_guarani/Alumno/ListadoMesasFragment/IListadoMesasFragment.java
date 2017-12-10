package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicoc on 08/10/17.
 */

public interface IListadoMesasFragment {

    interface ViewFragment{
        public void updateList();
    }
    interface ViewContainer{
        public void onItemSelectedInFragment(Mesa mesa);
        public void mostrarError(String error);
    }
    interface View {
        public void getItems();
        public void mostrarError(String error);
        public void setItems();
    }
    interface Presenter {
        public void getItems();
        public void setItems(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones);
        public void mostrarError(String error);
    }
    interface Model{
        public void getMesasEInscripciones();
    }
}
