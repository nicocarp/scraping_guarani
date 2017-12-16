package com.example.nicoc.scraping_guarani.Guarani.Modelos;

/**
 * Created by nicoc on 01/12/17.
 * Esta clase representa la cuenta de usuario necesaria para realizar peticiones http
 *  al servidor SIU Guarani. Es un objeto inmutable.
 */

public class Auth {
    private String username;
    private String password;

    private Auth(){}
    public Auth(String username, String password) {
        this.username = username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object obj){
        if (obj.getClass() == this.getClass()) {
            Auth a = (Auth) obj;
            return (a.getPassword().equals(this.password) && a.getUsername().equals(this.username));
        }
        return false;
    }
}
