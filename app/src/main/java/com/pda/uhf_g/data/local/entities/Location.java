package com.pda.uhf_g.data.local.entities;

public class Location {
    private String mega_zona_id;
    private String zona_id;
    private String sector_id;
    private String piscina;

    private String piscina_id;

    public Location(String mega_zona, String zona, String sector, String piscina_id, String piscina) {
        this.mega_zona_id = mega_zona;
        this.zona_id = zona;
        this.sector_id = sector;
        this.piscina_id = piscina_id;
        this.piscina = piscina;
    }

    public String getMegaZonaID() {
        return mega_zona_id;
    }

    public String getZonaID() {
        return zona_id;
    }

    public String getSectorID() {
        return sector_id;
    }
    public String getPiscinaID() {
        return piscina_id;
    }
    public String getPiscina() {
        return piscina;
    }
}
