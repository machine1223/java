package com.wendy.plsa;

import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;
import com.googlecode.javacv.cpp.opencv_core;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pairwinter on 14-3-18.
 */
public class BuildKmeans {
    private final static String kmeans_result = "D:/workspace/dandan/result/plsa/sift/kmeans_result";
    public static void main(String[] args) throws Exception{
        int k = 10;
        List<KDFeaturePoint> features = BuildSift.parseAllImages();
        opencv_core.CvMat samples = opencv_core.CvMat.create(features.size(),128,opencv_core.CV_32FC1);
        opencv_core.CvMat clusters = opencv_core.CvMat.create(features.size(),1,opencv_core.CV_32SC1);
        opencv_core.CvMat centers = opencv_core.CvMat.create(k,128,opencv_core.CV_32FC1);
        opencv_core.cvSetZero(clusters);
        opencv_core.cvSetZero(centers);
        for (KDFeaturePoint feature : features) {
            samples.data_i().put(feature.descriptor);
        }
        double[] doubles = new double[]{};
        opencv_core.cvKMeans2(samples, 10, clusters, opencv_core.cvTermCriteria(opencv_core.CV_TERMCRIT_EPS, 10, 1.0),
                3,null, Core.KMEANS_USE_INITIAL_LABELS,centers,doubles);
        opencv_core.cvSave(kmeans_result, clusters);
    } 
}
