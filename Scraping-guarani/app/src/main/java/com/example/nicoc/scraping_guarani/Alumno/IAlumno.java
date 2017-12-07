package com.example.nicoc.scraping_guarani.Alumno;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;

/**
 * Created by nicoc on 06/12/17.
 */

public class IAlumno {
    public interface View {
        public void mostrarError(String error);
        public void setAlumno(Alumno alumno);
    }
    public interface Presenter {
        public void mostrarError(String error);
        public void desloguearse();
        public void getAlumno();
        public void onAlumno(Alumno alumno);
        void deslogueado();
    }
    public interface Model {
        public void getAlumno();
        public void desloguearse();
    }
}
