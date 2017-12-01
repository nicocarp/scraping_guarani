package com.example.nicoc.scraping_guarani;

import com.example.nicoc.scraping_guarani.Modelos.Alumno;

import java.io.IOException;

/**
 * Created by nicoc on 24/11/17.
 */

public class ManagerGuarani {
    private static Guarani instance = null;
    private static String url_base = "";
    private String username = "";
    private String password = "";
    private static String error="";
    public static Alumno alumno;


    private static void setError(String err) {
        error = err;
    }

    private static void setAlumno(Alumno a){
        alumno = a;
    }
    public ManagerGuarani(){
    }

    public static String getError(){
        return error;
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

        if (instance == null){
            try {
                Guarani g = new Guarani();
                if (g.login(username, password)){
                    instance = g;
                }else
                    setError(g.getError());
            } catch (IOException e) {
                e.printStackTrace();
                setError("Error en la conexion");
            }
        }
        return instance;
    }


}
