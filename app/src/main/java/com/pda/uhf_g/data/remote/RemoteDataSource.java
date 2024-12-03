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

public class RemoteDataSource {
    private CompositeDisposable disposables = new CompositeDisposable();
    public String API_URL;
    private Retrofit retrofit;

    public RemoteDataSource(String api_url){
        API_URL = api_url;

        // Create a very simple REST adapter which points the GitHub API.
        retrofit =
                new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

//    public void publishInventory() {
//        Call<List<TestService.Contributor>> call = github.contributors("square", "retrofit");
//
//        disposables.add(
//            Single.fromCallable(() -> TestService.main("").execute())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                    if (response.isSuccessful()) {
//                        // Fetch and print a list of the contributors to the library.
//                        List<TestService.Contributor> contributors = response.body();
//                        assert contributors != null;
//                        for (TestService.Contributor contributor : contributors) {
//                            Log.d("TestService", contributor.login + " (" + contributor.contributions + ")");
//                        }
//
//                    } else {
//                        // Handle error
//                    }
//                }, error -> {
//                    // Handle network or other exceptions
//                    Log.e("TestService", "Error fetching items", error);
//                    // Show error message to user, retry the request, etc.
//                    // Example: Toast.makeText(context, "Failed to fetch items", Toast.LENGTH_SHORT).show();
//                })
//        );
//        disposables.clear(); // Dispose of subscriptions to prevent leaks
//    }


}
