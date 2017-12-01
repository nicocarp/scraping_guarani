package com.example.nicoc.scraping_guarani;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.util.Calendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServicioIntent extends IntentService {


    public ServicioIntent() {
        super(" Intent Servicio Scraping");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Entre al intent service...: ","Me creeeeee!!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Entre al intent service...: ","Estoy funcionado!!!!!");

        

        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Entre al intent service...: ","Me moriii!!");
    }

    /* public static void main(String [] args)
    {
        ServicioIntent i = new ServicioIntent();
        Intent intent = new Intent();
        i.startService(intent);
        System.out.println("Hijo de puta");
    }*/

}
