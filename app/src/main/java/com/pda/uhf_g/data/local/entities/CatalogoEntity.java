package com.pda.uhf_g.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "catalogo")
public class CatalogoEntity {
    @PrimaryKey
    @NonNull
    public String cid;  // Catalogo ID

    private String metadata;


    public CatalogoEntity(@NonNull String cid) {
        this.cid = cid;
    }

    public String getCid() {
        return cid;
    }


    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
