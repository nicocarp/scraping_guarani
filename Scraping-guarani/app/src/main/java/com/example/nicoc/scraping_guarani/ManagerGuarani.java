package com.example.nicoc.scraping_guarani;

import java.io.IOException;

/**
 * Created by nicoc on 24/11/17.
 */

public class ManagerGuarani {
    private static Guarani instance = null;
    private static String url_base = "";

    public ManagerGuarani(String url, String username, String password){
        this.url_base = url;
        Guarani g = null;
        try {
            g = new Guarani(url);
            if (g.login(username, password))
                this.instance = g;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Guarani getInstance(){
        return instance;
    }


}
