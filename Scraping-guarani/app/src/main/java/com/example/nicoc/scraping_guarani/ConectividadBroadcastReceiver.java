package com.example.nicoc.scraping_guarani;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/* Clase que se encarga de ver conectividad de la red,
* y reiniciar el servicio (ServicioIntent) cuando vuelve la conexion.
* */
public class ConectividadBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences  = context.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        Boolean aviso = preferences.getBoolean("aviso", false);

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean conectado =  activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (conectado && aviso){
            context.startService(new Intent(context.getApplicationContext(), ServicioIntent.class));
        }
    }
}
