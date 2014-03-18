package com.wendy.plsa;

import com.alibaba.simpleimage.analyze.sift.SIFT;
import com.alibaba.simpleimage.analyze.sift.io.KDFeaturePointListInfo;
import com.alibaba.simpleimage.analyze.sift.io.KDFeaturePointWriter;
import com.alibaba.simpleimage.analyze.sift.render.RenderImage;
import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 3/17/14
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildSift {
    private final static Logger logger = Logger.getLogger(BuildSift.class);
    public static void main(String[] args) throws Exception{
        String resources = BuildSift.class.getResource("/").getPath();
//        String filename = resources+File.separator+"/plsa/sift_result";
        String filename = "/home/damon/tmp/sift_result";
        ObjectOutputStream out = null;
        try {
            File imagesDirectory = new File(resources+File.separator+"/plsa/images");
            File[] images = imagesDirectory.listFiles();
            for (File image : images) {
                out = new ObjectOutputStream(new FileOutputStream(filename));
                KDFeaturePointWriter.writeComplete(out,BuildSift.parseImg(image));
            }
        } catch (Exception e) {
            logger.equals(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.equals(e.getMessage());
                }
            }
        }
    }

    public static KDFeaturePointListInfo parseImg(File image) throws Exception{
        KDFeaturePointListInfo kdFeaturePointListInfo = new KDFeaturePointListInfo();
        BufferedImage img = ImageIO.read(image);
        RenderImage ri = new RenderImage(img);
        SIFT sift = new SIFT();
        sift.detectFeatures(ri.toPixelFloatArray(null));
        List<KDFeaturePoint> al = sift.getGlobalKDFeaturePoints();
        kdFeaturePointListInfo.setHeight(ri.getHeight());
        kdFeaturePointListInfo.setWidth(ri.getWidth());
        kdFeaturePointListInfo.setImageFile(image.getPath());
        kdFeaturePointListInfo.setList(al);
        return kdFeaturePointListInfo;
    }
}
