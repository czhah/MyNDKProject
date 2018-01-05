package com.thedream.cz.myndkproject.bean;

/**
 * Created by cz on 2018/1/5.
 * 优先级队列  进阶后缀表达式
 */

public class PriorityQ {

    private int maxSize;
    private long[] queArray;
    private int nItems;

    public PriorityQ(int maxSize) {
        this.maxSize = maxSize;
        queArray = new long[maxSize];
        nItems = 0;
    }

    public void insert(long item) {
        if (nItems == 0) {
            queArray[nItems++] = item;
        } else {
            int j;
            for (j = nItems - 1; j >= 0; j--) {
                if (item > queArray[j]) {
                    queArray[j + 1] = queArray[j];
                } else {
                    break;
                }
            }
            queArray[j + 1] = item;
            nItems++;
        }
    }

    public long remove() {
        return queArray[--nItems];
    }

    /**
     * 弹出最小项
     *
     * @return
     */
    public long peekMin() {
        return queArray[nItems - 1];
    }

    public boolean isEmpty() {
        return nItems == 0;
    }

    public boolean isFull() {
        return nItems == maxSize;
    }
}
