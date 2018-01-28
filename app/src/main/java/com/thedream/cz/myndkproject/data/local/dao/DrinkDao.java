package com.thedream.cz.myndkproject.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thedream.cz.myndkproject.constant.DatabaseConstant;
import com.thedream.cz.myndkproject.data.entity.DrinkInfo;

import java.util.List;

/**
 * Created by cz on 2018/1/15.
 */
@Dao
public interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public List<Long> insertDrink(List<DrinkInfo> list);

    @Query("SELECT * FROM " + DatabaseConstant.TB_DRINK_NAME)
    public List<DrinkInfo> queryDrink();

    @Query("SELECT * FROM " + DatabaseConstant.TB_DRINK_NAME + " WHERE " + DatabaseConstant.FIELD_DRINK_ID + " = :id")
    public List<DrinkInfo> queryDrink(String id);

    @Query("SELECT * FROM " + DatabaseConstant.TB_DRINK_NAME + " WHERE " + DatabaseConstant.FIELD_DRINK_TARGET + " > :minTarget AND " + DatabaseConstant.FIELD_DRINK_TARGET + " < :maxTarget")
    public List<DrinkInfo> queryDrink(int minTarget, int maxTarget);

    @Update
    public int updateDrink(DrinkInfo drinkInfo);

    @Delete
    public int deleteUsers(DrinkInfo... infos);
}
