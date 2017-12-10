package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static android.content.Context.MODE_PRIVATE;

public class ListadoMesasFragment extends Fragment implements
        IListadoMesasFragment.View, IListadoMesasFragment.ViewFragment {

    private IListadoMesasFragment.Presenter presenter;
    private IListadoMesasFragment.ViewContainer mListener;

    @BindView(R.id.lista) ListView lista;

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
        lista.setAdapter(new ListadoMesasAdapter(this, AlumnoActivity._alumno));
    }

    @Override
    public void updateList() {
        getItems();
    }


}
