package com.wendy.svm;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 14-3-12
 * Time: 下午10:13
 * To change this template use File | Settings | File Templates.
 */
public class Result {
    private String funName;
    private String C;
    private String accuracy;
    public Result(String funName,String C,String accuracy){
          this.funName = funName;
        this.C = C;
        this.accuracy = accuracy;
    }

    public String getFunName() {
        return funName;
    }

    public void setFunName(String funName) {
        this.funName = funName;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }
}
