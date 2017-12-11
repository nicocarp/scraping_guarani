package com.example.nicoc.scraping_guarani.Alumno.Servicio;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.io.IOException;

/**
 * Created by nicoc on 01/12/17.
 */

public class AsyncInscribirse extends AsyncTask<Object, Void, Boolean> {

    public interface IInscribirse {
        public void onError(String error);
        public void onInscripcion(String mensaje);
    }
    private IInscribirse listener;
    private String error;
    private String mensaje;

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

        try {
            Guarani guarani = Guarani.getInstance();
            Boolean result =guarani.inscribirseMesaById(alumno, mesa, tipo);
            if (!result)
                error = guarani.getError();
            else
                mensaje = guarani.getMensaje();
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
            this.listener.onInscripcion(mensaje);
        else
            this.listener.onError(error);
    }
}
