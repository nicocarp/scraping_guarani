package com.example.nicoc.scraping_guarani;

/**
 * Created by Ivan on 06/12/2017.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Alarma;


import java.util.Calendar;

/**
 * Created by Ivan on 05/12/2017.
 */
//Esta alarma se debe ejecutar 3 veces, una vez por hora, desde que comienza la Alarma principal.
public class AlarmaTimer extends Thread{
    private static AlarmManager alarmMgr;
    private static PendingIntent alarmIntent;
    public static int REPETICION_ACTUAL = 1;
    public static int MAXIMA_REPETICION = 1;
    public static boolean ACTIVO = false;
    private Calendar calendar;
    private int tiempo_repeticion = 1000 * 60 * 60;// SE DEBE REPETIR 3 VECES CADA 1 hs
    private int hora;
    public static Context contexto = null;

    public static void cancelarAlarma()
    {
        // If the alarm has been set, cancel it.
        try{
            if (alarmMgr!= null) {
                //Intent checkIntent = new Intent(contexto, clase_del_servicio);
                //boolean alarmUp = (PendingIntent.getService(contexto,0,checkIntent,PendingIntent.FLAG_NO_CREATE) != null);
                alarmMgr.cancel(alarmIntent);
                Log.i("AlarmaTimer: ","ha sido cancelada");
                REPETICION_ACTUAL = 1;
                ACTIVO = false;
                Log.i("AlarmaTimer-Valor-REPETICION_ACTUAL =  ","" + REPETICION_ACTUAL);
                Log.i("AlarmaTimer-Valor-ACTIVO =  ","" + ACTIVO);
            }
        }catch(Exception e){
            Log.i("Alarma: ","ha ocurrido un error al intentar cancelar la alarma, \n, " +
                    "puede haber sido provocado en LoginActivity, al cerrar sesion, \n," +
                    "lo cual hizo que el servicio se parara mientras se estaba ejecutando. \n" +
                    " Datos del Error: " + e.getMessage());
        }


    }



    public void run(){

        try{
            if (contexto != null){

                Log.i("AlarmaTimer: ","ha comenzado la alarma");

                //https://developer.android.com/training/scheduling/alarms.html
                alarmMgr = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(contexto, ServicioIntent.class);
                alarmIntent = PendingIntent.getService(contexto, 1, intent, 0);



                // Servicio empieza 1 hora mas tarde desde que se inicio
                calendar = Calendar.getInstance();
                hora = calendar.get(Calendar.HOUR_OF_DAY);
                Log.i("AlarmaTimer - HORA ACTUAL: ","" + hora);
                hora++;
                int minutos = calendar.get(Calendar.MINUTE);

                Log.i("AlarmaTimer - MINUTO ACTUAL: ","" + minutos);
                if (hora == 24)hora = 00;
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hora);//8
                calendar.set(Calendar.MINUTE, 1);//30




                //Nota: poner calendar 1 hs despues,
                //hora = calendar.get(Calendar.HOUR_OF_DAY) + 1;
                alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        tiempo_repeticion, alarmIntent);//60 segundos
                //tiempo_repeticion = 1000 * 60 = 1 minuto
            }
            else{
                Log.i("AlarmaTimer: ","che el contexto es nulo....");
                contexto = Alarma.contexto;
                Log.i("AlarmaTimer chequeando contexto de Alarma: ","" + contexto);
            }
        }catch(Exception e){
            Log.i("AlarmaTimer RUNTIME ERRROR: ","" + e.getMessage());
        }



    }


    public static void morir(){
        try {
            cancelarAlarma();
            //Thread.sleep(1000 * 60 * 2);//6 horas
            REPETICION_ACTUAL = 1;
            ACTIVO = false;
            contexto = null;
            Log.i("AlarmaTimer: ","me han matado! PUTOOOOOOOOO");
        } catch (Exception e) {
            Log.i("AlarmaTimer: ","me han matado!");
        }
    }

}