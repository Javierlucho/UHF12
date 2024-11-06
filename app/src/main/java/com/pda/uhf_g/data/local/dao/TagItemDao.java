package com.pda.uhf_g.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pda.uhf_g.data.local.entities.TagItemEntity;

import java.util.List;

@Dao
public interface TagItemDao {
    @Query("SELECT * FROM tag_items")
    List<TagItemEntity> getAllItems();

    @Query("SELECT * FROM tag_items WHERE tid = :tid")
    TagItemEntity getItemByEpc(String tid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(TagItemEntity item);

    @Update
    void updateItem(TagItemEntity item);

    @Delete
    void deleteItem(TagItemEntity item);
}