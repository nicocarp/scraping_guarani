package com.example.nicoc.scraping_guarani;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.Calendar;

import static android.os.Looper.getMainLooper;


/**
 * Created by Ivan on 26/11/2017.
 * Clase que se encarga de disparar una alarma para que ServicioIntent.java se ejecute cada 12 hs.
 */

public class Alarma extends Thread{

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

    public static Context contexto;
    private Class clase_del_servicio;
    private Calendar calendar;
    private int tiempo_repeticion = 1000 * 60 * 60 * 12;// cada 12 hs


    public Alarma (Context contexto, Class clase_del_servicio){
        this.contexto = contexto;
        this.clase_del_servicio = clase_del_servicio;
    }

    public static AlarmManager getAlarmMgr() {
        return alarmMgr;
    }

    public static void setAlarmMgr(AlarmManager alarmMgr) {
        Alarma.alarmMgr = alarmMgr;
    }

    public static PendingIntent getAlarmIntent() {
        return alarmIntent;
    }

    public static void setAlarmIntent(PendingIntent alarmIntent) {
        Alarma.alarmIntent = alarmIntent;
    }

    public Context getContexto() {
        return contexto;
    }

    public void setContexto(Context contexto) {
        this.contexto = contexto;
    }

    public Class getClase_del_servicio() {
        return clase_del_servicio;
    }

    public void setClase_del_servicio(Class clase_del_servicio) {
        this.clase_del_servicio = clase_del_servicio;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getTiempo_repeticion() {
        return tiempo_repeticion;
    }

    public void setTiempo_repeticion(int tiempo_repeticion) {
        this.tiempo_repeticion = tiempo_repeticion;
    }

    /* Cancelo la alarma para que ya no siga ejecutando el servicio */
    public static void cancelarAlarma()
    {
        // Si la alarma ha sido seteada, la cancelo.
        try{
            if (alarmMgr!= null) {
                alarmMgr.cancel(alarmIntent);
                Log.i("Alarma: ","ha sido cancelada");
            }
        }catch(Exception e){
            Log.i("Alarma: ","ha ocurrido un error al intentar cancelar la alarma, \n, " +
                    "puede haber sido provocado en LoginActivity, al cerrar sesion, \n," +
                    "lo cual hizo que el servicio se parara mientras se estaba ejecutando. \n" +
                    " Datos del Error: " + e.getMessage());
        }
    }


    public void run(){
        try {
            Log.i("Alarma: ", "ha comenzado la alarma");

            alarmMgr = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(contexto, clase_del_servicio);
            alarmIntent = PendingIntent.getService(contexto, 0, intent, 0);

            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 12); //Se disparará la alarma a las 12:00 pm
            calendar.set(Calendar.MINUTE, 00);//30
            calendar.set(Calendar.SECOND, 00);

            // Alarma seteada a las 12:00 PM y se repite el servicio cada 12hs.
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    tiempo_repeticion, alarmIntent);//60 segundos

        }catch(Exception e){
            Log.i("Alarma RUNTIME ERRROR: ","" + e.getMessage());
        }
    }


}
