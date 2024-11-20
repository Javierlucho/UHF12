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

    private String tipo_de_item_id;
    private String tipo_de_item_nombre;

    private String ubicacion_prevista_mega_zona;
    private String ubicacion_prevista_zona;
    private String ubicacion_prevista_sector;
    private String ubicacion_prevista_piscina;

    private String ubicacion_actual_mega_zona;
    private String ubicacion_actual_zona;
    private String ubicacion_actual_sector;
    private String ubicacion_actual_piscina;

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

    public void setActualLocation(String mega_zona, String zona, String sector, String piscina){
        this.ubicacion_actual_mega_zona = mega_zona;
        this.ubicacion_actual_zona = zona;
        this.ubicacion_actual_sector = sector;
        this.ubicacion_actual_piscina = piscina;
    }

    public void setPreviewLocation(String mega_zona, String zona, String sector, String piscina){
        this.ubicacion_prevista_mega_zona = mega_zona;
        this.ubicacion_prevista_zona = zona;
        this.ubicacion_prevista_sector = sector;
        this.ubicacion_prevista_piscina = piscina;
    }

    public void setGps(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setTipoDeItem(String tipo_de_item_id, String tipo_de_item_nombre){
        this.tipo_de_item_id = tipo_de_item_id;
        this.tipo_de_item_nombre = tipo_de_item_nombre;
    }

    public String getTipo_de_item_id() {
        return tipo_de_item_id;
    }

    public void setTipo_de_item_id(String tipo_de_item_id) {
        this.tipo_de_item_id = tipo_de_item_id;
    }

    public String getTipo_de_item_nombre() {
        return tipo_de_item_nombre;
    }

    public void setTipo_de_item_nombre(String tipo_de_item_nombre) {
        this.tipo_de_item_nombre = tipo_de_item_nombre;
    }

    public String getUbicacion_prevista_mega_zona() {
        return ubicacion_prevista_mega_zona;
    }

    public void setUbicacion_prevista_mega_zona(String ubicacion_prevista_mega_zona) {
        this.ubicacion_prevista_mega_zona = ubicacion_prevista_mega_zona;
    }

    public String getUbicacion_prevista_zona() {
        return ubicacion_prevista_zona;
    }

    public void setUbicacion_prevista_zona(String ubicacion_prevista_zona) {
        this.ubicacion_prevista_zona = ubicacion_prevista_zona;
    }

    public String getUbicacion_prevista_sector() {
        return ubicacion_prevista_sector;
    }

    public void setUbicacion_prevista_sector(String ubicacion_prevista_sector) {
        this.ubicacion_prevista_sector = ubicacion_prevista_sector;
    }

    public String getUbicacion_prevista_piscina() {
        return ubicacion_prevista_piscina;
    }

    public void setUbicacion_prevista_piscina(String ubicacion_prevista_piscina) {
        this.ubicacion_prevista_piscina = ubicacion_prevista_piscina;
    }

    public String getUbicacion_actual_mega_zona() {
        return ubicacion_actual_mega_zona;
    }

    public void setUbicacion_actual_mega_zona(String ubicacion_actual_mega_zona) {
        this.ubicacion_actual_mega_zona = ubicacion_actual_mega_zona;
    }

    public String getUbicacion_actual_zona() {
        return ubicacion_actual_zona;
    }

    public void setUbicacion_actual_zona(String ubicacion_actual_zona) {
        this.ubicacion_actual_zona = ubicacion_actual_zona;
    }

    public String getUbicacion_actual_sector() {
        return ubicacion_actual_sector;
    }

    public void setUbicacion_actual_sector(String ubicacion_actual_sector) {
        this.ubicacion_actual_sector = ubicacion_actual_sector;
    }

    public String getUbicacion_actual_piscina() {
        return ubicacion_actual_piscina;
    }

    public void setUbicacion_actual_piscina(String ubicacion_actual_piscina) {
        this.ubicacion_actual_piscina = ubicacion_actual_piscina;
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
}
