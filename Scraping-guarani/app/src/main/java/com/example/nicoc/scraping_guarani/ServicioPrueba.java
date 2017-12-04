package com.example.nicoc.scraping_guarani;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity;
import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Mesa.Listado.MesaActivity;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.io.IOException;
import java.util.ArrayList;

public class ServicioPrueba extends Service {

    private static final int COUNT_NOTIFICATION_ID = 1234;

    public ServicioPrueba() {
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
            //1. consultar la BD para traer user y pass
            SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            String usuario = loginPreferences.getString("username", "");
            String password = loginPreferences.getString("password", "");

            if (usuario.isEmpty() || password.isEmpty())
                return; //
            //2.Verifico si ese usuario esta logueado
            Guarani guarani = ManagerGuarani.getInstance(usuario, password);
            if (guarani == null){//usuario no logueado
                //3.a Hago una notificacion mostrando el error.
                //3.b Si estoy en cualquiera de las 2 activitys, muestro mje y vuelvo a activity_login: broadcastear en ambas.
                Log.i("Guarani...: ","Estoy Muerto");
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
                    Log.i("Guarani...: ","Estoy Vivo");
                    if (mesas.size()>0){//si hay mesas
                        //4.b Pasar las mesas
                        //4.b.1 No estoy usando la app: armo notificación con pendingIntent
                        //4.b.2 Estoy en activity_login: consulto la bd.
                        //4.b.3 Estoy en activity_alumno: consulto la bd.
                        //4.b.4 Estoy en activty_mesa: recibo un broadcast y refresco pantalla.

                        //Nota: por ahora mando notificacion
                        //Log.i("Guarani...: ","Hay Mesas");
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
                        Log.i("Service: ", "No hay mesas disponibles.");
                    }





                }catch(Exception e){
                    e.printStackTrace();
                    Log.i("Error al consultar carrera, alumno, mesas: ",e.getMessage());
                    //5.¿Como prosigo?
                    //alarma.reiniciar(); hago un for o algo.
                }



            }


            //Me mato !!!!!!! waaaaa
            stopSelf();
        }


        private void crearNotificacionMesasDisponibles(ArrayList<Mesa> mesas){
            NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

            int icono = R.mipmap.ic_launcher;
            Intent intent = new Intent(ServicioPrueba.this, MesaActivity.class);

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


            PendingIntent pendingIntent = PendingIntent.getActivity(ServicioPrueba.this, 0, intent, 0);
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
            LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(ServicioPrueba.this);

            Intent resultIntent = new Intent("MesasActivity");//le pongo un nombre al intent asi se como atraparlo despues.
            //resultIntent.putExtra("TNT", "Hay mesas de examenes");
            Bundle bundle = new Bundle();
            bundle.putString("Nombre","Nuevas mesas disponibles!.");
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

        Log.i("Service: ","Estoy funcionado!!!!!");
        Thread thread = new Thread(new ServicioPrueba.MyThreadClass(startId));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {

        Log.i("Service:  ","Me moriii!!");


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
