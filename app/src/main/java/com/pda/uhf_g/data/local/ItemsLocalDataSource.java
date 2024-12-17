package com.pda.uhf_g.data.local;

import android.content.Context;

import com.google.gson.JsonObject;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.entities.CategoriaEntity;
import com.pda.uhf_g.data.local.entities.ItemEntity;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.data.local.entities.PosicionamientoEntity;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

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

        this.initStubDB();
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
        pondsDao.insertAllPonds(ponds);
    }

    public List<PosicionamientoEntity> getAllItems() {
        List<PosicionamientoEntity> entities = tagItemDao.getAllItems();
        return entities;
    }

    public PosicionamientoEntity findItemByTid(String tid) {
        PosicionamientoEntity item = tagItemDao.getItemByTid(tid);
        return item;
    }

    public void insertNewPosicionamiento(PosicionamientoEntity updatedData) {
        tagItemDao.insertItem(updatedData);
    }

    public void insertPonds(List<PondsRemoteDataSource.PondData>  pondsJson) {
        List<PondEntity> ponds = new ArrayList<>();
        for (PondsRemoteDataSource.PondData pond : pondsJson) {
            JsonObject metaData = pond.meta_data;
            PondEntity pondDB = new PondEntity(
                    metaData.get("uuid").getAsString(),
                    metaData.get("mega_zona").getAsString(),
                    metaData.get("id_mega_zona").getAsString(),
                    metaData.get("zona").getAsString(),
                    metaData.get("id_zona").getAsString(),
                    metaData.get("sector").getAsString(),
                    metaData.get("id_sector").getAsString(),
                    metaData.get("piscina").getAsString());
            ponds.add(pondDB);
        }

        pondsDao.insertAllPonds(ponds);
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

    public void  insertItemsIPSP(List<CatalogRemoteDataSource.ItemsResponse.Item> Items) {
        for (CatalogRemoteDataSource.ItemsResponse.Item item : Items) {
            ItemEntity itemEntity = new ItemEntity(item.idItem, item.descripcion, item.serie, item.codigoCampo, item.marca);
            itemsDao.insertItem(itemEntity);
        }
//        itemsDao.insertAllItems(Items);
    }

    public Observable<List<PondsDao.MegaZoneList>> getPondsMegazones() {
        return pondsDao.getPondsMegaZones();
    }

    public Completable resetDb(){
        return Completable.fromCallable( () -> {
            db.clearAllTables();
            return true;
        });
        //return resetDao.clearPrimaryKeyIndex();
    }
}