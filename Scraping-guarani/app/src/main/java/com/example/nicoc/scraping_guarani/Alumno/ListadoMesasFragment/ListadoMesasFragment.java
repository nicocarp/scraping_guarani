package com.example.nicoc.scraping_guarani.Alumno.ListadoMesasFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity;
import com.example.nicoc.scraping_guarani.Alumno.AlumnoActivity.IUpdateList;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

import static android.content.Context.MODE_PRIVATE;



public class ListadoMesasFragment extends Fragment implements IListadoMesasFragment.View {

    private IListadoMesasFragment.Presenter presenter;

    @BindView(R.id.lista) ListView lista;

    public interface onMesaSeleccionadaListener {
        void onMesaSeleccionadaFragment(Mesa mesa);
        public void mostrarError(String error);
    }

    private onMesaSeleccionadaListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_listado_mesas, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        this.presenter = new ListadoMesasPresenter(this, getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE));
        this.setItemss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onMesaSeleccionadaListener) {
            mListener = (onMesaSeleccionadaListener) context;
            ((AlumnoActivity)getActivity()).setFragment(new IUpdateList(){
                @Override
                public void updateList(){
                    setItemss();
                }
            });

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @OnItemClick(R.id.lista) void itemClick(int position){
        Mesa mesa= (Mesa) lista.getAdapter().getItem(position);
        this.mListener.onMesaSeleccionadaFragment(mesa);
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
    public void setItems(ArrayList<Mesa> mesas, ArrayList<Inscripcion> inscripciones) {
        lista.setAdapter(new ListadoMesasAdapter(this, AlumnoActivity._alumno));

    }
    public void setItemss(){
        mostrarError("en set items");
        lista.setAdapter(new ListadoMesasAdapter(this, AlumnoActivity._alumno));

    }


}
