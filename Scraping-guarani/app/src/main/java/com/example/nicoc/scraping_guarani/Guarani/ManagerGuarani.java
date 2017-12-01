package com.example.nicoc.scraping_guarani.Guarani;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;

import java.io.IOException;

/**
 * Created by nicoc on 24/11/17.
 */

public class ManagerGuarani {
    private static Guarani instance = null;
    private Guarani guarani;
    public static Auth getAuth() {
        return auth;
    }

    public static void setAuth(Auth auth) {
        ManagerGuarani.auth = auth;
    }

    private static Auth auth;

    private static String error="";
    public static Alumno alumno;
    /* Nuevo */
    private static ManagerGuarani manager;

    public ManagerGuarani(Auth _auth){
        auth = _auth;
        manager = this;
    }
    public ManagerGuarani(){
        manager = this;
    }


    private static void setError(String err) {
        error = err;
    }

    private static void setAlumno(Alumno a){
        alumno = a;
    }


    public static String getError(){
        return error;
    }

    public static ManagerGuarani getInstanceManager (){
        if (manager == null)
            throw new RuntimeException("Error al iniciar Guarani");
        return manager;
    }

    public static Guarani _getInstance(){
        if (instance == null){
            try {
                instance = new Guarani();
            } catch (IOException e) {
                e.printStackTrace();
                setError("Error en la conexion"); // mejorar este mensaje y leerlo de strings
            }
        }
        return instance;
    }

    public static Guarani getInstance(String username, String password){

        try {
            if (instance == null){
                Guarani g =  new Guarani();
                instance = g;
            }
            if (!instance.login(username, password))
                setError(instance.getError());
            return instance;
        }
        catch (IOException e) {
            e.printStackTrace();
            setError("Error en la conexion");
        }
        return null;
    }

    private Guarani getInstanceGuarani() throws IOException {
        if (this.guarani == null){
            return new Guarani();
        }
        return this.guarani;
    }

    public boolean startSession() {
        try {
            Guarani g = this.getInstanceGuarani();
            if (g.login(this.auth.getUsername(), this.auth.getPassword()))
                return true;
            else
                this.setError(g.getError());
        } catch (IOException e) {
            e.printStackTrace();
            this.setError("Error de conexion");
        }
        return false;
    }
}
