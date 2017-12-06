package com.example.nicoc.scraping_guarani.Guarani;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Inscripcion;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Guarani.Modelos.Mesa;
import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
    private String last_cookies;
    private String error;
    private String username;
    private String password;

    public String getMensaje() {
        String m = this.mensaje;
        this.mensaje = "";
        return m;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    private String mensaje;

    public Guarani() throws IOException {
        this.url_base = URL;
        this.username = "";
        this.password= "";
        this.startConnection();
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

    public boolean inscribirseMesaById(Alumno alumno, Mesa mesa, String tipo) throws IOException {
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
        m.put("anio_academico", "2017");
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

    public ArrayList<Inscripcion> getMesasAnotadas() throws IOException {
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
            String cod_carrera = getMatch(divs_detalle.get(i).select("span").get(1).text(), "\\(([0-9]+)\\)");

            Elements links_mesas = table_mesas.get(i).select("[href*=desinscribirseExamen]");
            for (int z = 0; z < links_mesas.size(); z++){
                Inscripcion inscripcion = new Inscripcion();
                String cod_materia = getMatch(links_mesas.get(z).text(), "\\(([A-Z0-9]+)\\)");
                inscripcion.setCarrera(cod_carrera);
                inscripcion.setMateria(cod_materia);
                inscripcion.setTipo(table_mesas.get(i).select("tr").get(1).children().get(5).text().toLowerCase());
                inscripciones.add(inscripcion);
            }
        }
        return inscripciones;
    }

    public Boolean desinscribirseDeMesa(String id_carrera, String id_materia) throws IOException {
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

    private Map<String, String> getPostInscripcionMesa(String codigo_materia, String legajo){
        Map<String, String> m = new HashMap<String, String>();
        m.put("operacion", "exa00006");
        m.put("carrera", "38");
        m.put("legajo", legajo);
        m.put("materia", codigo_materia);
        m.put("generica", "");
        m.put("anio_academico", "2017");
        m.put("turno_examen", "NOVIEMBRE%202017%20%282%29");
        m.put("mesa_examen", codigo_materia+"-REG");
        m.put("llamado", "1");
        m.put("tipo inscripcion", "R"); // este cambia a L si es libre
        m.put("tipo_incripcion0", "L");
        m.put("mesa", "on");
        m.put("tipo_inscripcion1", "R");
        return m;
    }

    /**
     * Crea instancia alumnmo seteandole sus datos: nombre, legajo y carreras. Por cada carrera setea si es regular o no.
     * @param carreras del plan de estudio del alumno.
     * @return Alumno instancia.
     * @throws IOException
     */
    public Alumno getDatosAlumno(ArrayList<Carrera> carreras) throws IOException {
        Document document = this.document_base;
        String url;

        Alumno alumno = new Alumno();

        // DATOS DEL ALUMNO
        url = document.select("[src*=barra]").first().attr("abs:src");
        document = this.connection.url(url).get();
        String nombre_alumno = getMatch(document.html(), "NombreUsuario   = \"([A-Z | \\s]+)\";");
        String apellido_alumno = getMatch(document.html(), "ApellidoUsuario = \"([A-Z | \\s]+)\";");
        alumno.setNombre(nombre_alumno + " "+apellido_alumno);
        // LEGAJO DEL ALUMNO
        document = this.document_base;
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();
        url = document.select("[href*=elegirCertificado]").first().attr("abs:href");
        String url_certificado = url;
        document = this.connection.url(url).get();
        Element div_errores = document.select("div.mensaje_ua_contenido").first();

        alumno.setCarreras(carreras);
        if (div_errores != null){
            alumno.setRegular(false);
        }else{
            alumno.setRegular(true);
            url = document.select("a:contains(Alumno Regular)").first().attr("abs:href");
            document = this.connection.url(url).get();
            String legajo_alumno = "";
            for (Carrera carrera : alumno.getCarreras()){
                url = document.select("a:contains("+carrera.getNombre()+")").first().attr("abs:href");
                document = this.connection.url(url).get();
                div_errores = document.select("div.mensaje_ua_contenido").first();
                if (div_errores == null){
                    if (legajo_alumno == "")
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

    public ArrayList<Mesa> getMesasDeExamen(Carrera carrera) throws IOException {

        Document document = this.document_base;
        String url;


        document = this.document_base;
        // obtengo frame de operaciones (menu izquierda)
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        String url_inscripcion = document.select("a:contains(examen)").first().attr("abs:href");
        document = this.connection.url(url_inscripcion).get();

        //url = document.select("[text*=LICENCIATURA]").first().attr("abs:href");
        Element e = document.select("[text*=LICENCIATURA EN SISTEMAS]").first();

        Elements link_carreras = document.select("[href*=elegirMateriaInscCursada]");
        for (Element elem : link_carreras){
            System.out.print(elem.text());
            System.out.println(elem.attr("abs:href"));
        }

        // IGNORAMOS LAS CARRERAS SOLO MIRAMOS LICENCIATURA
        url = document.select("a:contains(LICENCIATURA EN SISTEMAS)").first().attr("abs:href");
        document = this.connection.url(url).get();

        Elements link_materias = document.select("[href*=elegirMesaInscExamen]");

        String cod, nombre, fecha, hora, profesores, turno;
        Element elem;

        ArrayList<String> resultados = new ArrayList<>();
        String result = " {\"Result:\" { \"alumno\": [[alumno]], \"mesas\": [[mesas]], \"inscripciones\": [[inscripciones]] }}";


        String mesas_json="";
        String base_mesa_json = "{\"codigo\": [[codigo]], " +
                "\"fecha\": [[fecha]], " +
                "\"hora\":[[hora]], " +
                "\"turno\":[[turno]], " +
                "\"profesores\": [[profesores]]," +
                "\"correlativas\": [[correlativas]] }";


        ArrayList<Mesa> mesas = new ArrayList<Mesa>();
        for (int i = 0;  i < link_materias.size(); i++ ){
            Mesa mesa = new Mesa();
            elem = link_materias.get(i);

            cod = getMatch(elem.text(), "\\(([A-Z0-9]+)\\)");
            nombre = getMatch(elem.text(), "([\\s | [- | A-ZÑÁÉÍÓÚ]]+)");

            mesa.setMateria(cod);

            url = elem.attr("abs:href");
            document = this.connection.url(url).get();

            String mesa_json = base_mesa_json;
            mesa_json=mesa_json.replace("[[codigo]]", cod);

            Element div_errores = document.select("div.mensaje_ua_contenido").first();
            System.out.print("MATERIA "+ cod + nombre );
            if (div_errores == null){
                fecha = document.select("td:matches(^[0-9]{2}/[0-9]{2}/[0-9]{4})").first().text();
                hora  = document.select("td:matches(^[0-9]{2}:[0-9]{2})").first().text();
                System.out.println("TURNOOO");
                turno = getMatch(document.html(), "([A-Z]+%[0-9]+[%[0-9]+]*)");

                mesa_json = mesa_json.replace("[[fecha]]", fecha);
                mesa_json = mesa_json.replace("[[hora]]", hora);
                mesa_json = mesa_json.replace("[[turno]]", turno);

                mesa.setFecha(fecha+hora);
                mesa.setTurno(turno);

                url = document.select("[href*=verDetalle]").first().attr("abs:href");
                document = this.connection.url(url).get();
                profesores = document.select("span.detalle_resaltado").first().html();
                mesa_json= mesa_json.replace("[[profesores]]", profesores);

            }
            else{
                for (String _cod_materia :  getMatches(div_errores.html(), "([A-Z]+[0-9]+)")){
                    mesa.addMateriaNecesariaById(_cod_materia);
                }
            }
            // vuelvo a navegar hacia el listado e mesas.
            document = this.connection.url(url_inscripcion).get();
            url = document.select("a:contains(LICENCIATURA EN SISTEMAS)").first().attr("abs:href");
            document = this.connection.url(url).get();
            link_materias = document.select("[href*=elegirMesaInscExamen]");
            mesas_json = mesas_json.concat(mesa_json);
            mesas.add(mesa);
        }
        //mesas_json= mesas_json.replaceAll("[\\[\\[\\D\\]\\]]", "");
        result = result.replace("[[mesas]]", mesas_json);


        System.out.println("RESULT \n"+ result);

        // AHORA RECORRO LAS MATERIAS PARA LAS CUALES ESTOY ANOTADO.
        return mesas;
    }

    public ArrayList<Mesa> _getMesasDeExamen() throws IOException {

        Document document = this.document_base;
        String url;
        ArrayList<Mesa> mesas = new ArrayList<Mesa>();

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        String url_inscripcion = document.select("a:contains(examen)").first().attr("abs:href");
        document = this.connection.url(url_inscripcion).get();

        Elements links_carreras = document.select("a[href*=elegirMateriaInscExamen]");
        for (int indice = 0; indice<links_carreras.size(); indice++){
            Element link_carrera = links_carreras.get(indice);
            String codigo_carrera = getMatch(link_carrera.text(), "([0-9]+)");
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

                    cod = getMatch(elem.text(), "\\(([A-Z0-9]+)\\)");

                    mesa.setMateria(cod);

                    url = elem.attr("abs:href");
                    document = this.connection.url(url).get();

                    div_errores = document.select("div.mensaje_ua_contenido").first();
                    if (div_errores == null){
                        fecha = document.select("td:matches(^[0-9]{2}/[0-9]{2}/[0-9]{4})").first().text();
                        hora  = document.select("td:matches(^[0-9]{2}:[0-9]{2})").first().text();
                        turno = getMatch(document.html(), "([A-Z]+%[0-9]+[%[0-9]+]*)");

                        mesa.setFecha(fecha+" "+hora);
                        mesa.setTurno(turno);

                        url = document.select("[href*=verDetalle]").first().attr("abs:href");
                        document = this.connection.url(url).get();

                        profesores = document.select("span.detalle_resaltado").first().html();
                    }
                    else{
                        for (String _cod_materia :  getMatches(div_errores.html(), "([A-Z]+[0-9]+)")){
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

        return mesas;
    }

    /**
     * Devuelve listado de todos los matches
     * @param cadena donde buscar
     * @param regex expresion regular del patron.
     * @return ArrayList<String> de todos los match
     */
    private ArrayList<String> getMatches(String cadena, String regex){
        Pattern patron_codigo = Pattern.compile(regex);
        Matcher matcher = patron_codigo.matcher(cadena);
        String codigo_masteria="";
        ArrayList<String> result = new ArrayList();
        while (matcher.find())
            result.add(matcher.group(1));
        return result;
    }

    /**
     * Recibe una cadena y un patron, devuelve el ultimo match
     * @param cadena donde buscar
     * @param regex expresion regular del patron que se desea buscar.
     * @return String del ultimo match
     */
    private String getMatch(String cadena, String regex){
        Pattern patron = Pattern.compile(regex);
        Matcher matcher = patron.matcher(cadena);
        String result = "";
        while (matcher.find()){
            result = matcher.group(1);
        }
        return result;
    }

    public Boolean estaLogueado() throws IOException {
        Document document = this.connection.url(URL).get();
        String url = document.select("[src*=barra]").first().attr("abs:src");
        document = this.connection.url(url).get();

        // click sobre boton iniciar Sesion
        if (document.select("[href*=identificarse]").first() == null)
            return true;
        return false;
    }
    /**
     * Intentamos hacer un login al servidor. Si hubo error, retrorno false y seteo mensaje de error.
     * @param username nombre de usuario plano.
     * @param password contraseña plana.
     * @return boolean si se pudo loguear.
     * @throws IOException
     */
    public boolean login(String username, String password) throws IOException {
        if (this.connection == null)
            this.startConnection();

        if (this.username.equals(username) && !this.password.equals(password) && estaLogueado())
            return true;
        if (!this.username.equals(username))
            desloguearse();


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
        String encrypted_password = this.getEncryptedPassword(this.getHash(document.html()), password);

        document = this.connection
                .data(getPost(username, encrypted_password))
                .url(url)
                .post();

        Elements div_mensajes_errores = document.select("div.mensaje_ua_contenido");
        if (!div_mensajes_errores .isEmpty()){
            this.setError(div_mensajes_errores.first().text());
            return false;
        }
        this.username = username;
        this.password = password;
        return true;
    }

    /**
     * Creamos el paquete con datos para relizar el post para LOGIN.
     * @param username nombre de usuario plano a loguearse.
     * @param password hash de la contrasenia del usuario.
     * @return Los datos del post.
     */
    private Map<String, String> getPost(String username, String password){
        Map<String, String> m = new HashMap<String, String>();
        m.put("calc_diff", "");
        m.put("campo_set", "fUsuario");
        m.put("fUsuario", username);
        m.put("bClave", "");
        m.put("fClave", password);
        m.put("Aceptar", "aceptar");
        return m;
    }

    /**
     * Busca en texto html+js el valor de hashSesion.
     * @param buscar String que representa una pagina web (html + js).
     * @return el valor de la variable "var hashSesion = [/w]"
     */
    private String getHash(String buscar){

        Pattern patron = Pattern.compile("hashSesion = \"([a-z0-9]+)\";");
        Matcher matcher = patron.matcher(buscar);
        String hash="";
        while (matcher.find())
            hash = matcher.group(1);
        return hash;
    }
    /**
     * Utiliza el algoritmo MD5 para encriptar hash y password
     * @param hash debe ser obtenido de la pagina web.
     * @param password es el valor de la clave real que se desea loguear.
     * @return La clave encriptada junto con el hash.
     */
    private String getEncryptedPassword(String hash, String password){

        String md5 = MD5(password);
        md5 = MD5(md5+ hash);
        return md5;
    }

    private String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    /**
     * Recorremos el plan de estudios de cada una de las materias del alumno.
     * @return ArrayList<Carrera> representan las carreras del alumno, sea regular o no.
     * @throws IOException
     */
    public ArrayList<Carrera> getPlanDeEstudios() throws IOException {
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

            String nombre_carrera = getMatch(a_carrera.text(), "([A-Z][[A-Z]+ | [,\\(\\)\\s]{1}]+)");
            String codigo_carrera = getMatch(a_carrera.text(), "([0-9]+)");

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
     * Borramos las cookies.
     * @throws IOException
     */
    public void desloguearse() throws IOException {
        this.connection.cookies(new HashMap<String,String>());
        this.username = "";
        this.password = "";
        this.startConnection();
    }

    public Alumno _getDatosAlumno(String username, String password) throws IOException {
        if(login(username, password)){

            ArrayList<Carrera> carreras = getPlanDeEstudios();
            Alumno alumno = getDatosAlumno(carreras);
            //ArrayList<Mesa_> mesas = _getMesasDeExamen(carreras);
            alumno.setInscripciones(getMesasAnotadas());
            return alumno;
        }
        return null;
    }

    public Alumno getAlumno() throws IOException {
        ArrayList<Carrera> carreras = getPlanDeEstudios();
        Alumno alumno = getDatosAlumno(carreras);
        alumno.setInscripciones(getMesasAnotadas());
        return alumno;
    }

    public static void main(String [] args) throws IOException, NoSuchAlgorithmException {
        // franco = 31636564 gabriel1
        // gaston = 27042881 valenti2
        // maxi =  37860301  ym7k
        Guarani g = new Guarani();
        if (g.login("38147310", "q1w2e3r4")){
            ArrayList<Carrera> carreras = g.getPlanDeEstudios();
            Alumno alumno = g.getDatosAlumno(carreras);
            System.out.println("-- Alumno " + alumno.getNombre());
            System.out.println("-- Carreras" );

            for (Carrera c : carreras){
                System.out.println(c.getCodigo() +" "+ c.getNombre() +" "+ c.getActivo());
            }
            ArrayList<Mesa> mesas = g._getMesasDeExamen();
            System.out.println("-- MESAS DE EXAMEN" + mesas.size());
            for (Mesa mesa : mesas){
                System.out.println(mesa.getCarrera() +" "+mesa.getMateria());
            }

            /*
            if (g.desinscribirseDeMesa("38", "MA048"))
                System.out.println(g.getMensaje());
            else
                System.out.println(g.getError());


            if (g.inscribirseMesaById(alumno, mesas.get(mesas.size()-1), "regular"))
                System.out.println(g.getMensaje());
            else
                System.out.println("Error: "+g.getError());

            */
            System.out.println("-- Mesas a las que esta anotado");


            alumno.setInscripciones(g.getMesasAnotadas());

            System.out.println(alumno.getInscripciones());

            Gson gson = new Gson();
            String s = gson.toJson(alumno);
            System.out.println(s);
        }
        else{
            System.out.println(g.getError());
        }



     }

}
