package com.example.nicoc.scraping_guarani;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Modelos.Carrera;

import java.io.IOException;

/**
 * Created by nicoc on 23/11/17.
 */

public class AsyncLogin extends AsyncTask<String, Void, Alumno> {

    public interface IView{
        public void mostrarError(String s);
        public void logueado(Alumno alumno);
    }
    private IView activity;

    public AsyncLogin(IView activity) {
        this.activity = activity;
    }

    /**
     * Precondicion: recibe parametros (url, user, password)
     * @param parametros
     * @return
     */
    @Override
    protected Alumno doInBackground(String... parametros) {
        String username = parametros[0];
        String password = parametros[1];

        Guarani guarani = ManagerGuarani.getInstance(username, password);
        if (guarani == null){
            return null;
        }
        try {
            Alumno alumno = guarani.getAlumno();
            ManagerGuarani.alumno = alumno;
            return alumno;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Alumno alumno) {
        super.onPostExecute(alumno);
        if (alumno != null){
            this.activity.logueado(alumno);
        }
        else{
            String error = ManagerGuarani.getError();
            this.activity.mostrarError(error);
        }
    }
}


