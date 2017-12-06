package com.example.nicoc.scraping_guarani.Login;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;

/**
 * Created by nicoc on 05/12/17.
 */

public interface ILogin {
    interface View {
        public void mostrarError(String mensaje);
        public void logueado(Alumno alumno);
        public void getAlumno();

        public void iniciarViews();
    }
    interface Presenter {
        public void mostrarError(String error);
        public void getAlumno();
        public void login(String username, String password);
        public void onAlumno(Alumno alumno);
    }
    interface Model {
        public void getAlumno();

        public void login(String username, String password);
    }
}
