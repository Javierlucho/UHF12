package com.pda.uhf_g.data.local.entities;

public class Location {
    private String mega_zona;
    private String zona;
    private String sector;
    private String piscina;

    public Location(String mega_zona, String zona, String sector, String piscina) {
        this.mega_zona = mega_zona;
        this.zona = zona;
        this.sector = sector;
        this.piscina = piscina;
    }

    public String getPiscina() {
        return piscina;
    }
}
