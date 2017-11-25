package com.example.nicoc.scraping_guarani;

import android.os.SystemClock;
import android.util.Log;

import com.example.nicoc.scraping_guarani.Modelos.Materia;
import com.example.nicoc.scraping_guarani.Modelos.Mesa;
import com.example.nicoc.scraping_guarani.Modelos.Profesor;
import com.example.nicoc.scraping_guarani.Modelos.TipoMesa;

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

    public boolean inscribirseMesaById(String id_materia) throws IOException {
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

        System.out.println(document.html());

        Element form = document.select("form").first();
        url = form.attr("abs:action");

        System.out.println(url);
        document = this.connection
                .data(getPostInscripcionMesa(id_materia))
                .url(url)
                .post();
        System.out.println(document.html());
        return true;
    }

    public void getMesasAnotadas() throws IOException {
        Document document = this.document_base;
        String url;

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("[href*=eliminarInscExamenes]").first().attr("abs:href");
        document = this.connection.url(url).get();

        System.out.println(document.html());
        System.out.println("PARA DESINCRIBIRSE");
        for (Element mesa : document.select("[href*=desinscribirseExamen]")){
            System.out.println(mesa.text());
        }
    }

    public void desinscribirseDeMesa(String id_materia) throws IOException {
        Document document = this.document_base;
        String url;

        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("[href*=eliminarInscExamenes]").first().attr("abs:href");
        document = this.connection.url(url).get();

        Element a_materia = document.select("a:contains(IF017)").first();
        System.out.println("DEBO ELIMINAR");
        System.out.println(a_materia.text() + a_materia.attr("abs:href"));
        url = a_materia.attr("abs:href");
        document = this.connection.url(url).get();
        System.out.println(document.html());

    }

    private Map<String, String> getPostInscripcionMesa(String codigo){
        Map<String, String> m = new HashMap<String, String>();
        m.put("operacion", "exa00006");
        m.put("carrera", "38");
        m.put("legajo", "34-38-5620");
        m.put("materia", "IF017");
        m.put("generica", "");
        m.put("anio_academico", "2017");
        m.put("turno_examen", "NOVIEMBRE%202017%20%282%29");
        m.put("mesa_examen", "IF017-REG");
        m.put("llamado", "1");
        m.put("tipo inscripcion", "R"); // este cambia a L si es libre
        m.put("tipo_incripcion0", "L");
        m.put("mesa", "on");
        m.put("tipo_inscripcion1", "R");
        return m;
    }

    public ArrayList<Mesa> getMesasDeExamen() throws IOException {

        Document document = this.document_base;
        String url;

        // obtengo frame de operaciones (menu izquierda)
        url = document.select("[src*=operaciones]").first().attr("abs:src");
        document = this.connection.url(url).get();

        url = document.select("a:contains(examen)").first().attr("abs:href");
        document = this.connection.url(url).get();

        //url = document.select("[text*=LICENCIATURA]").first().attr("abs:href");
        Element e = document.select("[text*=LICENCIATURA]").first();

        Elements link_carreras = document.select("[href*=elegirMateriaInscCursada]");
        for (Element elem : link_carreras){
            System.out.print(elem.text());
            System.out.println(elem.attr("abs:href"));
        }

        //url = link_carreras.get(1).attr("abs:href");
        url = document.select("a:contains(LICENCIATURA)").first().attr("abs:href");
        document = this.connection.url(url).get();

        Elements link_materias = document.select("[href*=elegirMesaInscExamen]");
        for (Element element : link_materias){
            Mesa mesa = new Mesa();
            String codigo_materia = getCodigoMateria(element.text());
            String nombre_materia = getNombreMateria(element.text());
            System.out.println("Un codigo de mesa disponible"+ codigo_materia.toString());
        }


        //document = this.connection.url(url).get();

        return null;


    }

    private String getCodigoMateria(String cadena){
        Pattern patron_codigo = Pattern.compile("(\\([A-Z0-9]+\\))");
        Matcher matcher = patron_codigo.matcher(cadena);
        String codigo_masteria="";
        while (matcher.find())
            codigo_masteria = matcher.group(1);
        return codigo_masteria;
    }

    private String getNombreMateria(String cadena){
        Pattern patron_nombre = Pattern.compile("([\\s | [A-ZÑÁÉÍÓÚ]]+)");
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

    public static void main(String [] args) throws IOException, NoSuchAlgorithmException {
        Guarani g = new Guarani("http://www.dit.ing.unp.edu.ar/v2070/www/");
        g.login("38147310", "n0r1m2n3");
        //g.getMesasDeExamen();
        //g.inscribirseMesaById("IF017");
        //g.getMesasAnotadas();
        g.desinscribirseDeMesa("uncodigo");
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
