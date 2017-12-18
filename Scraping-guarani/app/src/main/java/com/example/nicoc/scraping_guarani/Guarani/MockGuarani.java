package com.example.nicoc.scraping_guarani.Guarani;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by nicoc on 23/11/17.
 * Modulo representante del servidor SIU GUARANI. Presenta una api para la aplicacion.
 * Se realizan peticiones http por lo que deben ejecutarse en tareas en segundo pĺano.
 * Guarani es un singleton, con metodos synchronized para evitar inconsistencias entre hilos.
 */

public class MockGuarani {

    private final static String URL  = "http://www.dit.ing.unp.edu.ar/v2070/www/";
    private Document document_base;
    private Connection connection;
    private String url_base;
    private String error;
    private String mensaje;
    private static MockGuarani _instance = null;
    private Utils utils;

    private static Auth _auth = null;

    private MockGuarani() {
        this.url_base = URL;
        this.utils = new Utils();
        //this.startConnection();
    }

    public static void setAuth(Auth auth){
        if (auth == null || auth.getUsername().isEmpty() || auth.getPassword().isEmpty())
            throw new IllegalArgumentException("Debe inicializar guarani con una cuenta valida.");
        _auth = auth;
    }

    public synchronized static MockGuarani getInstance() throws IOException, IllegalArgumentException {
        if (_auth == null)
            throw new IllegalArgumentException("Debe inicializar guarani con una cuenta valida.");
        if (_instance == null)
            _instance = new MockGuarani();
        return _instance;
    }

    public String getMensaje() {
        String m = this.mensaje;
        this.mensaje = "";
        return m;
    }

    private void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getError() {
        String e = this.error;
        this.error = "";
        return e;
    }

    private void setError(String error) {
        this.error = error;
    }

    /**
     * Este metodo existe fundamentalmente para setear las cookies y mantener la misma conexion.
     * @throws IOException
     */
    private void startConnection() throws IOException {
        this.connection = Jsoup.connect(url_base).userAgent("Mozilla");
        this.document_base = this.connection.get();
        this.connection.cookies(this.connection.response().cookies());
    }

    /**
     * Se realiza una inscripcion a una mesa.
     * @param alumno instancia de Alumno que se inscribira a la mesa.
     * @param mesa instancia de Mesa que se va a inscribir.
     * @param _tipo de inscripcion, regular o libre. Convertida toLowerCase
     * @return true si no existio error, caso contrario obtener error de getError
     * @throws IOException
     */
    public synchronized boolean inscribirseMesaById(Alumno alumno, Mesa mesa, String _tipo) throws IOException {
        this.setError("Mock no puede inscribirse");
        return false;
    }

    /**
     * Devolvemos un listado de las inscripciones del alumno. Representan inscripcinoes que pueden
     *  se canceladas.
     * @return ArrayList<Inscripcion>
     * @throws IOException
     */
    public synchronized ArrayList<Inscripcion> getMesasAnotadas() throws IOException {

        ArrayList<Inscripcion> inscripciones = new ArrayList<Inscripcion>();
        if (inscripciones.size() == 0){
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setTipo("regular");
                inscripcion.setMateria("IF005");
                inscripcion.setCarrera("38");
                inscripcion.setFecha(" 23/02/2017");
                inscripciones.add(inscripcion);
        }
        return inscripciones;
    }

    /**
     * Se desinscribe de una mesa de examen, identificandola por codigo de carrera y materia.
     * @param id_carrera
     * @param id_materia
     * @return true si se logro desinscribir correctamente, sino obtener error con getError().
     * @throws IOException
     */
    public synchronized Boolean desinscribirseDeMesa(String id_carrera, String id_materia) throws IOException {

        this.setError("Mock no puede desinscribirse");
        return false;
    }

    /**
     * Crea instancia alumnmo seteandole sus datos: nombre y carreras.
     * Por cada carrera setea si es regular o no, y su correspondiente legajo.
     * @param carreras del plan de estudio del alumno.
     * @return Alumno instancia.
     * @throws IOException
     */
    private Alumno getDatosAlumno(ArrayList<Carrera> carreras) throws IOException {
        //crear alumno
        Alumno  a = new Alumno();
        return a;
    }

    /**
     * Este metodo devuelve las mesas de examenes habilitadas para inscripcion.
     * @return ArrayList<Mesa>
     * @throws IOException
     */
    public synchronized ArrayList<Mesa> getMesasDeExamen() throws IOException {

        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        if (mesas.isEmpty()){
            Mesa m = new Mesa();
            m.setCarrera("38");
            m.setMateria("IF003");
            m.setFecha("11/12/2013");
            m.setTurno("un-turno");
            mesas.add(m);
            Mesa m1 = new Mesa();
            m1.setCarrera("38");
            m1.setMateria("MA008");
            m1.setFecha("11/12/2013");
            m1.setTurno("un-turno");
            mesas.add(m1);
            Mesa m2 = new Mesa();
            m2.setCarrera("38");
            m2.setMateria("IF005");
            m2.setFecha("11/12/2013");
            m2.setTurno("un-turno");
            mesas.add(m2);
            Mesa m3 = new Mesa();
            m3.setCarrera("18");
            m3.setMateria("MA008");
            m3.setFecha("11/12/2013");
            m3.setTurno("un-turno");
            mesas.add(m3);
            Mesa m4 = new Mesa();
            m4.setCarrera("18");
            m4.setMateria("IF005");
            m4.setFecha("11/12/2013");
            m4.setTurno("un-turno");
            mesas.add(m4);
        }
        return mesas;
    }

    /**
     * Determinamos si el alumno esta logueado ingresando al menu.
     * @return true si todavia existe la sesion.
     * @throws IOException
     */
    private synchronized Boolean estaLogueado() throws IOException {
        return true;

    }
    /**
     * Intentamos hacer un login al servidor. Si hubo error, retrorno false y seteo mensaje de error.
     * @return boolean si se pudo loguear.
     * @throws IOException
     */
    private  synchronized boolean login() throws IOException, IllegalArgumentException {
        return true;
    }

    /**
     * Recorremos el plan de estudios de cada una de las materias del alumno.
     * @return ArrayList<Carrera> representan las carreras del alumno, sea regular o no.
     * @throws IOException
     */
    private ArrayList<Carrera> getPlanDeEstudios() throws IOException {
        ArrayList<Carrera> carreras  =new ArrayList<Carrera>();
        return carreras;
    }
    /**
     * Si existe una sesion activa, cerramos sesion contra el servidor guarani.
     * @throws IOException
     */
    public void desloguearse() throws IOException {

    }

    /**
     * Obtenemos datos del alumno, nombre, carreras y respectivas materias.
     * @return
     * @throws IOException
     */
    public Alumno getAlumno() throws IOException {
        if (!estaLogueado())
            login();
        ArrayList<Carrera> carreras = getPlanDeEstudios();
        Alumno alumno = getDatosAlumno(carreras);
        return alumno;
    }

    // EJEMPLO DE USO DE GUARANI.
    public static void main(String [] args) throws IOException, NoSuchAlgorithmException, ParseException {



    }
}
