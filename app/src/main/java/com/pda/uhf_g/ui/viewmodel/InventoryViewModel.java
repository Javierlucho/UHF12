package com.pda.uhf_g.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

public class InventoryViewModel extends AndroidViewModel {
    private final MutableLiveData<GPSInfo> currentLocation = new MutableLiveData<GPSInfo>();
    private final MutableLiveData<TagData> currentTag = new MutableLiveData<TagData>();

    private final MutableLiveData<Location> selected_location = new MutableLiveData<Location>();

    private final MutableLiveData<ListItem> selected_item = new MutableLiveData<ListItem>();

    //private final MutableLiveData<Location> selected_item = new MutableLiveData<Location>();

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
        return selected_location;
    }

    public void setSelectedLocation(String megazona, String zona, String sector, String piscina) {
        Location location = new Location(megazona, zona, sector, piscina);
        selected_location.setValue(location);
    }
    public void setSelectedItem(ListItem item) {
        selected_item.setValue(item);
    }

    public MutableLiveData<ListItem> getSelectedItem() {
        return selected_item;
    }

    public void deselectItem() {
        selected_item.setValue(null);
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
        String megazone = "";
        String zone = "";
        String sector = "";
        String level = "megazone";

        // Pull Pools Data
        //public void fillFilterPoolsDB();

        // Pull IPSP Inventory Catalog
    }
}
