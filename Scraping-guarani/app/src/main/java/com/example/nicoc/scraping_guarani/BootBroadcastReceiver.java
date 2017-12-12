package com.example.nicoc.scraping_guarani;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        SharedPreferences preferences = context.getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String usuario = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        if (usuario.isEmpty() || password.isEmpty())
            return; // debemos mandar notificar esto es un error.

        Alarma alarma = new Alarma(context,ServicioIntent.class);
        alarma.start();
    }
}
