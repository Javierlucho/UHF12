package com.pda.uhf_g.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_sightings",
        foreignKeys = @ForeignKey(
                entity = TagItemEntity.class,
                parentColumns = "cid",
                childColumns = "cid",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = {"cid"})}) // Optional: Define onDelete behavior

public class ItemSightingEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NonNull
    public int id;

    @ColumnInfo(name = "cid")
    @NonNull
    public String cid; // Foreign key referencing TagItemEntity.cid


    @NonNull
    private long tagged_time; // Timestamp of when the item was seen

    private String latitude; // Latitude
    private String longitude; // Longitude

    public ItemSightingEntity(String cid, long tagged_time) {
        this.cid = cid;
        this.setTagged_time(tagged_time);
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public long getTagged_time() {
        return tagged_time;
    }

    public void setTagged_time(long tagged_time) {
        this.tagged_time = tagged_time;
    }

    // Getters and setters
}