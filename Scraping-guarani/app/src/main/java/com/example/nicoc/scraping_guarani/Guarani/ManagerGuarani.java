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

        return null;
    }

    public static Guarani getInstance(){


        return null;
    }



}
