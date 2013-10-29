package com.pairwinter.usage.generics;

import com.pairwinter.usage.generics.itface.GenericsInterfaceWithGenericsInInterfaceName;
import com.pairwinter.usage.generics.itface.GenericsInterfaceWithOutGenericsInInterfaceName;
import com.pairwinter.usage.generics.itfaceimpl.GenericsInterfaceImplWithGenericsInInterfaceName;
import com.pairwinter.usage.generics.itfaceimpl.GenericsInterfaceImplWithOutGenericsInInterfaceName;

import java.util.ArrayList;
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
        Result result = new Result();
        result.setResult("string");
        System.out.println(result.getResult());
        result = new Result();
        result.setResult(1);
        System.out.println(result.getResult());

        List list = new ArrayList();
        list.add("list string");
        System.out.println(list.get(0));

        GenericsInterfaceWithGenericsInInterfaceName<Result<Long>> genericsInterface = new GenericsInterfaceImplWithGenericsInInterfaceName<Result<Long>>();
        Result<Long> result1 = new Result<Long>();
        result1.setResult(1l);
        Result<Long> result1_ = genericsInterface.fetchResultWithGenericsDeclaration(result1);
        System.out.println(result1_.getResult());

        GenericsInterfaceWithGenericsInInterfaceName genericsInterfaceWithGenericsInInterfaceName = new GenericsInterfaceImplWithGenericsInInterfaceName();
        GenericsInterfaceWithOutGenericsInInterfaceName genericsInterfaceWithOutGenericsInInterfaceName = new GenericsInterfaceImplWithOutGenericsInInterfaceName();
//        如果 Result<Long> result2 就会报编译错误
//        Result<Long> result2 = genericsInterfaceWithGenericsInInterfaceName.fetchResultWithGenericsDeclaration(new Result<Long>());
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
}
