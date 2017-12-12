package com.example.nicoc.scraping_guarani.Alumno;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import com.example.nicoc.scraping_guarani.Login.AsyncLogin;
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.R;
import com.example.nicoc.scraping_guarani.ServicioIntent;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlumnoActivity extends AppCompatActivity implements
        IAlumno.View, IListadoMesasFragment.ViewContainer {

    private IListadoMesasFragment.ViewFragment fragment;
    @BindView(R.id.lblAlumno)
    TextView lblAlumno;

    public static Alumno _alumno = null;
    public static IAlumno.Presenter presenter;
    private SharedPreferences loginPreferences;
    private ProgressDialog progressDialog;
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
        escucharBroadcastsError();
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

    @Override
    public void inscriptoCorrectamente(String mensaje) {
        mostrarError(mensaje);
        startService(new Intent(this, ServicioIntent.class));

        //this.updateItems();
    }

    @Override
    public void desinscriptoCorrectamente(String mensaje) {
        mostrarError(mensaje);
        startService(new Intent(this, ServicioIntent.class));
    }

    private void escucharBroadcasts() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter("MesasActivity");
        broadcastManager.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle bundle = intent.getExtras();
                        //Toast.makeText(AlumnoActivity.this, bundle.getString("Nombre"), Toast.LENGTH_LONG).show();
                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(123);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                        mNotifyMgr.cancel(1234);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                        updateItems();

                        mostrarDialog();
                    }
                },
                filter
        );
    }

    private void escucharBroadcastsError(){
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter("LoginError");
        broadcastManager.registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle bundle = intent.getExtras();
                        //Toast.makeText(AlumnoActivity.this, bundle.getString("Nombre"), Toast.LENGTH_LONG).show();
                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(123);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                        mNotifyMgr.cancel(1234);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                        mNotifyMgr.cancel(234);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.


                        mostrarDialogError();
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
                updateItems();
            }
        });
        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    private void mostrarDialogError(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_login_error,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //aca va el codigo que actualiza la activity
                cerrarSesion();
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

        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean estoyEjecutandome = isMyServiceRunning(ServicioIntent.class);


                while (estoyEjecutandome) {

                    try {

                        Thread.sleep(1000 * 10);
                        estoyEjecutandome = isMyServiceRunning(ServicioIntent.class);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }





            }
        });
        t.start();*/


        /*mostrarProgressDialog();
        long delayInMillis = 1000 * 10;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, delayInMillis);*/

        /*Alarma.cancelarAlarma();
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
        this.presenter.desloguearse();
        finish();
        Intent intent = new Intent(AlumnoActivity.this, LoginActivity.class);
        startActivity(intent);*/





        //ACA VA LO NUEVO...
        AsyncTask<Void, Void, Void> myAsyncTask = new AsyncLogout(this).execute();
    }



    public void desloguearse(){
        Alarma.cancelarAlarma();
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(123);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
        this.presenter.desloguearse();
        finish();
        Intent intent = new Intent(AlumnoActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public void mostrarProgressDialog(){
        try{
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Cerrando Sesión...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }catch (Exception e){
        }

    }

    public void ocultarProgressDialog(){
        try{
            progressDialog.dismiss();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
        if (_alumno.estaInscripto(mesa)){
            mostrarDialogDesinscripcion(_alumno.getInscripcionByMesa(mesa));
        }else{
            if (mesa.puedeInscribirse()){
                mostrarDialogInscripcion(mesa, carrera, materia);
            }else{
                mostrarDialogFaltanMaterias();
            }
        }

        mostrarError(materia.getNombre().toLowerCase());
    }


    private void mostrarDialogInscripcion(final Mesa mesa, Carrera carrera, Materia materia){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final AlertDialog.Builder confirmaInscripcion = new AlertDialog.Builder(this);
        confirmaInscripcion.setIcon(android.R.drawable.ic_dialog_alert);
        confirmaInscripcion.setTitle(getResources().getString(R.string.confirmar_titulo));
        confirmaInscripcion.setMessage(getResources().getString(R.string.confirmar_mensaje));
        //confirmaInscripcion.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {


            View dialogView = inflater.inflate(R.layout.dialog_inscripcion,null);
        dialogBuilder.setView(dialogView);


        ((TextView) dialogView.findViewById(R.id.lblCarrera)).setText(carrera.getNombre());
        ((TextView) dialogView.findViewById(R.id.lblMateria)).setText(materia.getNombre());
        ((TextView) dialogView.findViewById(R.id.lblFecha)).setText(mesa.getSoloFecha());
        ((TextView) dialogView.findViewById(R.id.lblHora)).setText(mesa.getHora());
        final Spinner cbo_tipo_mesa =(Spinner) dialogView.findViewById(R.id.cmb_tipo_mesa);
        dialogBuilder.setPositiveButton("Inscribir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmaInscripcion.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int boton) {
                    inscribirse(cbo_tipo_mesa.getSelectedItem().toString(), mesa);
                    }
                });
                confirmaInscripcion.setNegativeButton(android.R.string.no, null);
                confirmaInscripcion.show();
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
    public void inscribirse(String tipo, Mesa mesa){
        this.presenter.inscribirseAMesa(_alumno, mesa, tipo);
    }

    public void desinscribirse(Inscripcion inscripcion){
        mostrarError("Desinsribirse "+inscripcion.getMateria()+inscripcion.getCarrera());
        this.presenter.desinscribirseDeMesa(inscripcion);
    }

    private void mostrarDialogDesinscripcion(final Inscripcion inscripcion){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_desinscripcion,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Desinscribir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                desinscribirse(inscripcion);
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
