/*
 * Copyright (c) 2011-2013 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv2_cookbook.chapter06

import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_highgui._
import com.googlecode.javacv.cpp.opencv_imgproc._
import java.io.File
import opencv2_cookbook.OpenCVUtils._

/**
 * The example for section "Filtering images using a median filter" in Chapter 6, page 147.
 */
object Ex2MedianFilter extends App {

    // Read input image with a salt noise
    val src = loadAndShowOrExit(new File("data/boldt_salt.jpg"), CV_LOAD_IMAGE_GRAYSCALE)

    // Remove noise with a median filter
    val dest = IplImage.create(cvGetSize(src), src.depth, 1)
    val kernelSize = 3
    medianBlur(src, dest, kernelSize)
    show(dest, "Median filtered")

    // Since median filter really cleans up outlier with values above (salt) and below (pepper),
    // in this case, we can reconstruct dark pixels that are most likely not effected by the noise.
    val dest2 = IplImage.create(cvGetSize(src), src.depth, 1)
    cvMin(src, dest, dest2)
    show(dest2, "Median filtered + dark pixel recovery")
}
