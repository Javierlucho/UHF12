package com.pda.uhf_g.data.local.dao;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pda.uhf_g.data.local.entities.PondEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;


@Dao
public interface PondsDao {

    abstract interface PondList {

        public String getVisualName();
        public String getID();
    }

    class SectorList implements PondList {
        public String sector;
        public String sector_id;

        public SectorList(String sector, String sector_id) {
            this.sector = sector;
            this.sector_id = sector_id;
        }

        @NonNull
        @Override
        public String toString() {
            return sector;
        }

        public String getVisualName(){
            return sector;
        }
        public String getID(){
            return sector_id;
        }

    }

    class ZoneList implements PondList {
        public String zone;
        public String zone_id;

        public ZoneList(String zone, String zone_id) {
            this.zone = zone;
            this.zone_id = zone_id;
        }

        @NonNull
        @Override
        public String toString() {
            return zone;
        }

        public String getVisualName(){
            return zone;
        }

        public String getID(){
            return zone_id;
        }
    }

    class MegaZoneList implements PondList {
        public String megazone;
        public String megazone_id;

        public MegaZoneList(String megazone, String megazone_id) {
            this.megazone = megazone;
            this.megazone_id = megazone_id;
        }

        @NonNull
        @Override
        public String toString() {
            return megazone;
        }

        public String getVisualName(){
            return megazone;
        }

        public String getID(){
            return megazone_id;
        }
    }

    class PondsList extends PondEntity implements PondList {

        public PondsList(String uuid, String megazone, String megazone_id, String zone, String zone_id, String sector, String sector_id, String pond) {
            super(uuid, megazone, megazone_id, zone, zone_id, sector, sector_id, pond);
        }

        @NonNull
        @Override
        public String toString() {
            return getPond();
        }

        public String getVisualName(){
            return getPond();
        }

        public String getID(){
            return getUuid();
        }
    }

//    @Transaction
//    Completable insertAllPonds(List<PondEntity> ponds){
//           for (PondEntity entity : ponds) {
//               insertPond(entity);
//           }
//    }
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPond(PondEntity pond);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllPonds(List<PondEntity> ponds);

    @Query("SELECT DISTINCT megazone, megazone_id FROM ponds")
    List<MegaZoneList> getPondsMegaZones();

    @Query("SELECT DISTINCT zone, zone_id FROM ponds WHERE megazone_id = :megazone_id")
    List<ZoneList> getPondsZones(String megazone_id);

    @Query("SELECT DISTINCT sector, sector_id FROM ponds WHERE zone_id = :zone_id")
    List<SectorList> getPondsSectors(String zone_id);

    @ColumnInfo()
    @Query("SELECT * FROM ponds WHERE sector_id = :sector_id")
    List<PondsList> getPondsBySector(String sector_id);

}