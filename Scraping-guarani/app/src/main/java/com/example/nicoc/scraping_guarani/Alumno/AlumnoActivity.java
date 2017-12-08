package com.example.nicoc.scraping_guarani.Alumno;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.Mesa.Listado.MesaActivity;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlumnoActivity extends AppCompatActivity implements IAlumno.View {

    @BindView(R.id.lblAlumno)    TextView lblAlumno;
    @BindView(R.id.lblLegajo)    TextView lblLegajo;
    Alumno alumno;
    private IAlumno.Presenter presenter;
    @BindView(R.id.buttonMesas) Button buttonMesas;
   // @BindView(R.id.lblFecha)    TextView lblFecha;
   // @BindView(R.id.lblMaterias)    TextView lblMaterias;
   // @BindView(R.id.listaMaterias)    ListView listaMatrias;
   private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        this.presenter = new AlumnoPresenter(this, getSharedPreferences("loginPrefs", MODE_PRIVATE));

        getAlumno();
        verificar_login();
        //escucharBroadcasts();
    }

    public void getAlumno(){
        this.presenter.getAlumno();
    }
    public void setAlumno(Alumno alumno){
        this.alumno = alumno;
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

        Alarma.cancelarAlarma();
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
        this.presenter.desloguearse();
        finish();
        Intent intent = new Intent(AlumnoActivity.this, LoginActivity.class);
        startActivity(intent);
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
        lblLegajo.setText("sin legajo");
    }

    @OnClick(R.id.buttonMesas)
    public void cargarMaterias(){
        Intent intent = new Intent(this, MesaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(AlumnoActivity.this, error, Toast.LENGTH_LONG).show();
    }
}
