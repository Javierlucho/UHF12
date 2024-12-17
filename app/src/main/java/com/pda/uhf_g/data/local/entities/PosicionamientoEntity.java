package com.pda.uhf_g.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "posicionamiento")
public class PosicionamientoEntity {
    @PrimaryKey
    @NonNull
    public String afid; // Activos Fijos ID
    public String cid;  // Catalogo ID
    public String tid;  // Tag ID
    private String categoria_id;
    private String ubicacion_prevista;
    private String ubicacion_actual;

    private double latitude; // Latitude
    private double longitude; // Longitude

    public PosicionamientoEntity(@NonNull String cid, String afid, String tid) {
        this.cid = cid;
        this.afid = afid;
        this.tid = tid;
    }

    public String getTid() {
        return tid;
    }


    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }

    public void setAfid(String afid) {
        this.afid = afid;
    }

    public String getAfid() {
        return afid;
    }

    public void setGps(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUbicacion_prevista() {
        return ubicacion_prevista;
    }

    public void setUbicacion_prevista(String ubicacion_prevista) {
        this.ubicacion_prevista = ubicacion_prevista;
    }

    public String getUbicacion_actual() {
        return ubicacion_actual;
    }

    public void setUbicacion_actual(String ubicacion_actual) {
        this.ubicacion_actual = ubicacion_actual;
    }
}
