package com.pairwinter.importnew;

import com.pairwinter.usage.generics.Result;
import utils.UsageUtils;

import java.util.*;

/**
 * <p>Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-11-18
 * Time: 上午11:14 </p>
 *
 * <p>HashMap和HashSet的区别是Java面试中最常被问到的问题。 </p>
 * <p>如果没有涉及到Collection框架以及多线程的面试，可以说是不完整。 </p>
 * <p>而Collection框架的问题不涉及到HashSet和HashMap，也可以说是不完整。 </p>
 * <p>HashMap和HashSet都是collection框架的一部分，它们让我们能够使用对象的集合。</p>
 * <p>collection框架有自己的接口和实现，主要分为Set接口，List接口和Queue接口。 </p>
 * <p>它们有各自的特点，Set的集合里不允许对象有重复的值，List允许有重复，它对集合中的对象进行索引，Queue的工作原理是FCFS算法(First Come, First Serve)。 </p>
 * <p style="color:red"><b>HashMap 与 HashSet 的区别</b></p>
 * <table>
 *     <tr><td><b>HashMap</b></td><td><b>HashSet</b></td></tr>
 *     <tr><td>HashMap实现了Map接口</td><td>HashSet实现了Set接口</td></tr>
 *     <tr><td>HashMap储存键值对</td><td>HashSet仅仅存储对象</td></tr>
 *     <tr><td>使用put()方法将元素放入map中</td><td>使用add()方法将元素放入set中</td></tr>
 *     <tr><td>HashMap中使用键对象来计算HashCode值</td><td>HashSet使用成员对象来计算hashcode值，对于两个对象来说hashcode可能相同，所以equals()方法用来判断对象的相等性，如果两个对象不同的话，那么返回false</td></tr>
 *     <tr><td>HashMap比较快，因为是使用唯一的键来获取对象</td><td>HashSet较HashMap来说比较慢</td></tr>
 * </table>
 * <p style="color:red"><b>HashMap 与 HashTable 的区别</b></p>
 * <p>都实现了Map接口，但决定用哪一个之前先要弄清楚它们之间的分别。主要的区别有：线程安全性，同步(synchronization)，以及速度。</p>
 * <table>
 *     <tr><td><b>HashMap</b></td><td><b>HashTable</b></td></tr>
 *     <tr><td>HashMap实现了Map接口。</td><td>HashSet实现了Map接口。</td></tr>
 *     <tr>
 *          <td>非synchronized的，如果没有正确的同步，那么HashMap是不能被多线程共享的。</td>
 *          <td>HashTable是synchronized（线程同步）的，多个线程可以共享一个HashTable,Java 5提供了ConcurrentHashMap，它是HashTable的替代，比HashTable的扩展性更好。</td>
 *     </tr>
 *     <tr><td>可以接受为null的key,和为null的value。</td><td>不能接受为null的key和value。</td></tr>
 *     <tr>
 *          <td>
 *              <p>HashMap的迭代器(Iterator)是fail-fast迭代器，所以当有其它线程改变了HashMap的结构（增加或者移除元素），将会抛出ConcurrentModificationException，</p>
 *              <p>但迭代器本身的remove()方法移除元素则不会抛出ConcurrentModificationException异常。</p>
 *              <p>但这并不是一个一定发生的行为，要看JVM。这条同样也是Enumeration和Iterator的区别。</p>
 *          </td>
 *          <td>HashTable的enumerator迭代器不是fail-fast的。</td>
 *     </tr>
 *     <tr><td>HashMap比较快，因为它使用唯一的键来获取对象，而且是非synchronized的</td><td>HashTable较HashMap来说比较慢</td></tr>
 * </table>
 * <p style="color:red">HashTable和HashMap有几个主要的不同：线程安全以及速度。仅在你需要完全的线程安全的时候使用HashTable，而如果你使用Java 5或以上的话，请使用ConcurrentHashMap吧。</p>
 */
public class TestHash {
    public static void main(String[] args) {
        testHashMap();
        testLinkedHashMap();
        testTreeMap(true);//升序
        testTreeMap(false);//降序
        testHashTable();
        testSet();
    }

    /**
     * <p>HashMap实现了Map接口，Map接口对键值对进行映射。</p>
     * <p>Map中不允许重复的键。                          </p>
     * <p>Map接口有两个基本的实现，HashMap和TreeMap。    </p>
     * <p>TreeMap保存了对象的排列次序，而HashMap则不能。 </p>
     * <p>LinkedHashMap保存了对象的put次序，而HashMap则不能。 </p>
     * <p>HashMap允许键和值为null。                      </p>
     * <p>HashMap是非synchronized的，但collection框架提供方法能保证HashMap synchronized，这样多个线程同时访问HashMap时，能保证只有一个线程更改Map。</p>
     */
    public static void testHashMap() {
        UsageUtils.splitLine("Test HashMap");
        Map<String,Integer> hashMap = new HashMap<String,Integer>();
        hashMap.put("one", 1);
        hashMap.put("two", 2);
        hashMap.put("three", 3);
        hashMap.put("four", 4);
        hashMap.put("five",5);
//        添加重复的Key，将会覆盖掉以前的。
        hashMap.put("one", 11);
//        HashMap并不是有序的，所以下面语句打印的顺序可能并不是上面put的顺序。LinkedHashMap保存了对象的put顺序
        for (Object o : hashMap.keySet()) {
            System.out.println(hashMap.get(o));
        }
        /*
        Fail-safe()和iterator迭代器相关。
        如果某个集合对象创建了Iterator或者ListIterator，然后其它的线程试图“结构上”更改集合对象，将会抛出ConcurrentModificationException异常。
        但其它线程可以通过set()方法更改集合对象是允许的，因为这并没有从“结构上”更改集合。
        但是假如已经从结构上进行了更改，再调用set()方法，将会抛出IllegalArgumentException异常。
        结构上的更改指的是删除或者插入一个元素，这样会影响到map的结构
         */
        Iterator<String> iterator = hashMap.keySet().iterator();
        try {
            while (iterator.hasNext()){
                String next = iterator.next();
                hashMap.remove(next);//试图从结构上更改集合对象，这是不允许的。
            }
        }catch (ConcurrentModificationException cme){
            System.out.println("出现异常：ConcurrentModificationException");
        }
        Map<String,Result<String>> resultHashMap = new HashMap<String,Result<String>>();
        Iterator<String> resultIterator = resultHashMap.keySet().iterator();
        try{
            while (resultIterator.hasNext()){
//                Iterator 在工作的时候是不允许被迭代的对象被改变的。但你可以使用 Iterator 本身的方法 remove() 来删除对象， Iterator.remove() 方法会在删除当前迭代对象的同时维护索引的一致性。
                iterator.remove();//这是正确的方式。
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * <p>LinkedHashMap保存了对象的put次序，在遍历的时候按照put的顺序输出。 </p>
     */
    public static void testLinkedHashMap() {
        UsageUtils.splitLine("Test LinkedHashMap");
        Map<String, Integer> linkedHashMap = new LinkedHashMap<String, Integer>();
        linkedHashMap.put("one",1);
        linkedHashMap.put("two",2);
        linkedHashMap.put("three",3);
        linkedHashMap.put("Four", 4);//F:70
        linkedHashMap.put("five",5); //f:102
        for(String key : linkedHashMap.keySet()){
            System.out.println(linkedHashMap.get(key));
        }
    }

    /**
     * TreeMap 默认是按照key的升序排列，而不是Put的顺序
     * @param isAsc 是否升序
     *              true 升序（也是默认排序）
     *              false 降序（实现Comparator接口）
     */
    public static void testTreeMap(boolean isAsc) {
        UsageUtils.splitLine("Test TreeMap");
        Map<String, Integer> treeMap;
        if(!isAsc){
            treeMap = new TreeMap<String,Integer>();
        }else{
            treeMap = new TreeMap<String, Integer>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2)*-1;
                }
            });
        }
        treeMap.put("one", 1);
        treeMap.put("two", 2);
        treeMap.put("three", 3);
        treeMap.put("Four", 4);//F:70
        treeMap.put("five", 5); //f:102
        for(String key : treeMap.keySet()){
            System.out.println(treeMap.get(key));
        }
    }

    public static void testHashTable() {
        UsageUtils.splitLine("Test HashTable");
        Hashtable<String,Integer> hashTable = new Hashtable<String,Integer>();
        hashTable.put("1",1);
        hashTable.put("2",2);
        hashTable.put("3",3);
        try{
//            enumerator迭代器不是fail-fast的
            Enumeration<String> enumeration = hashTable.keys();
            while (enumeration.hasMoreElements()){
                String next = enumeration.nextElement();
                hashTable.remove(next);
                System.out.println(next);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * <p>HashSet实现了Set接口，它不允许集合中有重复的值，</p>
     * <p>当我们提到HashSet时，第一件事情就是在将对象存储在HashSet之前，</p>
     * <p>要先确保对象重写equals()和hashCode()方法，这样才能比较对象的值是否相等，以确保set中没有储存相等的对象。</p>
     * <p>如果我们没有重写这两个方法，将会使用这个方法的默认实现。</p>
     * <p>public boolean add(Object o)方法用来在Set中添加元素，当元素值重复时则会立即返回false，如果成功添加的话会返回true。</p>
     */
    public static void testSet() {
        UsageUtils.splitLine("Test HashSet");
        Set hashSet = new HashSet<String>();
        hashSet.add("one");
        hashSet.add("two");
        hashSet.add("one");
        Iterator iterator = hashSet.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

}
