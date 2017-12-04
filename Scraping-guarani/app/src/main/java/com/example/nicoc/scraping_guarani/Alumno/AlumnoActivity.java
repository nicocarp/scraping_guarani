package com.example.nicoc.scraping_guarani.Alumno;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Alarma;
import com.example.nicoc.scraping_guarani.Guarani.Guarani;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.Mesa.Listado.MesaActivity;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.R;
import com.example.nicoc.scraping_guarani.ServicioIntent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlumnoActivity extends AppCompatActivity {

    @BindView(R.id.lblAlumno)    TextView lblAlumno;
    @BindView(R.id.lblLegajo)    TextView lblLegajo;
    Alumno alumno;
    @BindView(R.id.buttonMesas) Button buttonMesas;
   // @BindView(R.id.lblFecha)    TextView lblFecha;
   // @BindView(R.id.lblMaterias)    TextView lblMaterias;
   // @BindView(R.id.listaMaterias)    ListView listaMatrias;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_a, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cerrarSesion:
                cerrarSesion();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Alumno");
        Drawable myIcon = getResources().getDrawable(R.drawable.ic_action_name );
        DrawableCompat.setTint(myIcon, getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_alumno);
        ButterKnife.bind(this);

        this.alumno= ManagerGuarani.alumno;

        Toast.makeText(AlumnoActivity.this, "Alumno "+this.alumno.getNombre(), Toast.LENGTH_SHORT).show();
        verificar_login();
        //start_service();
        //consultar_bd_mesas();
        escucharBroadcasts();

    }

    private void escucharBroadcasts(){
        //Escucho por mensajes que vienen a mi.....
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter("MesasActivity");
        //filter.addAction(CountService.EXTRA_COUNT_TARGET);
        broadcastManager.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.i("AlumnoActivity....","encontre el mensaje brodcasteado!");
                        Bundle bundle = intent.getExtras();

                        //Aqui hay que hacer la consulta a la BD de MESAS para mostrar mesas disponibles.
                        Toast.makeText(AlumnoActivity.this,bundle.getString("Nombre"),Toast.LENGTH_LONG).show();

                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                    }
                },
                filter
        );
    }

    private void cerrarSesion() {
        Intent intent = new Intent(AlumnoActivity.this, LoginActivity.class);
        startActivity(intent);

        Alarma.cancelarAlarma();
        //stopService(new Intent(AlumnoActivity.this, ServicioIntent.class));
        //al servicio no lo puedo parar mientras se esta ejecutando, porque puede dejar incosistente la bd
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.

        finish();
    }

    private void verificar_login(){
        if (this.alumno!=null)
            setDatosAlumno();
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
    private void setDatosAlumno(){
        lblAlumno.setText(this.alumno.getNombre());
        lblLegajo.setText(this.alumno.getLegajo());
    }

    @OnClick(R.id.buttonMesas)
    public void cargarMaterias(){
        Intent intent = new Intent(this, MesaActivity.class);
        startActivity(intent);
        Toast.makeText(AlumnoActivity.this, "EN CARGAR MATERIAS", Toast.LENGTH_SHORT).show();
        // lanzar la otra activity, necesaria para chupar datos de materias.
    }

    @Override
    public void onBackPressed() {
        //no hago nada
        return;
    }
}
