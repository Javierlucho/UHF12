package com.pda.uhf_g.entity;

public class TagData {
    public String cid;            // Catalogo ID
    public String afid;           // Activos Fijos ID
    public String tid;            // Tag ID
    public String name;           // Name
    public String description;    // Description

    public TagData(String cid, String afid, String tid, String name, String description) {
        this.tid = tid;
        this.cid = cid;
        this.afid = afid;
        this.name = name;
        this.description = description;
    }

    public String getTid() {
        return this.tid;
    }

    public String getCid() {
        return this.cid;
    }

    public String getAfid() {
        return this.afid;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
