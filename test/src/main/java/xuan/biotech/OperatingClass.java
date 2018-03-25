package xuan.biotech;

import weka.classifiers.functions.SMOreg;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/*
 * @note: this is main class that will perform the general steps of building the model
 * 1.gather the data and make it trainable; 2. pass the path of the datafile (.arff) to 
 * WekaBuildModel.java to build the right model. 3. get the suitable model, and perform 
 * prediction (predicition input: smiles string, SDF file, inchi), and the input has to 
 * be the drug (can't be the protein, e.g. transporter)
 * 
 * @input: interested drug (compound)
 * @output: the predicted transporters
 * 
 */

public class OperatingClass {
	
	/*
	 * @input: path of the model, predicted input (has to be arff file)
	 */
	public void make_prediction(String path_of_model, String predictedInput) throws Exception{
		SMOreg smo2 = (SMOreg) weka.core.SerializationHelper.read(path_of_model);
		
		// test data: 
		// note: for my real program, user input the string of smiles or sdf file,
		// GeneratingFeatures.java will create the testfile for doing prediction
		// here is just an example
		DataSource test_datasource = new DataSource(predictedInput);
		Instances test_dataset = test_datasource.getDataSet();
		test_dataset.setClassIndex(test_dataset.numAttributes()-1);
		
		double PredictedOutput = test_dataset.instance(0).classValue();
		Instance newinst = test_dataset.instance(0);
		double PredictedValue = smo2.classifyInstance(newinst);
		System.out.print(PredictedValue);
		
	}
	
	
	
	public void main(String args[]) throws Exception{
		//build model
		
		
		//load model
		
		
		
		
	}

}
