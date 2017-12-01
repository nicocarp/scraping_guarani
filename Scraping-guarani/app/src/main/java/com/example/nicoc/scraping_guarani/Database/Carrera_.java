package com.example.nicoc.scraping_guarani.Database;

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
public class Carrera_ {
    @Id(autoincrement=true) private Long id;
    @NotNull
    @Unique
    private String codigo;

    @NotNull private String nombre;

    @NotNull private Integer plan;

    private String legajo;

    private Boolean regular;

    @NotNull private long alumnoId;

    @ToMany(referencedJoinProperty = "carreraId")
    private List<Materia_> materias;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 320858770)
    private transient Carrera_Dao myDao;

    @Generated(hash = 318150552)
    public Carrera_(Long id, @NotNull String codigo, @NotNull String nombre,
            @NotNull Integer plan, String legajo, Boolean regular, long alumnoId) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.plan = plan;
        this.legajo = legajo;
        this.regular = regular;
        this.alumnoId = alumnoId;
    }

    @Generated(hash = 116861140)
    public Carrera_() {
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

    public Integer getPlan() {
        return this.plan;
    }

    public void setPlan(Integer plan) {
        this.plan = plan;
    }

    public String getLegajo() {
        return this.legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public Boolean getRegular() {
        return this.regular;
    }

    public void setRegular(Boolean regular) {
        this.regular = regular;
    }

    public long getAlumnoId() {
        return this.alumnoId;
    }

    public void setAlumnoId(long alumnoId) {
        this.alumnoId = alumnoId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1461259221)
    public List<Materia_> getMaterias() {
        if (materias == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            Materia_Dao targetDao = daoSession.getMateria_Dao();
            List<Materia_> materiasNew = targetDao._queryCarrera__Materias(id);
            synchronized (this) {
                if (materias == null) {
                    materias = materiasNew;
                }
            }
        }
        return materias;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1194076139)
    public synchronized void resetMaterias() {
        materias = null;
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
    @Generated(hash = 1371461605)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCarrera_Dao() : null;
    }


}
