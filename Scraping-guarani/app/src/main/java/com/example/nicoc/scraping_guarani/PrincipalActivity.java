package com.example.nicoc.scraping_guarani;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrincipalActivity extends AppCompatActivity {

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
        IntentFilter filter = new IntentFilter("PrincipalActivity");
        //filter.addAction(CountService.EXTRA_COUNT_TARGET);
        broadcastManager.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.i("PrincipalActivity....","encontre el mensaje brodcasteado!");
                        Bundle bundle = intent.getExtras();

                        //Aqui hay que hacer la consulta a la BD de MESAS para mostrar mesas disponibles.
                        Toast.makeText(PrincipalActivity.this,bundle.getString("Nombre"),Toast.LENGTH_LONG).show();

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
        boolean hay_usuario_en_bd = false;
        if (!hay_usuario_en_bd) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Alarma.cancelarAlarma();

        }
        else{
            //consulto la BD para ver si existen materias activas.
        }

    }

    public void cargarMaterias(){
        Toast.makeText(PrincipalActivity.this, "EN CARGAR MATERIAS", Toast.LENGTH_SHORT).show();

    }

}
