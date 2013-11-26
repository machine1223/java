package com.pairwinter.importnew;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-19
 * Time: 下午2:33
 * <p>Java Collections Framework</p>
 * <p>包含两大分支，Collections和Map，Collection集合用来存储和处理多个对象，Map用来处理多个键值对</p>
 */
public class TestJavaCollectionsFramework {
    public static void main(String[] args) {
        TestCollection.testArrayList(10*10000);
        TestCollection.testLinkedList(10*10000);
    }
}
