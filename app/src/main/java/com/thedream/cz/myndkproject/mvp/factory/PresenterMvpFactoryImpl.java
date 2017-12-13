package com.thedream.cz.myndkproject.mvp.factory;

import com.thedream.cz.myndkproject.mvp.presenter.BaseMvpPresenter;
import com.thedream.cz.myndkproject.mvp.view.BaseMvpView;
import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/9.
 */

public class PresenterMvpFactoryImpl<V extends BaseMvpView, P extends BaseMvpPresenter<V>> implements PresenterMvpFactory<V, P> {

    private final Class<P> mPresenterClass;

    private PresenterMvpFactoryImpl(Class<P> pClass) {
        this.mPresenterClass = pClass;
    }

    /**
     * 根据注解创建Presenter的工厂实现类
     *
     * @param <V>       {@link BaseMvpView}的实现类
     * @param <P>       {@link BaseMvpPresenter} 的实现类
     * @param viewClazz 需要创建Presenter的V层实现类
     * @return
     */
    public static <V extends BaseMvpView, P extends BaseMvpPresenter<V>> PresenterMvpFactoryImpl<V, P> createFactory(Class<?> viewClazz) {
        CreatePresenter annotation = viewClazz.getAnnotation(CreatePresenter.class);
        Class<P> pClass = null;
        if (annotation != null) {
            pClass = (Class<P>) annotation.value();
        }
        PrintUtil.print("是否包含注解:" + (pClass != null));
        return pClass == null ? null : new PresenterMvpFactoryImpl<V, P>(pClass);
    }

    @Override
    public P createMvpPresenter() {
        PrintUtil.printCZ("创建P层创建工厂,编译期会检查注解的P层是否为空");
        try {
            return mPresenterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Presenter创建失败！请检查是否声明了@CreatePresenter(xxxPresenter.class)注解");
        }
    }
}
