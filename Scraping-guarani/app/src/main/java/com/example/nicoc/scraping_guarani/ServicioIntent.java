package com.example.nicoc.scraping_guarani;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

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
    protected void onHandleIntent(Intent intent) {
        Log.i("Entre al intent service...: ","Piola!!");
    }


}
