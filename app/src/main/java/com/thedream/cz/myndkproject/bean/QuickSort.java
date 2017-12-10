package com.thedream.cz.myndkproject.bean;

import com.thedream.cz.myndkproject.utils.PrintUtil;

/**
 * Created by cz on 2017/12/8.
 */

public class QuickSort {

    private long[] array;
    private int nElems;

    public QuickSort(int max) {
        array = new long[max];
        nElems = 0;
    }

    public void insert(long value) {
        array[nElems] = value;
        nElems++;
    }

    public void diplay() {
        PrintUtil.printCZ("---------------");
        for (int i = 0; i < nElems; i++) {
            PrintUtil.printCZ("打印日志:" + array[i]);
        }
    }

    public void quickSort() {
        quickSort(0, nElems - 1);
    }

    private void quickSort(int left, int right) {
        if (left >= right) {
            return;
        }
        PrintUtil.printCZ("left: " + left + "  right:" + right);
        long rightValue = array[right];
        int partition = partitionIt(left, right, rightValue);
        quickSort(left, partition - 1);
        quickSort(partition + 1, right);

    }

    private int partitionIt(int left, int right, long pivot) {
        int leftPtr = left - 1;
        int rightPtr = right;
        PrintUtil.printCZ("leftPtr: " + leftPtr + "  rightPtr:" + rightPtr + " pivot:" + pivot);
        while (true) {
            while (array[++leftPtr] < pivot) ;
            while (rightPtr > 0 && array[--rightPtr] > pivot) ;
            PrintUtil.printCZ("leftPtr2: " + leftPtr + "  rightPtr2:" + rightPtr);
            if (leftPtr >= rightPtr) {
                break;
            } else {
                swap(leftPtr, rightPtr);
            }
        }
        swap(leftPtr, right);
        return leftPtr;
    }

    private void swap(int dex1, int dex2) {
        long temp = array[dex1];
        array[dex1] = array[dex2];
        array[dex2] = temp;
    }
}
