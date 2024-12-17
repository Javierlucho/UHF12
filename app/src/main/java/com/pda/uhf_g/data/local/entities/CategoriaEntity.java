package com.pda.uhf_g.data.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "catalogo_tipo")
public class CategoriaEntity {
    @PrimaryKey(autoGenerate = false)
    public int idCategoria;
    public String nombre;
    public String nomenclatura;
    public String codigoHexadecimal;
    public String estado;

    public CategoriaEntity(int idCategoria, String nombre, String nomenclatura, String codigoHexadecimal, String estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.nomenclatura = nomenclatura;
        this.codigoHexadecimal = codigoHexadecimal;
        this.estado = estado;
    }
}
