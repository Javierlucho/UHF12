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
    public long tagged_time; // Timestamp of when the item was seen

    public String latitude; // Latitude
    public String longitude; // Longitude

    public ItemSightingEntity(String cid, long tagged_time) {
        this.cid = cid;
        this.tagged_time = tagged_time;
    }

    // Getters and setters
}