package com.wendy.plsa;

import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;
import com.googlecode.javacv.cpp.opencv_core;
import com.wendy.plsa.edu.Clusters;
import com.wendy.plsa.edu.KMeansClusterer;
import org.opencv.core.Core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvRNG;
import com.googlecode.javacv.cpp.opencv_core.CvTermCriteria;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pairwinter on 14-3-18.
 */
public class BuildKmeans {
    private final static String kmeans_result = "D:/workspace/dandan/result/plsa/sift/kmeans_result";
    public static void main(String[] args) throws Exception{
        int k = 2;
        List<KDFeaturePoint> features = BuildSift.parseAllImages();
        opencv_core.CvMat samples = opencv_core.CvMat.create(features.size(),128,opencv_core.CV_32FC1);
        int index = 0;
        for (KDFeaturePoint feature : features) {

            for(int i=0;i<feature.descriptor.length;i++){
                samples.data_fl().position(index).put(feature.descriptor[i]);
            }
            index ++;
        }
        KMeansClusterer kMeansClusterer = new KMeansClusterer(k,1.0,10,3);
        Clusters clusters1 = kMeansClusterer.cluster(samples);
        opencv_core.cvSave(kmeans_result, clusters1.getClusterCenters());
    } 
}
