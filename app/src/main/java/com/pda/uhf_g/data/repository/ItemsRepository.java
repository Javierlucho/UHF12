package com.pda.uhf_g.data.repository;

import android.util.Log;

import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class ItemsRepository {
    private final ItemsRemoteDataSource itemsRemoteDataSource;     // network
    private final PondsRemoteDataSource pondsRemoteDataSource;     // network
    private final CatalogRemoteDataSource catalogRemoteDataSource; // network
    private final ItemsLocalDataSource itemsLocalDataSource;       // database

    // Add a constructor
    public ItemsRepository(ItemsRemoteDataSource itemsRemoteDataSource,
                           PondsRemoteDataSource pondsRemoteDataSource,
                           CatalogRemoteDataSource catalogRemoteDataSource,
                           ItemsLocalDataSource itemsLocalDataSource) {
        this.itemsRemoteDataSource = itemsRemoteDataSource;
        this.pondsRemoteDataSource = pondsRemoteDataSource;
        this.catalogRemoteDataSource = catalogRemoteDataSource;
        this.itemsLocalDataSource = itemsLocalDataSource;
    }

    // ------------------------------- POSICIONAMIENTO ------------------------------------------//
    public Observable<List<PosicionamientoEntity>> getAllItems(){
        // Perform database insertion on a background thread
        return Observable.fromCallable(itemsLocalDataSource::getAllItems)
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }
    public Observable<PosicionamientoEntity> findPosicionamientoItem(String tid) {
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    return itemsLocalDataSource.findItemByTid(tid);
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public  Observable<PosicionamientoEntity> savePosicionamientoToDB(PosicionamientoEntity updatedData) {
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    itemsLocalDataSource.insertNewPosicionamiento(updatedData);
                    return updatedData;
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }


    @NonNull
    public Completable getPosicionamientoItems(){
        try {
            // TODO: Use itemsRemoteDataSource.downloadItems()
            List<PosicionamientoEntity> remoteItems = itemsRemoteDataSource.fetchItems();
            // ... logic to compare and synchronize local and remote data
            // (e.g., update local database with new/modified items,
            // upload new local items to server)
            Log.d("db","Syncronizing with server");

            return Completable.fromCallable(() -> {
                        for (PosicionamientoEntity localItem : remoteItems) {
                            itemsLocalDataSource.insertNewPosicionamiento(localItem);
                        }
                        return true;
                    })
                    .subscribeOn(Schedulers.io());

        } catch (Exception e) {
            // Handle network or other errors
            Log.d("db","Save failed "  + e);
        }
        return null;
    }

    public @NonNull Observable<Response<ItemsRemoteDataSource.PosicionamientoRequest>> publishPosicionamientos(){
        return Observable.fromCallable(() -> {
                List<PosicionamientoEntity> items = itemsLocalDataSource.getAllItems();
                return itemsRemoteDataSource.upload(items).execute();
            })
            .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    // ------------------------------- POSICIONAMIENTO END ---------------------------------------//

    // ------------------------------------ PONDS -----------------------------------------------//
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

    public io.reactivex.Observable<List<PondsDao.MegaZoneList>> getPondsMegazones() {
        return itemsLocalDataSource.getPondsMegazones().subscribeOn(io.reactivex.schedulers.Schedulers.io()); // Specify the background scheduler
    }
    public void savePondsToDB(List<PondsRemoteDataSource.PondData> items) {
        itemsLocalDataSource.insertPonds(items);
    }
    // ------------------------------------ PONDS END -------------------------------------------//

    // ------------------------------------ ITEMS -----------------------------------------------//

    public void saveItemsIPSPToDB(List<CatalogRemoteDataSource.ItemsResponse.Item> items) {
        itemsLocalDataSource.insertItemsIPSP(items);
    }


    public @NonNull Observable<Response<CatalogRemoteDataSource.ItemsResponse>> getItemsIPSP(String zone){
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    return catalogRemoteDataSource.getItems(zone).execute();
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }
    // ------------------------------------ ITEMS END --------------------------------------------//


    // ------------------------------------ CATEGORIA --------------------------------------------//
    public void saveCategoriaToDB(List<CatalogRemoteDataSource.CatalogoResponse.Item> items) {
        itemsLocalDataSource.insertCategories(items);
    }
    public @NonNull Observable<Response<CatalogRemoteDataSource.CatalogoResponse>> getCatalogoItems(){
        return Observable.fromCallable(() -> {
                // Perform database insertion on a background thread
                return catalogRemoteDataSource.getCatalogoItems().execute();
            })
            .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }


    // -------------------------------- CATEGORIA END --------------------------------------------//


    // ------------------------------------ MULTI --------------------------------------------//


    public @NonNull io.reactivex.Completable resetDb(){
        return itemsLocalDataSource.resetDb().subscribeOn(io.reactivex.schedulers.Schedulers.io()); // Specify the background scheduler
    }

    // Serialize to JSON
    // Gson gson = new Gson();
    // String jsonString = gson.toJson(myDataObject);

    // Deserialize from JSON
    // MyDataObject myDataObject = gson.fromJson(jsonString, MyDataObject.class);



// ------------------------------------ MULTI END --------------------------------------------//

}
