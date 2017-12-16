package com.example.nicoc.scraping_guarani.Alumno;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import java.util.ArrayList;

/**
 * Created by nicoc on 06/12/17.
 */

public class IAlumno {
    public interface View {
        public void mostrarError(String error);
        public void setAlumno(Alumno alumno);
        void inscriptoCorrectamente(String mensaje);
        void desinscriptoCorrectamente(String mensaje);
    }
    public interface Presenter {
        public void mostrarError(String error);
        public void desloguearse();
        public void getAlumno();
        public void onAlumno(Alumno alumno);
        public void inscribirseAMesa(Alumno alumno, Mesa mesa, String tipo);
        void deslogueado();

        public void onInscripcion(String mensaje);

        void desinscribirseDeMesa(Inscripcion inscripcion);

        void onDesinscripcion(String mensaje);
    }
    public interface Model {
        public void getAlumno();
        public void inscribirseAMesa(Alumno alumno, Mesa mesa, String tipo);
        public void desloguearse();

        void desinscribirseDeMesa(Inscripcion inscripcion);
    }
}
