package xuan.biotech;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.Arrays;

import weka.classifiers.trees.RandomForest;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;


/*
 * 
 */

public class MakePrediction {
	
//	this is constructor
//	public MakePrediction(String Something){
//		String Sth = Something;
//		
//	}
	
	
	public static String makePrediction_SMO(String Path_to_model, String predictedFilePath) throws Exception{
	
		SMO smo = (SMO) SerializationHelper.read(new FileInputStream(Path_to_model));
		DataSource predictedDataSource = new DataSource(predictedFilePath);
		Instances predict = predictedDataSource.getDataSet();
		predict.setClassIndex(predict.numAttributes() - 1);
		
		for(int i = 0; i < predict.numInstances(); i++){
			
	        Instance inst = predict.instance(i);
	        //System.out.println(inst.toString());
	        double result = smo.classifyInstance(inst);
	        
	        String prediction = predict.classAttribute().value((int)result);
	        System.out.println(prediction);
	        //System.out.println(train.classAttribute().value((int)r));
	    }
		
		return "Ok";
	
	}
	
	
	public static String makePrediction_J84(String Path_to_model, String predictedFilePath) throws Exception{
		
		
		J48 j48 = (J48) SerializationHelper.read(new FileInputStream(Path_to_model));
		
		DataSource predictedDataSource = new DataSource(predictedFilePath);
		Instances predict = predictedDataSource.getDataSet();
		predict.setClassIndex(predict.numAttributes() - 1);
//		System.out.println(predict.toString());
//		System.exit(0);
		
		//make prediction
		//System.out.println(predict.numInstances());
		for(int i = 0; i < predict.numInstances(); i++){
		
	        Instance inst = predict.instance(i);
	        //System.out.println(inst.toString());
	        double result = j48.classifyInstance(inst);
	        
	        String prediction = predict.classAttribute().value((int)result);
	        System.out.println(prediction);
	        //System.out.println(train.classAttribute().value((int)r));
	    }
		
		
		return "ok";
		
		
	}
    public  String makePrediction_Random_Forest_GUI(String ModelPath, String predictedFilePath, String TransporterName) throws Exception{
	
		
//    		System.out.println(ModelPath);
//    		System.out.println(predictedFilePath);
//    		System.out.println(TransporterName);
			RandomForest RandomForest = (RandomForest) SerializationHelper.read(new FileInputStream(ModelPath));
			
			DataSource predictedDataSource = new DataSource(predictedFilePath);
			Instances predict = predictedDataSource.getDataSet();
			
			predict.setClassIndex(predict.numAttributes() - 1);
			
			Instance inst = predict.instance(0);
			
			
			//inst = 9,5,0,3,19,0,11,-2.7457,7.538868,65.6159,-4.23,1.46,39.947481,197.62,307.083806,25.356519,0.831149,-0.338743,-0.11123,0.252759,-0.35329,31.854508,23.493247,31.481472,33.401626,31.993195,2,1,?,?,?,?,?,?,?,?,?
			//can only have one ? mark at the end

	        double result = RandomForest.classifyInstance(inst);
	        
	        String results = predict.classAttribute().value((int)result);
			
	        results += " ";
			results += TransporterName;
			System.out.println(results);
		return results;
		
	}
    
    
    
    
    public String[] makePrediction_Random_Forest_GUILargeScale(String ModelPath, String predictedFilePath, String TransporterName) throws Exception{
		RandomForest RandomForest = (RandomForest) SerializationHelper.read(new FileInputStream(ModelPath));
		
		Instances unlabeled = new Instances(new BufferedReader(new FileReader(predictedFilePath)));
		unlabeled.setClassIndex(unlabeled.numAttributes()-1);
		Instances labeled = new Instances(unlabeled);
		String[] all_result = new String[unlabeled.numInstances()];
		for (int i = 0; i <unlabeled.numInstances(); i++) {
			double clsLabel = RandomForest.classifyInstance(unlabeled.instance(i));
			String results = unlabeled.classAttribute().value((int)clsLabel);
			all_result[i] = results;

		}
		
	return all_result;
}
	
	
	public static String makePrediction_Random_Forest (String Path_to_model, String predictedFilePath) throws Exception{
	
		
		if(!(predictedFilePath.contains(".arff")) || !(Path_to_model.contains(".model"))) {
			System.out.println("Check the input file.");
			System.exit(0);
		}
		else {
			RandomForest RandomForest = (RandomForest) SerializationHelper.read(new FileInputStream(Path_to_model));
			
			DataSource predictedDataSource = new DataSource(predictedFilePath);
			Instances predict = predictedDataSource.getDataSet();
			predict.setClassIndex(predict.numAttributes() - 1);
//			System.out.println(predict.toString());
//			System.exit(0);
			
			//make prediction
			//System.out.println(predict.numInstances());
			for(int i = 0; i < predict.numInstances(); i++){
			
		        Instance inst = predict.instance(i);
		        //System.out.println(inst.toString());
		        double result = RandomForest.classifyInstance(inst);
		        //System.out.println(result);
		        System.out.println(predict.classAttribute().value((int)result));
		    }

		}

		return predictedFilePath;
			
	}
	
	
	public static void main(String[] args) throws Exception{
		
		String PredictionFilePath = args[1];
		String Path_to_model = args[0];
		
		//makePrediction_Random_Forest(Path_to_model,PredictionFilePath);
		makePrediction_J84(Path_to_model,PredictionFilePath);
		//makePrediction_SMO(Path_to_model,PredictionFilePath);
		
		
		
		
		
	}
}