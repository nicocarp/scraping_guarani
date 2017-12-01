package com.example.nicoc.scraping_guarani.Database;

import android.content.Context;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Alumno;

import org.greenrobot.greendao.database.Database;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


/**
 * Created by nicoc on 01/12/17.
 */

public class ManagerDB extends DaoMaster.OpenHelper{

    private static Database db;
    private static DaoSession daoSession;
    private static ManagerDB instance = null;

    public ManagerDB(Context context, String name){
        super(context, name);

        db = this.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        instance = this;

        /*if (daoSession.getProductoDao().queryBuilder().list().isEmpty())
            this.generar_usuario();*/
    }

    public static ManagerDB getInstance(){

        if (instance == null)
            throw new RuntimeException("Sin conexion a con bd");
        return instance;
    }


    public Alumno_ getAlumnoActivo() {
        List<Alumno_> result = daoSession.getAlumno_Dao().queryBuilder().list();
        if (result.isEmpty())
            return null;
        else
        return result.get(0);
    }
}