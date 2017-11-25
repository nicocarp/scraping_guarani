package com.example.nicoc.scraping_guarani;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nicoc on 23/11/17.
 */

public class AsyncLogin extends AsyncTask<String, Void, Boolean> {

    public interface IView{
        public void mostrarError(String s);
        public void logueado();
    }
    private IView activity;

    public AsyncLogin(IView activity) {
        this.activity = activity;
    }

    /**
     * Precondicion: recibe parametros (url, user, password)
     * @param parametros
     * @return
     */
    @Override
    protected Boolean doInBackground(String... parametros) {
        Guarani guarani = new ManagerGuarani(parametros[0], parametros[1], parametros[2]).getInstance();
        return (guarani != null);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean){
            this.activity.logueado();
        }
        else{
            this.activity.mostrarError("Error no se pudo loguear");
        }
    }

    public static void main(String [] args) throws IOException, NoSuchAlgorithmException {
        String url_base = "http://www.dit.ing.unp.edu.ar/v2070/www/";;
        Document document;
        Connection connection;
        String url;

        System.out.println("Inciiado");

        // Hacemos la primer peticion a url base.
        connection = Jsoup.connect(url_base).userAgent("Mozilla");
        document = connection.get();

        // Seteamos las cookies a la conexion.
        Map<String, String> loginCookies = connection.response().cookies();
        connection.cookies(loginCookies);

        List<String> frames_src = new ArrayList<String>();
        for (Element frame : document.getElementsByTag("frame")){
            frames_src.add(frame.attr("abs:src"));
        }

        url = frames_src.get(1);
        System.out.println("GET a "+url);
        connection.url(url);
        document = connection.get();

        Element a_href_login = document.getElementsByTag("a").first();
        url = a_href_login.attr("abs:href");

        System.out.println(url);
        connection.url(url);
        document = connection.get();

        Pattern patron = Pattern.compile("hashSesion = \"([a-z0-9]+)\";");
        Matcher buscar = patron.matcher(document.html());
        String hash="";
        while (buscar.find())
            hash = buscar.group(1);
        System.out.println(document.html().toString());



        String url_form_login = document.getElementsByTag("form").first().attr("abs:action");
        System.out.println("URL de Login "+ url_form_login);


        String _md = MD5("n0r1m2n3");
        String _md2 = MD5(_md + hash);

        System.out.println("MI CLAVE: "+_md2);

        document = connection
                .url(url_form_login)
                .data("calc_diff", "")
                .data("campo_set", "fUsuario")
                .data("fUsuario", "38147310")
                .data("bClave","")
                .data("fClave", _md2)
                .data("Aceptar", "aceptar").post();
        System.out.println("ULTIMO DOC: "+ document.html());



    }



    public static String MD5(String md5) {
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


}


