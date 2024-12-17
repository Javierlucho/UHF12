package com.pda.uhf_g.data.local;

import static com.pda.uhf_g.util.UtilSound.context;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pda.uhf_g.data.local.dao.CatalogDao;
import com.pda.uhf_g.data.local.dao.ItemsDao;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.dao.ResetDao;
import com.pda.uhf_g.data.local.dao.TagItemDao;
import com.pda.uhf_g.data.local.entities.CategoriaEntity;
import com.pda.uhf_g.data.local.entities.ItemEntity;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;

import java.io.File;

@Database(
        entities = {
            PosicionamientoEntity.class,
            CategoriaEntity.class,
            PondEntity.class,
            ItemEntity.class
        },
        version = 7
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TagItemDao TagItemDao();
    public abstract PondsDao PondsDao();
    public abstract ItemsDao ItemsDao();
    public abstract CatalogDao CatalogDao();
    public abstract ResetDao ResetDao();

    private static AppDatabase instance;

    private final static String DB_NAME = "rfid_database";
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                    .build();
        }
        return instance;
    }

}