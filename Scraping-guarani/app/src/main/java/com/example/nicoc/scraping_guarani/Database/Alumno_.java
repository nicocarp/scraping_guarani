package com.example.nicoc.scraping_guarani.Database;

import com.example.nicoc.scraping_guarani.Guarani.Modelos.Carrera;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by nicoc on 01/12/17.
 */

@Entity
public class Alumno_ {
    @Id(autoincrement=true) private Long id;

    @NotNull@Unique private String codigo;

    @NotNull private String nombre;
    @NotNull private String username;

    @NotNull private String password;

    @ToMany(referencedJoinProperty = "alumnoId")
    private List<Carrera_> carreras;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1968508766)
    private transient Alumno_Dao myDao;

    @Generated(hash = 477693166)
    public Alumno_(Long id, @NotNull String codigo, @NotNull String nombre,
            @NotNull String username, @NotNull String password) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.username = username;
        this.password = password;
    }

    @Generated(hash = 1480228186)
    public Alumno_() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1306115062)
    public List<Carrera_> getCarreras() {
        if (carreras == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            Carrera_Dao targetDao = daoSession.getCarrera_Dao();
            List<Carrera_> carrerasNew = targetDao._queryAlumno__Carreras(id);
            synchronized (this) {
                if (carreras == null) {
                    carreras = carrerasNew;
                }
            }
        }
        return carreras;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1828847903)
    public synchronized void resetCarreras() {
        carreras = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 795374045)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAlumno_Dao() : null;
    }

}
