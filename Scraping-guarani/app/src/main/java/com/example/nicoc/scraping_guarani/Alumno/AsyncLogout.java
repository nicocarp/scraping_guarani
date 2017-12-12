package com.example.nicoc.scraping_guarani.Alumno;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.ServicioIntent;

/**
 * Created by Ivan on 11/12/2017.
 */

class AsyncLogout extends AsyncTask<Void, Void, Void> {
    private AlumnoActivity alumnoActivity;

    public AsyncLogout(AlumnoActivity alumnoActivity) {
        this.alumnoActivity = alumnoActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alumnoActivity.mostrarProgressDialog();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intent = new Intent(alumnoActivity, LoginActivity.class);
        alumnoActivity.desloguearse();
        //Toast.makeText(alumnoActivity,"OKOKKKOKO",Toast.LENGTH_LONG);


    }

    @Override
    protected Void doInBackground(Void... voids) {
        boolean bandera = isMyServiceRunning(ServicioIntent.class);
        while(bandera){
            try {
                Thread.sleep(1000 * 10);
                bandera = isMyServiceRunning(ServicioIntent.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
                bandera = isMyServiceRunning(ServicioIntent.class);
            }

        }
        return null;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) alumnoActivity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
