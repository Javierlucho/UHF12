package com.pda.uhf_g.data.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.pda.uhf_g.data.local.ItemsLocalDataSource;
import com.pda.uhf_g.data.local.entities.TagItemEntity;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.entity.TagData;
import com.pda.uhf_g.entity.TagInfo;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ItemsRepository {
    private final ItemsRemoteDataSource itemsRemoteDataSource; // network
    private final ItemsLocalDataSource itemsLocalDataSource; // database


    // Add a constructor
    public ItemsRepository(ItemsRemoteDataSource itemsRemoteDataSource, ItemsLocalDataSource itemsLocalDataSource) {
        this.itemsRemoteDataSource = itemsRemoteDataSource;
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
}
