package com.pairwinter.usage.generics;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 13-10-26
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public class Result<T> {
    private T result;
    public <t> T getResult(){
        return this.result;
    }
    public <t> void setResult(T result){
        this.result = result;
    }
}
