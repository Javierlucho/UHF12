package com.pda.uhf_g.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag_items")
public class TagItemEntity {
    @PrimaryKey
    @NonNull
    public String cid;  // Catalogo ID
    public String afid; // Activos Fijos ID
    public String tid;  // Tag ID
    
    public long created_timestamp; // Timestamp
    public long updated_timestamp; // Timestamp

    // TODO: Add expected location fields (Latitude, Longitude) 
    
    public TagItemEntity(@NonNull String cid, String afid, String tid) {
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
    
    public void setCreatedTimestamp(long created_timestamp) {
        this.created_timestamp = created_timestamp;
    }
    
    public long getCreatedTimestamp() {
        return created_timestamp;
    }
    
    public void setUpdatedTimestamp(long updated_timestamp) {
        this.updated_timestamp = updated_timestamp;
    }


}
