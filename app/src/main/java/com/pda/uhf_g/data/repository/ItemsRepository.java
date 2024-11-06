package com.pda.uhf_g.data.repository;

import com.pda.uhf_g.data.local.AppDatabase;
import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.entity.TagData;
import com.pda.uhf_g.entity.TagInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ItemsRepository {
    private ItemsRemoteDataSource itemsRemoteDataSource; // network
    private ItemsLocalDataSource itemsLocalDataSource; // database

    // Add a constructor
    public ItemsRepository(ItemsRemoteDataSource itemsRemoteDataSource, ItemsLocalDataSource itemsLocalDataSource) {
        this.itemsRemoteDataSource = itemsRemoteDataSource;
        this.itemsLocalDataSource = itemsLocalDataSource;
    }

    List<TagData> getAllItems(){
        return itemsLocalDataSource.getAllItems();
    }
    TagInfo getItemByTid(String epc){
        return itemsLocalDataSource.getItemByTid(epc);
    }

    public Completable insertItem(final TagInfo item) {
        return Completable.fromAction(() -> {
                    // Perform database insertion on a background thread
                    itemsLocalDataSource.insertItem(item);
                })
                .subscribeOn(Schedulers.io()); // Specify the background scheduler
    }

    void updateItem(TagInfo item){
        itemsLocalDataSource.updateItem(item);
    }

    void deleteItem(TagInfo item){
        itemsLocalDataSource.deleteItem(item);
    }


    void syncWithServer(){
        try {
            List<TagData> localItems = itemsLocalDataSource.getAllItems();
            List<TagInfo> remoteItems = itemsRemoteDataSource.fetchItems();
            // ... logic to compare and synchronize local and remote data
            // (e.g., update local database with new/modified items,
            // upload new local items to server)
        } catch (Exception e) {
            // Handle network or other errors
        }
    }
}
