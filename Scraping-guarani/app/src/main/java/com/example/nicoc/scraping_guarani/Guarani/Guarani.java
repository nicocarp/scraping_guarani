package com.example.nicoc.scraping_guarani.Guarani;

import android.util.Log;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Auth;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nicoc on 23/11/17.
 */

public class Guarani {

    private final static String URL  = "http://www.dit.ing.unp.edu.ar/v2070/www/";
    private Document document_base;
    private Connection connection;
    private String url_base;
    private String error;
    private String mensaje;
    private static Guarani _instance = null;
    private Utils utils;

    private static Auth _auth = null;

    private Guarani() throws IOException {
        this.url_base = URL;
        this.utils = new Utils();
        this.startConnection();
    }

    public static void setAuth(Auth auth){
        if (auth == null || auth.getUsername().isEmpty() || auth.getPassword().isEmpty())
            throw new IllegalArgumentException("Debe inicializar guarani con una cuenta valida.");
        _auth = auth;
    }

    public synchronized static Guarani getInstance() throws IOException, IllegalArgumentException {
        if (_auth == null)
            throw new IllegalArgumentException("Debe inicializar guarani con una cuenta valida.");
        if (_instance == null)
            _instance = new Guarani();
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
        String tipo = _tipo.toLowerCase();
        Document document = this.document_base;
        String url;
        if (!tipo.equals("libre") && !tipo.equals("regular")){
            this.setError("Error en tipo de mesa. {libre, regular}");
            return false;
        }else{
            if (tipo == "libre")
                tipo = "LIBRE";
            else
                tipo = "REG";
        }

        // obtengo frame de operaciones (menu izquierda)
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("a:contains(examen)").first().attr("abs:href");
        document = this.connection.url(url).get();

        //url = document.select("[text*=LICENCIATURA]").first().attr("abs:href");
        url = document.select("a:contains("+mesa.getCarrera()+")").first().attr("abs:href");
        document = this.connection.url(url).get();

        Element link_mesa = document.select("a:contains("+mesa.getMateria()+")").first();


        System.out.println(link_mesa.attr("abs:href"));
        url = link_mesa.attr("abs:href");
        document = this.connection.url(url).get();

        Element form = document.select("form").first();
        url = form.attr("abs:action");

        Map<String, String> m = new HashMap<String, String>();
        m.put("operacion", "exa00006");
        m.put("carrera", mesa.getCarrera());
        m.put("legajo", alumno.getCarreraById(mesa.getCarrera()).getLegajo());
        m.put("materia", mesa.getMateria());
        m.put("generica", "");
        m.put("anio_academico", mesa.getAnio_acedemico());
        m.put("turno_examen", mesa.getTurno());
        m.put("mesa_examen", mesa.getMateria()+"-"+tipo);
        m.put("llamado", "1");
        m.put("tipo inscripcion", tipo.substring(0,1)); // este cambia a L si es libre
        m.put("tipo_incripcion0", "L");
        m.put("mesa", "on");
        m.put("tipo_inscripcion1", "R");

        document = this.connection
                .data(m)
                .url(url)
                .post();

        String result = document.select("div.mensaje_ua_contenido").first().text();
        if (result.contains("se ha registrado correctamente")){
            this.setMensaje(result);
            return true;
        }
        else
            this.setError(result);
        return false;
    }

    public synchronized ArrayList<Inscripcion> getMesasAnotadas() throws IOException {
        Document document = this.document_base;
        String url;

        ArrayList<Inscripcion> inscripciones = new ArrayList<Inscripcion>();
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("[href*=eliminarInscExamenes]").first().attr("abs:href");
        document = this.connection.url(url).get();

        Elements divs_detalle = document.select("div.detalle");
        Elements table_mesas = document.select("table");

        //if (divs_detalle.size() == table_mesas.size()){}
        for (int i = 0; i < divs_detalle.size(); i++){
            String cod_carrera = this.utils.getMatch(divs_detalle.get(i).select("span").get(1).text(), "\\(([0-9]+)\\)");

            Elements links_mesas = table_mesas.get(i).select("[href*=desinscribirseExamen]");
            for (int z = 0; z < links_mesas.size(); z++){
                Inscripcion inscripcion = new Inscripcion();
                String cod_materia = this.utils.getMatch(links_mesas.get(z).text(), "\\(([A-Z0-9]+)\\)");
                inscripcion.setCarrera(cod_carrera);
                inscripcion.setMateria(cod_materia);
                inscripcion.setTipo(table_mesas.get(i).select("tr").get(1).children().get(5).text().toLowerCase());
                inscripciones.add(inscripcion);
            }
        }
        /*if (inscripciones.size() == 0){
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setTipo("regular");
                inscripcion.setMateria("IF005");
                inscripcion.setCarrera("38");
                inscripcion.setFecha(" 23/02/2017");
                inscripciones.add(inscripcion);
        }*/
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
        Document document = this.document_base;
        String url;

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("[href*=eliminarInscExamenes]").first().attr("abs:href");
        document = this.connection.url(url).get();

        Elements divs_detalle = document.select("div.detalle");
        Elements table_mesas = document.select("table");

        int indice_carrera = -1;
        int indice_mesa = -1;

        for (Element div_detalle : divs_detalle){
            if (div_detalle.select("span").get(1).text().contains(id_carrera))
                indice_carrera = divs_detalle.indexOf(div_detalle);
        }

        if (indice_carrera != -1){
            //divs_detalle.get(indice).select("span").get(1).text();
            Elements link_mesas = table_mesas.get(indice_carrera).select("[href*=desinscribirseExamen]");
            for (Element link_mesa : link_mesas){
                if (link_mesa.text().contains(id_materia))
                    indice_mesa = link_mesas.indexOf(link_mesa);
            }
            System.out.println(indice_mesa);
        }
        if (indice_carrera != -1 && indice_mesa != -1){
            url = table_mesas.get(indice_carrera).select("[href*=desinscribirseExamen]").get(indice_mesa).attr("abs:href");
            document = this.connection.url(url).get();
            if (document.text().contains("se ha registrado correctamente")){
                this.setMensaje(document.select("div.mensaje_ua_contenido").text());
                return true;
            }
            else
                this.setError(document.select("div.mensaje_ua_contenido").first().text());
        }else{
            this.setError("No existe una inscripcion para la materia "+id_materia +" e la carrera "+id_carrera);
        }
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
        Document document = this.document_base;
        String url;

        Alumno alumno = new Alumno();
        alumno.setCarreras(carreras);

        // nombre y apellido del Alumno
        url = document.select("[src*=barra]").first().attr("abs:src");
        document = this.connection.url(url).get();
        String nombre_alumno = this.utils.getMatch(document.html(), "NombreUsuario   = \"([A-Z | \\s]+)\";");
        String apellido_alumno = this.utils.getMatch(document.html(), "ApellidoUsuario = \"([A-Z | \\s]+)\";");
        alumno.setNombre(nombre_alumno + " "+apellido_alumno);
        // Es regular en la universidad ?
        document = this.document_base;
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();
        url = document.select("[href*=elegirCertificado]").first().attr("abs:href");
        String url_certificado = url;
        document = this.connection.url(url).get();
        Element div_errores = document.select("div.mensaje_ua_contenido").first();

        if (div_errores != null){
            alumno.setRegular(false);
        }else{
            alumno.setRegular(true);
            /* Para cada carrera del alumno, obtenemos su legajo y si es regular o no. */
            url = document.select("a:contains(Alumno Regular)").first().attr("abs:href");
            document = this.connection.url(url).get();
            for (Carrera carrera : alumno.getCarreras()){
                url = document.select("a:contains("+carrera.getNombre()+")").first().attr("abs:href");
                document = this.connection.url(url).get();
                div_errores = document.select("div.mensaje_ua_contenido").first();
                if (div_errores == null){
                    carrera.setLegajo(document.select("input[name=legajo]").first().attr("value"));
                    carrera.setActivo(true);
                }
                else
                    carrera.setActivo(false);
                // recargamos la pagin
                document = this.connection.url(url_certificado).get();
                url = document.select("a:contains(Alumno Regular)").first().attr("abs:href");
                document = this.connection.url(url).get();
            }
        }
        return alumno;
    }

    public synchronized ArrayList<Mesa> getMesasDeExamen() throws IOException {

        if (!estaLogueado())
            login();
        Document document = this.connection.url(URL).get();
        String url;
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        String url_inscripcion = document.select("a:contains(examen)").first().attr("abs:href");
        document = this.connection.url(url_inscripcion).get();

        Elements links_carreras = document.select("a[href*=elegirMateriaInscExamen]");
        for (int indice = 0; indice<links_carreras.size(); indice++){
            Element link_carrera = links_carreras.get(indice);
            String codigo_carrera = this.utils.getMatch(link_carrera.text(), "([0-9]+)");
            document = this.connection.url(link_carrera.attr("abs:href")).get();
            /* Codigo general */
            Element div_errores = document.select("div.mensaje_ua_contenido").first();
            Elements link_mesas;
            if (div_errores == null){
                link_mesas = document.select("[href*=elegirMesaInscExamen]");
                String cod, nombre, fecha, hora, profesores, turno;
                Element elem;

                for (int i = 0;  i < link_mesas.size(); i++ ){
                    Mesa mesa = new Mesa();
                    mesa.setCarrera(codigo_carrera);
                    elem = link_mesas.get(i);

                    cod = this.utils.getMatch(elem.text(), "\\(([A-Z0-9]+)\\)");

                    mesa.setMateria(cod);

                    url = elem.attr("abs:href");
                    document = this.connection.url(url).get();

                    div_errores = document.select("div.mensaje_ua_contenido").first();
                    if (div_errores == null){
                        mesa.setAnio_acedemico(document.select("legend").first().children().first().text());
                        fecha = document.select("td:matches(^[0-9]{2}/[0-9]{2}/[0-9]{4})").first().text();
                        hora  = document.select("td:matches(^[0-9]{2}:[0-9]{2})").first().text();
                        turno = this.utils.getMatch(document.html(), "([A-Z]+%[0-9]+[%[0-9]+]*)");

                        mesa.setFecha(fecha+" "+hora);
                        mesa.setTurno(turno);

                        url = document.select("[href*=verDetalle]").first().attr("abs:href");
                        document = this.connection.url(url).get();

                        profesores = document.select("span.detalle_resaltado").first().html();
                    }
                    else{
                        for (String _cod_materia :  this.utils.getMatches(div_errores.html(), "([A-Z]+[0-9]+)")){
                            mesa.addMateriaNecesariaById(_cod_materia);
                        }
                    }
                    mesas.add(mesa);
                    // vuelvo a navegar hacia el listado e mesas.
                    document = this.connection.url(url_inscripcion).get();
                    url = document.select("a:contains("+codigo_carrera+")").first().attr("abs:href");
                    document = this.connection.url(url).get();
                    link_mesas = document.select("[href*=elegirMesaInscExamen]");
                }
            }

            /* Refecargamosla pagina*/
            document = this.connection.url(url_inscripcion).get();
            links_carreras = document.select("a[href*=elegirMateriaInscExamen]");
        }
        /* Probando tirar datos*/
        /*if (mesas.isEmpty()){
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
        }*/
        return mesas;
    }

    /**
     * Determinamos si el alumno esta logueado ingresando al menu.
     * @return true si todavia existe la sesion.
     * @throws IOException
     */
    private synchronized Boolean estaLogueado() throws IOException {
        Document document = this.connection.url(URL).get();
        String url = document.select("[src*=barra]").first().attr("abs:src");
        document = this.connection.url(url).get();
        //  boton iniciar Sesion
        return (document.select("[href*=identificarse]").first() == null);
    }
    /**
     * Intentamos hacer un login al servidor. Si hubo error, retrorno false y seteo mensaje de error.
     * @return boolean si se pudo loguear.
     * @throws IOException
     */
    private  synchronized boolean login() throws IOException, IllegalArgumentException {
        if (estaLogueado())
            desloguearse();
        String username = _auth.getUsername();
        String password = _auth.getPassword();

        Document document;
        String url;

        // pedimos contenido del frame que contiene el boton iniciar sesion.
        url = document_base.select("[src*=barra]").first().attr("abs:src");
        document = this.connection.url(url).get();

        // click sobre boton iniciar Sesion
        url = document.select("[href*=identificarse]").first().attr("abs:href");
        document = this.connection.url(url).get();

        // obtenemos url a donde tirar el POST login.
        url = document.select("[name*=form1]").first().attr("abs:action");

        // obtenemos la encriptacion de la clave que se manda al servidor.
        String encrypted_password = this.utils.getEncryptedPassword(this.utils.getHash(document.html()), password);

        document = this.connection
                .data(this.utils.getPostLogin(username, encrypted_password))
                .url(url)
                .post();

        Elements div_mensajes_errores = document.select("div.mensaje_ua_contenido");
        if (!div_mensajes_errores .isEmpty()){
            throw new IllegalArgumentException(div_mensajes_errores.first().text());
        }
        return true;
    }

    /**
     * Recorremos el plan de estudios de cada una de las materias del alumno.
     * @return ArrayList<Carrera> representan las carreras del alumno, sea regular o no.
     * @throws IOException
     */
    private ArrayList<Carrera> getPlanDeEstudios() throws IOException {
        Document document = this.document_base;
        String url;

        ArrayList<Carrera> carreras = new ArrayList<Carrera>();

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();
        url = document.select("a:contains(Plan de Estudios)").first().attr("abs:href");
        document = this.connection.url(url).get();
        String url_plan_estudios = url;

        Elements a_carreras = document.select("[href*=planDeEstudios]");
        for (int i = 0; i< a_carreras.size() ; i++){
            Element a_carrera = a_carreras.get(i);

            String nombre_carrera = this.utils.getMatch(a_carrera.text(), "([A-Z][[A-Z]+ | [,\\(\\)\\s]{1}]+)");
            String codigo_carrera = this.utils.getMatch(a_carrera.text(), "([0-9]+)");

            url = document.select("a:contains("+nombre_carrera+")").first().attr("abs:href");
            document = this.connection.url(url).get();

            String plan_carrera = document.select("div.detalle_contenido").first().children().get(2).text();
            Element tabla = document.select("table.normal_plana_plan_estudios").first();

            Elements trs = tabla.select("tr[class*=normal_plana]");

            Carrera carrera = new Carrera();
            carrera.setNombre(nombre_carrera);
            carrera.setCodigo(codigo_carrera);
            carrera.setPlan(plan_carrera);

            ArrayList<Materia> materias = new ArrayList<Materia>();

            for (Element tr : trs){
                Materia materia = new Materia();

                materia.setAño(tr.children().get(0).text());
                materia.setCodigo(tr.children().get(1).text());
                materia.setNombre(tr.children().get(2).text());
                materia.setPeriodoLectivo(tr.children().get(3).text());

                if (tr.children().get(6).text().contains("correlativas")){
                    Element tr_correlatividad = tr.nextElementSibling();
                    Elements divs = tr_correlatividad.children().get(1).children();
                    materia.setCorrelatividad(tr_correlatividad.children().get(1).text());
                }
                materias.add(materia);
            }
            carrera.setMaterias(materias);
            carreras.add(carrera);

            // recargamos la apgina.
            document = this.connection.url(url_plan_estudios).get();
            a_carreras = document.select("[href*=planDeEstudios]");
        }
        return carreras;
    }
    /**
     * Si existe una sesion activa, cerramos sesion contra el servidor guarani.
     * @throws IOException
     */
    public void desloguearse() throws IOException {
        if (!this.estaLogueado())
            return;
        Document document = this.connection.url(URL).get();
        String url = document.select("[src*=barra]").first().attr("abs:src");
        document = this.connection.url(url).get();
        url = document.select("a[href*=finalizarSesion]").first().attr("abs:href");
        this.connection.url(url).get();
        _auth = null;
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

    public static void main(String [] args) throws IOException, NoSuchAlgorithmException {
        // franco = 31636564 gabriel1
        // gaston = 27042881 valenti2
        // maxi =  37860301  ym7k

        /*Guarani.setAuth(new Auth("37860301", "ym7k"));
        Guarani g = getInstance();

        if (g.login()){

            ArrayList<Carrera> carreras = g.getPlanDeEstudios();
            Alumno alumno = g.getDatosAlumno(carreras);
            System.out.println("-- Alumno " + alumno.getNombre());
            System.out.println("-- Carreras" );

            for (Carrera c : carreras){
                System.out.println(c.getCodigo() +" "+ c.getNombre() +" "+ c.getActivo());
            }
            ArrayList<Mesa> mesas = g.getMesasDeExamen();
            System.out.println("-- MESAS DE EXAMEN" + mesas.size());
            for (Mesa mesa : mesas){
                System.out.println(mesa.getCarrera() +" "+mesa.getMateria());
            }


            if (g.desinscribirseDeMesa("38", "MA048"))
                System.out.println(g.getMensaje());
            else
                System.out.println(g.getError());


            if (g.inscribirseMesaById(alumno, mesas.get(mesas.size()-1), "regular"))
                System.out.println(g.getMensaje());
            else
                System.out.println("Error: "+g.getError());


            System.out.println("-- Mesas a las que esta anotado");


            //alumno.setInscripciones(g.getMesasAnotadas());

            //System.out.println(alumno.getInscripciones());


        }
        else{
            System.out.println(g.getError());
        }
*/
        Gson gson = new Gson();

        String s = gson.toJson(new ArrayList<Mesa>());
        System.out.println(s);
        Type collectionType = new TypeToken<ArrayList<Mesa>>(){}.getType();
        ArrayList<Mesa> result = new Gson().fromJson(s, collectionType);
        System.out.println(result.size());

     }

}
