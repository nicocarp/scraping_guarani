package com.example.nicoc.scraping_guarani.Mesa.Servicio;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nicoc on 01/12/17.
 */

public class AsyncInscribirse extends AsyncTask<Object, Void, Boolean> {

    public interface IInscribirse {
        public void onError(String error);
        public void onInscripcion();
    }
    private IInscribirse listener;
    private String error;

    public AsyncInscribirse(IInscribirse listener){
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Object... objects) {

        Alumno alumno = (Alumno) objects[0];
        Mesa mesa = (Mesa) objects[1];
        String tipo = (String) objects[2];
        Log.i("Por enviar TIPO: ",tipo);
        Log.i("PROBANDO OTRO TIPO: ",tipo = String.valueOf(objects[2]));


        Guarani guarani = ManagerGuarani._getInstance();
        try {
            Boolean result =guarani.inscribirseMesaById(alumno, mesa, tipo);
            if (!result)
                error = guarani.getError();
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean inscripto) {
        super.onPostExecute(inscripto);
        if (inscripto)
            this.listener.onInscripcion();
        else
            this.listener.onError(error);
    }
}
