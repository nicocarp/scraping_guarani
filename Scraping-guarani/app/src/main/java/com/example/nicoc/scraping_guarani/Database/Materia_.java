package com.example.nicoc.scraping_guarani.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nicoc on 01/12/17.
 */
@Entity
public class Materia_ {
    @Id(autoincrement=true) private Long id;

    @NotNull@Unique private String codigo;
    @NotNull private String periodoLectivo;
    @NotNull private Integer año;
    @NotNull private String nombre;
    @NotNull private long carreraId;
    @NotNull private String correlatividad;
    @Generated(hash = 1727064333)
    public Materia_(Long id, @NotNull String codigo, @NotNull String periodoLectivo,
            @NotNull Integer año, @NotNull String nombre, long carreraId,
            @NotNull String correlatividad) {
        this.id = id;
        this.codigo = codigo;
        this.periodoLectivo = periodoLectivo;
        this.año = año;
        this.nombre = nombre;
        this.carreraId = carreraId;
        this.correlatividad = correlatividad;
    }
    @Generated(hash = 1767898706)
    public Materia_() {
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
    public String getPeriodoLectivo() {
        return this.periodoLectivo;
    }
    public void setPeriodoLectivo(String periodoLectivo) {
        this.periodoLectivo = periodoLectivo;
    }
    public Integer getAño() {
        return this.año;
    }
    public void setAño(Integer año) {
        this.año = año;
    }
    public String getNombre() {
        return this.nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public long getCarreraId() {
        return this.carreraId;
    }
    public void setCarreraId(long carreraId) {
        this.carreraId = carreraId;
    }
    public String getCorrelatividad() {
        return this.correlatividad;
    }
    public void setCorrelatividad(String correlatividad) {
        this.correlatividad = correlatividad;
    }


}
