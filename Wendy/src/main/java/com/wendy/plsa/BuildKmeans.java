package com.wendy.plsa;

import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacv.cpp.opencv_core;
import com.wendy.plsa.edu.Clusters;
import com.wendy.plsa.edu.KMeansClusterer;
import org.opencv.core.Core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvRNG;
import com.googlecode.javacv.cpp.opencv_core.CvTermCriteria;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pairwinter on 14-3-18.
 */
public class BuildKmeans {
    private final static String kmeans_result = "D:/workspace/dandan/result/plsa/sift/kmeans_result";
    private final static String norm_result = "D:/workspace/dandan/result/plsa/sift/norm_result";
    public static void main(String[] args) throws Exception{
        int k = 80;
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
//        opencv_core.cvSave(kmeans_result, clusters1.getClusterCenters());
//        opencv_core.CvFileStorage fileStorage = opencv_core.CvFileStorage.open(kmeans_result,null, opencv_core.CV_STORAGE_READ);
//        Pointer pointer = opencv_core.cvReadByName(fileStorage, null, "vocabulary", null);
//        CvMat wordList = new CvMat(pointer);
        CvMat wordList = clusters1.getClusterCenters();
        int wordNum = k;

        List<CvMat> words = new ArrayList<CvMat>();
        int rows = wordList.rows();
        int cols = wordList.cols();
        for(int i=0;i<rows;i++){
            CvMat word = CvMat.create(1,cols,opencv_core.CV_32FC1);
            for(int j=0;j<cols;j++){
                word.put(0,j,wordList.get(i,j));
            }
            words.add(word);
        }

        List<List<KDFeaturePoint>> featuresList = BuildSift.parseAllImagesByList();
        FileWriter fw =  new FileWriter(norm_result,false);
        for (List<KDFeaturePoint> featurePoints : featuresList) {
            int wordPosition = 0;
            int[] wordTimes = new int[wordNum];
            //初始化词频数组
            for(int i = 0; i < wordNum; i++){
                wordTimes[i] = 0;
            }
            for (KDFeaturePoint feature : featurePoints) {
                CvMat featureMat =  opencv_core.CvMat.create(1,128,opencv_core.CV_32FC1);
//                opencv_core.cvSet(featureMat,new double[]);
                for(int i=0;i<feature.descriptor.length;i++){
                    featureMat.data_fl().position(i).put(feature.descriptor[i]);
                }
                double minDist = -1;
                for(int j=0;j<wordNum;j++){
                    double currentDist = opencv_core.cvNorm(featureMat,words.get(j),opencv_core.NORM_L2);
                    if(j==0){
                        minDist = currentDist;
                        wordPosition = j;
                    }else{
                        if(currentDist<=minDist){
                            minDist = currentDist;
                            wordPosition = j;
                        }
                    }
                }
                wordTimes[wordPosition]++;
            }
            StringBuffer result = new StringBuffer();
            int validWordNum =0;
            for(int i=0;i<wordTimes.length;i++){
                if(wordTimes[i]>0){
                    validWordNum++;
                    result.append(i+":"+wordTimes[i]);
                }
                if(i == wordTimes.length-1){
                    result.append("\n");
                }else{
                    result.append(" ");
                }
            }
            result.insert(0,validWordNum+"   ");
            fw.write(result.toString());
        }
        fw.close();
    }
}
