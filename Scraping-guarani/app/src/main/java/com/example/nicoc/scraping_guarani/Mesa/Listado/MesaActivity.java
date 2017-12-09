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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicoc.scraping_guarani.Alarma;
import com.example.nicoc.scraping_guarani.Guarani.ManagerGuarani;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Login.LoginActivity;
import com.example.nicoc.scraping_guarani.R;

import java.lang.reflect.Array;
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

        this.presenter = new ListadoPresenter(this, getSharedPreferences("loginPrefs", MODE_PRIVATE));
        this.getItems();

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

                        mostrarDialog();

                    }
                },
                filter
        );
    }


    private void mostrarDialog(){

        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_mesas,null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //aca va el codigo que actualiza la activity
            }
        });
        dialogBuilder.setCancelable(false);
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }


    private void cerrarSesion() {
        Alarma.cancelarAlarma();
        NotificationManager mNotifyMgr =(NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(1);//aca estoy matando automaticamente a la notificacion en el panel de notificaciones.
        Intent intent = new Intent(MesaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getItems() {
        this.presenter.getItems();
    }

    @Override
    public void setItems(List<Mesa> mesas, ArrayList<Inscripcion> inscripciones) {
        listaMesas.setAdapter(new ListadoAdapter(this, mesas, inscripciones));
        if (mesas.size() == 0)
            mostrarError("Sin mesas de examen");

        // Leer combo y aplicar filtro
        //filtroMesas();

    }
    // capturar evento on Change select carrera
    private void filtroMesas(){
        ListadoAdapter adapter = (ListadoAdapter)this.listaMesas.getAdapter();
        //adapter.filtrado("38");
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void lanzarDetalleMesa(Mesa mesa) {

    }
    public void inscribirse(Mesa mesa, String tipo){
        //Alumno alumno = ManagerGuarani.alumno;
        //this.presenter.inscribirse(mesa, alumno, tipo);
    }

    @OnItemClick(R.id.listMesas) void itemClick(int position){
        //Alumno alumno = ManagerGuarani.alumno;
        final Mesa mesa = (Mesa)listaMesas.getAdapter().getItem(position);
        final EditText txtNuevoStock = new EditText(this);
        txtNuevoStock.setInputType(InputType.TYPE_CLASS_TEXT);
        txtNuevoStock.setText("regular");


        new AlertDialog.Builder(this)
                .setTitle("Detalle de la mesa")
                .setMessage(mesa.getMateria()+" "+ mesa.getCarrera())
                //.setMessage(mesa.getMateria().getNombre() +" "+ mesa.getMateria().getCarrera().getCodigo())
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
