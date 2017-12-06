package com.example.nicoc.scraping_guarani.Login;

import android.os.AsyncTask;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;

import java.io.IOException;

/**
 * Created by nicoc on 23/11/17.
 */

public class AsyncLogin extends AsyncTask<String, Void, Alumno> {

    public interface IView{
        public void mostrarError(String s);
        public void logueado(Alumno alumno);
    }
    private IView listener;
    private String error;

    public AsyncLogin(IView activity) {
        this.listener= activity;
    }

    /**
     * Retorna el alumno correspondiente con los parametros (String username, String password)
     * @param parametros
     * @return Alumno.
     */
    @Override
    protected Alumno doInBackground(String... parametros) {
        String username = parametros[0];
        String password = parametros[1];

        try {
            Guarani.setAuth(new Auth(username, password));
            Guarani guarani = Guarani.getInstance();
            Alumno alumno = guarani.getAlumno();
            return alumno;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            this.error = e.getMessage().toString();
        } catch (IOException e) {
            e.printStackTrace();
            this.error = "Error en la conexion";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Alumno alumno) {
        super.onPostExecute(alumno);
        if ( alumno != null)
            this.listener.logueado(alumno);
        else{
            this.listener.mostrarError(this.error);
        }
    }
}


