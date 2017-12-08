package com.thedream.cz.myndkproject.bean;

import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/7.
 * 希尔排序
 */

public class ShellSort {

    private long[] theArray;
    private int size;

    public ShellSort(int max) {
        theArray = new long[max];
        this.size = 0;
    }

    public void insert(long value) {
        theArray[size] = value;
        size++;
    }

    public void diplay() {
        PrintUtil.printCZ("-------------------------------------------------");
        for (int i = 0; i < size; i++) {
            PrintUtil.printCZ("编译  i:" + i + "  value:" + theArray[i]);
        }
    }

    public void shellSort() {
        int inner, outer;
        long temp;
        int h = 1;
        while (h <= size / 3) {
            h = h * 3 + 1;
        }
        while (h > 0) {
            PrintUtil.print("h: " + h);
            for (outer = h; outer < size; outer++) {
                temp = theArray[outer];
                inner = outer;
                PrintUtil.printCZ("for  --->outer:" + outer + "  temp:" + temp + "  inner:" + inner);
                while (inner > h - 1 && theArray[inner - h] >= temp) {
                    theArray[inner] = theArray[inner - h];
                    inner = h - 1;
                    PrintUtil.printCZ("while  --->outer:" + outer + "  temp:" + temp + "  inner:" + inner);
                }
                theArray[inner] = temp;
            }
            h = (h - 1) / 3;
        }
    }
}
