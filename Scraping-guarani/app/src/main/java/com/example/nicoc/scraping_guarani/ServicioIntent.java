package com.example.nicoc.scraping_guarani;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Modelos.Mesa;

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
        Log.i("Intent Service: ","Estoy funcionado!!!!!");

        //1. consultar la BD para traer user y pass
        String usuario = "27042881";
        String password = "valenti2";

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
                        crearNotificacionMesasDisponibles(mesas);
                    }catch(Exception e){
                        Log.i("Guarani...ERROR: ", e.getMessage());
                    }




                }





            }catch(IOException e){
                e.printStackTrace();
                Log.i("Error al consultar carrera, alumno, mesas: ",e.getMessage());
                //5.¿Como prosigo?
            }

        }


        //Me mato !!!!!!! waaaaa
        stopSelf();
    }


    private void crearNotificacionMesasDisponibles(ArrayList<Mesa> mesas){
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        int icono = R.mipmap.ic_launcher;
        Intent intent = new Intent(ServicioIntent.this, MesaActivity.class);

        /* Hacerrrrrrrr  */
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Mesas",mesas);
        intent.putExtras(bundle);

        //intent.putParcelableArrayListExtra("Mesas",mesas);


        PendingIntent pendingIntent = PendingIntent.getActivity(ServicioIntent.this, 0, intent, 0);
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

}
