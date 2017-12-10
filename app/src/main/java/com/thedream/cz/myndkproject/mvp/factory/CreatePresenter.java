package com.thedream.cz.myndkproject.mvp.factory;

import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cz on 2017/12/9.
 * 注解类：标注Presenter
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CreatePresenter {
    Class<? extends BaseMvpPresenter> value();
}
