package com.pairwinter.usage.array;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 10/15/13
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArrayUsage {

    public static void main(String args[]) throws  Exception{
        List<Long> ids1 = new ArrayList<Long>();
        ids1.add(1l);
        ids1.add(2l);
        ids1.add(3l);
        List<Long> ids2 = new ArrayList<Long>();
        ids2.add(2l);
        ids2.add(4l);

        filterIds(ids1,ids2);

        for (Long id : ids1) {
            System.out.println(id);
        }

        Iterable<Long> iterable = new ArrayList<Long>();

    }

    /**
     * 要从后往前删除
     * @param first
     * @param second
     */
    public static void filterIds(List<Long> first , List<Long> second){
        for (int i = first.size() - 1; i >= 0; i--) {
            Long l = first.get(i);
            if(!second.contains(l)){
                first.remove(i);
            }
        }
    }
}
