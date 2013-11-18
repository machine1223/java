package com.pairwinter.algorithm.chapter1.content;

import utils.UsageUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-13
 * Time: 下午5:04
 * To change this template use File | Settings | File Templates.
 */
public class GreatestCommonDivisor {
    public static void main(String[] args) {
        UsageUtils.splitLine("(319,377)");
        System.out.println(euclid(319,377));
        System.out.println(euclid2(319,377));
        UsageUtils.splitLine("(542340,56765635)");
        System.out.println(euclid(542340,56765635));
        System.out.println(euclid2(542340,56765635));
        UsageUtils.splitLine("(23459,372347)");
        System.out.println(euclid(23459,372347));
        System.out.println(euclid2(23459,372347));
    }

    /**
     * 欧几里德算法求最大公约数
     * 使用的是递归方式，但是对于数值计算使用递归通常不是个好主意
     * @param first
     * @param second
     */
    public static int euclid(int first,int second){
        System.out.println("(" + first +"," + second + ")");
        if(first>=second){
            if(first%second == 0){
                return second;
            }else{
                return GreatestCommonDivisor.euclid(second,first%second);
            }
        }else{
            if(second%first == 0){
                return first;
            }else{
                return GreatestCommonDivisor.euclid(first,second%first);
            }
        }
    }
    /**
     * 欧几里德算法求最大公约数
     * 不使用递归，下面的算法要比上面的好。
     * @param first
     * @param second
     */
    public static int euclid2(int first,int second){
        System.out.println("(" + first +"," + second + ")");
        if(first<second){
            int temp = first;
            first = second;
            second = temp;
        }
        while (first % second != 0){
            int temp = first % second;
            first = second;
            second = temp;
        }
        return second;
    }
}
