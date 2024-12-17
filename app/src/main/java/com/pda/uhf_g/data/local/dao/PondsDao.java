package com.pda.uhf_g.data.local.dao;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pda.uhf_g.data.local.entities.PondEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;


@Dao
public interface PondsDao {

    abstract class PondList {
        public PondList() {
        }
    }

    class SectorList extends PondList {
        public String sector;
        public String sector_id;

        public SectorList(String sector, String sector_id) {
            this.sector = sector;
            this.sector_id = sector_id;
        }
    }

    class ZoneList extends PondList {
        public String zone;
        public String zone_id;

        public ZoneList(String zone, String zone_id) {
            this.zone = zone;
            this.zone_id = zone_id;
        }
    }

    class MegaZoneList extends PondList {
        public String megazone;
        public String megazone_id;

        public MegaZoneList(String megazone, String megazone_id) {
            this.megazone = megazone;
            this.megazone_id = megazone_id;
        }
    }

//    @Transaction
//    Completable insertAllPonds(List<PondEntity> ponds){
//           for (PondEntity entity : ponds) {
//               insertPond(entity);
//           }
//    }
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertPond(PondEntity pond);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllPonds(List<PondEntity> ponds);

    @Query("SELECT DISTINCT megazone, megazone_id FROM ponds")
    Observable<List<MegaZoneList>> getPondsMegaZones();

    @Query("SELECT DISTINCT zone, zone_id FROM ponds WHERE megazone_id = :megazone_id")
    Single<List<ZoneList>> getPondsZones(String megazone_id);

    @Query("SELECT DISTINCT sector, sector_id FROM ponds WHERE zone_id = :zone_id")
    Single<List<SectorList>> getPondsSectors(String zone_id);

    @ColumnInfo()
    @Query("SELECT * FROM ponds WHERE sector_id = :sector_id")
    Single<List<PondEntity>> getPondsBySector(String sector_id);

}