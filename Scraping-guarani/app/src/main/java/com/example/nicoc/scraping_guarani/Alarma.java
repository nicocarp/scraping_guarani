package com.example.nicoc.scraping_guarani;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.Calendar;



/**
 * Created by Ivan on 26/11/2017.
 */

public class Alarma extends Thread{

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;

    private Context contexto;
    private Class clase_del_servicio;
    private Calendar calendar;
    private int tiempo_repeticion = 1000 * 60;//60 segundos
    private int hora;



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

    public static void cancelarAlarma()
    {
        // If the alarm has been set, cancel it.
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
            Log.i("Alarma: ","ha sido cancelada");
        }

    }

    public void run(){

        Log.i("Alarma: ","ha comenzado la alarma");

        //https://developer.android.com/training/scheduling/alarms.html
        alarmMgr = (AlarmManager)contexto.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(contexto, clase_del_servicio);
        alarmIntent = PendingIntent.getService(contexto, 0, intent, 0);



        // Set the alarm to start at 14:51 p.m.
        calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR_OF_DAY) - 1;
        if (hora == -1)hora = 23;
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora);//8
        calendar.set(Calendar.MINUTE, 1);//30




        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                tiempo_repeticion, alarmIntent);//60 segundos
        //tiempo_repeticion = 1000 * 60 = 1 minuto


    }


}
