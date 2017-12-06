package com.example.nicoc.scraping_guarani.Alumno;

/**
 * Created by nicoc on 06/12/17.
 */

public class IAlumno {
    public interface View {
        public void mostrarError(String error);
    }
    public interface Presenter {
        public void desloguearse();

        void deslogueado();
    }
    public interface Model {
        public void desloguearse();
    }
}
