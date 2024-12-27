package com.pda.uhf_g.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pda.uhf_g.data.local.entities.CategoriaEntity;
import com.pda.uhf_g.data.local.entities.ItemEntity;
import com.pda.uhf_g.data.local.entities.TagItemEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface ItemsDao {

    @Query("SELECT * FROM items")
    List<ItemEntity> getAllItemsIPSP();

    @Query("SELECT * FROM items WHERE cid = :cid")
    ItemEntity getItemByTid(String cid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllItems(List<ItemEntity> items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(ItemEntity items);

    @Query("SELECT * FROM items WHERE cid = :id")
    Observable<ItemEntity> getItemByID(String id);

    @Query("SELECT EXISTS (SELECT 1 FROM items)")
    Single<Boolean> hasData();

}