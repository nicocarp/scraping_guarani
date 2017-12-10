package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicoc on 08/10/17.
 */

public interface IListadoMesasFragment {

    interface View {
        public void getItems();
        public void mostrarError(String error);
        public void setItems(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones);
    }
    interface Presenter {
        public void getItems();
        public void setItems(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones);
        public void mostrarError(String error);
    }
    interface Model{
        public void getItems();
    }
}
