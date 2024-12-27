package com.pda.uhf_g.data.remote;


import com.google.gson.annotations.SerializedName;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ItemsRemoteDataSource {
    private Retrofit retrofit;
    private Items api;
    public static final String API_URL = "http://10.100.1.182:8001/";
    private List<PosicionamientoEntity> catalog;

    public static class Ubicacion {
        public final String megazona;
        public final String zona;
        public final String sector;
        public final String piscina;

        public Ubicacion( String megazona, String zona, String sector, String piscina) {
            this.megazona = megazona;
            this.zona = zona;
            this.sector = sector;
            this.piscina = piscina;
        }
    }

    public static class GPS {
        public final Double latitud;
        public final Double longitud;

        public GPS( Double latitud, Double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }
    }

    public static class InventarioItem{
        public final String cid;
        public final String afid;
        public final String tid;
        public final String hex;
        @SerializedName("tipo_id")
        public final String tipoId;

        public final Ubicacion ubicacion;

        public final GPS gps;

        public InventarioItem( String cid, String afid, String tid, String hex, String tipoId, Ubicacion ubicacion, GPS gps) {
            this.cid = cid;
            this.afid = afid;
            this.tid = tid;
            this.hex = hex;
            this.tipoId = tipoId;
            this.ubicacion = ubicacion;
            this.gps = gps;
        }

    }

    public static class InventarioItemAlt{
        public final String cid;
        public final String afid;
        public final String tid;
        public final String hex;
        @SerializedName("tipo_id")
        public final String tipoId;
        public final Ubicacion ubicacion_prevista;
        public final Ubicacion ubicacion;
        public final GPS gps;

        public InventarioItemAlt( String cid, String afid, String tid, String hex, String tipoId, Ubicacion ubicacion_prevista, Ubicacion ubicacion, GPS gps) {
            this.cid = cid;
            this.afid = afid;
            this.tid = tid;
            this.hex = hex;
            this.tipoId = tipoId;
            this.ubicacion_prevista = ubicacion_prevista;
            this.ubicacion = ubicacion;
            this.gps = gps;
        }

    }
    public static class PosicionamientoPost {
        @SerializedName("fecha_inventario")
        public final String fechaInventario;
        @SerializedName("fecha_asociado")
        public final String fechaAsociado;
        public final List<InventarioItem> inventario;

        public PosicionamientoPost( String fechaInventario, String fechaAsociado, List<InventarioItem> inventario) {
            this.fechaInventario = fechaInventario;
            this.fechaAsociado = fechaAsociado;
            this.inventario = inventario;
        }
    }

    public static class PosicionamientoGet {
        @SerializedName("fecha_inventario")
        public String fechaInventario;
        public List<InventarioItemAlt> inventario;

        public PosicionamientoGet(String fechaInventario, List<InventarioItemAlt> inventario) {
            this.fechaInventario = fechaInventario;
            this.inventario = inventario;
        }
    }

    public ItemsRemoteDataSource(){
        retrofit =
                new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        // Create an instance of our API interface.
        api = getRetrofit().create(Items.class);
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }

    public interface Items {
        @POST("post_associated_inventory/")
        Call<PosicionamientoPost> upload(@Body PosicionamientoPost requestData);

        @GET("get_inventory/")
        Call<PosicionamientoGet> download();
    }

    public Call<PosicionamientoPost> upload(List<InventarioItem> items){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("America/Bogota")); // Set your desired time zone
        String formattedDateTime = formatter.format(new Date());

        PosicionamientoPost requestData = new PosicionamientoPost(formattedDateTime,formattedDateTime, items);
        return api.upload(requestData);
    }

    public Call<PosicionamientoGet> downloadItems(){
        return api.download();
    }

    public List<PosicionamientoEntity> fetchItems() {
        catalog = getStubData();
        return catalog;
    }

    public List<PosicionamientoEntity> getStubData() {
        ArrayList<PosicionamientoEntity> items = new ArrayList<PosicionamientoEntity>();
        //items.add(new PosicionamientoEntity("1", "1", "E280699520005011FB53EC59"));
        //items.add(new PosicionamientoEntity("2", "1", "E28068942000501F1286CC5A"));
        return items;
    }

}
