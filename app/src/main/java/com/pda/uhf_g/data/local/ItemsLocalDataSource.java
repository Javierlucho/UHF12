package com.pda.uhf_g.data.local;

import android.content.Context;

import androidx.room.Query;

import com.google.gson.JsonObject;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.dao.TagItemDao;
import com.pda.uhf_g.data.local.entities.CategoriaEntity;
import com.pda.uhf_g.data.local.entities.ItemEntity;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.ItemsRemoteDataSource;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

public class ItemsLocalDataSource {

    private final com.pda.uhf_g.data.local.dao.TagItemDao tagItemDao;
    private final com.pda.uhf_g.data.local.dao.PondsDao pondsDao;
    private final com.pda.uhf_g.data.local.dao.CatalogDao catalogDao;
    private final com.pda.uhf_g.data.local.dao.ItemsDao itemsDao;
    private final com.pda.uhf_g.data.local.dao.ResetDao resetDao;

    AppDatabase db;

    public ItemsLocalDataSource(Context context) {

        db = AppDatabase.getInstance(context);
        tagItemDao = db.TagItemDao();
        pondsDao = db.PondsDao();
        catalogDao = db.CatalogDao();
        itemsDao = db.ItemsDao();
        resetDao = db.ResetDao();

    }

    public void initStubDB(){
        List<PondEntity> ponds = new ArrayList<>();
        PondEntity pondDB = new PondEntity(
                "U001",
                "ZONA",
                "M001",
                "MEGAZONA",
                "Z001",
                "SECTOR",
                "S001",
                "PISCINA");
        ponds.add(pondDB);
        //pondsDao.insertAllPonds(ponds);
    }

    public List<PosicionamientoEntity> getAllItems() {
        List<PosicionamientoEntity> entities = tagItemDao.getAllItems();
        return entities;
    }

    public Single<PosicionamientoEntity> findItemByTid(String tid) {
        Single<PosicionamientoEntity> item = tagItemDao.getItemByTid(tid);
        return item;
    }


    public void insertPonds(List<PondsRemoteDataSource.PondData>  pondsJson) {
        List<PondEntity> ponds = new ArrayList<>();
        for (PondsRemoteDataSource.PondData pond : pondsJson) {
            JsonObject metaData = pond.meta_data;
            PondEntity pondDB = new PondEntity(
                    pond.uuid,
                    metaData.get("Megazona").getAsString(),
                    metaData.get("Id_Megazona").getAsString(),
                    metaData.get("Zona").getAsString(),
                    metaData.get("Id_Zona").getAsString(),
                    metaData.get("Sector").getAsString(),
                    metaData.get("Id_Sector").getAsString(),
                    metaData.get("Sector").getAsString() + metaData.get("Unidad").getAsString());

            ponds.add(pondDB);
            pondsDao.insertPond(pondDB);
        }

//        pondsDao.insertAllPonds(ponds);
    }

    public void insertCategories(List<CatalogRemoteDataSource.CatalogoResponse.Item> catalogItems) {
        for (CatalogRemoteDataSource.CatalogoResponse.Item catalogItem : catalogItems) {
            CategoriaEntity catalogEntity = new CategoriaEntity(
                    catalogItem.idCategoria,
                    catalogItem.nombre,
                    catalogItem.nomenclatura,
                    catalogItem.codigoHexadecimal,
                    catalogItem.estado);

            catalogDao.insertCatalogItem(catalogEntity);
        }
//        catalogDao.insertAllCatalogItems(catalogItems);
    }

    public List<ItemEntity> getAllItemsIPSP() {
        List<ItemEntity> entities = itemsDao.getAllItemsIPSP();
        return entities;
    }

    public void  insertItemsIPSP(List<CatalogRemoteDataSource.ItemsResponse.Item> Items) {
        for (CatalogRemoteDataSource.ItemsResponse.Item item : Items) {
            ItemEntity itemEntity = new ItemEntity(item.idItem, item.descripcion, item.serie, item.codigoCampo, item.marca);
            itemsDao.insertItem(itemEntity);
        }
//        itemsDao.insertAllItems(Items);
    }

    public List<PondsDao.MegaZoneList> getPondsMegazones() {
        return pondsDao.getPondsMegaZones();
    }

    public List<PondsDao.ZoneList> getPondsZones(String megazone_id) {
        return pondsDao.getPondsZones(megazone_id);
    }

    public List<PondsDao.SectorList> getPondsSectors(String zona_id) {
        return pondsDao.getPondsSectors(zona_id);
    }

    public List<PondsDao.PondsList> getPonds(String sector_id) {
        return pondsDao.getPondsBySector(sector_id);
    }

    public Completable resetDb(){
        return Completable.fromCallable( () -> {
            db.clearAllTables();
            return true;
        });
        //return resetDao.clearPrimaryKeyIndex();
    }

    public Completable insertDownloadedPosicionamientoData(Response<ItemsRemoteDataSource.PosicionamientoGet> response) {
        List<PosicionamientoEntity> itemsToInsert = new ArrayList<>();
        List<ItemsRemoteDataSource.InventarioItemAlt> inventario = response.body().inventario;
        for (ItemsRemoteDataSource.InventarioItemAlt inventarioItemAlt : inventario) {
            PosicionamientoEntity item = new PosicionamientoEntity(
                    inventarioItemAlt.cid,
                    inventarioItemAlt.afid,
                    inventarioItemAlt.tid,
                    inventarioItemAlt.tipoId,
                    inventarioItemAlt.ubicacion_prevista.piscina,
                    inventarioItemAlt.gps.latitud,
                    inventarioItemAlt.gps.longitud
                    );
            itemsToInsert.add(item);
        }
        return tagItemDao.insertAll(itemsToInsert);
    }

    public Completable updatePosicionamiento(PosicionamientoEntity updatedData) {
        return tagItemDao.updateItem(updatedData);
    }

    public Observable<PondsDao.PondsList> getPondByID(String pondID) {
        return pondsDao.getPondByID(pondID);
    }

    public Observable<CategoriaEntity> getCategoryByID(String pondID) {
        return catalogDao.getCategoryByID(pondID);
    }

    public Observable<ItemEntity> getItemByID(String cid) {
        return itemsDao.getItemByID(cid);
    }

    public Observable<List<TagItemDao.PondWithLocation>> getAllItemsWithLocationIDs(){
        return tagItemDao.getAllItemsIPSPWithLocation();
    }

    public Single<Boolean> hasPondsInDB(){
        return pondsDao.hasData();
    }

    public Single<Boolean> hasPosicionamientosInDB(){
        return tagItemDao.hasData();
    }

    public Single<Boolean> hasItemsInDB(){
        return itemsDao.hasData();
    }
    public Single<Boolean> hasCategoriesInDB(){
        return catalogDao.hasData();
    }

}