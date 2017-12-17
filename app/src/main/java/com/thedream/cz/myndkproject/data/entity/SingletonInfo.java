package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.utils.PrintUtil;

import java.io.ObjectStreamException;

/**
 * Created by cz on 2017/12/17.
 * 单例模式
 * 优点：
 * 1、在内存中只有一个实例，减小内存开支(针对需要频繁创建或销毁的对象时)
 * 2、减少系统开销(针对对象产生需要较多的资源时，如读取配置，产生其他依赖对象时)
 * 3、避免对资源的多重占用，例如一个写文件操作，由于只有一个实例存在内存，避免对同一个资源文件的同时写操作
 * 4、设置全局访问点，共享和优化资源访问，例如设计一个单例类，负责所有数据表的映射处理
 * 缺点：
 * 1、单例模式没有借口，扩展很困难，例如构造方法中传入参数，除了重写没有其他方法
 * 2、单例对象中持有Context，很容易引发内存泄漏，此时注意传递给单例对象的Context最好是ApplicationContext
 */

public class SingletonInfo {

    public void show() {
        PrintUtil.printCZ("我是单例模式");
    }

    private SingletonInfo() {

    }

    //  懒汉模式
//    private static SingletonInfo instance;
//    public static SingletonInfo getInstance() {
//        if(instance == null) {
//            instance = new SingletonInfo();
//        }
//        return instance;
//    }

    //  饿汉模式
//    private static SingletonInfo instance = new SingletonInfo();
//    public static SingletonInfo getInstance() {
//        return instance;
//    }

    //  DCL模式(Double Check Look)
    //  不赞成使用这种方法
    private volatile static SingletonInfo instance = new SingletonInfo();
//    public static SingletonInfo getInstance() {
//        if(instance == null) {
//            synchronized (SingletonInfo.class) {
//                if(instance == null) {
//                    instance = new SingletonInfo();
//                }
//            }
//        }
//        return instance;
//    }

    //  静态内部类
    public static SingletonInfo getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final SingletonInfo instance = new SingletonInfo();
    }

    //  枚举方法

    //  容器实现
    /*
        //  伪代码
        public class SingletonManager{
            private static Map<String, Object> map = new HashMap<String, Object>();
            private SingletonManager(){
            }

            public static void registerService(String key, Object obj) {
                if(!map.containskKey(key)){
                    map.put(key, obj);
                }
            }

            public static Object getService(String key) {
                return map.get(key);
            }
        }
     */

    //  杜绝反序列化
    private Object readResolve() throws ObjectStreamException {
        return instance;
    }
}
