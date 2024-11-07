package com.pda.uhf_g.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.entity.GPSInfo;
import com.pda.uhf_g.entity.TagInfo;
import com.pda.uhf_g.data.repository.ItemsRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class InventoryViewModel extends AndroidViewModel {
    private final MutableLiveData<GPSInfo> currentLocation = new MutableLiveData<GPSInfo>();
    private final MutableLiveData<TagInfo> currentTag = new MutableLiveData<TagInfo>();

    private ItemsRepository itemsRepository;

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

    public void setCurrentTag(TagInfo tag) {
        currentTag.setValue(tag);
    }
    public LiveData<TagInfo> getCurrentTag() {
        return currentTag;
    }

    public void saveToDatabase(LiveData<TagInfo> currentTag){
        itemsRepository.insertItem(currentTag.getValue())
            // Switch to main thread for UI updates (if needed)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> {
                // Handle successful insertion (e.g., update UI)
                Log.d("db","Save success");
            }, error -> {
                // Handle insertion error
                Log.d("db","Save failed");
            });
    }

    public void setTagInfoList(List<TagInfo> tagInfoList) {
        itemsRepository.setTagInfoList(tagInfoList);
        setCurrentTag(itemsRepository.getLatestTag());
        saveToDatabase(getCurrentTag());
    }

}
