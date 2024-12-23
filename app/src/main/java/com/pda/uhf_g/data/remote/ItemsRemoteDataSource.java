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
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ItemsRemoteDataSource {
    private Retrofit retrofit;
    private Items api;
    public static final String API_URL = "http://10.100.1.182:8001/";
    private List<PosicionamientoEntity> catalog;

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

    public static class PosicionamientoRequest {
        @SerializedName("fecha_inventario")
        public String fechaInventario;
        public List<PosicionamientoEntity> inventario;

        public PosicionamientoRequest(String fechaInventario, List<PosicionamientoEntity> inventario) {
            this.fechaInventario = fechaInventario;
            this.inventario = inventario;
        }
    }

    public interface Items {
        @POST("post_associated_inventory/")
        Call<PosicionamientoRequest> upload(PosicionamientoRequest requestData);

        @GET("get_inventory/")
        Call<PosicionamientoRequest> download();
    }

    public Call<PosicionamientoRequest> upload(List<PosicionamientoEntity> items){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        formatter.setTimeZone(TimeZone.getTimeZone("America/Bogota")); // Set your desired time zone
        String formattedDateTime = formatter.format(new Date());

        PosicionamientoRequest requestData = new PosicionamientoRequest(formattedDateTime, items);
        return api.upload(requestData);
    }

    public Call<PosicionamientoRequest> downloadItems(){
        return api.download();
    }

    public List<PosicionamientoEntity> fetchItems() {
        catalog = getStubData();
        return catalog;
    }

    public List<PosicionamientoEntity> getStubData() {
        ArrayList<PosicionamientoEntity> items = new ArrayList<PosicionamientoEntity>();
        items.add(new PosicionamientoEntity("1", "1", "E280699520005011FB53EC59"));
        items.add(new PosicionamientoEntity("2", "1", "E28068942000501F1286CC5A"));
        return items;
    }

}
