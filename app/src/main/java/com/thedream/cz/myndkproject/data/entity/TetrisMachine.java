package com.thedream.cz.myndkproject.data.entity;

import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/23.
 * 命令模式
 * <p>
 * 定义：将一个请求封装成一个对象，从而让用户使用不通过的请求吧客户端参数化；
 * 对请求排队或者记录请求日志，以及支持可撤销的操作。
 * <p>
 * 使用场景
 * 1、需要抽象出待执行的动作，然后以参数的形式提供出来——类似于过程设计中的回调机制，
 * 而命令模式正式回调机制的一个面向对象的替代品；
 * 2、在不同的时刻指定、排序和执行请求，一个命令对象可以有与初始请求无关的生存期；
 * 3、需要支持取消操作？？？
 * 4、需要修改日志功能，这样当系统崩溃时，这些修改可以被重做一次；
 * 5、需要支持事务操作；
 * <p>
 * 角色：
 * TetrisMachine：接收者角色。该类负责具体实施或执行一个请求；接收者角色就是真正
 * 执行各项关机逻辑的底层代码。
 * ICommand：定义所有具体命令类的抽象接口；
 * LeftCommand、RightCommand：具体命令角色。该类实现了ICommand接口，在toLeft()
 * 方法中调用接收者角色的相关方法，在接收者或命令执行的具体行为之间加以弱耦合。如果
 * 将这一系列具体的逻辑处理看作接收者，那么调用这些具体逻辑的方法就可以看作是执行方法。
 * Buttons：请求者角色。该类的职责是调用命令执行具体的请求，相关的方法我们称之为行动
 * 方法。
 * <p>
 * <p>
 * 优点：
 * 更弱的耦合性，更灵活的控制性以及更好扩展性
 * <p>
 * 缺点：
 * 类的膨胀，大量衍生类的创建
 */

public class TetrisMachine {

    /**
     * 真正处理向左的方法
     */
    public void toLeft() {
        PrintUtil.printCZ("向左");
    }

    /**
     * 真正处理向右的方法
     */
    public void toRight() {
        PrintUtil.printCZ("向右");
    }
}
