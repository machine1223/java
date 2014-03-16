package com.pairwinter.wendy.plsa;

import com.pairwinter.wendy.plsa.edu.thu.mltool4j.topicmodel.plsa.ProbabilisticLSA;

/**
 * Created with IntelliJ IDEA.
 * User: pairwinter
 * Date: 14-3-11
 * Time: 下午8:00
 * To change this template use File | Settings | File Templates.
 */
public class PLSA {
    public static void main(String[] args) {
        ProbabilisticLSA plsa = new ProbabilisticLSA();
        //the file is not used, the hard coded data is used instead, but file name should be valid,
        //just replace the name by something valid.
        plsa.doPLSA("D:\\workspace\\dandan\\plsa_data\\afterplsa.arff", 2, 60);
        System.out.println("end PLSA");
    }
}
