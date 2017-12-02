package com.example.nicoc.scraping_guarani;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nicoc.scraping_guarani.Modelos.Mesa;

import java.util.ArrayList;

public class MesaActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_m, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.cerrarSesion:
                cerrarSesion();
                return true;
            case R.id.atras:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cerrarSesion() {
        Intent intent = new Intent(MesaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar
        getSupportActionBar().setTitle("Mesas");
        Drawable myIcon = getResources().getDrawable(R.drawable.ic_action_name );
        DrawableCompat.setTint(myIcon, getResources().getColor(R.color.colorPrimary));
        Drawable myIcon2 = getResources().getDrawable(R.drawable.ic_action_atras );
        DrawableCompat.setTint(myIcon2, getResources().getColor(R.color.colorPrimary));

        setContentView(R.layout.activity_mesa);


        // Para que esto ande todos los modelos tienen que implementar Parcelable
        //y redefinir los metodos writeToParcel()

        Log.i("Datos de las Mesas. ","...");
        Bundle bundle_mesas = this.getIntent().getExtras();
        ArrayList<Mesa> mesas = bundle_mesas.getParcelableArrayList("Mesas");
        Log.i("Mesas.cantidad: ", "" + mesas.size());


         //ArrayList<Mesa> mesas = this.getIntent().getParcelableArrayListExtra("Mesas");
        //Log.i("Mesas.cantidad: ", "" + mesas.size());


    }
}
