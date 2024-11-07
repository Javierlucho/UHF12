package com.pda.uhf_g.data.local;

import android.content.Context;

import com.pda.uhf_g.data.local.entities.TagItemEntity;
import com.pda.uhf_g.entity.TagData;
import com.pda.uhf_g.entity.TagInfo;

import java.util.ArrayList;
import java.util.List;

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
            items.add(new TagData(entity.tid));
        }
        return items;
    }

    public TagData getItemByEpc(String epc) {
        TagItemEntity entity = TagItemDao.getItemByEpc(epc);
        if (entity != null) {
            return new TagData(entity.tid);
        } else {
            return null;
        }
    }

    public void insertItem(TagInfo item) {
        TagItemEntity entity = new TagItemEntity("1", "1", item.getTid());
        entity.setCreatedTimestamp(System.currentTimeMillis());

        TagItemDao.insertItem(entity);
    }

    public void deleteItem(TagInfo item) {

    }

    public void updateItem(TagInfo item) {
        TagItemEntity entity = new TagItemEntity("1", "1", item.getTid());
        entity.setCreatedTimestamp(System.currentTimeMillis());
    }

    public TagInfo getItemByTid(String tid) {
        return null;
    }

}