package com.example.nicoc.scraping_guarani;

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



    private static void setError(String error) {
        error = error;
    }


    public ManagerGuarani(){
    }

    public static String getError(){
        return error;
    }
    public static Guarani getInstance(String username, String password){

        if (instance == null){
            try {
                Guarani g = new Guarani();
                if (g.login(username, password)){
                    instance = g;
                }else
                    error = g.getError();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


}
