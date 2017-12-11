package com.example.nicoc.scraping_guarani.Alumno;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Alarma;
import com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment.IListadoMesasFragment;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlumnoActivity extends AppCompatActivity implements
        IAlumno.View, IListadoMesasFragment.ViewContainer {

    private IListadoMesasFragment.ViewFragment fragment;
    @BindView(R.id.lblAlumno)
    TextView lblAlumno;

    public static Alumno _alumno = null;
    private IAlumno.Presenter presenter;
    private SharedPreferences loginPreferences;

    private SharedPreferences.Editor loginPrefsEditor;
    private android.app.AlertDialog alertDialog;
    private static final String KEY_1 = "alertDialog";
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
        Drawable myIcon = getResources().getDrawable(R.drawable.ic_action_name);
        DrawableCompat.setTint(myIcon, getResources().getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_alumno);
        ButterKnife.bind(this);

        this.fragment = (IListadoMesasFragment.ViewFragment)
                this.getSupportFragmentManager().findFragmentById(R.id.fragmentListadoProductos);
        this.presenter = new AlumnoPresenter(this, getSharedPreferences("loginPrefs", MODE_PRIVATE));
        this.presenter.getAlumno();
        verificar_login();

        //Si paso de portrait a landscape o viceversa, veo en que estado quedo.
        if (savedInstanceState != null) {
            String estado_dialog = savedInstanceState.getString(KEY_1);
            if(estado_dialog.equals("Visible"))
                mostrarDialog();
        }
        escucharBroadcasts();
    }

    public void setAlumno(Alumno alumno) {
        _alumno = alumno;
    }


    @Override
    public void setMesasEInscripciones(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones) {
        _alumno.loadInscripciones(inscripciones);
        _alumno.loadMesas(mesas);
        this.fragment.updateList();
    }

    private void escucharBroadcasts() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter("MesasActivity");
        broadcastManager.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle bundle = intent.getExtras();
                        Toast.makeText(AlumnoActivity.this, bundle.getString("Nombre"), Toast.LENGTH_LONG).show();
                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                        updateItems();

                        //mostrarDialog();
                    }
                },
                filter
        );
    }

    public final void updateItems(){
        this.fragment.updateList();
    }
    private void mostrarDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_mesas,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //aca va el codigo que actualiza la activity
            }
        });
        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        try{
            if(alertDialog!=null){
                if(alertDialog.isShowing())
                    outState.putString(KEY_1, "Visible");
                else
                    outState.putString(KEY_1, "Invisible");
            }
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
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
        if (_alumno!=null)
            setDatosAlumno();
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void setDatosAlumno(){
        lblAlumno.setText(_alumno.getNombre());
    }
    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(AlumnoActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelectedInFragment(Mesa mesa) {
        Carrera carrera= _alumno.getCarreraById(mesa.getCarrera());
        Materia materia = carrera.getMateriaById(mesa.getMateria());
        // ver especificacion en trello
        if (_alumno.estaInscripto(mesa)){
            // mostrar dialogo para desinscribirse
            mostrarDialogDesinscripcion();
        }else{
            if (mesa.puedeInscribirse()){
                // mostrar dialogo indicando las materias necesarias para isncribirse.
                mostrarDialogFaltanMaterias();
            }else{
                // mostrar dialogo para inscribirse.
                mostrarDialogInscripcion();
            }
        }

        mostrarError(materia.getNombre().toLowerCase());
    }


    private void mostrarDialogInscripcion(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_inscripcion,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Inscribir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //aca va el codigo que actualiza la activity
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    private void mostrarDialogDesinscripcion(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_desinscripcion,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Desinscribir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //aca va el codigo que actualiza la activity
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void mostrarDialogFaltanMaterias(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_faltan_materias,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //aca va el codigo que actualiza la activity
            }
        });

        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



}
