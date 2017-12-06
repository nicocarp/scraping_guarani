package com.example.nicoc.scraping_guarani;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Mesa.Listado.MesaActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServicioIntent extends IntentService {

    NotificationCompat.Builder mBuilder;
    private boolean bandera = true;
    private int contador = 0;

    public ServicioIntent() {
        super(" Intent Servicio Scraping");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Intent Service: ","Me creeeeee!!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Verifico si hay internet....

        /*try {
            Thread.sleep(1000 * 25);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        //sendBroadcast(new Intent("YouWillNeverKillMe"));
        if (!isNetworkAvailable()){
            //intento 3 veces en el dia sino chau...


            Log.i("Internet...: ","No hay conexión a Internet");

            //sendBroadcast(new Intent("YouWillNeverKillMe"));
            if (Alarma.alarmaTimer.ACTIVO==false){
                Alarma.alarmaTimer = new AlarmaTimer();
                Alarma.alarmaTimer.contexto = this;
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

            stopSelf();
            return;

        }
        else{
            Alarma.alarmaTimer.morir();
            Log.i("Internet...: ","Hay conexión a Internet");

        }


        Log.i("Intent Service: ","Estoy funcionado!!!!!");
        stopSelf();


        Log.i("Intent Service: ","Estoy funcionado!!!!!");

        //1. consultar la BD para traer user y pass
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String usuario = loginPreferences.getString("username", "");
        String password = loginPreferences.getString("password", "");

        if (usuario.isEmpty() || password.isEmpty())
            return; //
        //2.Verifico si ese usuario esta logueado
        ManagerGuarani.setAuth(new Auth(usuario, password));
        Guarani guarani = ManagerGuarani.getInstance();
        if (guarani == null){//usuario no logueado
            //3.a Hago una notificacion mostrando el error.
            //3.b Si estoy en cualquiera de las 2 activitys, muestro mje y vuelvo a activity_login: broadcastear en ambas.
            Log.i("Guarani...: ","Estoy Muerto");
            String error = ManagerGuarani.getError();
            Alarma.cancelarAlarma();
        }
        else{
            //4a.Consulto al modulo de scraping: carrera, alumno, mesas.
            while(bandera==true && contador<=Alarma.MAXIMA_REPETICION){
                try
                {
                    ArrayList<Carrera> carreras = guarani.getPlanDeEstudios();
                    Alumno alumno = guarani.getDatosAlumno(carreras);
                    ArrayList<Mesa> mesas = guarani.getMesasDeExamen(carreras.get(1));
                    Log.i("Guarani...: ","Estoy Vivo");
                    bandera = false;
                    if (mesas.size()>0){//si hay mesas
                        //4.b Pasar las mesas
                        //4.b.1 No estoy usando la app: armo notificación con pendingIntent
                        //4.b.2 Estoy en activity_login: consulto la bd.
                        //4.b.3 Estoy en activity_alumno: consulto la bd.
                        //4.b.4 Estoy en activty_mesa: recibo un broadcast y refresco pantalla.

                        //Nota: por ahora mando notificacion
                        Log.i("Guarani...: ","Hay Mesas");
                        //crearNotificacionMesasDisponibles(mesas);

                        try{
                            Log.i("Guarani...: ","Hay Mesas");
                            Log.i("Guarani...: ", mesas.get(0).getFecha());
                            Log.i("Guarani...: ", mesas.get(0).getSede());
                            Log.i("Guarani...: ","" + mesas.get(0).getTipoMesa());
                            Log.i("Guarani...: ", "" + mesas.get(0).getProfesores());
                            Log.i("Guarani...: ", mesas.get(0).getCarrera()+"");
                            Log.i("Guarani...: ", mesas.get(0).getMateria()+"");
                            Log.i("Guarani...: ", mesas.get(0).getMaterias_necesarias()+"");
                            Log.i("Guarani...: ", mesas.get(0).getTurno());
                            //crearNotificacionMesasDisponibles(mesas);
                        }catch(Exception e){
                            Log.i("Guarani...ERROR: ", e.getMessage());
                        }

                        crearNotificacionMesasDisponibles(mesas);//solucion 4.b.1
                        sendBroadcast();//solucion 4.b.2, 4.b.3 y 4.b.4
                    }
                    else{
                        Log.i("Intent Service: ", "No hay mesas disponibles.");
                    }





                }catch(Exception e){
                    e.printStackTrace();
                    Log.i("Error al consultar carrera, alumno, mesas: ",e.getMessage());
                    Log.i("N° de Intento: ",""+contador);
                    //5.¿Como prosigo?
                    //https://stackoverflow.com/questions/21550204/how-to-automatically-restart-a-service-even-if-user-force-close-it
                    bandera = true;
                    contador++;
                    try {
                        Thread.sleep(1000 * 15);//espero 15 segundos.
                    } catch (Exception e1) {
                        Log.i("Error en el Servicio Intent, problema en sleep(): ", "" + e1.getMessage());
                    }
                }
            }//while




        }//else


        //Me mato !!!!!!! waaaaa
        stopSelf();
    }


    private void crearNotificacionMesasDisponibles(ArrayList<Mesa> mesas){
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        int icono = R.mipmap.ic_launcher;
        Intent intent = new Intent(ServicioIntent.this, MesaActivity.class);

        /* Nota: yo necesito pasar las mesas por intent. Problema: lanza parcelable stackoverflow
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Mesas",mesas);
        intent.putExtras(bundle);
        */

        /*try{
            intent.putParcelableArrayListExtra("Mesas",mesas);
        }catch(Exception e){
            Log.i("Intent Service ERROR:  ",e.getMessage());
        }*/


        PendingIntent pendingIntent = PendingIntent.getActivity(ServicioIntent.this, 0, intent, 0);
        //aca iria toda la informacion que recibimos del scraping dentro del intent


        mBuilder =new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("TNT")
                .setContentText("Hay mesas de examenes disponibles.")
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);

        mNotifyMgr.notify(1, mBuilder.build());//
    }




    private void sendBroadcast()
    {
        Log.i("MyService....","estoy en sendBroadcast.");
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(ServicioIntent.this);

        Intent resultIntent = new Intent("MesasActivity");//le pongo un nombre al intent asi se como atraparlo despues.
        //resultIntent.putExtra("TNT", "Hay mesas de examenes");
        Bundle bundle = new Bundle();
        bundle.putString("Nombre","Nuevas mesas disponibles!.");
        resultIntent.putExtras(bundle);
        broadcastManager.sendBroadcast(resultIntent);//envio el intent a toda la plataforma para que alguien lo capture.

    }


    //Metodo devuelve si hay conexion a Internet o no.
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Intent Service:  ","Me moriii!!");
    }

    /* public static void main(String [] args)
    {
        ServicioIntent i = new ServicioIntent();
        Intent intent = new Intent();
        i.startService(intent);
        System.out.println("Hijo de puta");
    }*/


    private void timer(){
        CountDownTimer timer = new CountDownTimer(1000 * 60, 1000) {
            @Override
            public void onTick(long l) {
                Log.i("COUNT_DOWN_TIMER: ", "" + l / 1000 + " segundos.");
            }

            @Override
            public void onFinish() {
                Alarma.REPETICION_ACTUAL = 1;
                Log.i("COUNT_DOWN_TIMER: ", "" + Alarma.REPETICION_ACTUAL);
            }
        }.start();
    }

}
