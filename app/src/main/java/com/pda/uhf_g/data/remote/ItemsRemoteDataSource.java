package com.pda.uhf_g.data.remote;

import android.util.Log;

import com.pda.uhf_g.entity.TagInfo;
import com.pda.uhf_g.service.TestService;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ItemsRemoteDataSource {
    private CompositeDisposable disposables = new CompositeDisposable();

    public List<TagInfo> fetchItems() {
        return null;
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
}
