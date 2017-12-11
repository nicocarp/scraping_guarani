package com.example.nicoc.scraping_guarani.Alumno.Servicio;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;

import java.io.IOException;

/**
 * Created by nicoc on 06/12/17.
 */

public class AsyncDesloguear extends AsyncTask<Void, Void, Void> {

    public interface IDesloguear{
        public void onSuccess();
        public void onError(String error);
    }
    private IDesloguear listener;
    private String error;

    public AsyncDesloguear(IDesloguear listener){
        this.listener = listener;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Guarani.getInstance().desloguearse();
        } catch (IOException e) {
            e.printStackTrace();
            this.error = "Error en la conexion";
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.error=e.getMessage().toString();
        }
        return null;
    }


}
