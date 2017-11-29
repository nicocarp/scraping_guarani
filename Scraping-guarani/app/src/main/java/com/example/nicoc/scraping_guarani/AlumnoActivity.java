package com.example.nicoc.scraping_guarani;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Modelos.Mesa;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlumnoActivity extends AppCompatActivity {

    @BindView(R.id.lblAlumno)    TextView lblAlumno;
    @BindView(R.id.lblLegajo)    TextView lblLegajo;
    Alumno alumno;
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
                this.finish();
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
        testInscripciones();

        Toast.makeText(AlumnoActivity.this, "Alumno"+this.alumno.getNombre(), Toast.LENGTH_SHORT).show();
        verificar_login();
        //start_service();
        //consultar_bd_mesas();
        //Escucho por mensajes que vienen a mi.....
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter("AlumnoActivity");
        //filter.addAction(CountService.EXTRA_COUNT_TARGET);
        broadcastManager.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.i("AlumnoActivity....","encontre el mensaje brodcasteado!");
                        Bundle bundle = intent.getExtras();

                        //Aqui hay que hacer la consulta a la BD de MESAS para mostrar mesas disponibles.
                        Toast.makeText(AlumnoActivity.this,bundle.getString("Nombre"),Toast.LENGTH_LONG).show();
                        Toast.makeText(AlumnoActivity.this,bundle.getString("Legajo"),Toast.LENGTH_LONG).show();

                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(1);
                    }
                },
                filter
        );

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
        Toast.makeText(AlumnoActivity.this, "EN CARGAR MATERIAS", Toast.LENGTH_SHORT).show();
        // lanzar la otra activity, necesaria para chupar datos de materias.
    }

    private void testInscripciones(){
        Guarani guarani = ManagerGuarani._getInstance();
        //ArrayList<Mesa> mesas = guarani.getMesasDeExamen();

    }
}
