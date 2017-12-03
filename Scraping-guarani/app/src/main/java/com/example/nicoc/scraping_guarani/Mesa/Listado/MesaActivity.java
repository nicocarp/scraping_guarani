package com.example.nicoc.scraping_guarani.Mesa.Listado;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MesaActivity extends AppCompatActivity implements IListado.View{

    @BindView(R.id.listMesas) ListView listaMesas;

    private IListado.Presenter presenter;

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
        ButterKnife.bind(this);

        this.presenter = new ListadoPresenter(this);
        this.getItems();


        //Paso 1: Obtener las mesas del intent que me pasa el servicio a traves de la notificacion.
        // Para que esto ande todos los modelos tienen que implementar Parcelable
        //y redefinir los metodos writeToParcel()
        //Problema: el codigo lanza parcelable stackoverflow.

        /* Esto pincha  por Parselable
        Log.i("Datos de las Mesas. ","...");
        Bundle bundle_mesas = this.getIntent().getExtras();
        ArrayList<Mesa> mesas = bundle_mesas.getParcelableArrayList("Mesas");
        Log.i("Mesas.cantidad: ", "" + mesas.size()); */


         /*Log.i("Datos de las Mesas. ","...");
         ArrayList<Mesa> mesas = this.getIntent().getParcelableArrayListExtra("Mesas");
         Log.i("Mesas.cantidad: ", "" + mesas.size());*/

        escucharBroadcasts();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                        Toast.makeText(MesaActivity.this,bundle.getString("Nombre"),Toast.LENGTH_LONG).show();

                        //aca elimino la notificacion
                        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
                    }
                },
                filter
        );
    }





    private void cerrarSesion() {
        Intent intent = new Intent(MesaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getItems() {
        this.presenter.getItems();
    }

    @Override
    public void setItems(List<Mesa> items) {
        listaMesas.setAdapter(new ListadoAdapter(this, items, ManagerGuarani.alumno));
        if (items.size() == 0)
            mostrarError("Sin mesas de examen");
        else
            filtroMesas(); // esto cambiar, hacer un combo para q seleccione carrera
    }

    // capturar evento on Change select carrera
    private void filtroMesas(){
        ListadoAdapter adapter = (ListadoAdapter)this.listaMesas.getAdapter();
        adapter.filtrado("38");
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void lanzarDetalleMesa(Mesa mesa) {

    }
    public void inscribirse(Mesa mesa, String tipo){
        Alumno alumno = ManagerGuarani.alumno;
        this.presenter.inscribirse(mesa, alumno, tipo);
    }

    @OnItemClick(R.id.listMesas) void itemClick(int position){
        Alumno alumno = ManagerGuarani.alumno;
        final Mesa mesa = (Mesa)listaMesas.getAdapter().getItem(position);
        if (alumno.estaInscripto(mesa.getMateria()))
            mostrarError("Desea desinscribirse?");

        final EditText txtNuevoStock = new EditText(this);
        txtNuevoStock.setInputType(InputType.TYPE_CLASS_TEXT);
        txtNuevoStock.setText("regular");


        new AlertDialog.Builder(this)
                .setTitle("Detalle de la mesa")
                .setMessage(mesa.getMateria().getNombre() +" "+ mesa.getMateria().getCarrera().getCodigo())
                .setView(txtNuevoStock)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String tipo =txtNuevoStock.getText().toString();
                        inscribirse(mesa, tipo);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
