package com.example.nicoc.scraping_guarani.Mesa.Listado;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicoc on 26/09/17.
 */

public interface IListado {

    interface View {
        public void getItems();
        public void setItems(ArrayList<Mesa> items, ArrayList<Inscripcion> inscripciones);
        public void mostrarError(String error);
        public void lanzarDetalleMesa(Mesa mesa);
    }
    interface Presenter{
        public void getItems();
        public void mostrarError(String error);
        public void setItems(ArrayList<Mesa> items, ArrayList<Inscripcion> inscripciones);
        public void inscribirse(Mesa mesa, Alumno alumno, String tipo);
        //public void lanzarProductoDetalle(Producto producto);

    }
    interface Model {
        public void getMesas();
        public void inscribirse(Mesa mesa, Alumno alumno, String tipo);
    }
}
