package com.example.nicoc.scraping_guarani.Login;

import android.app.NotificationManager;
import android.app.ProgressDialog;
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
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ILogin.View {

    private AwesomeValidation validator;
    @BindView(R.id.txtUsername) EditText txtUsername;
    @BindView(R.id.txtPassword) EditText txtPassword;
    @BindView(R.id.checkBoxRememberMe) CheckBox checkBoxRememberMe;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private SharedPreferences loginPreferences;

    private ILogin.Presenter presenter;
    private ProgressDialog progressDialog;

    private static final String KEY_1 = "btnLogin";
    private static final String KEY_2 = "progressBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.presenter = new LoginPresenter(this, getSharedPreferences("loginPrefs", MODE_PRIVATE));

        ButterKnife.bind(this);
        this.validator = new AwesomeValidation(ValidationStyle.BASIC);
        iniciarViews();
        //Si paso de portrait a landscape o viceversa, veo en que estado quedo.
        if (savedInstanceState != null) {
            String estadoProgressBar = savedInstanceState.getString(KEY_2);
            if(estadoProgressBar.equals("Visible"))
                mostrarProgressBar();
            else
                ocultarProgressBar();
        }

        escucharBroadcasts();
        getAlumno();
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

    public void iniciarViews(){

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        validator.addValidation(txtUsername, RegexTemplate.NOT_EMPTY, "Ingrese usuario");
        validator.addValidation(txtPassword, RegexTemplate.NOT_EMPTY, "Ingrese contraseña");
    }

    /**
     * Precondicion: el alumno no debe ser nullo.     *
     * @param alumno instancia de Alumno no null.
     */
    @Override
    public void logueado(Alumno alumno) {

        Alarma alarma3 = new Alarma(this,ServicioIntent.class);
        alarma3.start();

        Intent intent = new Intent(this, AlumnoActivity.class);
        startActivity(intent);
    }

    /**
     * Soliciamos el alumno necesario para el login.
     */
    @Override
    public void getAlumno() {
        this.presenter.getAlumno();
    }

    @Override
    public void mostrarError(String s){
        ocultarProgressBar();
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        this.validator.clear();
        if (!this.validator.validate())
            return;
        mostrarProgressBar();
        this.presenter.login(txtUsername.getText().toString(), txtPassword.getText().toString());
    }

    private void mostrarProgressBar(){
        try{
            progressDialog.show();
        }catch (Exception e){
        }
        btnLogin.setEnabled(false);
        btnLogin.setBackgroundColor(getResources().getColor(R.color.colorGris));
    }

    private void ocultarProgressBar(){
        try{
            progressDialog.dismiss();
         }catch (Exception e){
        }
        btnLogin.setEnabled(true);
        btnLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

/*    @Override
    protected void onStop() {
        super.onStop();
        //ocultarProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ocultarProgressBar();
    }
*/
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        if(progressBar.getVisibility()==View.VISIBLE)
            outState.putString(KEY_2, "Visible");
        else
            outState.putString(KEY_2, "Invisible");
    }
}