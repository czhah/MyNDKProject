package com.thedream.cz.myndkproject.base;

import android.util.Log;

/**
 * Created by cz on 2017/11/12.
 * 双向链表
 */
public class MyLinkList<E> {

    transient int size = 0;

    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     * (first.prev == null && first.item != null)
     */
    transient Node<E> first;

    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     * (last.next == null && last.item != null)
     */
    transient Node<E> last;

    public void add(E e) {
        linkLast(e);
    }

    public void add(int index, E e) {
        if (index < 0 || index >= size) return;
        if (index == size) {
            linkLast(e);
        } else {
            linkBefore(e, node(index));
        }
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

        final Node<E> prev = x.prev;
        final Node<E> next = x.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

    }

    private void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<E>(l, e, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    private void linkBefore(E e, Node<E> node) {
        final Node<E> prev = node.prev;
        final Node<E> newNode = new Node<>(prev, e, node);
        node.prev = newNode;
        if (prev == null) {
            first = newNode;
        } else {
            prev.next = newNode;
        }
        size++;
    }

    private Node<E> node(int index) {
        if (index < size >> 1) {
            Node<E> node = first;
            for (int i = 0; i < index; i++)
                node = node.next;
            return node;
        } else {
            Node<E> node = last;
            for (int i = size - 1; i > index; i--)
                node = node.prev;
            return node;
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
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
