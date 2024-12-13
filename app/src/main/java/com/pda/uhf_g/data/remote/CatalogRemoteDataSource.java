package com.pda.uhf_g.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class CatalogRemoteDataSource {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Retrofit retrofit;
    private Externo api;
    public static final String API_URL = "https://192.168.1.43:8005";
    public static final String AUTH_TOKEN = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzY1Mzg2Mjc5LCJpYXQiOjE3MzM4NTAyNzksImp0aSI6IjU4Y2RlNjM3NGY0NTQzYWJiMDBmNTYyODFmM2ZjNWYxIiwidXNlcl9pZCI6IjIifQ.Mn5hHzMQmT7-hIEGrl6LWPMeUAHvqfA0PFNrEMdpIUQ";
    public CatalogRemoteDataSource(){
        // Create a very simple REST adapter which points the GitHub API.
        retrofit =
                new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        // Create an instance of our API interface.
        api = getRetrofit().create(Externo.class);
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
    public static class ItemsResponse {
        public static class Item {
            @SerializedName("Id Item")
            public int idItem;

            @SerializedName("Descripcion")
            public String descripcion;

            @SerializedName("Serie")
            public String serie;

            @SerializedName("Codigo campo")
            public String codigoCampo;

            @SerializedName("Marca")
            public String marca;

            @SerializedName("Item padre")
            public String itemPadre; // Assuming it can be null

            @SerializedName("Estado")
            public String estado;

            @SerializedName("Sector")
            public String sector;

            @SerializedName("Zona")
            public String zona;
        }
        @SerializedName("Fecha de consulta")
        public String fechaDeConsulta;

        public List<ItemsResponse.Item> items;
    }

    public static class CatalogoResponse {
        public static class Item {
            @SerializedName("Id Categoria")
            public int idCategoria;

            @SerializedName("Nombre")
            public String nombre;

            @SerializedName("Nomenclatura")
            public String nomenclatura;

            @SerializedName("Codigo Hexadecimal")
            public String codigoHexadecimal;

            @SerializedName("Estado")
            public String estado;

            public Item(int idCategoria, String nombre, String nomenclatura, String codigoHexadecimal, String estado) {
                this.idCategoria = idCategoria;
                this.nombre = nombre;
                this.nomenclatura = nomenclatura;
                this.codigoHexadecimal = codigoHexadecimal;
                this.estado = estado;
            }

        }
        @SerializedName("Fecha de consulta")
        public String fechaConsulta;

        public List<CatalogoResponse.Item> items;

        public CatalogoResponse(String fechaConsulta, List<CatalogoResponse.Item> items) {
            this.fechaConsulta = fechaConsulta;
            this.items = items;
        }

    }

    public interface Externo {
//        @GET("/api/IPSP-GDE-Externo/items-list")
//        Call<ItemsResponse> getItemsById(
//                @Header("Authorization") String authHeader,
//                @Query("id_item") int id_item
//        );
        @GET("/api/IPSP-GDE-Externo/items-list")
        Call<ItemsResponse> getItemsBySector(
                @Header("Authorization") String authHeader,
                @Query("sector") String sector
        );
//        Call<ItemsResponse> getItemsByZona(
//                @Header("Authorization") String authHeader,
//                @Query("zona") String zona
//        );
        @GET("/api/IPSP-GDE-Externo/catalogo-items-list")
        Call<CatalogoResponse> getCatalogoItems(
                @Header("Authorization") String authHeader,
                @Query("estado") String estado
//                @Query("id_categoria") int id_categoria,
//                @Query("nombre") String limit,
//                @Query("nomenclatura") String nomenclatura,
        );
    }

    public Call<ItemsResponse> getItems(String sector){
        return api.getItemsBySector(AUTH_TOKEN, sector);
    }

    public Call<CatalogoResponse> getCatalogoItems(){
        return api.getCatalogoItems(AUTH_TOKEN, "ACTIVO");
    }

}
