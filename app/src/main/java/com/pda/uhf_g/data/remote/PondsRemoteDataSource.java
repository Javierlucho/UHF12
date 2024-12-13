package com.pda.uhf_g.data.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class PondsRemoteDataSource{
    public final static String API_URL= "http://192.168.1.49:31000/";
    private Retrofit retrofit;
    private Ponds ponds;
    //{ "match": {"meta_data.Sector": ["CAMERUN"]},  "range": {},  "contains": {}}
    public static class Filter {
        public final String megazone;
        public final String zone;
        public final String sector;
        public final String level;

        public Filter( String megazone, String zone, String sector, String level) {
            this.megazone = megazone;
            this.zone = zone;
            this.sector = sector;
            this.level = level;
        }
    }

    public static class PondData {
        public String uuid;
        public String name;
        public String status;
        public String type;
        public double hectares;
        public List<String> projects;
        public List<String> roles;
        public String updated_date;
        public JsonObject meta_data;
        public JsonArray gateways;
        public PondData( String uuid, String name, String status, String type, double hectares, List<String> projects, List<String> roles, String updated_date, JsonObject meta_data, JsonArray gateways) {
            this.uuid = uuid;
            this.name = name;
            this.status = status;
            this.type = type;
            this.hectares = hectares;
            this.projects = projects;
            this.roles = roles;
            this.updated_date = updated_date;
            this.meta_data = meta_data;
            this.gateways = gateways;
        }

    }

    public static class Items {
        public final List<PondData> items;

        public Items(List<PondData> items) {
            this.items = items;
        }
    }

    public static class PondsResponse {
        public final String message;
        public final Integer status_code;
        public final String func;
        public final Items payload;
        public final Integer total;

        public PondsResponse(String message, Integer status_code, String func, Items payload, Integer total) {
            this.message = message;
            this.status_code = status_code;
            this.func = func;
            this.payload = payload;
            this.total = total;
        }
    }

    public PondsRemoteDataSource(){
        // Create a very simple REST adapter which points the GitHub API.
        retrofit =
                new Retrofit.Builder()
                        .baseUrl(API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        // Create an instance of our API interface.
        ponds = getRetrofit().create(Ponds.class);
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public interface Ponds {
        @POST("/api/v1/pond/filters")
        Call<ResponseBody> getFilters(@Body Filter filter);
        @GET("/api/v1/pond")
        Call<PondsResponse> getPonds(
              @Query("offset") int offset,
              @Query("limit") int limit,
              @Query("all") boolean all,
              @Query("filters") String filters);
    }

    public Call<ResponseBody> getFilters(String megazone, String zone, String sector, String level){
        Filter filter  = new Filter("CALIFORNIA", null, null, "megazone");
        if (Objects.equals(level, "megazone")){
            filter  = new Filter(null, null, null, level);
        } else if (Objects.equals(level, "zone")){
            filter  = new Filter(megazone, null, null, level);
        } else if (Objects.equals(level, "sector")){
            filter  = new Filter(megazone, zone, null, level);
        } else if (Objects.equals(level, "pond")){
            filter  = new Filter(megazone, zone, sector, level);
        } else {
            return ponds.getFilters(filter);
        }

        return ponds.getFilters(filter);
    }

    public Call<PondsResponse> getPonds(String matchKey, String matchValue){

        // Create the JSON object dynamically
        JsonObject filters = new JsonObject();
        JsonObject match = new JsonObject();

        // Create array to make the match
        JsonArray matchArray = new JsonArray();
        matchArray.add(matchValue);
        match.add(matchKey, matchArray);
        filters.add("match", match);
        filters.add("range", new JsonObject());
        filters.add("contains", new JsonObject());

        // Convert the JSON object to a string
        Gson gson = new Gson();
        String jsonFilters = gson.toJson(filters);

        Log.d("remote", "Filters: " + jsonFilters);
        return ponds.getPonds(0, 0, true, jsonFilters);
    }

}
