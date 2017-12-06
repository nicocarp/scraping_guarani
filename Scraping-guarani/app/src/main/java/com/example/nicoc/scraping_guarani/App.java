package com.example.nicoc.scraping_guarani;

import android.app.Application;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Database.Alumno_;
import com.example.nicoc.scraping_guarani.Database.ManagerDB;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;

/**
 * Created by nicoc on 01/12/17.
 */

public class App extends Application {
    private final String URL ="";
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // inicio base de datos.
            ManagerDB manager_db = new ManagerDB(this, "guarani-db");

            // this.deleteDatabase("notes-db");
        } catch (Exception e){
            // SIN BD SE ROMPE LA APP. sacar trucatch despues
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
