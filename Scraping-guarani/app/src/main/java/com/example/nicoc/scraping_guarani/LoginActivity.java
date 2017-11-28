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
    @BindView(R.id.txtUsername) EditText txtUsername;
    @BindView(R.id.txtPassword) EditText txtPassword;
    @BindView(R.id.checkBoxRememberMe) CheckBox checkBoxRememberMe;
    //private String username,password;
    public SharedPreferences loginPreferences;
    public SharedPreferences.Editor loginPrefsEditor;
    public Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //AGREGADO
        checkBoxRememberMe = (CheckBox)findViewById(R.id.checkBoxRememberMe);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtUsername.setText(loginPreferences.getString("username", ""));
            txtPassword.setText(loginPreferences.getString("password", ""));
            checkBoxRememberMe.setChecked(true);
        }
    }

    @Override
    public void logueado(Alumno alumno) {

        Toast.makeText(LoginActivity.this, "Logueado con exito " + alumno.getNombre(), Toast.LENGTH_LONG).show();
        // REFACTORIZAR ESTO DE SHAREDPREFERENCES es solo de prueba.
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        editor.putString("username", username); // Storing string
        editor.putString("password", password); // Storing string
        editor.apply();

        Log.i("ACTIVITY....","por llamar a get mesas");
        //Aca inicio el servicio
        Alarma alarma = new Alarma(this,Servicio.class);
        alarma.start();
        //me voy a la pantalla de ALUMNO y con un intent pasarle la instancia de alumno ...
        Intent intent = new Intent(this, AlumnoActivity.class);
        startActivity(intent);
        //aca habria que poner una progressbar o algo esperando a que se ejecute el servicio...
    }

    @Override
    public void mostrarError(String s){
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        if(username.isEmpty()){
            Toast.makeText(this, "Ingrese Usuario! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(this, "Ingrese Contraseña! ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkBoxRememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.putBoolean("saveLogin", false);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
            //loginPrefsEditor.clear();
            //loginPrefsEditor.commit();
        }
        String[] parametros = { username, password};
        AsyncTask<String, Void, Alumno> myAsyncTask = new AsyncLogin(this).execute(parametros);
        Toast.makeText(LoginActivity.this, "Iniciando Sesion ...", Toast.LENGTH_SHORT).show();
    }

    /**
     * mocks de datos que deben venir de guarani. Borrar despues.
     */
    public void mockDatosListado(){
        Alumno alumno = new Alumno();
        alumno.setNombre("Un nombre");
        alumno.setLegajo("23-3455-322");

        Materia materia = new Materia();
        materia.setNombre("Ingenieria 3");
        materia.setCodigo("IF0345");

        Mesa mesa = new Mesa();
        mesa.setMateria(materia);
        mesa.setProfesores(new ArrayList<Profesor>(Arrays.asList(new Profesor("Ricardo Lopez"), new Profesor("Gabriel Ingravallo"))));
        mesa.setFecha("asi por ahora");
        mesa.setMaterias_necesarias(null); // se puede anotar a esta materia

        Materia materia_2 = new Materia();
        materia.setNombre("Administracion de redes y seguridad");
        materia.setCodigo("IF0323");

        // si un alumno no se puede inscribir a una mesa, no tiene fecha ni profes, solo las materias necesarias.
        Mesa mesa_2 = new Mesa();
        mesa.setMateria(materia_2);
        mesa.setProfesores(null);
        mesa.setFecha(null);
        mesa.setMaterias_necesarias(new ArrayList<Materia>(Arrays.asList(materia))); // Para rendir seguridad necesitas ing3
        // a partir de estos objetos: alumno y mesas se llena el listado.
        // al dar click en boton inscribir, es necesario el legajo del alumno y codigo materia.
        // falta defnir listado de materias a las que el alumno ya esta inscripto para desincribirse y no dejar inscribir
        
    }
}
