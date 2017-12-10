package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by nicoc on 27/09/17.
 */

public class ListadoMesasAdapter extends BaseAdapter  {

    private Context activity;
    private Activity context;
    private List<Mesa> items = Collections.emptyList();
    private List<Mesa> items_all = Collections.emptyList();
    private Alumno alumno;
    public ListadoMesasAdapter(ListadoMesasFragment fragment, Alumno alumno){
        this.context = fragment.getActivity();
        this.alumno = alumno;
        this.items_all = alumno.getMesas();
        this.items = alumno.getMesas();
        Log.i("ADAPTER", this.items.toString());
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
            v = inf.inflate (R.layout.item_mesa_nuevo, null);
        }
        //ImageView view_img_producto = (ImageView) v.findViewById(R.id.imagenProducto);
        TextView view_txt_nombre = (TextView) v.findViewById(R.id.txtNombre);
        TextView view_txt_codigo = (TextView) v.findViewById(R.id.txtCodigo);

        // cargamos los datos
        Mesa mesa = this.items.get(position);

        // view_img_producto.setImageDrawable(p.getImagen()); ARREGLAR LO DE IMAGEN
        if (alumno.estaInscripto(mesa))
            v.setBackgroundColor(Color.GREEN);
        //txtNombre.setText(mesa.getMateria().getNombre()+" "+mesa.getCarrera().getCodigo());
        Carrera carrera = alumno.getCarreraById(mesa.getCarrera());
        Materia materia = carrera.getMateriaById(mesa.getMateria());

        view_txt_codigo.setText(materia.getNombre()+" "+mesa.getFecha());
        view_txt_nombre.setText(carrera.getNombre().toLowerCase());

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
     * Metodo personalizado para filtrar listado de productos por nombre y codigo
     * @param codigo String (convertido a minuscula para buscar)
     * @param nombre String (convertido a minuscula para buscar)
     */
    public void filtrado(String codigo, String nombre){

        String filtro_nombre = nombre.toString().toLowerCase();
        String filtro_codigo = codigo.toString().toLowerCase();

        List<Mesa> filtrado = new ArrayList<>();

        /*for (Mesa producto : this.items_all){
            if (producto.getNombre().toLowerCase().contains(filtro_nombre) &&
                    producto.getCodigo().toLowerCase().contains(filtro_codigo))
                filtrado.add(producto);
        }*/

        this.refreshData(filtrado);
    }



}