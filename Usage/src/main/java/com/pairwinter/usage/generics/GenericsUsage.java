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
        Object result2 = genericsInterfaceWithGenericsInInterfaceName.fetchResultWithGenericsDeclaration(new Result<Long>());

        Result<Long> result3 = null;
        try{
            result3 = genericsInterfaceWithGenericsInInterfaceName.<Result<Long>>fetchResultWithGenericsDeclaration(new Result<Long>());
        }catch(Exception e){
            e.printStackTrace();
        }

        Result<Long> result4 = genericsInterfaceWithOutGenericsInInterfaceName.<Result<Long>>fetchResultWithGenericsDeclaration(new Result<Long>());

    }
}
