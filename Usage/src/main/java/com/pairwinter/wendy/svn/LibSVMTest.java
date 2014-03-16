package com.pairwinter.wendy.svn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibSVMTest {

	/**JAVA test code for LibSVM
	 * @author yangliu
	 * @throws java.io.IOException
	 * @blog http://blog.csdn.net/yangliuy
	 * @mail yang.liu@pku.edu.cn
	 */
	public static void main(String[] args) throws IOException {
        List<String> funs = new ArrayList<String>();
        List<String> cs = new ArrayList<String>();
        for(int i=0;i<4;i++){
            funs.add(i+"");
        }
        for(int i=1;i<=10;i++){
//            cs.add((Math.pow(100,i))+"");
            cs.add((Math.pow(100,i))+"");
        }
        for (String c : cs) {
            Result.accuracies.add(c+"\n");
            for (String f : funs) {
                Result.accuracies.add(f+"\n");
                run(f,c);
            }
        }
        for(String result : Result.accuracies){
            System.out.println(result);
        }
    }

    public static void run(String fun,String C) throws IOException {
        String[] trainArgs = {"-t",fun,"-c",C,"D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-tra"};//directory of training file
        String modelFile = SvmTrain.main(trainArgs);
        String[] testArgs = {"D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-test", modelFile, "D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-result"};//directory of test file, model file, result file
        Double accuracy = SvmPredict.main(testArgs);
        System.out.println("SVM Classification is done! The accuracy is " + accuracy);

        //Test for cross validation
        String path3 = "D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-tra";
        String[] crossValidationTrainArgs = {"-v", "10",path3 };// 10 fold cross validation
        modelFile = SvmTrain.main(crossValidationTrainArgs);
        System.out.print("Cross validation is done! The modelFile is " + modelFile);
    }

}
