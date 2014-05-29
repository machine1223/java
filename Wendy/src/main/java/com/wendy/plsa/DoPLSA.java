package com.wendy.plsa;

import edu.thu.mltool4j.topicmodel.plsa.ProbabilisticLSA;

/**
 * Created by pairwinter on 14-5-29.
 */
public class DoPLSA {
    public static void main(String[] args) {
         String norm_result = "D:/workspace/dandan/result/plsa/sift/norm_result";
        ProbabilisticLSA probabilisticLSA = new ProbabilisticLSA();
        probabilisticLSA.doPLSA(norm_result,5,3);
    }
}
