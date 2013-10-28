package com.pairwinter.usage.generics.itfaceimpl;

import com.pairwinter.usage.generics.Result;
import com.pairwinter.usage.generics.itface.GenericsInterfaceWithGenericsInInterfaceName;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-10-28
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class GenericsInterfaceImplWithGenericsInInterfaceName<T> implements GenericsInterfaceWithGenericsInInterfaceName<T> {
    @Override
    public Result fetchResult() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T1> T1 fetchResultWithGenericsDeclaration(T1 t) {
        return t;
    }

    @Override
    public <T1> T1 fetchResultWithGenericsDeclaration(Class<T1> t) throws Exception{
        T1 tInstance =  t.newInstance();
        return tInstance;
    }
    @Override
    public T fetchResultWithOutGenericsDeclaration(T t) {
        return t;
    }
}
