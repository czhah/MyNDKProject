package com.thedream.cz.myndkproject.data.entity;

/**
 * Created by cz on 2017/12/23.
 * 责任链模式：
 * 1、节点上要么处理事件，要么向下传递；
 * 2、结果是要么事件消费，要么事件没有被消费；
 * <p>
 * 定义：
 * 使多个对象都有机会处理请求，从而避免了请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，
 * 并沿着这条链传递该请求，只到有对象处理为止。
 * <p>
 * 使用场景：
 * 1、多个对象可以处理同一请求，但具体由哪个对象处理则在运行时动态决定。
 * 2、在请求处理者不明确的情况下向多个对象的一个提交一个请求(不太明白)。
 * 3、需要动态指定一组对象处理请求。
 * <p>
 * 角色：
 * Leader：抽象处理者角色，声明一个请求处理的方法(handleRequest)，并在其中保存一个对下一个处理
 * 节点handle(nextHandle)对象的引用。
 * GroupLoader、BossLoader：具体处理者角色，对请求进行处理，如果不能处理则将请求转发给下一个
 * 节点上的处理对象。
 * <p>
 * 优点：
 * 可以对请求者和处理者关系解耦，提高代码的灵活性。
 * <p>
 * 缺点：
 * 对链中请求处理者的遍历，如果处理者太多那么遍历必定会影响性能。
 * <p>
 * 注意：
 * 1、对于责任链中的一个处理对象，其只有两个行为，一是处理请求，二是将请求转送给下一个节点，不允许
 * 某个处理者对象在处理了请求后又将请求转送给上一个节点的情况。
 * 2、对于一个责任链来说，一个请求最终只有两种选择，一是被某个处理对象所处理，二是所有对象均未处理。
 */

public abstract class Leader {

    public Leader nextHandle;

    public final void handleRequest(int money) {
        if (money <= limit()) {
            handle(money);
        } else {
            if (nextHandle != null) {
                nextHandle.handleRequest(money);
            }
        }
    }

    /**
     * 最大限额(消费等级)
     *
     * @return
     */
    public abstract int limit();

    /**
     * 处理金额(消费了此次事件)
     *
     * @param money
     */
    public abstract void handle(int money);
}
