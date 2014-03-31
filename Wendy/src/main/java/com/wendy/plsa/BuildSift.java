package com.wendy.plsa;

import com.alibaba.simpleimage.analyze.sift.SIFT;
import com.alibaba.simpleimage.analyze.sift.io.KDFeaturePointListInfo;
import com.alibaba.simpleimage.analyze.sift.io.KDFeaturePointWriter;
<<<<<<< HEAD
import com.alibaba.simpleimage.analyze.sift.match.Match;
import com.alibaba.simpleimage.analyze.sift.match.MatchKeys;
import com.alibaba.simpleimage.analyze.sift.render.RenderImage;
import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;
import org.apache.commons.lang.StringUtils;
=======
import com.alibaba.simpleimage.analyze.sift.render.RenderImage;
import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;
>>>>>>> a4e59c9fccd17b90c54d82074778bb341bad3d7a
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
<<<<<<< HEAD
import java.io.*;
import java.util.ArrayList;
=======
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
>>>>>>> a4e59c9fccd17b90c54d82074778bb341bad3d7a
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
<<<<<<< HEAD
    public final static String resources = BuildSift.class.getResource("/").getPath();
    public final static String filename = "D:/workspace/dandan/result/plsa/sift/sift_result";
    public static void main(String[] args) throws Exception{
        File f = new File(filename);
        FileWriter fw =  new FileWriter(f);
        fw.write("");
        fw.close();
//        BuildSift.matchTwoImage();
        parseAllImages();
    }

    public static List<KDFeaturePoint> parseAllImages() throws Exception{
        File imagesDirectory = new File(resources+File.separator+"/plsa/images");
        File[] images = imagesDirectory.listFiles();
        List<KDFeaturePoint> all = new ArrayList<KDFeaturePoint>();
        for (File image : images) {
            List<KDFeaturePoint> testKD = BuildSift.buildKDFeaturePoints(image);
            all.addAll(testKD);
        }
        return all;
    }

    public static void parseAllImagesAndPrint() throws Exception{
        File imagesDirectory = new File(resources+File.separator+"/plsa/images");
        File[] images = imagesDirectory.listFiles();
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        for (File image : images) {
            List<KDFeaturePoint> testKD = BuildSift.buildKDFeaturePoints(image);
            String firstLine = testKD.size()+" 128";
            writer.println(firstLine);
            for (KDFeaturePoint kdFeaturePoint : testKD) {
                String secondLine = kdFeaturePoint.x+" "+kdFeaturePoint.y+" "+kdFeaturePoint.scale+kdFeaturePoint.orientation;
                writer.println(secondLine);
                List<String> ds = new ArrayList<String>();
                for(int i : kdFeaturePoint.descriptor){
                    ds.add(i+"");
                }
                writer.println(StringUtils.join(ds," "));
            }
        }
        writer.close();
    }

    public static List<KDFeaturePoint> buildKDFeaturePoints(File image) throws Exception{
        BufferedImage img = ImageIO.read(image);
        RenderImage ri = new RenderImage(img);
        SIFT sift = new SIFT();
        sift.detectFeatures(ri.toPixelFloatArray(null));
        return sift.getGlobalKDFeaturePoints();
    }
    public static void matchTwoImage() throws  Exception{
        File imagesDirectory = new File(resources+File.separator+"/plsa/images");
        File[] images = imagesDirectory.listFiles();
        List<KDFeaturePoint> image1 = BuildSift.buildKDFeaturePoints(images[0]);
        List<KDFeaturePoint> image2 = BuildSift.buildKDFeaturePoints(images[0]);

        List<Match> ms = MatchKeys.findMatchesBBF(image1,image2);

        System.out.println(ms.size());
    }

    public static void writeResultToFile()  throws Exception{
        File f = new File(filename);
        FileWriter fw =  new FileWriter(f);
        fw.write("");
        fw.close();
=======
    public static void main(String[] args) throws Exception{
        String resources = BuildSift.class.getResource("/").getPath();
//        String filename = resources+File.separator+"/plsa/sift_result";
        String filename = "/home/damon/tmp/sift_result";
>>>>>>> a4e59c9fccd17b90c54d82074778bb341bad3d7a
        ObjectOutputStream out = null;
        try {
            File imagesDirectory = new File(resources+File.separator+"/plsa/images");
            File[] images = imagesDirectory.listFiles();
            for (File image : images) {
<<<<<<< HEAD
                out = new ObjectOutputStream(new FileOutputStream(filename,true));
                KDFeaturePointWriter.writeComplete(out,BuildSift.parseImg(image));
            }
        } catch (Exception e) {
            e.printStackTrace();
=======
                out = new ObjectOutputStream(new FileOutputStream(filename));
                KDFeaturePointWriter.writeComplete(out,BuildSift.parseImg(image));
            }
        } catch (Exception e) {
>>>>>>> a4e59c9fccd17b90c54d82074778bb341bad3d7a
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
