package com.thedream.cz.myndkproject.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.thedream.cz.myndkproject.constant.DatabaseConstant;

/**
 * Created by cz on 2018/1/15.
 */
@Entity(tableName = DatabaseConstant.TB_DRINK_NAME)
public class DrinkInfo {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DatabaseConstant.FIELD_DRINK_ID)
    private String id;

    @ColumnInfo(name = DatabaseConstant.FIELD_DRINK_WEIGHT)
    private String weight;

    @ColumnInfo(name = DatabaseConstant.FIELD_DRINK_TARGET)
    private int target;

    @ColumnInfo(name = DatabaseConstant.FIELD_DRINK_DEVICE_ID)
    private int deviceId;

    @Override
    public String toString() {
        return "DrinkInfo{" +
                "id='" + id + '\'' +
                ", weight='" + weight + '\'' +
                ", target=" + target +
                '}';
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

}
