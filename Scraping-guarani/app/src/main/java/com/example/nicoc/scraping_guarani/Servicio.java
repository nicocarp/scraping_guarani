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


            int i = 0;
            synchronized (this)
            {
                //aca iria la llamada a la API de scraping
                boolean estoy_logueado = true;
                boolean hay_mesas = true;
                if (estoy_logueado==false){
                    Alarma.cancelarAlarma();

                }
                else if (hay_mesas){
                    //guardar las mesas en la BD.
                    crearNotificacion(); //aparecera un mensaje en la barra de notificaciones cuando termine el servicio.
                    sendBroadcast(); //Si estoy en SecondActivity, se me actualiza
                }
                else
                {
                    //no hago nada....
                }

                /*
                while (i<10)
                {
                    try {
                        wait(1500);
                        i++;
                        /*if (i == 5){

                            Alarma.cancelarAlarma();
                            Log.i("Cuenta a 5....","Cago la alarma!!!!");
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/

                //crearNotificacion(); //aparecera un mensaje en la barra de notificaciones cuando termine el servicio.
                //sendBroadcast(); //Si estoy en SecondActivity, se me actualiza
                stopSelf(service_id); //este lo usamos para detener el servicio
            }

        }



        //Armo notificacion y la muestro en el celular.
        private void crearNotificacion(){
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.ic_launcher;
            Intent intent = new Intent(Servicio.this, MainActivity.class);
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

            Intent resultIntent = new Intent("MainActivity");//le pongo un nombre al intent asi se como atraparlo despues.
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
