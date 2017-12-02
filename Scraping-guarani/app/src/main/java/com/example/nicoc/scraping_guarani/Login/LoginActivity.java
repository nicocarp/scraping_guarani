package com.example.nicoc.scraping_guarani.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
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

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.validator = new AwesomeValidation(ValidationStyle.BASIC);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        this.iniciarViews();
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
}