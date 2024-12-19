package com.pda.uhf_g.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.local.entities.TagItemEntity;

import java.util.List;

@Dao
public interface TagItemDao {

    @Query("SELECT * FROM posicionamiento")
    List<PosicionamientoEntity> getAllItems();

    @Query("SELECT * FROM posicionamiento WHERE tid = :tid")
    PosicionamientoEntity getItemByTid(String tid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(PosicionamientoEntity item);

    @Update
    void updateItem(PosicionamientoEntity item);

    @Delete
    void deleteItem(PosicionamientoEntity item);


}