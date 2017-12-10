package com.example.nicoc.scraping_guarani.Mesa.Listado;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;
;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nicoc on 27/09/17.
 */

public class ListadoAdapter extends BaseAdapter  {

    private Activity context;
    private  List<Mesa> mesas = Collections.emptyList();
    private  List<Mesa> mesas_all = Collections.emptyList();
    private Alumno alumno;

    public ListadoAdapter(Activity activity, Alumno alumno){
        this.context = activity;
        this.alumno = alumno;
        this.mesas_all = this.alumno.getMesas();
        this.mesas = this.alumno.getMesas();
    }

    @Override
    public int getCount() {
        return this.mesas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mesas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 1;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        if (view == null){
            LayoutInflater inf = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate (R.layout.item_mesa, null);
        }
        TextView txtNombre = (TextView) v.findViewById(R.id.txtInfoMesa);
        TextView txtCodigo = (TextView) v.findViewById(R.id.txtFecha);

        Mesa mesa = this.mesas.get(position);
        if (alumno.estaInscripto(mesa))
            v.setBackgroundColor(Color.GREEN);
        //txtNombre.setText(mesa.getMateria().getNombre()+" "+mesa.getCarrera().getCodigo());
        Carrera carrera = alumno.getCarreraById(mesa.getCarrera());
        Materia materia = carrera.getMateriaById(mesa.getMateria());

        txtNombre.setText(materia.getNombre()+" "+mesa.getFecha());
        txtCodigo.setText(carrera.getNombre().toLowerCase());
        return v;
    }

    /**
     * Setea los mesas que se muestran y los mesas_all del adaptador
     * @param items Listado de objetos Producto
     */
    public void setData(List<Mesa> items){
        this.mesas.clear();
        this.mesas_all.clear();

        this.mesas_all = items;
        this.mesas = items;
        notifyDataSetChanged();
    }

    /**
     * Setea un nuevo listado de mesas que se muestran, mesas_all permanece igual. Usado despues de filtros.
     * @param items Listados de objetos Producto
     */
    private void refreshData(List<Mesa> items) {
        this.mesas = items;
        notifyDataSetChanged();
    }

    /**
     * Metodo personalizado para filtrar listado de mesas por codigo de carrera
     * @param codigo_carrera String (convertido a minuscula para buscar)
     */
    public void filtrado(String codigo_carrera){

        String filtro_codigo = codigo_carrera.toString().toLowerCase();
        List<Mesa> filtrado = new ArrayList<Mesa>();

        Log.i("FILTRANDO POR", filtro_codigo);
        for (Mesa mesa : this.mesas_all){
            if (mesa.getCarrera().equals(codigo_carrera))
                filtrado.add(mesa);
        }
        this.refreshData(filtrado);
    }
}
