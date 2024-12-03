package com.pda.uhf_g.data.remote;

import android.util.Log;

import com.pda.uhf_g.data.local.entities.Location;
import com.pda.uhf_g.data.local.entities.TagData;
import com.pda.uhf_g.service.TestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
        Call<List<Location>> getFilters(@Body Filter filter);
    }

    public Call<List<Location>> getFilters(String megazone, String zone, String sector, String level){
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


}
