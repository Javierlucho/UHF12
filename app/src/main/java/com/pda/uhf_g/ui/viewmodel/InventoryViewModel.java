package com.pda.uhf_g.ui.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;

import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.entities.ItemEntity;
import com.pda.uhf_g.data.local.entities.ListItem;
import com.pda.uhf_g.data.local.entities.Location;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.data.gps.GPSInfo;
import com.pda.uhf_g.data.local.entities.TagInfo;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;
import com.pda.uhf_g.data.repository.ItemsRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class InventoryViewModel extends AndroidViewModel {
    private final MutableLiveData<GPSInfo> currentLocation = new MutableLiveData<>();
    private final MutableLiveData<PosicionamientoEntity> currentTag = new MutableLiveData<>();

    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();

    private final MutableLiveData<ListItem> selectedItem = new MutableLiveData<>();

    // Sync Fragment
    private final MutableLiveData<Boolean> downloadedPonds = new MutableLiveData<>();
    private final MutableLiveData<Boolean> downloadedCatalog = new MutableLiveData<>();
    private final MutableLiveData<Boolean> downloadedItemsIPSP = new MutableLiveData<>();
    private final MutableLiveData<Boolean> downloadedPosicionamiento = new MutableLiveData<>();

    // Location Fragment
    private final MutableLiveData<List<PondsDao.MegaZoneList>> megazones = new MutableLiveData<>();
    private final MutableLiveData<List<PondsDao.ZoneList>> zones = new MutableLiveData<>();
    private final MutableLiveData<List<PondsDao.SectorList>> sectors = new MutableLiveData<>();
    private final MutableLiveData<List<PondsDao.PondsList>> ponds = new MutableLiveData<>();

    List<ListItem> items = new ArrayList<>();

    private ItemsRepository itemsRepository;
    //private List<TagInfo> tagInfoList;
    public InventoryViewModel(@NonNull Application application) {
        super(application);

        // Create Data handlers
        ItemsLocalDataSource local = new ItemsLocalDataSource(application.getApplicationContext());
        ItemsRemoteDataSource items = new ItemsRemoteDataSource();
        PondsRemoteDataSource ponds = new PondsRemoteDataSource();
        CatalogRemoteDataSource catalog = new CatalogRemoteDataSource();
        itemsRepository = new ItemsRepository(items, ponds, catalog, local);

        // Load Data from DB
        //fillItems();
        fillItemsFromDB();
    }

    public void setCurrentLocation(GPSInfo gpsInfo) {
        currentLocation.setValue(gpsInfo);
    }
    public LiveData<GPSInfo> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentTag(PosicionamientoEntity tag) {
        Log.d("tag", "Setting tag " + tag.getTid());
        currentTag.setValue(tag);
    }
    public LiveData<PosicionamientoEntity> getCurrentTag() {
        return currentTag;
    }

    public MutableLiveData<Boolean> getDownloadedPonds() {
        return downloadedPonds;
    }

    public void setDownloadedPonds(Boolean downloaded) {
        downloadedPonds.setValue(downloaded);
    }

    public MutableLiveData<Boolean> getDownloadedPosicionamiento() {
        return downloadedPosicionamiento;
    }

    public void setDownloadedPosicionamiento(Boolean downloaded) {
        downloadedPosicionamiento.setValue(downloaded);
    }

    public MutableLiveData<Boolean> getDownloadedCatalog() {
        return downloadedCatalog;
    }

    public void setDownloadedCatalog(Boolean downloaded) {
        downloadedCatalog.setValue(downloaded);
    }

    public MutableLiveData<Boolean> getDownloadedItemsIPSP() {
        return downloadedItemsIPSP;
    }

    public void setDownloadedItemsIPSP(Boolean downloaded) {
        downloadedItemsIPSP.setValue(downloaded);
    }

//    public void setTagInfoList(List<TagInfo> tagInfoList) {
//        this.tagInfoList = tagInfoList;
//    }

//    public void emptyTagInfoList() {
//        this.tagInfoList.clear();
//    }

    public void updateScanning(List<TagInfo> tagInfoList) {
        //setTagInfoList();
        updateLatestTag(tagInfoList);
    }
    @SuppressLint("CheckResult")
    public void updateLatestTag(List<TagInfo> tagInfoList) {
        if (tagInfoList != null && !tagInfoList.isEmpty()) {
            itemsRepository.findPosicionamientoItemFromList(tagInfoList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setCurrentTag, error -> {
                    for (TagInfo tagInfo: tagInfoList) {
                        Log.d("tag", "Finding failed " + tagInfo.getTid());
                        setCurrentTag(new PosicionamientoEntity("0", "0", tagInfo.getTid()));
                    }
                    Log.d("tag", error.getMessage());
                });
        }
    }

    @SuppressLint("CheckResult")
    public void getMegazonesFromDB() {
        itemsRepository.getPondsMegazones()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(db_result -> {
                    Log.d("db", "Obtained megazones from db");
                    setMegazones(db_result);
                },
                error -> {
                    Log.d("db", error.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    public void getZonesFromDB(String megazone_id) {
        itemsRepository.getPondsZones(megazone_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(db_result -> {
                    Log.d("db", "Obtained megazones from db");
                    setZones(db_result);
                },
                error -> {
                    Log.d("db", error.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    public void getSectorsFromDB(String zone_id) {
        itemsRepository.getPondsSectors(zone_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(db_result -> {
                    Log.d("db", "Obtained megazones from db");
                    setSectors(db_result);
                },
                error -> {
                    Log.d("db", error.getMessage());
                });
    }

    @SuppressLint("CheckResult")
    public void getPondsFromDB(String sector_id) {
        itemsRepository.getPonds(sector_id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(db_result -> {
                    Log.d("db", "Obtained megazones from db");
                    setPonds(db_result);
                },
                error -> {
                    Log.d("db", error.getMessage());
                });
    }

    public MutableLiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public void setSelectedLocation(String megazona, String zona, String sector, String piscina_id, String piscina) {
        Location location = new Location(megazona, zona, sector, piscina_id, piscina);
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

    public void fillItemsFromDB() {
        itemsRepository.getItemsIPSP()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(itemsFromDB -> {
                    int position = 0;
                    for (ItemEntity item : itemsFromDB) {
                        items.add(new ListItem(String.valueOf(item.getCid()), item.getDescripcion(), item.getMarca(), item.getSerie(), position));
                        position++;
                    }
                },
                error -> {
                    // Handle insertion error
                    Log.d("db", error.getMessage());
                });
    }
    private void fillItems(){
        items.add(new ListItem( "Item 2", "","", "",1));
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void pushToServer() {
        Log.d("db", "Pushing to server");
        itemsRepository.publishPosicionamientos().observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                // Handle successful insertion (e.g., update UI)
                if(response.isSuccessful()){
                    Log.d("remote", "Downloaded pool data" );
                    resetDatabase();
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
    }

    @SuppressLint("CheckResult")
    public void pullData(){
        // Pull Items Locations and Tag
        String zone = "CALIFORNIAA";

        // Pull Posicionamiento Data from mini PC SIEMAV API
        itemsRepository
            .getPosicionamientoItems()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> {
                // Handle successful insertion (e.g., update UI)
                setDownloadedPosicionamiento(true);
            },
            error -> {
                // Handle insertion error
                Log.d("remote", "Downloading Posicionamiento failed ");
                Log.d("remote", error.getMessage());
                setDownloadedPosicionamiento(false);
            });

        // Pull Ponds Data
        itemsRepository.getPondsByZone(zone)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                // Handle successful insertion (e.g., update UI)
                if(response.isSuccessful()){
                    Log.d("remote", "Downloading Ponds data" );
                    Log.d("remote", response.raw().request().toString());
                    PondsRemoteDataSource.PondsResponse responseBody = response.body();
//                  Log.d( "remote", "Items: " + responseBody.payload.items.get(1).meta_data.get("Id_Sector") );

                    // Save downloaded data to database
                    List<PondsRemoteDataSource.PondData> items = responseBody.payload.items;
                    Log.d( "remote", "Items: " + items.size() );
                    itemsRepository
                        .savePondsToDB(items)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(done -> {
                            Log.d("remote", "Downloaded Ponds data");
                            // Visual indicator for the user
                            setDownloadedPonds(true);
                        }, error -> {
                            Log.d("remote", "Downloading Ponds error" );
                            Log.d("remote", response.message());
                        });

                } else {
                    Log.d("remote", "Downloading Ponds error" );
                    Log.d("remote", response.message());
                }
            },
            error -> {
                // Handle insertion error
                Log.d("remote", "Downloading Ponds failed ");
                Log.d("remote", error.getMessage());
            });

        // Pull IPSP Inventory Catalog
        // API doesn't allow to search by zone or sector
        itemsRepository.getCatalogoItems()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                Log.d("remote", response.raw().request().toString());
                // Handle successful insertion (e.g., update UI)
                if(response.isSuccessful()){
                    Log.d("remote", "Downloading Categorias data" );
                    CatalogRemoteDataSource.CatalogoResponse responseBody = response.body();

                    // Save downloaded data to database
                    itemsRepository
                        .saveCategoriaToDB(responseBody.items)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(done -> {
                           if (done) {
                               Log.d("remote", "Downloaded Categorias data");
                               // Visual indicator for the user
                               setDownloadedCatalog(true);
                           }
                        });
                } else {
                    Log.d("remote", "Downloading Categorias error" );
                    Log.d("remote", response.message());
                }
            },
            error -> {
                // Handle insertion error
                Log.d("remote", "Downloading Categorias failed ");
                Log.d("remote", error.getMessage());
            });

        // Pull IPSP Items
        itemsRepository.getItemsIPSPRemote(zone)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                // Handle successful insertion (e.g., update UI)
                if(response.isSuccessful()){
                    Log.d("remote", "Downloading Items IPSP data" );
                    CatalogRemoteDataSource.ItemsResponse responseBody = response.body();

                    // Save downloaded data to database
                    itemsRepository
                            .saveItemsIPSPToDB(responseBody.items)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(done -> {
                                if (done) {
                                    Log.d("remote", "Downloaded Categorias data");
                                    // Visual indicator for the user
                                    setDownloadedItemsIPSP(true);
                                }
                            });

                } else {
                    Log.d("remote", "Downloading Items IPSP error" );
                    Log.d("remote", response.message());
                }
            },
            error -> {
                // Handle insertion error
                Log.d("remote", "Downloading Items IPSP failed ");
                Log.d("remote", error.getMessage());
            });
    }

    public void saveToDatabase(){
        PosicionamientoEntity updatedData = currentTag.getValue();
        // Save new data added to PosicionamientoEntity
        itemsRepository
                .savePosicionamientoToDB(updatedData)
                .subscribe(item -> {
        }, error -> {
            // Handle insertion error
            Log.d("db", "Save failed" + updatedData.getTid());
        });
    }

    public void resetDatabase(){
        // Save new data added to PosicionamientoEntity
        itemsRepository
            .resetDb()
            .subscribe( () -> {

            }, error -> {
                // Handle insertion error
                Log.d("db", "Delete DB failed" );
            });
    }

    public void setMegazones(List<PondsDao.MegaZoneList> megazonesFromDB) {
        megazones.setValue(megazonesFromDB);
    }

    public MutableLiveData<List<PondsDao.MegaZoneList>> getMegazones() {
        return megazones;
    }

    public void setZones(List<PondsDao.ZoneList> zonesFromDB) {
        zones.setValue(zonesFromDB);
    }
    public MutableLiveData<List<PondsDao.ZoneList>> getZones() {
        return zones;
    }

    public void setSectors(List<PondsDao.SectorList> sectorsFromDB) {
        sectors.setValue(sectorsFromDB);
    }
    public MutableLiveData<List<PondsDao.SectorList>> getSectors() {
        return sectors;
    }
    public void setPonds(List<PondsDao.PondsList> pondsFromDB) {
        ponds.setValue(pondsFromDB);
    }
    public MutableLiveData<List<PondsDao.PondsList>> getPonds() {
        return ponds;
    }

}

