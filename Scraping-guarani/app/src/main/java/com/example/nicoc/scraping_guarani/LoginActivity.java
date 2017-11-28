package com.example.nicoc.scraping_guarani;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    @BindView(R.id.txtUsername) TextView txtUsername;
    @BindView(R.id.txtPassword) TextView txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void logueado() {
        Toast.makeText(LoginActivity.this, "lOGUEADO CON EXITO", Toast.LENGTH_LONG).show();


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        editor.putString("username", username); // Storing string
        editor.putString("password", password); // Storing string
        editor.apply();

        Log.i("ACTIVITY....","por llamar a get mesas");

        //me voy a principal...
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //aca habria que poner una progressbar o algo esperando a que se ejecute el servicio...

        //getMesas();


        //Aca inicio el servicio
        Alarma alarma = new Alarma(this,Servicio.class);
        alarma.start();

        /* Pasos
        * obtengo hs actual y le resto 1.
        * lanzo alarma
        * inicio el servicio
        * */

    }

    @Override
    public void mostrarError(String s){
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        String[] parametros = { URL_BASE, username, password};
        AsyncTask<String, Void, Boolean> myAsyncTask = new AsyncLogin(this).execute(parametros);
        Toast.makeText(LoginActivity.this, "Logueando ...", Toast.LENGTH_SHORT).show();
    }

    /*public void getMesas(){

        Thread thread = new Thread(){
            public void run(){
                Log.i("HILO....","0-iniciando RUN");

                ArrayList<Mesa> mesas = null;

                try {
                    mesas = ManagerGuarani.getInstance().getMesasDeExamen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Mesa mesa = mesas.get(0);
                String mensaje = "CONEXION"+ mesa.getFecha() + mesa.getMateria();
                Log.i("CONEXION EXITOSA", "EXITO");
                //Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
                try {
                    Log.i("HILO....","Empecemos a contar");
                    sleep(10000);
                    Log.i("HILO","no mori");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("Empecemos a contar....","wenassssss");
            }
        };
        thread.start();

    }*/

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
