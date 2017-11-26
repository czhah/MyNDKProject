package com.thedream.cz.myndkproject.base;

import android.util.Log;

/**
 * Created by cz on 2017/11/12.
 */

public class MySignLinkList<E> {

    transient int size = 0;

    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     * (first.prev == null && first.item != null)
     */
    transient Node<E> first;


    public void add(E e) {
        linkLast(e);
    }

    public void add(int index, E e) {
        linkBefore(index, e);
    }

    public E get(int index) {
        if (index < 0 || index >= size) return null;
        Log.e("cz", "index:" + index + ",size:" + size);
        return node(index).item;
    }

    public void set(int index, E e) {
        if (index < 0 || index >= size) return;
        Node<E> node = node(index);
        node.item = e;
    }


    public boolean remove(Object o) {
        if (o != null) {
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }
        return false;
    }

    private void unlink(Node<E> x) {
        //  如果是头部
        final Node<E> next = x.next;
        Node<E> prev = prev(x);
        prev.next = next;
        size--;
    }

    private Node<E> prev(Node<E> x) {
        Node<E> node = first;
        while (node.next != x)
            node = node.next;
        return node;
    }

    private void linkLast(E e) {
        final Node<E> newNode = new Node<E>(e, null);
        if (first == null) {
            first = newNode;
        } else {
            node(size - 1).next = newNode;
        }
        size++;
    }

    private void linkBefore(int index, E e) {
        if (index < 0 || index > size - 1) return;
        if (index == 0) {
            final Node<E> temp = first;
            final Node<E> newNode = new Node<>(e, temp);
            first = newNode;
        } else {
            Node<E> prev = node(index - 1);
            Node<E> cur = node(index);
            Node<E> newNode = new Node<>(e, cur);
            prev.next = newNode;
        }
        size++;
    }

    private Node<E> node(int index) {
        Node<E> node = first;
        for (int i = 0; i < index; i++)
            node = node.next;
        return node;
    }

    /**
     * 反序
     */
    public void reverNodes() {
        // 方法一
//        Node<E> newNode = null;
//        Node<E> temp = first;
//        while(temp != null){
//            final Node<E> next = temp.next;
//            temp.next = newNode;
//            newNode = temp;
//            temp = next;
//        }
//        first = newNode;

        // 方法二
        revers(first);
    }


    /**
     * 递归
     */
    private Node<E> temp = null;

    public void revers(Node<E> node) {
        if (node != null) {
            final Node<E> next = node.next;
            node.next = temp;
            temp = node;
            revers(next);
        } else {
            first = temp;
        }
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    private static class Node<E> {
        E item;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }
}
