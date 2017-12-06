package com.example.nicoc.scraping_guarani.Login;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;

/**
 * Created by nicoc on 05/12/17.
 */

class LoginModel implements ILogin.Model, AsyncLogin.IView {

    private ILogin.Presenter presenter;
    public LoginModel(ILogin.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getAlumno() {

    }

    @Override
    public void login(String username, String password) {
        String[] parametros = { username, password};
        AsyncTask<String, Void, Alumno> myAsyncTask = new AsyncLogin(this).execute(parametros);
    }

    @Override
    public void mostrarError(String error) {
        this.presenter.mostrarError(error);
    }

    @Override
    public void logueado(Alumno alumno) {
        this.presenter.onAlumno(alumno);
    }

}
