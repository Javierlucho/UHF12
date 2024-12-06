package com.pda.uhf_g.data.remote;

import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.local.entities.TagData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ItemsRemoteDataSource {
    private CompositeDisposable disposables = new CompositeDisposable();
    public static final String API_URL = "http://localhost:3000/";
    private List<TagData> catalog;

    public ItemsRemoteDataSource(){
        super();
    }

    public interface Items {
        @POST("/upload")
        Call<List<PosicionamientoEntity>> upload();

        @GET("/download")
        Call<List<PosicionamientoEntity>> download();
    }

    public List<TagData> fetchItems() {
        catalog = getStubData();
        return catalog;
    }

    public List<TagData> getStubData() {
        ArrayList<TagData> items = new ArrayList<TagData>();
        items.add(new TagData("1", "1", "E280699520005011FB53EC59", "", ""));
        items.add(new TagData("1", "1", "1", "", ""));
        items.add(new TagData("1", "1", "1", "", ""));
        items.add(new TagData("1", "1", "1", "", ""));

        return items;
    }

}
