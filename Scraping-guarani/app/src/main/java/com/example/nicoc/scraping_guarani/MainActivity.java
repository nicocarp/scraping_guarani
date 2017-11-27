package com.example.nicoc.scraping_guarani;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Modelos.Mesa;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements AsyncLogin.IView{

    public static String URL_BASE = "http://www.dit.ing.unp.edu.ar/v2070/www/";
    @BindView(R.id.txtUsername) TextView txtUsername;
    @BindView(R.id.txtPassword) TextView txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void logueado() {
        Toast.makeText(MainActivity.this, "lOGUEADO CON EXITO", Toast.LENGTH_LONG).show();
        Log.i("ACTIVITY....","por llamar a get mesas");

        //me voy a principal...
        Intent intent = new Intent(this, PrincipalActivity.class);
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
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        String[] parametros = { URL_BASE, username, password};
        AsyncTask<String, Void, Boolean> myAsyncTask = new AsyncLogin(this).execute(parametros);
        Toast.makeText(MainActivity.this, "Logueando ...", Toast.LENGTH_SHORT).show();
    }

    public void getMesas(){

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

    }


}
