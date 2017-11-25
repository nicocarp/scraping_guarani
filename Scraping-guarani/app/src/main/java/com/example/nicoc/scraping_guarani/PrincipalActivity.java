package com.example.nicoc.scraping_guarani;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrincipalActivity extends AppCompatActivity {

    @BindView(R.id.lblAlumno)    TextView lblAlumno;
    @BindView(R.id.lblMaterias)    TextView lblMaterias;
    @BindView(R.id.listaMaterias)    ListView listaMatrias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        ButterKnife.bind(this);
        verificar_login();
        //start_service();
    }

    private void verificar_login(){
        // lee de la base de datos o preferencias y verifica que haya usuario y contraseña activa.
        boolean hay_usuario_en_bd = true;
        if (!hay_usuario_en_bd) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void cargarMaterias(){
        Toast.makeText(PrincipalActivity.this, "EN CARGAR MATERIAS", Toast.LENGTH_SHORT).show();

    }

}
