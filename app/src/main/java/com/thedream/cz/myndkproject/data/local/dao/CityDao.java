package com.thedream.cz.myndkproject.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.thedream.cz.myndkproject.constant.DatabaseConstant;
import com.thedream.cz.myndkproject.data.entity.CityInfo;

import java.util.List;

/**
 * @author chenzhuang
 * @time 2018/1/9 14:10
 * @class
 */
@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public List<Long> insertCity(List<CityInfo> list);

    @Query("SELECT * FROM " + DatabaseConstant.TB_CITY_NAME)
    public List<CityInfo> queryCity();


}
