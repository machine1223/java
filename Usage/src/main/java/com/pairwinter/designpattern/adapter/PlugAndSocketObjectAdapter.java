package com.pairwinter.designpattern.adapter;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-12
 * Time: 下午4:15
 * 这个是一个适配器，
 * 一方面它实现了PlugInterface，使得三项插头能够使用这个适配器，
 * 另一个方面它继承了两厢插座的方法，使得三厢插头能够使用两厢插座已有的方法。
 * 这种方式的适配器叫做”类适配器“
 */
public class PlugAndSocketObjectAdapter {
    private Socket socket = new Socket();
    public void plug1(){
        socket.plug1();
    }
    public void plug2(){
        socket.plug2();
    }
    public void plug3(){
        System.out.println("对象适配器的地极，它实现了三厢插头的第三个接口，从而能被三厢插头使用。");
    }
}
