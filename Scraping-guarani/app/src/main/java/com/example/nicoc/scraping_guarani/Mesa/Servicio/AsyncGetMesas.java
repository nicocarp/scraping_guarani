package com.example.nicoc.scraping_guarani.Mesa.Servicio;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
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

    public AsyncGetMesas(IGetMesas listener) {
        this.listener= listener;
    }

    @Override
    protected ArrayList<Mesa> doInBackground(Void... voids) {
        Guarani guarani = ManagerGuarani._getInstance();
        Alumno alumno = ManagerGuarani.alumno;
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        if (guarani != null){
            try {
                mesas = guarani._getMesasDeExamen(alumno.getCarreras());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mesas;
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Mesa> mesas) {
        super.onPostExecute(mesas);
        if (mesas != null)
            this.listener.onMesas(mesas);
        else
            this.listener.onError(ManagerGuarani.getError());
    }
}
