package com.pairwinter.designpattern.adapter;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-12
 * Time: 下午4:15
 * 这是一个插座，他只有两个方法，对应插头接口的前两个方法，但是缺少三厢插头的第三个方法，导致了三厢插头不能用。
 * 所以我们需要一个适配器，它一方面使用了当前这个插座已有的两个接口（plug1，plug2），另一方面实现了三厢插头的接口，使得三厢插头在这个
 * 适配器上能够使用。
 */
public class Socket {
    public void plug1(){
        System.out.println("插座的正极");
    };
    public void plug2(){
        System.out.println("插座的负极");
    };
}
