package com.example.nicoc.scraping_guarani.Guarani.Modelos;

/**
 * Created by nicoc on 01/12/17.
 */

public class Auth {
    private String username;

    public Auth(String username, String password) {
        this.username = username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    @Override
    public boolean equals(Object obj){
        if (obj.getClass() == this.getClass()) {
            Auth a = (Auth) obj;
            return (a.getPassword().equals(this.password) && a.getUsername().equals(this.username));
        }
        return false;
    }
}
