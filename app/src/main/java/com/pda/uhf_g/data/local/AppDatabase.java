package com.pda.uhf_g.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.dao.TagItemDao;
import com.pda.uhf_g.data.local.entities.CatalogoEntity;
import com.pda.uhf_g.data.local.entities.ItemSightingEntity;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.local.entities.TagItemEntity;

@Database(
        entities = {
            TagItemEntity.class,       // To delete
            ItemSightingEntity.class,  // To delete
            PosicionamientoEntity.class,
            CatalogoEntity.class,
            PondEntity.class,
        },
        version = 5
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TagItemDao TagItemDao();
    public abstract PondsDao PondsDao();

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