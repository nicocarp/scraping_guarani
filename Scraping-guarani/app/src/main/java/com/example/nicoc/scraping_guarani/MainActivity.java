package com.example.nicoc.scraping_guarani;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lblAlumno)    TextView lblAlumno;
    @BindView(R.id.lblMaterias)    TextView lblMaterias;
    @BindView(R.id.listaMaterias)    ListView listaMatrias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ButterKnife.bind(this);
        verificar_login();
        //start_service();
        //consultar_bd_mesas();



        //Escucho por mensajes que vienen a mi.....
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter("MainActivity");
        //filter.addAction(CountService.EXTRA_COUNT_TARGET);
        broadcastManager.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.i("MainActivity....","encontre el mensaje brodcasteado!");
                        Bundle bundle = intent.getExtras();

                        //Aqui hay que hacer la consulta a la BD de MESAS para mostrar mesas disponibles.
                        Toast.makeText(MainActivity.this,bundle.getString("Nombre"),Toast.LENGTH_LONG).show();

                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(1);
                    }
                },
                filter
        );

    }

    private void verificar_login(){
        // lee de la base de datos o preferencias y verifica que haya usuario y contraseña activa.


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        SharedPreferences.Editor editor = pref.edit();

        String u = pref.getString("username", null);

        if (u!=null)
            cargarMaterias();
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //Alarma.cancelarAlarma();
        }

    }

    public void cargarMaterias(){
        Toast.makeText(MainActivity.this, "EN CARGAR MATERIAS", Toast.LENGTH_SHORT).show();

    }

}
