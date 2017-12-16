package com.example.nicoc.scraping_guarani.Guarani.Modelos;

/**
 * Created by nicoc on 30/11/17.
 * Esta clase representa las inscripciones a examen activas, que pueden ser canceladas.
 * Guardamos codigo de carrera, codigo de materia, tipo si es regular o libre
 *  y fecha de la mesa asociada a la inscripcion.
 */

public class Inscripcion {

    private String carrera;
    private String  fecha;
    private String tipo;
    private String materia;

    /**
     * Retornamos el id de la carrera de la materia.
     * @return String id de la carrera.
     */
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

    /**
     * Retornamos la fecha y la hora de la mesa inscripta.
     * @return String fecha dd/MM/aaaa mm:ss.
     */
    public String getFechayHora() {
        return fecha;
    }
    public String getHora(){
        String[] result = this.getFechayHora().split(" ");
        return result[1];
    }

    /**
     * Retornamos la fecha de la mesa inscripta.
     * @return String fecha dd/mm/aaaa
     */
    public String getFecha(){
        String[] result = this.getFechayHora().split(" ");
        return result[0];
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Retornamos true si la inscripcion corresponde a la mesa.
     * Se comparan los id que son codigo_carrera+codigo_materia
     * @param mesa instancia de Mesa.
     * @return
     */
    public boolean esDeLaMesa(Mesa mesa) {
        return this.getCarrera().equals(mesa.getCarrera()) &&
                this.getMateria().equals(mesa.getMateria());
    }

    /**
     * Retornamos ID de inscripcion: codigo_carrera+codigo_materia
     * @return
     */
    public String getId() {
        return this.getCarrera()+this.getMateria();
    }
}
