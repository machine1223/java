package com.pairwinter.designpattern.adapter;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-12
 * Time: 下午4:06
 * 这是一个插头接口，它定义了一个三厢插头，可是插座只能使用两厢插头。为了让三厢插头能够使用插座，我们要在两者之间配置一个适配器。
 * 适配器的目的就是让三厢插头能够使用，为此适配器应该具有该接口的方法。
 */
public interface PlugInterface {
//    public void eat();
//    public void drink();
//    public void play();
//    public void study();
    public void plug1();
    public void plug2();
    public void plug3();
}
