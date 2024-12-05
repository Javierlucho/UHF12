package com.pda.uhf_g.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;
import com.pda.uhf_g.R;
import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.local.entities.ListItem;
import com.pda.uhf_g.data.local.entities.Location;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.data.gps.GPSInfo;
import com.pda.uhf_g.data.local.entities.TagData;
import com.pda.uhf_g.data.local.entities.TagInfo;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;
import com.pda.uhf_g.data.repository.ItemsRepository;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class InventoryViewModel extends AndroidViewModel {
    private final MutableLiveData<GPSInfo> currentLocation = new MutableLiveData<GPSInfo>();
    private final MutableLiveData<TagData> currentTag = new MutableLiveData<TagData>();

    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<Location>();

    private final MutableLiveData<ListItem> selectedItem = new MutableLiveData<ListItem>();

    private final MutableLiveData<Boolean> downloadedPools = new MutableLiveData<Boolean>();


    List<ListItem> items = new ArrayList<>();

    private ItemsRepository itemsRepository;
    private List<TagInfo> tagInfoList;
    public InventoryViewModel(@NonNull Application application) {
        super(application);

        fillItems();

        ItemsLocalDataSource local = new ItemsLocalDataSource(application.getApplicationContext());
        ItemsRemoteDataSource items = new ItemsRemoteDataSource();
        PondsRemoteDataSource ponds = new PondsRemoteDataSource();
        CatalogRemoteDataSource catalog = new CatalogRemoteDataSource();
        itemsRepository = new ItemsRepository(items, ponds, catalog, local);
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

    public MutableLiveData<Boolean> getDownloadedPools() {
        return downloadedPools;
    }

    public void setDownloadedPools(Boolean downloaded) {
        downloadedPools.setValue(downloaded);
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

    public MutableLiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String megazona, String zona, String sector, String piscina) {
        Location location = new Location(megazona, zona, sector, piscina);
        selectedLocation.setValue(location);
    }
    public void setSelectedItem(ListItem item) {
        selectedItem.setValue(item);
    }

    public MutableLiveData<ListItem> getSelectedItem() {
        return selectedItem;
    }

    public void deselectItem() {
        selectedItem.setValue(null);
    }

    private void fillItems(){
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 1", 0));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 2", 1));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 3", 2));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 4", 3));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 5", 4));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 6", 5));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 7", 6));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 8", 7));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 9", 8));
        items.add(new ListItem(R.drawable.button_disenabled_background, "Item 10", 9));
    }

    public List<ListItem> getItems() {
        return items;
    }


    public void pushToServer() {
        Log.d("db", "Pushing to server");
        //itemsRepository.publishInventory();
    }

    @SuppressLint("CheckResult")
    public void pullData(){
        // Pull Items Locations and Tag
        String megazone = "CALIFORNIA";
        String zone = "CALIFORNIAA";
        String zoneId = "Z005";

        // Pull Pools Data
        itemsRepository.getPoolsByZone(zoneId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                    // Handle successful insertion (e.g., update UI)
                    if(response.isSuccessful()){
                        Log.d("remote", "Downloaded pool data" );
                        PondsRemoteDataSource.PondsResponse responseBody = response.body();
                        Log.d( "remote", "Items: " + responseBody.payload.items.get(1).meta_data.get("Id_Sector") );

                        // Save downloaded data to database

                        // Visual indicator for the user
                        setDownloadedPools(true);
                    } else {
                        Log.d("remote", "Downloading error" );
                        Log.d("remote", response.message());
                    }
                },
                error -> {
                    // Handle insertion error
                    Log.d("remote", "Downloading failed ");
                    Log.d("remote", error.getMessage());
                });


        // Pull IPSP Inventory Catalog
    }


}
