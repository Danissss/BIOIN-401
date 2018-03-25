package xuan.biotech;


/* weka has two options of reading file: database(MySQL) and arff file or
*  csv file (need to take one more step to convert csv to arff file)
*
*/
//java lib
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
//weka lib
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.CSVLoader;
import weka.core.converters.ArffSaver;

//mysql lib
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class WekaBuildModel {

	
	/*
	 * @input: path to that csv file
	 * @function: simply convert csv file to arff file
	 * @reference: https://weka.wikispaces.com/Converting+CSV+to+ARFF
	 * @Usage: CSV2Arff <input.csv> <output.arff>
	 */
	public static String convert_csv_to_arff(String path_to_file) {
		
		
		String new_file_path = null;
		try {
			// load CSV
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(path_to_file));
			Instances data = loader.getDataSet();
		
	
	    
			//configure the path
			String[] temp         = path_to_file.split("/");
			Integer temp_length   = temp.length;
			String temp_file_name = temp[temp_length-1];
			new_file_path  = path_to_file.replace(temp_file_name, "csv_convert_arff.arff");
	 
			// save ARFF
			ArffSaver saver = new ArffSaver();
			saver.setInstances(data);
			saver.setFile(new File(new_file_path));
			saver.setDestination(new File(new_file_path));
			saver.writeBatch();
			
			
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	    return new_file_path;
		
	}
	
	/*
	 * @input: the file path of training data set
	 *              
	 * @note: build all classifier models, select the best algorithm based on the determines
	 */
	public static void Training_model(String input_file_path) throws Exception{
		BufferedReader buffer_reader = null;
		buffer_reader = new BufferedReader(new FileReader(input_file_path));
		Instances training = new Instances(buffer_reader);
		training.setClassIndex(training.numAttributes() -1); //training.numAttributes() -1: this 1 is the attribute
															//that we are going to predict
		buffer_reader.close();
		
		//below is an example of training data
		J48 test_J48 = new J48();
		test_J48.buildClassifier(training);
		Evaluation eval_test_J48 = new Evaluation(training);
		//evaluation
		eval_test_J48.crossValidateModel(test_J48, training, 10, new Random(1));
		String test_J48_summary = eval_test_J48.toSummaryString("Summary of test_J48", true);
		String test_J48_summary2 = eval_test_J48.fMeasure(1) + " " + eval_test_J48.precision(1) + " " + eval_test_J48.recall(1);
		
		//save the model
		weka.core.SerializationHelper.write("test_J48.model", test_J48);
		
		
	}
	
	
	
	/*
	 * @input: e.g. javac WekaBuildModel.java arff /path/to/arff
	 * 				javac WekaBuildModel.java csv /path/to/csv
	 *              javac WekaBUildModel.java database hostname user password databasename
	 *              
	 * @note:The DataSource class is not limited to ARFF files. It can also read CSV files and other formats 
	 *           (basically all file formats that Weka can import via its converters).
	 */
	public static void main(String[] args) throws Exception{
		
		if (args.length < 2) {
			System.out.println("Missing Input (see READ.md)");
			System.exit(0);
		}else {
			
			String fileType = args[0].toLowerCase();
		
			if (fileType.equals("database" )) {        // database 
				if (args.length < 4) {					 
					System.out.println("Missing database parameters (see READ.md)");
					System.exit(0);
				}else {
					// database setting is still need to be configured
					// 
					final String host     = args[1];
					final String username = args[2];
					final String password = args[3];
					final String database = args[4];
					final String MysqlURL = "jdbc:mysql://"+ host +":3306/"+ database;
					
					InstanceQuery query = new InstanceQuery();
					query.setUsername(username);
					query.setPassword(password);
					query.setQuery("select * from trainning_table");
					query.setDatabaseURL(MysqlURL);
					// You can declare that your data set is sparse
					// query.setSparseData(true);
					Instances data = query.retrieveInstances();
					
					
				}
				
			}else if (fileType.equals("csv")) {
				
				String path_to_arff = args[1];
				
				//this check if the input is csv: yes: convert, change the path; no: skip this step
				String new_path = convert_csv_to_arff(path_to_arff);
				path_to_arff = new_path;
				//pass the new filepath to method Training_model.
				Training_model(path_to_arff);
				
			
				
			}else if (fileType.equals("arff")) {
				
				Training_model(args[1]);
				
				
				
				
			}else {
				System.out.println("wrong input (see readme)");
				System.exit(0);
			}
			
			
		}
		
	}
}