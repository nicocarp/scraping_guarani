package com.example.nicoc.scraping_guarani.Guarani;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nicoc on 06/12/17.
 * Clase con funciones necesarias por Guarani.
 */

public class Utils {

    /**
     * Busca en texto html+js el valor de hashSesion.
     * @param buscar String que representa una pagina web (html + js).
     * @return el valor de la variable "var hashSesion = [/w]"
     */
    public String getHash(String buscar){

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
    public String getEncryptedPassword(String hash, String password){

        String md5 = MD5(password);
        md5 = MD5(md5+ hash);
        return md5;
    }

    /**
     * Encriptacion algoritmo md5
     * @param md5
     * @return
     */
    public String MD5(String md5) {
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
     * Devolvemos data para post de inscripcion de mesa
     * @param codigo_materia
     * @param legajo
     * @return
     */
    public Map<String, String> getPostInscripcionMesa(String codigo_materia, String legajo){
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
     * Creamos el paquete con datos para relizar el post para LOGIN.
     * @param username nombre de usuario plano a loguearse.
     * @param password hash de la contrasenia del usuario.
     * @return Los datos del post.
     */
    public Map<String, String> getPostLogin(String username, String password){
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
     * Devuelve listado de todos los matches
     * @param cadena donde buscar
     * @param regex expresion regular del patron.
     * @return ArrayList<String> de todos los match
     */
    public ArrayList<String> getMatches(String cadena, String regex){
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
    public String getMatch(String cadena, String regex){
        Pattern patron = Pattern.compile(regex);
        Matcher matcher = patron.matcher(cadena);
        String result = "";
        while (matcher.find()){
            result = matcher.group(1);
        }
        return result;
    }

}
