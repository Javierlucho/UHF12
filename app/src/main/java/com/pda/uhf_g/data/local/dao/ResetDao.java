package com.pda.uhf_g.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pda.uhf_g.data.local.entities.CategoriaEntity;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface ResetDao {

    // https://medium.com/@sdevpremthakur/how-to-reset-room-db-completely-including-primary-keys-android-6382f00df87b
    // To let the new primary key starts from 0.
    //@Query("DELETE FROM sqlite_sequence WHERE name = 'ponds'")
//    Completable clearPrimaryKeyIndex();
}