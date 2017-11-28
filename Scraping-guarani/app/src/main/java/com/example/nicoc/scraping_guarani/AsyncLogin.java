package com.example.nicoc.scraping_guarani;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Modelos.Carrera;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Guarani guarani = ManagerGuarani.getInstance(parametros[0], parametros[1]);
        if (guarani == null){
            return null;
        }

        try {
            Carrera carrera = guarani.getPlanMateria();
            Alumno alumno = guarani.getDatosAlumno(carrera);
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


