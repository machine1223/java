/*
 * Copyright (c) 2011-2013 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv2_cookbook.chapter08

import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_features2d._
import com.googlecode.javacv.cpp.opencv_nonfree._
import java.io.File
import opencv2_cookbook.OpenCVUtils._


/**
 * Example of extracting SIFT features from section "Detecting the scale-invariant SURF features" in chapter 8.
 */
object Ex6SIFT extends App {

    // Read input image
//    val image = loadAndShowOrExit(new File("data/dandan/2.bmp"))
//    val image = loadAndShowOrExit(new File("data/dandan/23.jpg"))
    val image = loadAndShowOrExit(new File("data/dandan/12346.bmp"))

    // Detect SIFT features.
    val keyPoints = new KeyPoint()
    val nFeatures = 0
    val nOctaveLayers = 3
    val contrastThreshold = 0.03
    val edgeThreshold = 10
    val sigma = 1.6
    val sift = new SIFT(nFeatures, nOctaveLayers, contrastThreshold, edgeThreshold, sigma)
    sift.detect(image, null, keyPoints)


    val size =0
    println(keyPoints.capacity())

//  keyPoints.pt()
//cvKMeans2(null,10,null,null,1,null,1,null,null)
  //    val featureDetector =sift.getFeatureDetector()
//    featureDetector.detect(image,keyPoints,null)
//  println(featureDetector.)

    // Draw keyPoints
    val featureImage = IplImage.create(cvGetSize(image), image.depth(), 3)
    drawKeypoints(image, keyPoints, featureImage, CvScalar.WHITE, DrawMatchesFlags.DEFAULT)
//      drawKeypoints(image, keyPoints, featureImage, CvScalar.WHITE, DrawMatchesFlags.DRAW_OVER_OUTIMG)
//      drawKeypoints(image, keyPoints, featureImage, CvScalar.WHITE, DrawMatchesFlags.DRAW_RICH_KEYPOINTS)
//        drawKeypoints(image, keyPoints, featureImage, CvScalar.WHITE, DrawMatchesFlags.NOT_DRAW_SINGLE_POINTS)
//  featureImage.
  show(featureImage, "SIFT Features")
}