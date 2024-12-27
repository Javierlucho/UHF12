package com.pda.uhf_g.data.repository;

import android.util.Log;

import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.dao.TagItemDao;
import com.pda.uhf_g.data.local.entities.CategoriaEntity;
import com.pda.uhf_g.data.local.entities.ItemEntity;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.local.entities.TagInfo;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
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

    public Single<PosicionamientoEntity> findPosicionamientoItemFromList(List<TagInfo> itemsToSearch) {
        return Observable.fromIterable(itemsToSearch)
                .concatMapSingle(tagFound -> itemsLocalDataSource.findItemByTid(tagFound.getTid()))
                .filter(item -> item != null)
                .firstOrError()
                .subscribeOn(Schedulers.io());

//                .onErrorResumeNext(throwable -> {
//                    // If no item was found, notify
//                    Log.d("tag", "No item found");
////                    return Completable.error(new Throwable("Item not found")).toObservable();
//                })
    }
//    public Observable<PosicionamientoEntity> findPosicionamientoItem(String tid) {
//        return Observable.fromCallable(() -> {
//                    // Perform database insertion on a background thread
//                    return itemsLocalDataSource.findItemByTid(tid);
//                })
//                .subscribeOn(Schedulers.io()); // Specify the background scheduler
//    }

    public  Completable savePosicionamientoToDB(PosicionamientoEntity updatedData) {
        return itemsLocalDataSource.updatePosicionamiento(updatedData).subscribeOn(Schedulers.io());
    }


    @NonNull
    public Completable getPosicionamientoItems(){
        return Observable.fromCallable(() -> {
                // Perform database insertion on a background thread
                Log.d("remote", "Downloading Posicionamiento data" );
                return itemsRemoteDataSource.downloadItems().execute();
            })
            .subscribeOn(Schedulers.io()) // Specify the background scheduler
            .flatMapCompletable(itemsLocalDataSource::insertDownloadedPosicionamientoData)
                .doOnError(throwable -> {
                    // Log the error
                    Log.e("remote", "Error during download and insert", throwable);
                })
                .onErrorResumeNext(throwable -> {
                    // Handle the error and return a Completable that completes
                    // or returns an error
                    if (throwable instanceof IOException) {
                        // Handle network related errors
                        Log.e("remote", "Network error occurred", throwable);
                        return Completable.error(new Throwable("Network error, please try again later"));
                    } else {
                        // Handle other errors
                        Log.e("remote", throwable.getMessage(), throwable);
                        return Completable.error(new Throwable("An unexpected error occurred"));
                    }
                });

    }


    public Observable<List<TagItemDao.PondWithLocation>> getItemsWithLocationsIDs(){
        return itemsLocalDataSource.getAllItemsWithLocationIDs();
    }

    public @NonNull Observable<Response<ItemsRemoteDataSource.PosicionamientoPost>> publishPosicionamientos(List<TagItemDao.PondWithLocation> items){
        return Observable.fromCallable(() -> {
                List<ItemsRemoteDataSource.InventarioItem> inventario = new ArrayList<>();
                for ( TagItemDao.PondWithLocation item : items ){
                    ItemsRemoteDataSource.InventarioItem ItemtoUpload = getInventarioItem(item);
                    inventario.add(ItemtoUpload);
                }
                return itemsRemoteDataSource.upload(inventario).execute();
            })
            .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    private static ItemsRemoteDataSource.InventarioItem getInventarioItem(TagItemDao.PondWithLocation item) {
        ItemsRemoteDataSource.Ubicacion ubicacion = new ItemsRemoteDataSource.Ubicacion(
                item.getMegazoneId(), item.getZoneId(), item.getSectorId(), item.getUuid());
        ItemsRemoteDataSource.GPS gps = new ItemsRemoteDataSource.GPS(item.getLatitude(), item.getLongitude());
        ItemsRemoteDataSource.InventarioItem ItemtoUpload = new ItemsRemoteDataSource.InventarioItem(
                item.getCid(),
                item.getAfid(),
                item.getCategoriaId(),
                "",
                item.getTid(),
                ubicacion,
                gps
        );
        return ItemtoUpload;
    }
    public Single<Boolean> posicionamientoApiIsLoaded(){
        return itemsLocalDataSource.hasPosicionamientosInDB().subscribeOn(Schedulers.io());
    }
    // ------------------------------- POSICIONAMIENTO END ---------------------------------------//

    // ------------------------------------ PONDS -----------------------------------------------//
    public @NonNull Observable<Response<ResponseBody>> getFilterPools(String megazone, String zone, String sector, String level){
        // Perform database insertion on a background thread
        return Observable.fromCallable( () -> pondsRemoteDataSource.getFilters(megazone, zone, sector, level).execute())
                .subscribeOn(Schedulers.io()); // Specify the background scheduler

    }

    public @NonNull Observable<Response<PondsRemoteDataSource.PondsResponse>> getPoolsByZoneId(String zoneId){
        String match_key = "meta_data.Id_Zona";
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    return pondsRemoteDataSource.getPonds(match_key, zoneId).execute();
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }
    public @NonNull Observable<Response<PondsRemoteDataSource.PondsResponse>> getPondsByZone(String zone){
        String match_key = "meta_data.Zona";
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    return pondsRemoteDataSource.getPonds(match_key, zone).execute();
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public Observable<List<PondsDao.MegaZoneList>> getPondsMegazones() {
        return Observable.fromCallable(itemsLocalDataSource::getPondsMegazones).subscribeOn(Schedulers.io()); // Specify the background scheduler
    }
    public Observable<List<PondsDao.ZoneList>> getPondsZones(String megazone_id) {
        return Observable.fromCallable( () -> {
            return itemsLocalDataSource.getPondsZones(megazone_id);
        }).subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public Observable<List<PondsDao.SectorList>> getPondsSectors(String zone_id) {
        return Observable.fromCallable( () -> {
            return itemsLocalDataSource.getPondsSectors(zone_id);
        }).subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public Observable<List<PondsDao.PondsList>> getPonds(String sector_id) {
        return Observable.fromCallable( () -> {
            return itemsLocalDataSource.getPonds(sector_id);
        }).subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public @NonNull Observable<Boolean> savePondsToDB(List<PondsRemoteDataSource.PondData> items) {
        return Observable.fromCallable(() -> {
            //return itemsLocalDataSource.insertPonds(items).subscribeOn(io.reactivex.schedulers.Schedulers.io()); // Specify the background scheduler
            itemsLocalDataSource.insertPonds(items);
            return true;
        }).subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public @NonNull Observable<PondsDao.PondsList> findLocationByPondID(String pondID) {
        return itemsLocalDataSource.getPondByID(pondID).subscribeOn(Schedulers.io());
    }

    public Single<Boolean> pondsApiIsLoaded(){
        return itemsLocalDataSource.hasPondsInDB().subscribeOn(Schedulers.io());
    }
    // ------------------------------------ PONDS END -------------------------------------------//

    // ------------------------------------ ITEMS -----------------------------------------------//

    public @NonNull Observable<Boolean> saveItemsIPSPToDB(List<CatalogRemoteDataSource.ItemsResponse.Item> items) {
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    itemsLocalDataSource.insertItemsIPSP(items);
                    return true;
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler

    }


    public @NonNull Observable<Response<CatalogRemoteDataSource.ItemsResponse>> getItemsIPSPRemote(String zone){
        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    return catalogRemoteDataSource.getItems(zone).execute();
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public @NonNull Observable<List<ItemEntity>> getItemsIPSP() {
        // Perform database insertion on a background thread
        return Observable.fromCallable(itemsLocalDataSource::getAllItemsIPSP)
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public @NonNull Observable<ItemEntity> getItemByID(String cid) {
        return itemsLocalDataSource.getItemByID(cid).subscribeOn(Schedulers.io());
    }
    public Single<Boolean> itemsApiIsLoaded(){
        return itemsLocalDataSource.hasItemsInDB().subscribeOn(Schedulers.io());
    }
    // ------------------------------------ ITEMS END --------------------------------------------//


    // ------------------------------------ CATEGORIA --------------------------------------------//
    public @NonNull Observable<Boolean> saveCategoriaToDB(List<CatalogRemoteDataSource.CatalogoResponse.Item> items) {

        return Observable.fromCallable(() -> {
                    // Perform database insertion on a background thread
                    itemsLocalDataSource.insertCategories(items);
                    return true;
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }
    public @NonNull Observable<Response<CatalogRemoteDataSource.CatalogoResponse>> getCatalogoItems(){
        return Observable.fromCallable(() -> {
                // Perform database insertion on a background thread
                return catalogRemoteDataSource.getCatalogoItems().execute();
            })
            .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    public @NonNull Observable<CategoriaEntity> findCategoryByID(String categoryID) {
        return itemsLocalDataSource.getCategoryByID(categoryID).subscribeOn(Schedulers.io());
    }

    public Single<Boolean> categoriesApiIsLoaded(){
        return itemsLocalDataSource.hasCategoriesInDB().subscribeOn(Schedulers.io());
    }
    // -------------------------------- CATEGORIA END --------------------------------------------//


    // ------------------------------------ MULTI --------------------------------------------//


    public @NonNull Completable resetDb(){
        return itemsLocalDataSource.resetDb().subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    // Serialize to JSON
    // Gson gson = new Gson();
    // String jsonString = gson.toJson(myDataObject);

    // Deserialize from JSON
    // MyDataObject myDataObject = gson.fromJson(jsonString, MyDataObject.class);



// ------------------------------------ MULTI END --------------------------------------------//

}
