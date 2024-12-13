package com.pda.uhf_g.data.local;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.pda.uhf_g.data.local.dao.PondsDao;
import com.pda.uhf_g.data.local.entities.ItemSightingEntity;
import com.pda.uhf_g.data.local.entities.PondEntity;
import com.pda.uhf_g.data.local.entities.TagItemEntity;
import com.pda.uhf_g.data.local.entities.TagData;
import com.pda.uhf_g.data.remote.CatalogRemoteDataSource;
import com.pda.uhf_g.data.remote.PondsRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class ItemsLocalDataSource {

    private final com.pda.uhf_g.data.local.dao.TagItemDao tagItemDao;
    private final com.pda.uhf_g.data.local.dao.PondsDao pondsDao;


    public ItemsLocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        tagItemDao = db.TagItemDao();
        pondsDao = db.PondsDao();

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

    public List<TagData> getAllItems() {
        List<TagItemEntity> entities = tagItemDao.getAllItems();
        // Convert entities to TagData objects
        List<TagData> items = new ArrayList<>();
        for (TagItemEntity entity : entities) {
            items.add(new TagData(entity.getCid(), entity.getAfid(), entity.getTid(),
                    entity.getName(), entity.getDescription()));
        }
        return items;
    }

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    public TagData findItemByTid(String tid) {
        TagItemEntity item = tagItemDao.getItemByTid(tid);
        return new TagData(item.getCid(), item.getAfid(), item.getTid(), item.getName(), item.getDescription());
    }

    public void insertItemSighting(TagData item) {
        ItemSightingEntity entity = new ItemSightingEntity(item.getCid(), System.currentTimeMillis());
        tagItemDao.insertSighting(entity);
    }

    public TagItemEntity insertItem(TagData item) {
        TagItemEntity entity = new TagItemEntity(item.getCid(), item.getAfid(), item.getTid());
        entity.setCreatedTimestamp(System.currentTimeMillis());
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        tagItemDao.insertItem(entity);
        return entity;
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

    public void  insertCatalogItems(List<CatalogRemoteDataSource.CatalogoResponse.Item>  pondsJson) {
        List<PondEntity> ponds = new ArrayList<>();
        for (CatalogRemoteDataSource.CatalogoResponse.Item pond : pondsJson) {
            //TODO
        }

        pondsDao.insertAllPonds(ponds);
    }

    public void  insertItemsIPSP(List<CatalogRemoteDataSource.ItemsResponse.Item>   pondsJson) {
        List<PondEntity> ponds = new ArrayList<>();
        for (CatalogRemoteDataSource.ItemsResponse.Item pond : pondsJson) {
            //TODO
        }

        pondsDao.insertAllPonds(ponds);
    }


    public Observable<List<PondsDao.MegaZoneList>> getPondsMegazones() {
        return pondsDao.getPondsMegaZones();
    }
}