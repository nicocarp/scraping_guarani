package com.example.nicoc.scraping_guarani;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity;
import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServicioIntent extends IntentService{

    NotificationCompat.Builder mBuilder;
    SharedPreferences preferences;

    private static final int MAXIMA_REPETICION = 2;
    private boolean bandera = true;
    private int contador = 1;
    private ConectividadBroadcastReceiver receiver;

    public ServicioIntent() {
        super(" Intent Servicio Scraping");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Intent Service","Me crearon.");
        preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences.Editor edit = preferences.edit();
        if (!isNetworkAvailable()){
            edit.putBoolean("aviso", true);
            edit.commit();
            Log.i("Intent Service"," No hay conexion.");
        }
        else{
            edit.putBoolean("aviso", false);
            edit.commit();
            Log.i("Intent Service"," Hay conexion.");

            String usuario = preferences.getString("username", "");
            String password = preferences.getString("password", "");

            if (usuario.isEmpty() || password.isEmpty())
                return; // debemos mandar notificar esto es un error.
            Guarani.setAuth(new Auth(usuario, password));

            while(bandera==true && contador<=MAXIMA_REPETICION){
                try {
                    ArrayList<Mesa> mesas = Guarani.getInstance().getMesasDeExamen();
                    ArrayList<Inscripcion> inscripciones = Guarani.getInstance().getMesasAnotadas();
                    crearNotificacionMesasDisponibles(mesas, inscripciones);
                    bandera = false;
                } catch (IOException e) { // es este el tipo de error que requiere nueva peticion.
                    e.printStackTrace();
                    esperar();
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    esperar();
                }
            }

        }
        stopSelf();
    }


    private void esperar() {
        bandera = true;
        contador++;
        try {
            Thread.sleep(1000 * 15);//espero 15 segundos.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Seteamos en Preferencias las mesas e inscripciones activas. Notificamos si hay mesas nuevas.
     * Prec: mesas e inscripciones no deben ser null, sino []
     * @param mesas
     * @param inscripciones
     */
    private void crearNotificacionMesasDisponibles(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones){

        SharedPreferences.Editor edit = preferences.edit();
        Boolean notificar, lanzar_broadcast = false;
        Gson gson = new Gson();

        String mesas_json = gson.toJson(mesas);
        String inscripciones_json = gson.toJson(inscripciones);

        String mesas_json_guardadas = preferences.getString("mesas", "");
        String inscripciones_json_guardadas = preferences.getString("inscripciones", "");
        if (mesas_json_guardadas.isEmpty())
            mesas_json_guardadas = "[]";
        if (inscripciones_json_guardadas.isEmpty())
            inscripciones_json_guardadas = "[]";

        Type collectionType = new TypeToken<ArrayList<Mesa>>(){}.getType();
        ArrayList<Mesa> mesas_guardadas = new Gson().fromJson(mesas_json_guardadas, collectionType);
        collectionType = new TypeToken<ArrayList<Inscripcion>>(){}.getType();
        ArrayList<Inscripcion> inscripciones_guardadas = new Gson().fromJson(inscripciones_json_guardadas, collectionType);

        notificar = (mesas_guardadas.size() < mesas.size());
        lanzar_broadcast = (inscripciones_guardadas.size() != inscripciones.size() || notificar);

        edit.putString("mesas", mesas_json);
        edit.putString("inscripciones", inscripciones_json);
        edit.commit();

        if (notificar)
            notificar();
        if (lanzar_broadcast)
            enviarBroadcast();

    }


    private void notificar(){
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        int icono = R.mipmap.ic_launcher;
        Intent intent = new Intent(ServicioIntent.this, AlumnoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(ServicioIntent.this, 0, intent, 0);
        mBuilder =new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("TNT")
                .setContentText("Hay mesas de examenes disponibles.")
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);
        mNotifyMgr.notify(123, mBuilder.build());
    }

    private void enviarBroadcast()
    {
        Log.i("MyService....","estoy en sendBroadcast.");
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(ServicioIntent.this);

        Intent resultIntent = new Intent("MesasActivity");//le pongo un nombre al intent asi se como atraparlo despues.
        //resultIntent.putExtra("TNT", "Hay mesas de examenes");
        Bundle bundle = new Bundle();
        bundle.putString("Nombre","Hay mesas de examenes disponibles.");
        resultIntent.putExtras(bundle);
        broadcastManager.sendBroadcast(resultIntent);//envio el intent a toda la plataforma para que alguien lo capture.

    }


    //Metodo devuelve si hay conexion a Internet o no.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Intent Service"," Me mataron.");
    }



}
