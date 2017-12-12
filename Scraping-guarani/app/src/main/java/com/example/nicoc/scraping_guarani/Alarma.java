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
 */

public class Alarma extends Thread{

    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    public static  AlarmaTimer alarmaTimer = null;

    public static Context contexto;
    private Class clase_del_servicio;
    private Calendar calendar;
    private int tiempo_repeticion = 1000 * 20;// 12 hs
    private int hora;

    public static final int MAXIMA_REPETICION = 1;
    public static int REPETICION_ACTUAL = 1;

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
        try{
            if (alarmMgr!= null) {
                //Intent checkIntent = new Intent(contexto, clase_del_servicio);
                //boolean alarmUp = (PendingIntent.getService(contexto,0,checkIntent,PendingIntent.FLAG_NO_CREATE) != null);
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


            //https://developer.android.com/training/scheduling/alarms.html
            alarmMgr = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(contexto, clase_del_servicio);
            alarmIntent = PendingIntent.getService(contexto, 0, intent, 0);


            // Set the alarm to start at 14:51 p.m.
            calendar = Calendar.getInstance();
            //hora = calendar.get(Calendar.HOUR_OF_DAY) - 1;
            //if (hora == -1) hora = 23;
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 10); //Se disparará la alarma a las 12:00 pm
            calendar.set(Calendar.MINUTE, 15);//30
            calendar.set(Calendar.SECOND, 00);


            // setRepeating() lets you specify a precise custom interval--in this case,
            // 20 minutes.
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    tiempo_repeticion, alarmIntent);//60 segundos
            //tiempo_repeticion = 1000 * 60 = 1 minuto
            // setearle horas predefinidas: por ejemplo a las 12 y a las 12, y correr el servicio YA,
            //contexto.startService(intent);

        }catch(Exception e){
            Log.i("Alarma RUNTIME ERRROR: ","" + e.getMessage());
        }
    }


    //Este es el codigo que debo poner en la activity donde deseo verificar si la alarma esta viva
    //en caso de que no lo este, la creo de nuevo!!!!
    public static boolean estoyVivo(){
        /*boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent("com.my.package.MY_UNIQUE_ACTION"),
                PendingIntent.FLAG_NO_CREATE) != null);*/


        //Primera forma de saber si alarma esta viva....
        /*boolean alarmUp = (PendingIntent.getBroadcast(contexto, 0,
                new Intent("com.example.nicoc.scraping_guarani.Alarma"),
                PendingIntent.FLAG_NO_CREATE) != null);*/

        Intent intent = new Intent(contexto, ServicioIntent.class);
        boolean alarmUp = (PendingIntent.getService(contexto, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);

        return alarmUp;
        //Segunda forma de saber si alarma esta viva....
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(contexto, 234324243, intent, PendingIntent.FLAG_NO_CREATE);
        //si pendingIntent es null significa que la alarma no existe.
    }


    public void reiniciarAlarma()
    {
        this.contexto.stopService(new Intent(contexto,clase_del_servicio));
        this.contexto.startService(new Intent(contexto,clase_del_servicio));
    }


}
