package com.thedream.cz.myndkproject.data.entity.helper;

import android.content.Context;

import com.thedream.cz.myndkproject.data.entity.LogoutStatus;
import com.thedream.cz.myndkproject.listener.ILoginStatus;

/**
 * Created by cz on 2017/12/23.
 * 状态模式:
 * 1、通过接口定义状态的公共属性
 * 2、通过接口的实现类来处理不同状态下的操作
 * 3、调用类只保存当前状态，通过切换状态来达到不同的实现效果
 * <p>
 * 介绍：状态模式的行为是平行的、不可替换的，策略模式的行为是彼此独立、可互相替换的；
 * 状态模式把对象的行为包装在不同的状态对象里，每一个状态对象都有一个公共的抽象状态
 * 基类。状态模式的意图是让一个对象在其内部状态改变的时候，其行为也随之改变
 * <p>
 * 定义：
 * 当一个对象的内在状态改变时允许改变其行为，这个对象看起来是改变了其类。
 * <p>
 * 使用场景：
 * 1、一个对象的行为取决于它的状态，并且它必须在运行时根据状态改变它的行为；
 * 2、代码中包含大量与对象状态有关的条件语句，例如，一个操作中包含有庞大的多分支
 * 语句，且这些分支依赖于该对象的状态。
 * <p>
 * 角色：
 * UserLoginContext：环境类，定义客户感兴趣的接口，维护一个Status子类的实例，这个实例定义了对象
 * 的当前状态。
 * ILoginStatus：抽象状态类或者状态接口，定义一个或者一组接口，表示该状态下的行为。
 * LoginStatus、LogoutStatus：具体状态类，每一个具体的状态类实现抽象Status中定义
 * 的接口，从而达到不同状态下的不同行为
 * <p>
 * 优点：
 * Status 模式将所有与一个特定的状态相关的行为都放入一个状态对象中，它提供了一个更好的
 * 方法来组织与特定状态相关的代码，将繁琐的转股国泰判断转换成接口清晰的状态类族，在避免
 * 代码膨胀的同时也保证了可扩展性与可维护性。
 * 缺点：
 * Status 模式会导致系统类和对象的增加
 */

public class UserLoginContext {

    private static UserLoginContext instance = new UserLoginContext();

    private ILoginStatus status = new LogoutStatus();

    private UserLoginContext() {
    }

    public static UserLoginContext getInstance() {
        return instance;
    }

    public void setStatus(ILoginStatus status) {
        this.status = status;
    }

    public void forward(Context context) {
        status.forward(context);
    }
}
