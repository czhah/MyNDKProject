package com.thedream.cz.myndkproject.data.entity;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CityInfo {

    private String id;
    private String cityName;
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
