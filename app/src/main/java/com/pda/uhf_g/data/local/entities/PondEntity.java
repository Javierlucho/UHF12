package com.pda.uhf_g.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ponds",
        indices = {@Index(value = {"uuid"})})
public class PondEntity {
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    @NonNull
    private String uuid;

    private String megazone;

    private String megazone_id;

    private String zone;

    private String zone_id;

    private String sector;

    private String sector_id;
    private String pond;

    public PondEntity( String uuid, String megazone, String megazone_id, String zone,
                       String zone_id, String sector, String sector_id, String pond ) {
        uuid = uuid;
        megazone = megazone;
        megazone_id = megazone_id;
        zone = zone;
        zone_id = zone_id;
        sector = sector;
        sector_id = sector_id;
        pond = pond;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getMegazone() {
        return megazone;
    }

    public void setMegazone(String megazone) {
        this.megazone = megazone;
    }

    public String getMegazone_id() {
        return megazone_id;
    }

    public void setMegazone_id(String megazone_id) {
        this.megazone_id = megazone_id;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSector_id() {
        return sector_id;
    }

    public void setSector_id(String sector_id) {
        this.sector_id = sector_id;
    }

    public String getPond() {
        return pond;
    }

    public void setPond(String pond) {
        this.pond = pond;
    }

}