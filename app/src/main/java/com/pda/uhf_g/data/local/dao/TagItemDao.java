package com.pda.uhf_g.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


@Dao
public interface TagItemDao {

    public static class PondWithLocation{
        public final String afid;
        public final String cid;
        public final String tid;
        public final String categoria_id;
        public final String megazone_id;
        public final String zone_id;
        public final String sector_id;
        public final String uuid;
        public final Double latitude;
        public final Double longitude;

        public PondWithLocation(String afid, String cid, String tid, String categoria_id,
                                String megazone_id, String zone_id, String sector_id, String uuid,
                                Double latitude, Double longitude) {
            this.afid = afid;
            this.cid = cid;
            this.tid = tid;
            this.categoria_id = categoria_id;
            this.megazone_id = megazone_id;
            this.zone_id = zone_id;
            this.sector_id = sector_id;
            this.uuid = uuid;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getUuid() {
            return this.uuid;
        }
        public String getAfid() {
            return this.afid;
        }
        public String getTid() {
            return this.tid;
        }
        public String getCategoriaId() {
            return this.cid;
        }
        public String getCid() {
            return this.categoria_id;
        }
        public String getMegazoneId() {
            return this.megazone_id;
        }
        public String getZoneId() {
            return this.zone_id;
        }
        public String getSectorId() {
            return this.sector_id;
        }
        public Double getLatitude() {
            return this.latitude;
        }
        public Double getLongitude() {
            return this.longitude;
        }
    }

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

    @Query("SELECT  afid, cid, tid, categoria_id, megazone_id, zone_id, sector_id, uuid, latitude, longitude FROM ponds p INNER JOIN posicionamiento pos ON p.uuid = pos.ubicacion_actual")
    Observable<List<PondWithLocation>> getAllItemsIPSPWithLocation();

    @Query("SELECT EXISTS (SELECT 1 FROM posicionamiento)")
    Single<Boolean> hasData();
}