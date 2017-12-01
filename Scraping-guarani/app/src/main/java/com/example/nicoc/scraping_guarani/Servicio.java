package com.example.nicoc.scraping_guarani;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Modelos.Mesa;

import java.io.IOException;
import java.util.ArrayList;

public class Servicio extends Service {

    private static final int COUNT_NOTIFICATION_ID = 123456;

    public Servicio() {
    }



    final class MyThreadClass implements Runnable
    {

        int service_id;
        NotificationCompat.Builder mBuilder;

        MyThreadClass(int service_id)
        {
            this.service_id = service_id;
        }

        @Override
        public void run() {



            synchronized (this)
            {
                //aca iria la llamada a la API de scraping
                boolean estoy_logueado = true;
                boolean hay_mesas = true;

                //1. consultar la BD para traer user y pass
                String usuario = "27042881";
                String password = "valenti2";


                //2.Verifico si ese usuario esta logueado
                Guarani guarani = ManagerGuarani.getInstance(usuario, password);
                if (guarani == null){//usuario no logueado
                    //3.a Hago una notificacion mostrando el error.
                    //3.b Si estoy en cualquiera de las 2 activitys, muestro mje y vuelvo a activity_login: broadcastear en ambas.
                    String error = ManagerGuarani.getError();
                    Alarma.cancelarAlarma();
                }
                else{
                    //4a.Consulto al modulo de scraping: carrera, alumno, mesas.
                    try
                    {
                        ArrayList<Carrera> carreras = guarani.getPlanDeEstudios();
                        Alumno alumno = guarani.getDatosAlumno(carreras);
                        ArrayList<Mesa> mesas = guarani.getMesasDeExamen(carreras.get(1));

                        if (mesas.size()>0){//si hay mesas
                            //4.b Pasar las mesas
                            //4.b.1 No estoy usando la app: armo notificación con pendingIntent
                            //4.b.2 Estoy en activity_login: consulto la bd.
                            //4.b.3 Estoy en activity_alumno: consulto la bd.
                            //4.b.4 Estoy en activty_mesa: recibo un broadcast y refresco pantalla.

                            //Nota: por ahora mando notificacion
                            Log.i("creando notificacion....","....");
                            crearNotificacionMesasDisponibles(mesas);
                        }





                    }catch(IOException e){
                        e.printStackTrace();
                        Log.i("Error al consultar carrera, alumno, mesas: ",e.getMessage());
                        //5.¿Como prosigo?
                    }

                }





                //crearNotificacion(); //aparecera un mensaje en la barra de notificaciones cuando termine el servicio.
                //sendBroadcast(); //Si estoy en SecondActivity, se me actualiza
                stopSelf(service_id); //este lo usamos para detener el servicio
            }

        }


        private void crearNotificacionMesasDisponibles(ArrayList<Mesa> mesas){
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.ic_launcher;
            Intent intent = new Intent(Servicio.this, MesaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Mesas",mesas);
            intent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getActivity(Servicio.this, 0, intent, 0);
            //aca iria toda la informacion que recibimos del scraping dentro del intent


            mBuilder =new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icono)
                    .setContentTitle("TNT")
                    .setContentText("Hay mesas de examenes disponibles.")
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);

            mNotifyMgr.notify(2, mBuilder.build());//
        }



        //Armo notificacion y la muestro en el celular.
        private void crearNotificacion(){
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.ic_launcher;
            Intent intent = new Intent(Servicio.this, AlumnoActivity.class);
            //aca poner las mesas....
            PendingIntent pendingIntent = PendingIntent.getActivity(Servicio.this, 0, intent, 0);
            //aca iria toda la informacion que recibimos del scraping dentro del intent


            mBuilder =new NotificationCompat.Builder(getApplicationContext())
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(icono)
                    .setContentTitle("TNT")
                    .setContentText("Hay mesas de examenes disponibles.")
                    .setVibrate(new long[] {100, 250, 100, 500})
                    .setAutoCancel(true);

            mNotifyMgr.notify(1, mBuilder.build());

        }



        //envio un mensaje a toda la plataforma, y que lo reciba alguien.
        /* este metodo envia un intent a toda la plataforma.
        * la idea es que lo atrape la activity que muestra la tabla de examenes.
        * entonces cuando el servicio envia una notificacion y yo estoy usando esa activity, se me
        * actualiza automaticamente las tablas de mesas de examenes.
        * */
        private void sendBroadcast()
        {
            Log.i("MyService....","estoy en sendBroadcast.");
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(Servicio.this);

            Intent resultIntent = new Intent("AlumnoActivity");//le pongo un nombre al intent asi se como atraparlo despues.
            //resultIntent.putExtra("TNT", "Hay mesas de examenes");
            Bundle bundle = new Bundle();
            bundle.putString("Nombre","Soy Ivan");
            resultIntent.putExtras(bundle);
            broadcastManager.sendBroadcast(resultIntent);//envio el intent a toda la plataforma para que alguien lo capture.

        }



    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Toast.makeText(this,"Service Started...",Toast.LENGTH_LONG).show();
        Thread thread = new Thread(new MyThreadClass(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {

        Toast.makeText(this,"Service Destroyed...",Toast.LENGTH_LONG).show();


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}

