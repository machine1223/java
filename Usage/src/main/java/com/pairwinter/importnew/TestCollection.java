package com.pairwinter.importnew;

import utils.UsageUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-19
 * Time: 下午2:31
 * To change this template use File | Settings | File Templates.
 */
public class TestCollection {
    /**
     * <p>通过ArrayList的源码可知，他内部维护了一个数组，提供了相应的方法对这个数组进行操作。</p>
     * <p>数组在内存中是连续的存储空间，由于数组的性质，ArrayList在检索数据时很快，但是在往中间插入或者删除数据时却比较慢，因为这牵扯到各个数据在内存中的移动</p>
     * <p>如果向ArrayList中追加元素那么就非常快，而且不需要像LinkedList那么样建立前后元素的关联关系，所以这种情况ArrayList性能比LinkedList好。</p>
     * <p>当前方法用于测试ArrayList的在增加或者删除元素时的性能</p>
     * @param count 插入的数目
     */
    public static void testArrayList(int count) {
        UsageUtils.splitLine("Test ArrayList!");
        List<Integer> integers = new ArrayList<Integer>();
        long start = System.currentTimeMillis();
        while (integers.size()<count){
            integers.add(0,integers.size()+1);
        }
        long end = System.currentTimeMillis();
        System.out.println("在ArrayList的首部插入“"+integers.size()+"”条记录的时间"+(end-start));

        integers.clear();
        start = System.currentTimeMillis();
        while (integers.size()<count){
            integers.add(integers.size() + 1);
        }
        end = System.currentTimeMillis();
        System.out.println("在ArrayList的末尾插入“"+integers.size()+"”条记录的时间"+(end-start));
    }

    /**
     * <p>LinkedList是一个双向链表，它在插入删除元素时非常快，因为它不牵扯到元素在内存中的移动。</p>
     * <p>当向LinkedList中添加元素时，只是指定当前元素前后节点。</p>
     * <p>LinkedList中元素不是在内存中连续的存储，所以在检索相应的节点时就不如ArrayList了。</p>
     * @param count 插入的数目
     */
    public static void testLinkedList(int count) {
        UsageUtils.splitLine("Test LinkedList!");
        LinkedList<Integer> integers = new LinkedList<Integer>();
        long start = System.currentTimeMillis();
        while (integers.size()<count){
            integers.add(0,integers.size());
        }
        long end = System.currentTimeMillis();
        System.out.println("在LinkedList的首部插入“" + integers.size() + "”条记录的时间" + (end - start));

        integers.clear();
        start = System.currentTimeMillis();
        while (integers.size()<count){
            integers.addLast(integers.size() + 1);
        }
        end = System.currentTimeMillis();
        System.out.println("在LinkedList的末尾插入“"+integers.size()+"”条记录的时间"+(end-start));
    }

    /**
     * HashSet的内部实现
     */
    public static void testHashSet() {
        UsageUtils.splitLine("Test HashSet");
    }
}
