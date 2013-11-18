package com.pairwinter.usage.generics;

import com.pairwinter.usage.generics.fruit.Apple;
import com.pairwinter.usage.generics.fruit.Banana;
import com.pairwinter.usage.generics.fruit.Fruit;
import com.pairwinter.usage.generics.fruit.GreenApple;
import com.pairwinter.usage.generics.itface.GenericsInterfaceWithGenericsInInterfaceName;
import com.pairwinter.usage.generics.itface.GenericsInterfaceWithOutGenericsInInterfaceName;
import com.pairwinter.usage.generics.itfaceimpl.GenericsInterfaceImplWithGenericsInInterfaceName;
import com.pairwinter.usage.generics.itfaceimpl.GenericsInterfaceImplWithOutGenericsInInterfaceName;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import utils.UsageUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-10-26
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 */
public class GenericsUsage {
    public static void main(String[] args) {
        testGenericsOfClass();
        testGenericsOfInterface();
        testParentClassAndSubClassInList();
        testParentClassAndSubClassInArray();
    }
    public static void testGenericsOfClass() {
        Result result = new Result();
        result.setResult("string");
        System.out.println(result.getResult());
        result = new Result();
        result.setResult(1);
        System.out.println(result.getResult());
    }
    public static void testGenericsOfInterface() {
        GenericsInterfaceWithGenericsInInterfaceName<Result<Long>> genericsInterface = new GenericsInterfaceImplWithGenericsInInterfaceName<Result<Long>>();
        Result<Long> result1 = new Result<Long>();
        result1.setResult(1l);
        Result<Long> result1_ = genericsInterface.fetchResultWithGenericsDeclaration(result1);
        System.out.println(result1_.getResult());

        GenericsInterfaceWithGenericsInInterfaceName genericsInterfaceWithGenericsInInterfaceName = new GenericsInterfaceImplWithGenericsInInterfaceName();
        GenericsInterfaceWithOutGenericsInInterfaceName genericsInterfaceWithOutGenericsInInterfaceName = new GenericsInterfaceImplWithOutGenericsInInterfaceName();
//        下面两行报编译错误
//        Result<Long> result2_ = genericsInterfaceWithGenericsInInterfaceName.fetchResultWithGenericsDeclaration(new Result<Long>());
//        Result<Long> result2__ = genericsInterfaceWithGenericsInInterfaceName.<Result<Long>>fetchResultWithGenericsDeclaration(new Result<Long>());
        Object object2 = genericsInterfaceWithGenericsInInterfaceName.fetchResultWithGenericsDeclaration(new Result<Long>());

        Result<Long> result3 = new Result<Long>();
        result3.setResult(3l);
//        下面一句的使用方法不正确，在泛型类中（即类名后面跟有'<T>'），似乎只有静态泛型方法才能够这么写！
//        Result<Long> result3_ = genericsInterfaceWithGenericsInInterfaceName.<Result<Long>>fetchResultWithGenericsDeclaration(result3);
        Result<Long> result3Static_ = GenericsInterfaceImplWithGenericsInInterfaceName.fetchResultWithGenericsDeclarationStatic(result3);
        System.out.println(result3Static_.getResult());

        Result<Long> result4 = new Result<Long>();
        result4.setResult(4l);
//        下面一句没有问题，GenericsInterfaceWithOutGenericsInInterfaceName 这个类不是泛型类
        Result<Long> result4_ = genericsInterfaceWithOutGenericsInInterfaceName.<Result<Long>>fetchResultWithGenericsDeclaration(result4);
        System.out.println(result4_.getResult());
    }

    /**
     * 总结 ? extends 和 the ? super 通配符的特征，我们可以得出以下结论：<br/>
     * 如果你想从一个数据类型里获取数据，使用 ? extends 通配符           <br/>
     * 如果你想把对象写入一个数据结构里，使用 ? super 通配符             <br/>
     * 如果你既想存，又想取，那就别用通配符。                            <br/>
     * 这就是Maurice Naftalin在他的《Java Generics and Collections》这本书中所说的存取原则，
     * 以及Joshua Bloch在他的《Effective Java》这本书中所说的PECS法则。
     * Bloch提醒说，这PECS是指”Producer Extends, Consumer Super”，这个更容易记忆和运用。
     */
    public static void testParentClassAndSubClassInList() {
        List list = new ArrayList();
        list.add("list string");
        System.out.println(list.get(0));

        Apple apple = new Apple();
        Fruit fruit = apple;

        List<Apple> appleList = new ArrayList<Apple>();
//        下面的赋值操作编译不会通过，因为fruitList不能只放Apple,而且在处理泛型的时候也不会类型转换成功，比如不是Apple的类型不能转换为Apple
//        List<Fruit> fruitList = appleList;
        List<Fruit> fruitList = new ArrayList<Fruit>();
        fruitList.add(new Apple());
        fruitList.add(new Banana());

//        为了解决一个fruitList只能放Apple的编译问题，可以在泛型定义上进行处理,使用带有通配符的扩展声明。
//        "<? extends Fruit>" 表示上限是Fruit,List中对象是Fruit或者Fruit的子类，但是无法确定具体是哪个类。
        List<? extends Fruit> listExtendsFruit = appleList;
        Fruit fruit_ = new Fruit();
//        但是下面操作会报编译错误，因为，给listExtendsFruit添加对象fruit_的时候，泛型不知道这个fruit_是Fruit还是Fruit的子对象，无法完成类型转换.
//        应该持有的对象是Fruit的子类，而且具体是哪一个子类还是个未知数，所以加入任何Fruit的子类都会有问题
//        通配符”extends“ 声明的对象无法添加任何对象。
//        listExtendsFruit.add(fruit_);
//        listExtendsFruit.add(new Apple());

//        "<? super Apple>" 表示List中持有的对象是Apple的父类，下限是Fruit，虽然无法确定具体是哪个父类，但是add Apple或Apple的子类是没有问题的
        List<? super Apple> listSuperApple = appleList;
        listSuperApple.add(new Apple());
        listSuperApple.add(new GreenApple());
//        编译报错，因为Fruit是Apple的父类，而且List中持有的对象是无法确定是哪个父类，所以添加Fruit就编译报错
//        listSuperApple.add(new Fruit());
        System.out.println("listSuperApple.size() ------> "+listSuperApple.size());

        List<Fruit> fruits = new ArrayList<Fruit>();
        listSuperApple = fruits;
        listSuperApple.add(new Apple());
//        同样的下面也编译不过去。
//        listSuperApple.add(new Fruit());
    }

    public static void testParentClassAndSubClassInArray(){
        Apple[] apples = new Apple[5];
//        下面的编译是可以通过的，因为这里不涉及泛型。所以编译没有问题，但在运行时会报异常ArrayStoreException
//        这也证明泛型在类型安全检查方面有很好的作用，它可以在编译阶段就发现类型的不匹配。
        Fruit[] fruits = apples;
        try{
            fruits[0] = new Banana();
        }catch (ArrayStoreException ase){
            System.out.println("fruits[0] = new Banana(); 这句话发生了ArrayStoreException异常");
        }

        fruits = new Fruit[5];
        fruits[0] = new Banana();
        System.out.println("fruits[0] = new Banana(); 赋值成功！");

        UsageUtils.splitLine("测试协变数组类型！");
        testComparable();
    }

    public static Comparable findMax (Comparable[] arr){
        int maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            Comparable comparable = arr[i];
            if(comparable.compareTo(arr[maxIndex])>0){
                maxIndex = i;
            }
        }
        return arr[maxIndex];
    }
//    测试数组的协变性，findMax传递的参数必须是类型兼容的
    public static void testComparable(){
        String[] strings=new String[]{"a","b","c"};
        Integer[] integers = new Integer[]{1,2,3};
        System.out.println(findMax(strings));
        System.out.println(findMax(integers));
    }
}
