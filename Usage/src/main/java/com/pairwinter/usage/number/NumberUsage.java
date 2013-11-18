package com.pairwinter.usage.number;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-2
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
public class NumberUsage {
    public static void main(String[] args) {
        testByte();
        testInteger();
    }

    public static void testByte(){
        byte min = -128;
        byte max = 127;
        System.out.println(Integer.toBinaryString(min));//11111111111111111111111110000000
        System.out.println(Integer.toBinaryString(max));//1111111
//        位操作符
        System.out.println("位操作符:");
        System.out.println("1 & 1 -------- " + (1 & 1));
        System.out.println("1 & 0 -------- " + (1 & 0));
        System.out.println("0 & 0 -------- " + (0 & 0));
        System.out.println("1 | 1 -------- " + (1 | 1));
        System.out.println("1 | 0 -------- " + (1 | 0));
        System.out.println("0 | 0 -------- " + (0 | 0));
        System.out.println("1 ^ 1 -------- " + (1 ^ 0));
        System.out.println("1 ^ 0 -------- " + (1 ^ 0));
        System.out.println("0 ^ 0 -------- " + (0 ^ 0));
        System.out.println("~1 ----------- " + Integer.toBinaryString(~ 1));
        System.out.println("~0 ----------- " + Integer.toBinaryString(~ 0));
        System.out.println("3 >> 2 ---- " + Integer.toBinaryString(3 >> 2));
        System.out.println("3 << 2 ---- " + Integer.toBinaryString(3 << 2));
        System.out.println("4 >> 2 ---- " + Integer.toBinaryString(4 >> 2));
        System.out.println("4 << 2 ---- " + Integer.toBinaryString(4 << 2));
        System.out.println("-5 >> 2 ---- " + Integer.toBinaryString(-5 >> 2));
        System.out.println("-5 << 2 ---- " + Integer.toBinaryString(-5 << 2));
    }

    public static void testInteger(){
        Integer integer = 1;
        integer.intValue();
    }
}
