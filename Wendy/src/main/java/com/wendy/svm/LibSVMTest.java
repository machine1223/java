package com.wendy.svm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibSVMTest {

    public static List<Result> results = new ArrayList<Result>();

	/**JAVA test code for LibSVM
	 * @author yangliu
	 * @throws java.io.IOException
	 * @blog http://blog.csdn.net/yangliuy
	 * @mail yang.liu@pku.edu.cn
	 */
	public static void main(String[] args) throws Exception {
//        fun_c();
//        gamma_c();

        System.out.println(LibSVMTest.class.getResource("/"));
//        run3();

    }

    public static void fun_c() throws Exception{
        List<String> funs = new ArrayList<String>();
        List<String> cs = new ArrayList<String>();
        for(int i=0;i<4;i++){
            funs.add(i+"");
        }
        for(int i=2;i<=10;i++){
//            cs.add((Math.pow(100,i))+"");
//            cs.add((Math.pow(10,i))+"");
            cs.add(((i*10))+"");
        }
        for (String c : cs) {
            for (String f : funs) {
                LibSVMTest.results.add(run(f, c));
            }
        }
        System.out.println("[");
        for(Result result : LibSVMTest.results){
            System.out.print("["+result.getC()+","+result.getFunName()+","+result.getAccuracy()+"],");
        }
        System.out.print("]");
    }

    public static Result run(String fun,String C) throws IOException {
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
        return new Result(fun,C,accuracy+"");
    }


    public static void gamma_c() throws Exception{
        List<String> gammas = new ArrayList<String>();
        List<String> cs = new ArrayList<String>();
        for(int i=-8;i<0;i++){
            gammas.add((Math.pow(2,i)) + "");
        }
        for(int i=70;i<71;i++){
            cs.add((i)+"");
//            cs.add((Math.pow(10,i))+"");
//            cs.add((Math.pow(2,i))+"");
        }
        for (String c : cs) {
            for (String f : gammas) {
                LibSVMTest.results.add(run2(f, c));
            }
        }
        System.out.println("[");
        for(Result result : LibSVMTest.results){
            System.out.print("["+result.getC()+","+result.getFunName()+","+result.getAccuracy()+"],");
        }
        System.out.print("]");
    }

    public static Result run2(String gamma,String C) throws IOException {
        String[] trainArgs = {"-g",gamma,"-t","1","-c",C,"D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-tra"};//directory of training file
        String modelFile = SvmTrain.main(trainArgs);
        String[] testArgs = {"D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-test", modelFile, "D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-result"};//directory of test file, model file, result file
        Double accuracy = SvmPredict.main(testArgs);
        System.out.println("SVM Classification is done! The accuracy is " + accuracy);

        //Test for cross validation
        String path3 = "D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-tra";
        String[] crossValidationTrainArgs = {"-v", "10",path3 };// 10 fold cross validation
        modelFile = SvmTrain.main(crossValidationTrainArgs);
        System.out.print("Cross validation is done! The modelFile is " + modelFile);
        return new Result(gamma,C,accuracy+"");
    }

    public static void run3() throws IOException {
        String[] trainArgs = {"D:\\workspace\\dandan\\data_file\\UCI-breast-cancer-tra"};//directory of training file
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
