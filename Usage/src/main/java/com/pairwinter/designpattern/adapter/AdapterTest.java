package com.pairwinter.designpattern.adapter;

import utils.UsageUtils;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-12
 * Time: 下午4:43
 * To change this template use File | Settings | File Templates.
 */
public class AdapterTest {
    public static void main(String[] args) {
        UsageUtils.splitLine("类适配器");
        PlugAndSocketClassAdapter classAdapter = new PlugAndSocketClassAdapter();
        //如果一个三厢插头想要使用两厢插座，那么调用适配器的三个接口就行了。
        classAdapter.plug1();
        classAdapter.plug2();
        classAdapter.plug3();
        UsageUtils.splitLine("对象适配器");
        PlugAndSocketObjectAdapter objectAdapter = new PlugAndSocketObjectAdapter();
        //如果一个三厢插头想要使用两厢插座，那么调用适配器的三个接口就行了。
        objectAdapter.plug1();
        objectAdapter.plug2();
        objectAdapter.plug3();
    }
}
