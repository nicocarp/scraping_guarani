package com.example.nicoc.scraping_guarani;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class ConectividadBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences  = context.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        Boolean aviso = preferences.getBoolean("aviso", false);

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean conectado =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i("BROADCAST RECEVIER", "0-me llamo la plataforma.");
        Log.i("BROADCAST RECEVIER", "Avisar "+aviso +" Conectado:"+ conectado);
        if (conectado && aviso){
            Log.i("BROADCAST RECEVIER", "1- estoy conectado");
            context.startService(new Intent(context.getApplicationContext(), ServicioIntent.class));
            Log.i("BROADCAST RECEVIER", "2- despues de llamar al servicio");
        }
    }
}
