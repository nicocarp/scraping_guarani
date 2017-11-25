package com.example.nicoc.scraping_guarani.Modelos;

import java.util.List;

public class Correlatividad{

    private String codigo_materia;
    private List<String> correlativas_aprobacion;
    private List<String> correlativas_cursar;

    /**
     * Retorna el codigo de la materia.
     * @return
     */
    public String getCodigo_materia() {
        return codigo_materia;
    }

    public void setCodigo_materia(String codigo_materia) {
        this.codigo_materia = codigo_materia;
    }

    /**
     * Devolvemos los codigo de las masterias correlativas necesarias para la aprobacion de la materia.
     * @return Lista de String s, donde s es un codigo de Materia.
     */
    public List<String> getCorrelativas_aprobacion() {
        return correlativas_aprobacion;
    }

    public void setCorrelativas_aprobacion(List<String> correlativas_aprobacion) {
        this.correlativas_aprobacion = correlativas_aprobacion;
    }

    /**
     * Devolvemos los codigo de las masterias correlativas necesarias para poder cursar la materia.
     * @return Lista de String s, donde s es un codigo de Materia.
     * @return
     */
    public List<String> getCorrelativas_cursar() {
        return correlativas_cursar;
    }

    public void setCorrelativas_cursar(List<String> correlativas_cursar) {
        this.correlativas_cursar = correlativas_cursar;
    }
}
