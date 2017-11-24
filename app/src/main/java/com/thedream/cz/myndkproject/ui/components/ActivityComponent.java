package com.thedream.cz.myndkproject.ui.components;

import android.app.Activity;

import com.thedream.cz.myndkproject.ui.modules.ActivityModule;
import com.thedream.cz.myndkproject.ui.scopes.PerActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/11/23.
 */
@PerActivity
@Component(modules = {ActivityModule.class})
public interface ActivityComponent {

    Activity getActivity();
}
