package com.example.nicoc.scraping_guarani.Alumno.Servicio;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nicoc on 01/12/17.
 */

public class AsyncGetMesas extends AsyncTask<Void, Void, ArrayList<Mesa>> {

    public interface IGetMesas{
        public void onError(String error);
        public void onMesas(ArrayList<Mesa> mesas);
    }
    private IGetMesas listener;
    private String error;

    public AsyncGetMesas(IGetMesas listener) {

        this.listener= listener;
        this.error = "";
    }

    /**
     * Retornamos las mesas de examenes habilitadas para inscripcion.
     * @param voids
     * @return ArrayList<Mesa>
     */
    @Override
    protected ArrayList<Mesa> doInBackground(Void... voids) {
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        try {
            Guarani guarani = Guarani.getInstance();
            mesas = guarani.getMesasDeExamen();
        } catch (IOException e) {
            e.printStackTrace();
            this.error = "Error en la conexion";
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.error = e.getMessage();
        }
        return mesas;
    }

    @Override
    protected void onPostExecute(ArrayList<Mesa> mesas) {
        super.onPostExecute(mesas);
        if (error.isEmpty())
            this.listener.onMesas(mesas);
        else
            this.listener.onError(this.error);
    }
}
