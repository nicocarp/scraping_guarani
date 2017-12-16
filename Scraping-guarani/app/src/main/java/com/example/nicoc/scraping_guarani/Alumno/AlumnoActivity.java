package com.example.nicoc.scraping_guarani.Alumno;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

        alarma();

    }

    public void setAlumno(Alumno alumno) {
        _alumno = alumno;
    }


    private void alarma(){
        Intent intent = new Intent(this, ServicioIntent.class);
        boolean alarmUp = (PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if(!alarmUp) new Alarma(this,ServicioIntent.class).start();

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



    /* Se queda a la escucha del mensaje enviado por ServiceIntent.java */
    private final BroadcastReceiver broadcastManagerMesas = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Toast.makeText(AlumnoActivity.this, bundle.getString("Nombre"), Toast.LENGTH_LONG).show();
            matarNotificaciones();
            //mostrarDialog();
            updateItems();
        }
    };

    private final BroadcastReceiver broadcastManagerError = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Toast.makeText(AlumnoActivity.this, bundle.getString("Nombre"), Toast.LENGTH_LONG).show();
            matarNotificaciones();
            cerrarSesion();
        }
    };

    private final IntentFilter filterMesas = new IntentFilter("MesasActivity");
    private final IntentFilter filterLogin = new IntentFilter("LoginError");


    /* le dijo a los broadcast que comiencen a escuchar */
    private void registrarBroadcasts(){
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastManagerMesas,filterMesas);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastManagerError,filterLogin);
        Log.i("registrarBroadcasts","ENTRE");
    }

    /* le dijo a los broadcast que dejen de escuchar */
    public void desregistrarBroadcasts(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastManagerMesas);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastManagerError);
    }



    @Override
    protected void onPause() {
        super.onPause();
        desregistrarBroadcasts();
        Log.i("ONPAUSE","ENTRE");

    }

    @Override
    protected void onStop() {
        super.onStop();
        desregistrarBroadcasts();
        Log.i("ONSTOP","ENTRE");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        desregistrarBroadcasts();
        Log.i("ONDESTROY","ENTRE");

    }



    @Override
    protected void onResume() {
        super.onResume();
        registrarBroadcasts();
        Log.i("ONRESUME","ENTRE");

    }


    public final void updateItems(){
        this.fragment.updateList();
    }





    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    /* Cerrar sesion con AsyncTask sin matar servicio */
    private void cerrarSesion() {
        Alarma.cancelarAlarma();
        desregistrarBroadcasts();
        AsyncTask<Void, Void, Void> myAsyncTask = new AsyncLogout(this).execute();
    }

    /* Cerrar sesión matando el servicio */
    private void cerrarSesionMatandoServicio(){
        Alarma.cancelarAlarma();
        stopService(new Intent(this,ServicioIntent.class)); //te lo mata de una.
        desloguearse();
    }

    /* Elimina todas las notificaciones que están en el area de notificaciones del dispositivo */
    private void matarNotificaciones(){
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(ServicioIntent.NOTIFICACION_INSCRIPCION);
        mNotifyMgr.cancel(ServicioIntent.NOTIFICACION_ERROR);
        mNotifyMgr.cancel(ServicioIntent.NOTIFICACION_MESAS);

    }

    /* acciones a llevar a cabo para desloguearse */
    public void desloguearse(){
        desregistrarBroadcasts();
        this.presenter.desloguearse();
        finish();
        matarNotificaciones();
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

        }
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
                mostrarDialogFaltanMaterias(materia);
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
                confirmaInscripcion.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
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
        final AlertDialog.Builder confirmaDesinscripcion = new AlertDialog.Builder(this);
        confirmaDesinscripcion.setIcon(android.R.drawable.ic_dialog_alert);
        confirmaDesinscripcion.setTitle(getResources().getString(R.string.confirmar_titulo2));
        confirmaDesinscripcion.setMessage(getResources().getString(R.string.confirmar_mensaje2));
        View dialogView = inflater.inflate(R.layout.dialog_desinscripcion,null);
        dialogBuilder.setView(dialogView);
        //
        //_alumno.getCarreraById()
        ((TextView) dialogView.findViewById(R.id.lblCarrera)).setText(inscripcion.getCarrera());
        ((TextView) dialogView.findViewById(R.id.lblMateria)).setText(inscripcion.getMateria());
        ((TextView) dialogView.findViewById(R.id.lblFecha)).setText(inscripcion.getFecha());
        ((TextView) dialogView.findViewById(R.id.lblTipo)).setText(inscripcion.getTipo());
        dialogBuilder.setPositiveButton("Desinscribir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                confirmaDesinscripcion.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int boton) {
                        desinscribirse(inscripcion);
                        //aca va el codigo que actualiza la activity                    }
                    }
                    });
                confirmaDesinscripcion.setNegativeButton(android.R.string.no, null);
                confirmaDesinscripcion.show();
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



    private void mostrarDialogFaltanMaterias(Materia materia){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_faltan_materias,null);
        dialogBuilder.setView(dialogView);
        ((EditText) dialogView.findViewById(R.id.editText)).setText(materia.getCorrelatividad());
        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //no hace nada.

            }
        });

        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }



}
