package com.example.nicoc.scraping_guarani.Alumno;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;

/**
 * Created by nicoc on 06/12/17.
 */

public class IAlumno {
    public interface View {
        public void mostrarError(String error);
        public void setAlumno(Alumno alumno);
        public void setMesasEInscripciones(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones);
    }
    public interface Presenter {
        public void mostrarError(String error);
        public void desloguearse();
        public void getAlumno();
        public void onAlumno(Alumno alumno);
        void deslogueado();
        public void getMesasEInscripciones();
        public void onMesasEInscripciones(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones);
    }
    public interface Model {
        public void getAlumno();
        public void getMesasEInscripciones();
        public void desloguearse();
    }
}
