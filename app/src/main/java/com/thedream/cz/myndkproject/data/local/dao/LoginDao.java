package com.thedream.cz.myndkproject.data.local.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.thedream.cz.myndkproject.data.entity.LoginInfo;

import java.util.List;

import io.reactivex.Flowable;


/**
 * Created by cz on 2017/11/29.
 * 用户登录信息保存 entity {@link LoginInfo}
 */
@Dao
public interface LoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLogin(LoginInfo info);

    @Query("SELECT * FROM LoginInfo where uid = :id")
    Flowable<LoginInfo> queryLoginById(String id);

    @Query("SELECT * FROM LoginInfo")
    List<LoginInfo> queryList();
}
