package com.example.nicoc.scraping_guarani.Alumno.Servicio;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;

import java.io.IOException;

/**
 * Created by nicoc on 11/12/17.
 */

public class AsyncDesinscribirse extends AsyncTask<String, Void, Boolean> {

    private String mensaje;
    private String error;
    private IDesinscribirse listener;

    public interface IDesinscribirse{
        public void onError(String error);
        public void onDesinscripcion(String mensaje);

    }
    public AsyncDesinscribirse(AsyncDesinscribirse.IDesinscribirse listener){
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String codigo_carrera = strings[0];
        String codigo_materia = strings[1];
        Boolean result = false;
        try {
            Guarani guarani = Guarani.getInstance();
            result = guarani.desinscribirseDeMesa(codigo_carrera, codigo_materia);
            if (result)
                this.mensaje = guarani.getMensaje();
            else
                this.error = guarani.getError();
        } catch (IOException e) {
            e.printStackTrace();
            this.error = "Error en la conexion";
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean desinscripto) {
        super.onPostExecute(desinscripto);
        if (desinscripto)
            listener.onDesinscripcion(this.mensaje);
        else
            listener.onError(this.error);

    }
}
