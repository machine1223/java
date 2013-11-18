package com.pairwinter.designpattern.adapter;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-12
 * Time: 下午4:44
 * To change this template use File | Settings | File Templates.
 */
public class ThreePlug implements PlugInterface {
    @Override
    public void plug1() {
        System.out.println("三厢插头的正极");
    }

    @Override
    public void plug2() {
        System.out.println("三厢插头的负极");
    }

    @Override
    public void plug3() {
        System.out.println("三厢插头的地级");
    }
}
