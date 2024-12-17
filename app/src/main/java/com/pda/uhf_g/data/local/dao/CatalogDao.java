package com.pda.uhf_g.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.pda.uhf_g.data.local.entities.CategoriaEntity;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface CatalogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllCatalogItems(List<CategoriaEntity> items);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCatalogItem(CategoriaEntity item);
}