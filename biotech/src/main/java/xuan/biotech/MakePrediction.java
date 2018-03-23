package xuan.biotech;


import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

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
			//double label = RandomForest.classifyInstance(predict.instance(10));
			//predict.instance(0).setClassValue(label);
			
			//System.out.println(predict.instance(0).stringValue(4));
			
			
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
