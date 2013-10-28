package com.pairwinter.usage.generics.itfaceimpl;

import com.pairwinter.usage.generics.Result;
import com.pairwinter.usage.generics.itface.GenericsInterfaceWithGenericsInInterfaceName;
import com.pairwinter.usage.generics.itface.GenericsInterfaceWithOutGenericsInInterfaceName;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-10-28
 * Time: 上午11:34
 * To change this template use File | Settings | File Templates.
 */
public class GenericsInterfaceImplWithOutGenericsInInterfaceName implements GenericsInterfaceWithOutGenericsInInterfaceName {
    @Override
    public Result fetchResult() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T fetchResultWithGenericsDeclaration(T t) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
