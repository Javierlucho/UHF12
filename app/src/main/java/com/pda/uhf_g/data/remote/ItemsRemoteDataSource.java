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

public class ItemsRemoteDataSource {
    private CompositeDisposable disposables = new CompositeDisposable();

    private List<TagData> catalog;

    public List<TagData> fetchItems() {
        catalog = getStubData();
        return catalog;
    }

    public void fetchItemsRemote() {
        disposables.add(
                Single.fromCallable(() -> TestService.main("").execute())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if (response.isSuccessful()) {
                                // Fetch and print a list of the contributors to the library.
                                List<TestService.Contributor> contributors = response.body();
                                assert contributors != null;
                                for (TestService.Contributor contributor : contributors) {
                                    Log.d("TestService", contributor.login + " (" + contributor.contributions + ")");
                                }
                                //YourDataModel data = response.body();

                            } else {
                                // Handle error
                            }
                        }, error -> {
                            // Handle network or other exceptions
                        })
        );
        disposables.clear(); // Dispose of subscriptions to prevent leaks
    }

    public List<TagData> getStubData(){
        List<TagData> tagDataList = new ArrayList<>();
        tagDataList.add(new TagData("1", "AF1", "E280699520005011FB53EC59", "Cyanzeo", "Tarjeta blanca"));
        tagDataList.add(new TagData("2", "AF2", "E28068942000501F1286CC5A", "Tag 2", "Desconocido 2"));
        tagDataList.add(new TagData("3", "AF3", "E28068942000402B4436B033", "Tag 3", "Desconocido 3"));
        tagDataList.add(new TagData("4", "AF4", "E28068942000402B4436B034","Tag 4", "Desconocido 4"));
        tagDataList.add(new TagData("5", "AF5", "E28068942000402B4436B035","Tag 5", "Desconocido 5"));
        return tagDataList;
    }
}
