package com.wendy.plsa;

import com.alibaba.simpleimage.analyze.sift.SIFT;
import com.alibaba.simpleimage.analyze.sift.io.KDFeaturePointListInfo;
import com.alibaba.simpleimage.analyze.sift.io.KDFeaturePointWriter;
import com.alibaba.simpleimage.analyze.sift.match.Match;
import com.alibaba.simpleimage.analyze.sift.match.MatchKeys;
import com.alibaba.simpleimage.analyze.sift.render.RenderImage;
import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
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
    public final static String resources = BuildSift.class.getResource("/").getPath();
    public final static String filename = "D:/workspace/dandan/result/plsa/sift/sift_result";
    public static void main(String[] args) throws Exception{
//        File f = new File(filename);
//        FileWriter fw =  new FileWriter(f);
//        fw.write("");
//        fw.close();
//        BuildSift.matchTwoImage();
//        parseAllImages();
        parseAllImagesAndPrint();
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
    public static List<List<KDFeaturePoint>> parseAllImagesByList() throws Exception{
        File imagesDirectory = new File(resources+File.separator+"/plsa/images");
        File[] images = imagesDirectory.listFiles();
        List<List<KDFeaturePoint>> all = new ArrayList<List<KDFeaturePoint>>();
        for (File image : images) {
            List<KDFeaturePoint> testKD = BuildSift.buildKDFeaturePoints(image);
            all.add(testKD);
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
        ObjectOutputStream out = null;
        try {
            File imagesDirectory = new File(resources+File.separator+"/plsa/images");
            File[] images = imagesDirectory.listFiles();
            for (File image : images) {
                out = new ObjectOutputStream(new FileOutputStream(filename,true));
                KDFeaturePointWriter.writeComplete(out,BuildSift.parseImg(image));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
