package com.pda.uhf_g.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class ItemEntity {
    @PrimaryKey
    @NonNull
    public int cid;  // Catalogo ID
    private String descripcion;
    private String serie;
    private String codigoCampo;
    private String marca;
//    private String itemPadre; // Assuming it can be null
//    private String estado;
//    private String sector;
//    private String zona;

    public ItemEntity(@NonNull int cid, String descripcion, String serie, String codigoCampo, String marca) {
        this.cid = cid;
        this.descripcion = descripcion;
        this.serie = serie;
        this.codigoCampo = codigoCampo;
        this.marca = marca;
//        this.itemPadre = itemPadre;
//        this.estado = estado;
//        this.sector = sector;
//        this.zona = zona;
    }

    public int getCid() {
        return cid;
    }


    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripion) {
        this.descripcion = descripion;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCodigoCampo() {
        return codigoCampo;
    }

    public void setCodigoCampo(String codigoCampo) {
        this.codigoCampo = codigoCampo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

//    public String getItemPadre() {
//        return itemPadre;
//    }
//
//    public void setItemPadre(String itemPadre) {
//        this.itemPadre = itemPadre;
//    }
//
//    public String getEstado() {
//        return estado;
//    }
//
//    public void setEstado(String estado) {
//        this.estado = estado;
//    }
//
//    public String getSector() {
//        return sector;
//    }
//
//    public void setSector(String sector) {
//        this.sector = sector;
//    }
//
//    public String getZona() {
//        return zona;
//    }
//
//    public void setZona(String zona) {
//        this.zona = zona;
//    }
}
