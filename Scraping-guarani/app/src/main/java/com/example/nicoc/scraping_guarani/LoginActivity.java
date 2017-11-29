package com.example.nicoc.scraping_guarani;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.nicoc.scraping_guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Modelos.Profesor;
import java.util.ArrayList;
import java.util.Arrays;
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
        //Alarma alarma = new Alarma(this,Servicio.class);
        //alarma.start();
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