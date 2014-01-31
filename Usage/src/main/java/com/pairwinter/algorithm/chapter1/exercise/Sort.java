package com.pairwinter.algorithm.chapter1.exercise;

import utils.UsageUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-12-11
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class Sort {
    public static void main(String[] args) {
        testMergeSort(1000);
        testInsertionSort(1000);
    }

    /**
     * 插入排序
     */
    public static void insertionSort(int arraysLength) {
        int[] arrays = UsageUtils.createIntArray(arraysLength);
        UsageUtils.printIntArray(arrays,false);
        for(int i=1;i<arrays.length;i++){
            for(int j=0;j<i;j++){
                if(arrays[i]<arrays[j]){
                    int temp = arrays[j];
                    arrays[j] = arrays[i];
                    arrays[i] = temp;
                }
            }
        }
        UsageUtils.printIntArray(arrays,false);
    }
    public static void testInsertionSort(int arraysLength) {
        UsageUtils.splitLine("测试插入排序--开始");
        long start = System.currentTimeMillis();
        insertionSort(arraysLength);
        long end = System.currentTimeMillis();
        System.out.println("测试插入排序--结束，用时："+(end-start));
    }

    private static int[] mergeSort(int[] arrays) {
        if(arrays.length==1){
            return arrays;
        }
        if(arrays.length==2){
            if(arrays[1]<arrays[0]){
                int temp = arrays[0];
                arrays[0] = arrays[1];
                arrays[1] = temp;
            }
            return arrays;
        }
        int separate = arrays.length/2;
        int[] arraysLeft = new int[separate];
        int[] arraysRight = new int[arrays.length-separate];
        for (int i = 0; i < arrays.length; i++) {
            if(i<separate){
                arraysLeft[i] = arrays[i];
            }else{
                arraysRight[i-separate] = arrays[i];
            }
        }
        arraysLeft = mergeSort(arraysLeft);
        arraysRight = mergeSort(arraysRight);
        int[] resultArrays = new int[arrays.length];
        int k=0, l = 0,r=0;
        while(l<arraysLeft.length && r<arraysRight.length){
            if(arraysLeft[l] <= arraysRight[r]){
                resultArrays[k++] = arraysLeft[l++];
            }else{
                resultArrays[k++] = arraysRight[r++];
            }
        }
        while (l<arraysLeft.length){
            resultArrays[k++] = arraysLeft[l++];
        }
        while (r<arraysRight.length){
            resultArrays[k++] = arraysRight[r++];
        }
        return resultArrays;
    }
    public static void testMergeSort(int arraysLength) {
        UsageUtils.splitLine("测试归并排序--开始");
        int[] arrays = UsageUtils.createIntArray(arraysLength);
        UsageUtils.printIntArray(arrays, false);
        long start = System.currentTimeMillis();
        arrays = mergeSort(arrays);
        long end = System.currentTimeMillis();
        UsageUtils.printIntArray(arrays,false);
        System.out.println("测试归并排序--结束，用时："+(end-start));
    }
}
