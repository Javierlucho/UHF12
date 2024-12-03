package com.pda.uhf_g.data.remote;

import android.util.Log;

import com.pda.uhf_g.data.local.entities.TagData;
import com.pda.uhf_g.service.TestService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class CatalogRemoteDataSource {
    private CompositeDisposable disposables = new CompositeDisposable();
    public static final String API_URL = "http://192.168.1.49:31000";
    public static final String GET_PONDS_BY_FILTER_ROUTE = "";
    private List<TagData> catalog;

    public CatalogRemoteDataSource(){
        super();
    }

}
