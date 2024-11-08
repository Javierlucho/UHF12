package com.pda.uhf_g.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pda.uhf_g.data.local.dao.TagItemDao;
import com.pda.uhf_g.data.local.entities.ItemSightingEntity;
import com.pda.uhf_g.data.local.entities.TagItemEntity;

@Database(
        entities = {
            TagItemEntity.class,
            ItemSightingEntity.class
        },
        version = 3
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TagItemDao TagItemDao();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "rfid_database")
                    .build();
        }
        return instance;
    }

}