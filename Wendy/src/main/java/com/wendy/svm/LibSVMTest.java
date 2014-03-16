package com.wendy.svm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibSVMTest {

    public static final String  UCI_breast_cancer_tra;
    public static final String  UCI_breast_cancer_test;
    public static final String  UCI_breast_cancer_result;
    public static final String  UCI_breast_cancer_tra_model;

    public static List<Result> results = new ArrayList<Result>();

    static {
        String resources = LibSVMTest.class.getResource("/").getPath();
        UCI_breast_cancer_tra = resources+File.separator+"/svm_files/UCI-breast-cancer-tra";
        UCI_breast_cancer_test = resources+File.separator+"/svm_files/UCI-breast-cancer-test";
        UCI_breast_cancer_result = resources+File.separator+"/svm_files/UCI-breast-cancer-result";
        UCI_breast_cancer_tra_model = resources+File.separator+"/svm_files/UCI-breast-cancer-tra.model";
    }

	/**JAVA test code for LibSVM
	 * @author yangliu
	 * @throws java.io.IOException
	 * @blog http://blog.csdn.net/yangliuy
	 * @mail yang.liu@pku.edu.cn
	 */
	public static void main(String[] args) throws Exception {
//        fun_c();
        gamma_c();
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
        String[] trainArgs = {"-t",fun,"-c",C,LibSVMTest.UCI_breast_cancer_tra};//directory of training file
        String modelFile = SvmTrain.main(trainArgs);
        String[] testArgs = {LibSVMTest.UCI_breast_cancer_test, modelFile, LibSVMTest.UCI_breast_cancer_result};//directory of test file, model file, result file
        Double accuracy = SvmPredict.main(testArgs);
        System.out.println("SVM Classification is done! The accuracy is " + accuracy);

        //Test for cross validation
        String[] crossValidationTrainArgs = {"-v", "10",LibSVMTest.UCI_breast_cancer_tra };// 10 fold cross validation
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
        System.out.print("\n[");
        for(Result result : LibSVMTest.results){
            System.out.print("["+result.getC()+","+result.getFunName()+","+result.getAccuracy()+"],");
        }
        System.out.print("]");
    }

    public static Result run2(String gamma,String C) throws IOException {
        String[] trainArgs = {"-g",gamma,"-t","1","-c",C,LibSVMTest.UCI_breast_cancer_tra};//directory of training file
        String modelFile = SvmTrain.main(trainArgs);
        String[] testArgs = {LibSVMTest.UCI_breast_cancer_test, modelFile, LibSVMTest.UCI_breast_cancer_result};//directory of test file, model file, result file
        Double accuracy = SvmPredict.main(testArgs);
        System.out.println("SVM Classification is done! The accuracy is " + accuracy);

        //Test for cross validation
        String[] crossValidationTrainArgs = {"-v", "10",LibSVMTest.UCI_breast_cancer_tra };// 10 fold cross validation
        modelFile = SvmTrain.main(crossValidationTrainArgs);
        System.out.print("Cross validation is done! The modelFile is " + modelFile);
        return new Result(gamma,C,accuracy+"");
    }

}
