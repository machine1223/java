package utils;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-12
 * Time: 下午5:39
 * Usage 工具类。
 */
public class UsageUtils {
    public static void splitLine(String name){
        System.out.println("\n******************************************"+name+"******************************************\n");
    }

    public static void print(String formate,Object... args){
        System.out.print(String.format(formate,args));
    }
    public static void println(String formate,Object... args){
        System.out.println(String.format(formate,args));
    }


    /**
     * 打印int数组
     * @param arrs
     * @param isNewLine  是否换行输出
     */
    public static void printIntArray(int[] arrs,boolean isNewLine){
        if(arrs.length>100){
            return;
        }
        splitLine("打印数组，长度"+arrs.length);
        for (int i = 0; i < arrs.length; i++) {
            int arr = arrs[i];
            if(isNewLine){
                System.out.println(arr);
            }else{
                System.out.print(arr + " ");
            }
        }
        System.out.println("");
    }

    public static int[] createIntArray(int arrayLength){
        Random random = new Random(37);
        int[] arrs = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            arrs[i] = random.nextInt(arrayLength);
        }
        return arrs;
    }
}
