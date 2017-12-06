package com.example.nicoc.scraping_guarani;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("RestartServiceReceiver...: ","buenasss..");
        /*try {
            Thread.sleep(1000 * 60);// 1 minuto
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        try{
            if (Alarma.alarmaTimer.ACTIVO==false){
                Alarma.alarmaTimer = new AlarmaTimer();
                Alarma.alarmaTimer.contexto = Alarma.contexto;
                Alarma.alarmaTimer.start();
                Alarma.alarmaTimer.ACTIVO=true;
                Log.i("BROADCASTRECEIRVER...: ","Creeeee la alarmatimeeeeerrrrrr!!!..");
            }
            else if (Alarma.alarmaTimer.REPETICION_ACTUAL<=Alarma.alarmaTimer.MAXIMA_REPETICION){
                Alarma.alarmaTimer.REPETICION_ACTUAL++;
                Log.i("BROADCASTRECEIRVER...: ","Aumente.");
            }
            else{

                if(Alarma.alarmaTimer!=null)Alarma.alarmaTimer.morir();
                Log.i("BROADCASTRECEIRVER...: ","Mate a la alarmatimerrrrr.");
            }
            //context.startService(new Intent(context.getApplicationContext(), ServicioIntent.class));
        }catch(Exception e){
            Log.i("RestartServiceReceiver...: ","error.." + e.getMessage());
        }
    }
}
