package com.pda.uhf_g.data.local;

import android.content.Context;

import com.pda.uhf_g.data.local.entities.ItemSightingEntity;
import com.pda.uhf_g.data.local.entities.TagItemEntity;
import com.pda.uhf_g.data.local.entities.TagData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ItemsLocalDataSource {

    private final com.pda.uhf_g.data.local.dao.TagItemDao TagItemDao;

    public ItemsLocalDataSource(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        TagItemDao = db.TagItemDao();
    }

    public List<TagData> getAllItems() {
        List<TagItemEntity> entities = TagItemDao.getAllItems();
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
        TagItemEntity item = TagItemDao.getItemByTid(tid);
        return new TagData(item.getCid(), item.getAfid(), item.getTid(), item.getName(), item.getDescription());
    }

    public void insertItemSighting(TagData item) {
        ItemSightingEntity entity = new ItemSightingEntity(item.getCid(), System.currentTimeMillis());
        TagItemDao.insertSighting(entity);
    }

    public TagItemEntity insertItem(TagData item) {
        TagItemEntity entity = new TagItemEntity(item.getCid(), item.getAfid(), item.getTid());
        entity.setCreatedTimestamp(System.currentTimeMillis());
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        TagItemDao.insertItem(entity);
        return entity;
    }
}