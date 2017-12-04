package com.example.nicoc.scraping_guarani.Login;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.nicoc.scraping_guarani.Alarma;
import com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity;
import com.example.nicoc.scraping_guarani.Database.Alumno_;
import com.example.nicoc.scraping_guarani.Database.ManagerDB;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;
import com.example.nicoc.scraping_guarani.R;
import com.example.nicoc.scraping_guarani.ServicioIntent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements AsyncLogin.IView{

    public static String URL_BASE = "http://www.dit.ing.unp.edu.ar/v2070/www/";
    private AwesomeValidation validator;
    @BindView(R.id.txtUsername) EditText txtUsername;
    @BindView(R.id.txtPassword) EditText txtPassword;
    @BindView(R.id.checkBoxRememberMe) CheckBox checkBoxRememberMe;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private static final String KEY_1 = "btnLogin";
    private static final String KEY_2 = "progressBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.validator = new AwesomeValidation(ValidationStyle.BASIC);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        this.iniciarViews();

        //Si paso de portrait a landscape o viceversa, veo en que estado quedo.
        if (savedInstanceState != null) {
            String estadoBoton = savedInstanceState.getString(KEY_1);
            String estadoProgressBar = savedInstanceState.getString(KEY_2);
            if(estadoBoton.equals("True")){
                botonHabilitar();
            }
            else{
                botonDeshabilitar();
            }

            if(estadoProgressBar.equals("Visible")){
                mostrarProgressBar();
            }
            else{
                ocultarProgressBar();
            }

        }

        //botonHabilitar();
        //ocultarProgressBar();
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
                        Toast.makeText(LoginActivity.this,bundle.getString("Nombre"),Toast.LENGTH_LONG).show();

                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                    }
                },
                filter
        );
    }


    private void iniciarViews(){
        if ((loginPreferences.getBoolean("saveLogin", false)) == true) {
            txtUsername.setText(loginPreferences.getString("username", ""));
            txtPassword.setText(loginPreferences.getString("password", ""));
            checkBoxRememberMe.setChecked(true);
        }

        validator.addValidation(txtUsername, RegexTemplate.NOT_EMPTY, "Ingrese usuario");
        validator.addValidation(txtPassword, RegexTemplate.NOT_EMPTY, "Ingrese contraseña");
    }

    /**
     * Precondicion: el alumno no debe ser nullo.     *
     * @param alumno instancia de Alumno no null.
     */
    @Override
    public void logueado(Alumno alumno) {
        //opcion 1
        //Alarma alarma = new Alarma(this,Servicio.class);
        //alarma.start();

        //opcion 2
        //Alarma alarma2 = new Alarma(this,ServicioPrueba.class);
        //alarma2.start();

        //opcion 3
        Log.i("Antes: ","ha comenzado la alarma");
        Alarma alarma3 = new Alarma(this,ServicioIntent.class);
        alarma3.start();
        Log.i("Despues: ","ha comenzado la alarma");

        Intent intent = new Intent(this, AlumnoActivity.class);
        startActivity(intent);
    }

    @Override
    public void mostrarError(String s){
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        mostrarProgressBar();
        botonDeshabilitar();

        //btnLogin.setBackgroundColor(Color.GRAY);
        this.validator.clear();
        if (!this.validator.validate())
            return;

        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        // esta logica deberia ir en logueado.
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putBoolean("saveLogin", (checkBoxRememberMe.isChecked()));
        loginPrefsEditor.putString("username", username);
        loginPrefsEditor.putString("password", password);
        loginPrefsEditor.commit();



        String[] parametros = { username, password};
        AsyncTask<String, Void, Alumno> myAsyncTask = new AsyncLogin(this).execute(parametros);
        Toast.makeText(LoginActivity.this, "Iniciando Sesion ...", Toast.LENGTH_SHORT).show();

    }

    public void botonDeshabilitar(){
        btnLogin.setEnabled(false);
        int color = R.color.colorGris;
        btnLogin.setBackgroundColor(getResources().getColor(color));
    }

    public void botonHabilitar(){
        btnLogin.setEnabled(true);
        int color = R.color.colorPrimary;
        btnLogin.setBackgroundColor(getResources().getColor(color));
    }

    private void mostrarProgressBar(){
        //ponerleColorAlProgressBar();
        try{
            progressBar.setVisibility(View.VISIBLE);
        }catch(Exception e){

        }
    }

    private void ocultarProgressBar(){
        try{
            progressBar.setVisibility(View.INVISIBLE);
        }catch(Exception e){

        }
    }

    private void ponerleColorAlProgressBar(){
        int color = R.color.colorPrimary;
        //progressBar.setBackgroundColor(getResources().getColor(color));
        //progressBar.setDrawingCacheBackgroundColor(getResources().getColor(color));
        try{
            //progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
            Log.i("ProgressBar: ","todo ok.");
        }catch(Exception e){
            Log.i("ProgressBar: ",""+e.getMessage());
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        botonHabilitar();
        ocultarProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        botonHabilitar();
        ocultarProgressBar();

    }



    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        if (btnLogin.isEnabled()){
            outState.putString(KEY_1, "True");
        }
        else{
            outState.putString(KEY_1, "False");
        }

        if(progressBar.getVisibility()==View.VISIBLE){
            outState.putString(KEY_2, "Visible");
        }
        else{
            outState.putString(KEY_2, "Invisible");
        }

    }

}