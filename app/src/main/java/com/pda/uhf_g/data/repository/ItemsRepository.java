package com.pda.uhf_g.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.entities.TagItemEntity;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.data.local.entities.TagData;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ItemsRepository {
    private final ItemsRemoteDataSource itemsRemoteDataSource; // network
    private final PondsRemoteDataSource pondsRemoteDataSource; // network
    private final CatalogRemoteDataSource catalogRemoteDataSource; // network
    private final ItemsLocalDataSource itemsLocalDataSource; // database


    // Add a constructor
    public ItemsRepository(ItemsRemoteDataSource itemsRemoteDataSource,
                           PondsRemoteDataSource pondsRemoteDataSource,
                           CatalogRemoteDataSource catalogRemoteDataSource,
                           ItemsLocalDataSource itemsLocalDataSource) {
        this.itemsRemoteDataSource = itemsRemoteDataSource;
        this.pondsRemoteDataSource = pondsRemoteDataSource;
        this.catalogRemoteDataSource = catalogRemoteDataSource;
        this.itemsLocalDataSource = itemsLocalDataSource;
        syncWithServer();
    }

     public Observable<List<TagData>> getAllItems(){
         // Perform database insertion on a background thread
         return Observable.fromCallable(itemsLocalDataSource::getAllItems)
                 .subscribeOn(Schedulers.io()); // Specify the background scheduler

    }

    public Observable<TagData> findItem(String tid) {
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    return itemsLocalDataSource.findItemByTid(tid);
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public Observable<TagItemEntity> insertItem(final TagData item) {
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    return itemsLocalDataSource.insertItem(item);
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public Completable insertItemSighting(final TagData item) {
        return Completable.fromAction(() -> {
                    // Perform database insertion on a background thread
                    itemsLocalDataSource.insertItemSighting(item);
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    void syncWithServer(){
        try {
            List<TagData> remoteItems = itemsRemoteDataSource.fetchItems();
            // ... logic to compare and synchronize local and remote data
            // (e.g., update local database with new/modified items,
            // upload new local items to server)
            Log.d("db","Syncronizing with server");
            for (TagData localItem : remoteItems) {
                insertItem(localItem)
                    .subscribe(item -> {
                            Log.d("db","Save success " + item.getTid());
                        }, error -> {
                            Log.d("db","Save failed "  + localItem.getTid());
                    });
            }
        } catch (Exception e) {
            // Handle network or other errors
            Log.d("db","Save failed "  + e);
        }
    }


    // Serialize to JSON
    // Gson gson = new Gson();
    // String jsonString = gson.toJson(myDataObject);

    // Deserialize from JSON
    // MyDataObject myDataObject = gson.fromJson(jsonString, MyDataObject.class);

    public void saveToDatabase(LiveData<TagData> currentTag){
        // Get read tag data
        TagData readTag = Objects.requireNonNull(currentTag.getValue());

        // Find item with TID
        findItem(readTag.getTid()).subscribe(item -> {
            // Handle successful insertion (e.g., update UI)
            Log.d("db", "Found successfully " + readTag.getTid());
            // If the item doesn't exist do nothing
            // Else save it as a record in ItemSighting
            if (item == null) {
                // Handle the case where the item is not found
                Log.d("db", "Item not found in DB: "  + readTag.getTid());
            } else {
                // Use TID to save item sighting
                insertItemSighting(item)
                        .subscribe(() -> {
                            Log.d("db", "Save success " + item.getTid());
                        }, error -> {
                            Log.d("db", "Save failed " + readTag.getTid());
                        });
            }
        }, error -> {
            // Handle insertion error
            Log.d("db", "Save failed" + readTag.getTid());
        });
    }

//    public void publishInventory() {
//        itemsRemoteDataSource.publishInventory();
//    }

    public @NonNull Observable<Response<ResponseBody>> getFilterPools(String megazone, String zone, String sector, String level){
        // Perform database insertion on a background thread
        return Observable.fromCallable( () -> pondsRemoteDataSource.getFilters(megazone, zone, sector, level).execute())
                .subscribeOn(Schedulers.io()); // Specify the background scheduler

    }

    public @NonNull Observable<Response<PondsRemoteDataSource.PondsResponse>> getPoolsByZone(String zoneId){
        String match_key = "meta_data.Id_Zona";
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    Log.d("remote", "getPoolsByZone:" + pondsRemoteDataSource.getPonds(match_key, zoneId).toString());
                    return pondsRemoteDataSource.getPonds(match_key, zoneId).execute();
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public @NonNull Observable<Response<CatalogRemoteDataSource.CatalogoResponse>> getCatalogoItems(){
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    Log.d("remote", "getPoolsByZone:" + catalogRemoteDataSource.getCatalogoItems().toString());
                    return catalogRemoteDataSource.getCatalogoItems().execute();
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public @NonNull Observable<Response<CatalogRemoteDataSource.ItemsResponse>> getItemsIPSP(){
        String sector = "SANDIEGO";
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    Log.d("remote", "getPoolsByZone:" + catalogRemoteDataSource.getItems(sector).toString());
                    return catalogRemoteDataSource.getItems(sector).execute();
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public void savePondsToDB(List<PondsRemoteDataSource.PondData> items) {
        itemsLocalDataSource.insertPonds(items);
    }

    public void saveCatalogToDB(List<CatalogRemoteDataSource.CatalogoResponse.Item> items) {
        itemsLocalDataSource.insertCatalogItems(items);
    }

    public void saveItemsIPSPToDB(List<CatalogRemoteDataSource.ItemsResponse.Item> items) {
        itemsLocalDataSource.insertItemsIPSP(items);
    }

    public io.reactivex.Observable<List<PondsDao.MegaZoneList>> getPondsMegazones() {
        return itemsLocalDataSource.getPondsMegazones().subscribeOn(io.reactivex.schedulers.Schedulers.io()); // Specify the background scheduler
    }
}
