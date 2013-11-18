package com.pairwinter.algorithm.chapter1.exercise;

import utils.UsageUtils;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-15
 * Time: 下午1:59
 * 编写一个程序解决选择问题。令k=N/2，画出表格显示程序对于N种不同的值的运行时间。
 * 分析，N个数，选择其中第K = N/2大的数。
 */
public class Exercise1_1 {
    public static void main(String[] args) {
//        testTimeOfSort(100000);
//        testTimeOfSelect(100000);
        bubbleSort(new int[]{2,3,1});
    }
    public static void testTimeOfSort(int N){
        int[] numbers = UsageUtils.createIntArray(N);
        UsageUtils.printIntArray(numbers, false);
        System.out.println("\n排序前第K=N/2个("+(N/2)+")数是：" + numbers[N/2]);
        long start = System.currentTimeMillis();
        System.out.println(N + "个数，冒泡排序开始--->"+start);
        maoPaoSort(numbers);
        System.out.println(N + "个数，冒泡排序结束,所用时间(ms)--->"+(System.currentTimeMillis()-start));
        UsageUtils.printIntArray(numbers, false);
        System.out.println("\n排序后第K=N/2个("+(N/2)+")大的数是：" + numbers[N/2]);
    }
    public static void testTimeOfSelect(int N){
        int[] numbers = UsageUtils.createIntArray(N);
        System.out.println("\n分批处理前第K=N/2个("+(N/2)+")数是：" + numbers[N/2]);
        long start = System.currentTimeMillis();
        System.out.println(N + "个数，分批处理开始--->" + start);
        select(numbers);
        System.out.println(N + "个数，分批处理结束,所用时间(ms)--->" + (System.currentTimeMillis() - start));
        System.out.println("\n分批处理后第K=N/2个("+(N/2)+")大的数是：" + numbers[N/2]);
    }
    /**
     * 使用冒泡排序
     * @param ints
     */
    public static void maoPaoSort(int[] ints){
//        for (int i = 0; i < ints.length; i++) {
//            for (int j = i+1; j < ints.length; j++) {
//                if(ints[i]>ints[j]){
//                    int temp = ints[i];
//                    ints[i] = ints[j];
//                    ints[j] = temp;
//                }
//            }
//        }
        int temp=0;
        for(int i=0;i<ints.length-1;i++){
            for(int j=0;j<ints.length-1-i;j++){
                if(ints[j]>ints[j+1]){
                    temp=ints[j];
                    ints[j]=ints[j+1];
                    ints[j+1]=temp;
                }
            }
        }
    }
    public static void bubbleSort(int[] ints){
        UsageUtils.printIntArray(ints,false);
        int temp=0;
        for(int i=0;i<ints.length-1;i++){
            System.out.println("i的值---:"+ints[i]);
            for(int j=0;j<ints.length-1-i;j++){
                System.out.println("    j的值---:"+ints[j]);
                if(ints[j]>ints[j+1]){
                    temp=ints[j];
                    ints[j]=ints[j+1];
                    ints[j+1]=temp;
                }
            }
        }
        UsageUtils.printIntArray(ints,false);
    }
    //分批处理
    public static int select(int[] values){
        if (values == null || values.length == 0) {
            throw new NullPointerException("values can't be null.");
        }
        int k = values.length / 2;
        int[] temp = Arrays.copyOf(values, k);
        maoPaoSort(temp);
        for (int i = k ; i < values.length; i++) {
            if (values[i] < temp[k - 1]) { //如果后面的数小于temp的最大数
                temp[k - 1] = temp[k - 2];
                for (int j = k - 2; j >0; j--) {
                    if (values[i] > temp[j]) {
                        temp[j + 1] = values[i];
                        break;
                    }else {
                        temp[j] = temp[j - 1];
                    }
                }
            }
        }
        return temp[k - 1];
    }
}
