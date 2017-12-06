package com.example.nicoc.scraping_guarani.Guarani;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;

import java.io.IOException;

/**
 * Created by nicoc on 24/11/17.
 */

public class ManagerGuarani {
    private static Guarani instance = null;
    private static Auth auth;

    private static String error="";

    public static Auth getAuth() {
        return auth;
    }

    public static void setAuth(Auth auth) {
        ManagerGuarani.auth = auth;
    }

    private ManagerGuarani(){

    }


    private static void setError(String err) {
        error = err;
    }

    public static String getError(){
        String e = error;
        error = "";
        return e;
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

    public static Guarani getInstance(){

        if (startSession())
            return instance;
        return null;
    }

    private static Guarani getInstanceGuarani() throws IOException {
        if (instance == null){
            instance = new Guarani();
        }
        return instance;
    }

    public static boolean startSession() {
        try {
            Guarani g = getInstanceGuarani();
            if (g.login(auth.getUsername(), auth.getPassword()))
                return true;
            else
                setError(g.getError());
        } catch (IOException e) {
            e.printStackTrace();
            setError("Error de conexion");
        }
        return false;
    }
}
