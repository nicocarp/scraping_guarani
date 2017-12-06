package com.example.nicoc.scraping_guarani.Login;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.google.gson.Gson;

import static com.example.nicoc.scraping_guarani.R.id.checkBoxRememberMe;
import static com.example.nicoc.scraping_guarani.R.id.txtUsername;

/**
 * Created by nicoc on 05/12/17.
 */

class LoginPresenter implements ILogin.Presenter {

    private ILogin.View view;
    private ILogin.Model model;
    private SharedPreferences preferences;

    public LoginPresenter(ILogin.View view, SharedPreferences preferences) {
        this.view = view;
        this.preferences = preferences;
        this.model = new LoginModel(this);
    }

    @Override
    public void mostrarError(String error) {
        this.view.mostrarError(error);
    }

    @Override
    public void getAlumno() {
        String json = this.preferences.getString("alumno_json", "");
        if (!json.isEmpty()){
            Alumno alumno = new Gson().fromJson(json, Alumno.class);
            this.view.logueado(alumno);
        }else{
            this.view.iniciarViews();
        }
    }

    @Override
    public void login(String username, String password) {
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("username",username);
        edit.putString("password",password);
        edit.commit();
        this.model.login(username, password);
    }

    @Override
    public void onAlumno(Alumno alumno) {
        this.storeAlumno(alumno);
        this.view.logueado(alumno);
    }

    private String storeAlumno(Alumno alumno){

        String obj = new Gson().toJson(alumno);
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putString("alumno_json",obj );
        edit.commit();
        return obj;

    }
}
