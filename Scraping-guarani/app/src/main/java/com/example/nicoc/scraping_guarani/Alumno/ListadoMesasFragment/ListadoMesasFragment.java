package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;

import static android.R.layout.simple_spinner_item;
import static android.content.Context.MODE_PRIVATE;
import static com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity._alumno;

public class ListadoMesasFragment extends Fragment implements
        IListadoMesasFragment.View, IListadoMesasFragment.ViewFragment {

    private IListadoMesasFragment.Presenter presenter;
    private IListadoMesasFragment.ViewContainer mListener;

    @BindView(R.id.lista) ListView lista;
    @BindView(R.id.cmb_carreras) Spinner cmb_carreras;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listado_mesas, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new ListadoMesasPresenter(this, getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE));
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        this.getItems();
        cargarComboCarreras();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IListadoMesasFragment.ViewContainer) {
            mListener = (IListadoMesasFragment.ViewContainer) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @OnItemClick(R.id.lista) void itemClick(int position){
        Mesa mesa= (Mesa) lista.getAdapter().getItem(position);
        this.mListener.onItemSelectedInFragment(mesa);
    }
    @Override
    public void getItems() {
        this.presenter.getItems();
    }

    @Override
    public void mostrarError(String error) {
        this.mListener.mostrarError(error);
    }

    @Override
    public void setItems() {
        lista.setAdapter(new ListadoMesasAdapter(this, _alumno));
    }

    @Override
    public void updateList() {
        getItems();
    }
    private void cargarComboCarreras() {
        ArrayList<String> carrera = new ArrayList<String>(Arrays.asList("Todas las carreras ..."));
        for (Carrera car : _alumno.getCarreras()) {
            carrera.add(car.getNombre().toLowerCase());
        }
        cmb_carreras.setAdapter(new ArrayAdapter<String>(getActivity(), simple_spinner_item, carrera));
    }
    @OnItemSelected(R.id.cmb_carreras)
    public void spinnerItemSelected(int position) {
        String nombre_carrera = cmb_carreras.getItemAtPosition(position).toString();
        ListadoMesasAdapter adapter = (ListadoMesasAdapter) lista.getAdapter();
        if (nombre_carrera.contains("Todas las carreras"))
            adapter.limpiarFiltro();
        else
            adapter.filtrado(_alumno.getCarreraByName(nombre_carrera).getCodigo());
    }


}
