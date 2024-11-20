package com.pda.uhf_g.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.data.gps.GPSInfo;
import com.pda.uhf_g.data.local.entities.TagData;
import com.pda.uhf_g.data.local.entities.TagInfo;
import com.pda.uhf_g.data.repository.ItemsRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class InventoryViewModel extends AndroidViewModel {
    private final MutableLiveData<GPSInfo> currentLocation = new MutableLiveData<GPSInfo>();
    private final MutableLiveData<TagData> currentTag = new MutableLiveData<TagData>();

    private ItemsRepository itemsRepository;
    private List<TagInfo> tagInfoList;
    public InventoryViewModel(@NonNull Application application) {
        super(application);

        ItemsLocalDataSource local = new ItemsLocalDataSource(application.getApplicationContext());
        ItemsRemoteDataSource remote = new ItemsRemoteDataSource();
        itemsRepository = new ItemsRepository(remote, local);
    }

    public void setCurrentLocation(GPSInfo gpsInfo) {
        currentLocation.setValue(gpsInfo);
    }
    public LiveData<GPSInfo> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentTag(TagData tag) {
        currentTag.setValue(tag);
    }
    public LiveData<TagData> getCurrentTag() {
        return currentTag;
    }

    public void updateScanning(List<TagInfo> tagInfoList) {
        setTagInfoList(tagInfoList);
        updateLatestTag();
    }

    public void setTagInfoList(List<TagInfo> tagInfoList) {
        this.tagInfoList = tagInfoList;
    }

    @SuppressLint("CheckResult")
    public TagData updateLatestTag() {
        if (tagInfoList != null && !tagInfoList.isEmpty()) {
            // TODO: Pop
            String tid = tagInfoList.get(0).getTid();
            itemsRepository.findItem(tid)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> {
                        // Handle successful insertion (e.g., update UI)
                        Log.d("db", "Found item " + tid);
                        setCurrentTag(item);
                        itemsRepository.saveToDatabase(getCurrentTag());
                    },
                    error -> {
                        // Handle insertion error
                        Log.d("db", "Finding failed " + tid);
                        Log.d("db", error.getMessage());
                    });
        }
        return null;
    }



}
