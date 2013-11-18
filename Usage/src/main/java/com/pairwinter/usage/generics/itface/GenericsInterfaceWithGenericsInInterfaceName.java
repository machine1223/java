package com.pairwinter.usage.generics.itface;

import com.pairwinter.usage.generics.Result;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-10-28
 * Time: 上午11:31
 * To change this template use File | Settings | File Templates.
 */
public interface GenericsInterfaceWithGenericsInInterfaceName<T> {
    public Result fetchResult();

    /**
     * 泛型方法,在返回类型前面添加 <T>
     * 其中 public <T> 中的 <T> 声明当前方法持有一个类型T，也可以理解为此方法为泛型方法
     * 这种定义方式不会放在泛型接口中，而是定义到非泛型接口、非泛型类或者某个类的静态泛型方法上，即静态泛型方法。
     * @param <T1>
     * @return
     */
    public <T1> T1 fetchResultWithGenericsDeclaration(T1 t);

    /**
     * 泛型方法，返回类型前未添加 <T>
     * @return
     */
    public T fetchResultWithOutGenericsDeclaration(T t);
}
