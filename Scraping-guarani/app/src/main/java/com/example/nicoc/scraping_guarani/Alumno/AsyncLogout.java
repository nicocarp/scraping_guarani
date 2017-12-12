package com.example.nicoc.scraping_guarani.Alumno;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.ServicioIntent;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

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
                Thread.sleep(1000 * 5);
                bandera = isMyServiceRunning(ServicioIntent.class);
            } catch (InterruptedException e) {
                e.printStackTrace();
                bandera = isMyServiceRunning(ServicioIntent.class);
            }
        }
        return null;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) alumnoActivity.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void eliminarServicio(){
        ActivityManager am = (ActivityManager) alumnoActivity.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        Iterator<ActivityManager.RunningAppProcessInfo> iter = runningAppProcesses.iterator();

        while(iter.hasNext()){
            ActivityManager.RunningAppProcessInfo next = iter.next();

            String pricessName = alumnoActivity.getPackageName() + ":service";

            if(next.processName.equals(pricessName)){
                Process.killProcess(next.pid);
                break;
            }
        }
    }


    //Nota: para matar el intent service
    //https://stackoverflow.com/questions/11258083/how-to-force-an-intentservice-to-stop-immediately-with-a-cancel-button-from-an-a
    //https://stackoverflow.com/questions/8709989/how-to-stop-intentservice-in-android
}
