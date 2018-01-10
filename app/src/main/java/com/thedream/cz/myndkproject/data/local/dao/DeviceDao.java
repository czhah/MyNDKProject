package com.thedream.cz.myndkproject.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.thedream.cz.myndkproject.data.entity.DeviceInfo;

import java.util.List;

/**
 * @author chenzhuang
 * @time 2018/1/10 16:19
 * @class
 */
@Dao
public interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertDevice(List<DeviceInfo> list);

    @Query("SELECT * FROM DeviceInfo")
    List<DeviceInfo> queryDevice();
}
