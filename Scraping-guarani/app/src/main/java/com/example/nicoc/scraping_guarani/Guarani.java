package com.example.nicoc.scraping_guarani;

import android.os.SystemClock;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Modelos.Alumno;
import com.example.nicoc.scraping_guarani.Modelos.Carrera;
import com.example.nicoc.scraping_guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Modelos.Profesor;
import com.example.nicoc.scraping_guarani.Modelos.TipoMesa;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nicoc on 23/11/17.
 */

public class Guarani {

    private Document document_base;
    private Connection connection;
    private String url_base;
    private String last_cookies;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;

    public Guarani(String url) throws IOException {
        this.url_base = url;
        this.startConnection();
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

    public boolean inscribirseMesaById(String id_materia, String legajo) throws IOException {
        Document document = this.document_base;
        String url;

        // obtengo frame de operaciones (menu izquierda)
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("a:contains(examen)").first().attr("abs:href");
        document = this.connection.url(url).get();

        //url = document.select("[text*=LICENCIATURA]").first().attr("abs:href");
        url = document.select("a:contains(LICENCIATURA)").first().attr("abs:href");
        document = this.connection.url(url).get();

        Element link_mesa = document.select("a:contains("+id_materia+")").first();

        System.out.println(link_mesa.attr("abs:href"));
        url = link_mesa.attr("abs:href");
        document = this.connection.url(url).get();

        Element form = document.select("form").first();
        url = form.attr("abs:action");


        document = this.connection
                .data(getPostInscripcionMesa(id_materia, legajo))
                .url(url)
                .post();
        return true;
    }

    public String getMesasAnotadas() throws IOException {
        Document document = this.document_base;
        String url;

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("[href*=eliminarInscExamenes]").first().attr("abs:href");
        document = this.connection.url(url).get();

        ArrayList<String> result = new ArrayList<String>();
        System.out.println("PARA DESINCRIBIRSE");
        for (Element mesa : document.select("[href*=desinscribirseExamen]")){
            result.add(getMatch(mesa.text(), "\\(([A-Z0-9]+)\\)"));
            System.out.println(mesa.text());
        }
        return result.toString();
    }

    public void desinscribirseDeMesa(String id_materia) throws IOException {
        Document document = this.document_base;
        String url;

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("[href*=eliminarInscExamenes]").first().attr("abs:href");
        document = this.connection.url(url).get();

        Element a_materia = document.select("a:contains("+id_materia+")").first();
        url = a_materia.attr("abs:href");
        document = this.connection.url(url).get();

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
    public Alumno getDatosAlumno(Carrera carrera) throws IOException {
        Document document = this.document_base;
        String url;

        Alumno alumno = new Alumno();

        // DATOS DEL ALUMNO
        url = document.select("[src*=barra]").first().attr("abs:src");
        document = this.connection.url(url).get();
        String nombre_alumno = getNombreAlumno(document.html(), "NombreUsuario   = \"([A-Z | \\s]+)\";");
        String apellido_alumno = getNombreAlumno(document.html(), "ApellidoUsuario = \"([A-Z | \\s]+)\";");
        // LEGAJO DEL ALUMNO
        document = this.document_base;
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();
        url = document.select("[href*=elegirCertificado]").first().attr("abs:href");
        document = this.connection.url(url).get();
        url = document.select("[href*=elegirCarreraCertificado]").first().attr("abs:href");
        document = this.connection.url(url).get();
        url = document.select("a:contains(LICENCIATURA)").first().attr("abs:href");
        document = this.connection.url(url).get();
        String legajo_alumno = document.select("input[name=legajo]").first().attr("value");
        System.out.println("Alumno "+ legajo_alumno+ " "+nombre_alumno +" "+ apellido_alumno);

        alumno.setLegajo(legajo_alumno);
        alumno.setNombre(nombre_alumno + " "+apellido_alumno);
        alumno.addCarrera(carrera);
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
        Element e = document.select("[text*=LICENCIATURA]").first();

        Elements link_carreras = document.select("[href*=elegirMateriaInscCursada]");
        for (Element elem : link_carreras){
            System.out.print(elem.text());
            System.out.println(elem.attr("abs:href"));
        }

        // IGNORAMOS LAS CARRERAS SOLO MIRAMOS LICENCIATURA
        url = document.select("a:contains(LICENCIATURA)").first().attr("abs:href");
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

            mesa.setMateria(carrera.getMateriaById(cod));

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


                System.out.println(" "+fecha + " " +hora +" "+ profesores+ " Turno "+turno);
            }
            else{
                for (String _cod_materia :  getMatches(div_errores.html(), "([A-Z]+[0-9]+)")){
                    mesa.addMateriaNecesariaById(carrera.getMateriaById(_cod_materia));
                }
                System.out.println("Debe aprobar "+ getMatches(div_errores.html(), "([A-Z]+[0-9]+)"));
            }
            // vuelvo a navegar hacia el listado e mesas.
            document = this.connection.url(url_inscripcion).get();
            url = document.select("a:contains(LICENCIATURA)").first().attr("abs:href");
            document = this.connection.url(url).get();
            link_materias = document.select("[href*=elegirMesaInscExamen]");
            mesas_json = mesas_json.concat(mesa_json);
            mesas.add(mesa);
        }
        //mesas_json= mesas_json.replaceAll("[\\[\\[\\D\\]\\]]", "");
        result = result.replace("[[mesas]]", mesas_json);
        String mesas_anotadas = getMesasAnotadas();
        result = result.replace("[[inscripciones]]", mesas_anotadas);
        System.out.println("RESULT \n"+ result);

        // AHORA RECORRO LAS MATERIAS PARA LAS CUALES ESTOY ANOTADO.
        return mesas;
    }



    private String getCodigoMateria(String cadena){
        Pattern patron_codigo = Pattern.compile("\\(([A-Z0-9]+)\\)");
        Matcher matcher = patron_codigo.matcher(cadena);
        String codigo_masteria="";
        while (matcher.find())
            codigo_masteria = matcher.group(1);
        return codigo_masteria;
    }
    private String getNombreAlumno(String cadena, String regex){
        Pattern patron_codigo = Pattern.compile(regex);
        Matcher matcher = patron_codigo.matcher(cadena);
        String codigo_masteria="";
        while (matcher.find())
            codigo_masteria = matcher.group(1);
        return codigo_masteria;
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

    private String getNombreMateria(String cadena){
        Pattern patron_nombre = Pattern.compile("([\\s | [- | A-ZÑÁÉÍÓÚ]]+)");
        Matcher matcher = patron_nombre.matcher(cadena);
        String nombre_materia="";
        while (matcher.find()) {
            nombre_materia = matcher.group(1);
        }
        return nombre_materia;
    }

    public boolean login(String username, String password) throws IOException {
        if (this.connection == null)
            this.startConnection();

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

        // Errores al hacer un login, cargados en div class=mensaje_ua_contenido:
        // Tu Clave es incorrecta, ya has intentado 1 vez/veces. Restan 2 intentos para que la clave sea bloqueada.
        // No existe el usuario de Autogestión ingresado, identifícate otra vez.
        Elements div_mensajes_errores = document.select("div.mensaje_ua_contenido");
        if (!div_mensajes_errores .isEmpty()){
            this.setError(div_mensajes_errores.first().text());
            return false;
        }
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

    public void testReplace(){
        String s = "123 [[hola]] eso no se debe borrar [[chau]] 12312 {}";

        String result = s.replaceAll(" \\[\\[\\D+\\]\\] ", "");
        System.out.println(s);
        System.out.println(result);
    }

    /**
     * Solo devolvemos el plan de la materia de la LICENCIATURA.
     * @return
     * @throws IOException
     */
    public Carrera getPlanMateria() throws IOException {
        Document document = this.document_base;
        String url;

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();
        url = document.select("a:contains(Plan de Estudios)").first().attr("abs:href");
        document = this.connection.url(url).get();
        url = document.select("a:contains(LICENCIATURA)").first().attr("abs:href");
        document = this.connection.url(url).get();
        String nombre_carrera = document.select("div.detalle_contenido").first().children().get(0).text();
        String codigo_carrera = getMatch(nombre_carrera, "\\(([0-9]+)\\)");
        nombre_carrera = getMatch(nombre_carrera, "([A-Z][[A-Z]+ | [,\\(\\)\\s]{1}]+)");
        String plan_carrera = document.select("div.detalle_contenido").first().children().get(2).text();
        Element tabla = document.select("table.normal_plana_plan_estudios").first();

        Elements trs = tabla.select("tr[class*=normal_plana]");
        Element td;

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
        return carrera;

    }

    public static void main(String [] args) throws IOException, NoSuchAlgorithmException {
        Guarani g = new Guarani("http://www.dit.ing.unp.edu.ar/v2070/www/");
        //g.testReplace();
        g.login("27042881", "valenti2");

        Carrera carrera = g.getPlanMateria();
        Alumno alumno = g.getDatosAlumno(carrera);

        ArrayList<Mesa> mesas = g.getMesasDeExamen(carrera);

        //g.getMesasAnotadas();
        //g.desinscribirseDeMesa("IF054");
        //g.inscribirseMesaById("IF054", "34-38-4606");
        //List<Materia> materias = new ArrayList<Mater  ia>();

    }

    /**
     * Se encarga de scrapear datos del usuario y persistirlo en disco.
     * @param guarani Instancia de Guarani logueado.
     */
    public static void sincronizar(Guarani guarani){
        // recorrer para obtener datos del alumno, su regularidad, sus carreras, sus materias y correlatividad.

    }

}
