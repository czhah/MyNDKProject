package com.thedream.cz.myndkproject.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.thedream.cz.myndkproject.constant.DatabaseConstant;

/**
 * Created by Administrator on 2017/12/15.
 */
@Entity(tableName = DatabaseConstant.TB_CITY_NAME)
public class CityInfo {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = DatabaseConstant.FIELD_CITY_ID)
    private String id;

    @ColumnInfo(name = DatabaseConstant.FIELD_CITY_NAME)
    private String cityName;

    @ColumnInfo(name = DatabaseConstant.FIELD_CITY_CID)
    private String cityId;

    @Override
    public String toString() {
        return "CityInfo{" +
                "id='" + id + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cityId='" + cityId + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
