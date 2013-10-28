package com.pairwinter.usage.array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-10-25
 * Time: 下午5:28
 * To change this template use File | Settings | File Templates.
 */
public class IteratorUsage {
    public static void main(String[] args) {
        List<Long> list = new ArrayList<Long>();
        list.add(1l);
        list.add(2l);
        list.add(3l);
        list.add(4l);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){
            Long l = (Long)iterator.next();
            if(l == 3l){
                iterator.remove();
            }
        }
        for (Long aLong : list) {
            System.out.println(aLong);
        }
        try{
            list = new ArrayList<Long>();
            list.add(1l);
            list.add(2l);
            list.add(3l);
            list.add(4l);
            for (Long aLong : list) {
                if(aLong==2l){
                    list.remove(aLong);
                }
            }
            for (Long aLong : list) {
                System.out.println(aLong);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
