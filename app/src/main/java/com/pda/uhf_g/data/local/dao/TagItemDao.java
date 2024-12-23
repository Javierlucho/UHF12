package com.pda.uhf_g.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface TagItemDao {

    @Query("SELECT * FROM posicionamiento")
    List<PosicionamientoEntity> getAllItems();

    @Query("SELECT * FROM posicionamiento WHERE tid = :tid")
    Single<PosicionamientoEntity> getItemByTid(String tid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<PosicionamientoEntity> inventario);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertItem(PosicionamientoEntity item);

    @Update
    Completable updateItem(PosicionamientoEntity item);


}