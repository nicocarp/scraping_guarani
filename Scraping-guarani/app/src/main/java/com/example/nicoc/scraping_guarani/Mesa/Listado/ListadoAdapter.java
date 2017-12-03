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
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nicoc on 27/09/17.
 */

public class ListadoAdapter extends BaseAdapter  {

    private Context activity;
    private Activity context;
    private  List<Mesa> items = Collections.emptyList();
    private  List<Mesa> items_all = Collections.emptyList();
    private Alumno alumno;

    public ListadoAdapter(Activity activity, List<Mesa> items, Alumno alumno){
        this.context = activity;
        this.items_all = items;
        this.items = items;
        this.alumno = alumno;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
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

        Mesa mesa = this.items.get(position);
        if (alumno.estaInscripto(mesa.getMateria()))
            v.setBackgroundColor(Color.GREEN);
        txtNombre.setText(mesa.getMateria().getNombre()+" "+mesa.getCarrera().getCodigo());
        txtCodigo.setText(mesa.getFecha());
        return v;
    }

    /**
     * Setea los items que se muestran y los items_all del adaptador
     * @param items Listado de objetos Producto
     */
    public void setData(List<Mesa> items){
        this.items.clear();
        this.items_all.clear();

        this.items_all = items;
        this.items = items;
        notifyDataSetChanged();
    }

    /**
     * Setea un nuevo listado de items que se muestran, items_all permanece igual. Usado despues de filtros.
     * @param items Listados de objetos Producto
     */
    private void refreshData(List<Mesa> items) {
        this.items = items;
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
        for (Mesa mesa : this.items_all){
            if (mesa.getCarrera().getCodigo().equals(codigo_carrera))
                filtrado.add(mesa);
        }
        this.refreshData(filtrado);
    }



}
