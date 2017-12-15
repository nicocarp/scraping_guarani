package com.example.nicoc.scraping_guarani.Guarani.Modelos;

/**
 * Created by nicoc on 30/11/17.
 */

public class Inscripcion {

    private String carrera;
    private String  fecha;
    private String tipo;
    private String materia;

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechayHora() {
        return fecha;
    }
    public String getHora(){
        String[] result = this.getFechayHora().split(" ");
        return result[1];
    }
    public String getFecha(){
        String[] result = this.getFechayHora().split(" ");
        return result[0];
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean esDeLaMesa(Mesa mesa) {
        return this.getCarrera().equals(mesa.getCarrera()) &&
                this.getMateria().equals(mesa.getMateria());
    }
}
