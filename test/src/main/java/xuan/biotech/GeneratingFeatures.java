package xuan.biotech;

import java.io.File;

/*
 * this program will take csv file and 
 * convert all smile string to chemical features, and save it to new/old csv file for later
 * WekaBuildModel to use.
 * general algorithm
 * 1. read the database file 
 * 2. take all the molecule's smile string and put it into database
 * 3. generate feature by the smile string and cdk library, and put the feature into the database
 * 4. read the database again, parse it into tuple
 * 5. run machine learning algorithm to generate model
 * 
 * author: Xuan Cao
 * 
 * 
 * All reference code:
 * https://stackoverflow.com/questions/14274259/read-csv-with-scanner
 * 
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.fingerprint.IBitFingerprint;
import org.openscience.cdk.fingerprint.MACCSFingerprinter;
import org.openscience.cdk.fingerprint.PubchemFingerprinter;
import org.openscience.cdk.exception.CDKException;





public class GeneratingFeatures
{
	
	




	/**
	 * take single smile string to get the arff file for weka prediction.
	 * @param: smiles_String
	 * @return: the file_path that contain the value for predicting on the model (class)
	 */
	 
	 public static String generating_feature(String smileString) throws Exception{
		 
		 String smiles = smileString;
		 String output_path = "/Users/xuan/Desktop/output.csv";
		 CSVWriter writer = new CSVWriter(new FileWriter(output_path));
		 
		 String tempFile = "/Users/xuan/Desktop/testcsv.sdf";
		 SDFWriter sdw  = new SDFWriter(new FileWriter(tempFile));
		 
		 SmilesParser temp_smiles = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		 IAtomContainer atom_container   = temp_smiles.parseSmiles(smiles);
		 StructureDiagramGenerator sdg = new StructureDiagramGenerator();
		 sdg.setMolecule(atom_container);
		 sdg.generateCoordinates();
		 IAtomContainer mole = sdg.getMolecule();
		 HashMap<Object,Object> properties = new HashMap<Object,Object>();
		 properties.put("SMILES", smiles);
		 mole.addProperties(properties);
	
	     sdw.write(mole);
	     sdw.close();
		 
	     
	     FeatureGeneration featureGeneration = new FeatureGeneration();
			// featureGeneration.readFile(tempFile) will read the sdf file (with all sdf molecule
			// then pass them to IAtomContainerSet moleSet
	     IAtomContainerSet moleSet = featureGeneration.readFile(tempFile);
	     
	     CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mole.getBuilder());
 	 	 System.out.println(mole.getProperties());
		 String molecularFeatures = featureGeneration.generateMolecularFeatures(mole);
		 String[] molecularFeature = molecularFeatures.split(",");
		 for(int i = 0 ; i < moleSet.getAtomContainerCount(); i++) {
				
				IAtomContainer molecule = moleSet.getAtomContainer(i);
				
		    	 	CDKHydrogenAdder adders = CDKHydrogenAdder.getInstance(molecule.getBuilder());
		    	 	System.out.println(molecule.getProperties());
				String molecularFeatures_single = featureGeneration.generateMolecularFeatures(molecule);
				String[] molecularFeature_single = molecularFeatures_single.split(",");
				
				
				String[] questionMark = new String[1];
				questionMark[0] = "?";
				molecularFeature = ArrayUtils.addAll(molecularFeature,questionMark);
				
				//System.out.println(Arrays.toString(combinedFeature)); //works
				
				writer.writeNext(molecularFeature);
				
			}
			
			
			File checkFile = new File(tempFile);
			if(checkFile.exists()) {
				checkFile.delete();
				System.out.println("Temp File deleted");
			}
			writer.close();
		 
		 return "ok";
		 
	 }
	 
	/**
	 * read_csv
	 * function: parse the csv file into java object
	 * notes: nextLine[n]; n might be change due to the table
	 * if it is predicting setting, it will add "?" at the end of string 
	 * @param:  path to csv_file in string
	 * @param:  isPredicting
	 * @return: java object
	 * 
	 */
	public static String generating_feature(String csv_file_path, boolean isPredicting) throws Exception
	{
		
		CSVReader reader = new CSVReader(new FileReader(csv_file_path));
		String output_path = "C:\\Users\\xcao\\Desktop\\Output.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(output_path));
		
		
	 	String tempFile = "C:\\Users\\xcao\\Desktop\\temp.sdf";
	 	SDFWriter sdw  = new SDFWriter(new FileWriter(tempFile));
	     String [] nextLine;
	     
	     //this loop will read all smile string, and convert it to sdf format of molecule
	     //then, write it back to sdf file SDFWriter sdw
	     while ((nextLine = reader.readNext()) != null) {    
	    	    String smile_string = nextLine[0];    //contain smile string
	    	    
	    	    
	    	    
 	 		SmilesParser temp_smiles = new SmilesParser(DefaultChemObjectBuilder.getInstance());
 	 		IAtomContainer atom_container   = temp_smiles.parseSmiles(smile_string);
	 		StructureDiagramGenerator sdg = new StructureDiagramGenerator();
	 		sdg.setMolecule(atom_container);
	 		sdg.generateCoordinates();
	 		IAtomContainer mole = sdg.getMolecule();
	 		HashMap<Object,Object> properties = new HashMap<Object,Object>();
	 		properties.put("SMILES", smile_string);
	 		mole.addProperties(properties);
	 		
	 		try {
	 			sdw.write(mole);
	 		} catch (Exception e) {
	 			System.out.println(smile_string);
	 			
	 		}
	        
	        
	        
	        
	     }
	     sdw.close();

		FeatureGeneration featureGeneration = new FeatureGeneration();
		// featureGeneration.readFile(tempFile) will read the sdf file (with all sdf molecule
		// then pass them to IAtomContainerSet moleSet
		ArrayList<String> Attribute = new ArrayList<String>();
		IAtomContainerSet moleSet = featureGeneration.readFile(tempFile);
		ArrayList<String> all_Value_Array = new ArrayList<String>();
		String AttribString = "";
		for(int i = 0 ; i < moleSet.getAtomContainerCount(); i++) {
			
			
			IAtomContainer mole = moleSet.getAtomContainer(i);
			
			ChemSearcher newChemSearcher = new ChemSearcher();
			ArrayList[] testArrayList = new ArrayList[2];
			testArrayList = newChemSearcher.generateClassyfireFingerprint(mole);
			
			Attribute = testArrayList[0];
			
			for(String attrib : Attribute) {
				String temp = attrib.toString();
				AttribString += temp;
				AttribString += ",";
			}
			AttribString = AttribString.substring(0, AttribString.length()-1);
			//System.out.println(AttribString);
			
			//get all values
			ArrayList<String> Value = new ArrayList<String>();
			Value = testArrayList[1];
			String ValueString = "";
			for(String values : Value) {
				String temp = values.toString();
				ValueString += temp;
				ValueString += ",";
				
			}
			ValueString = ValueString.substring(0, ValueString.length()-1);
			
			//System.out.println(ValueString);
			
			
			
			
			//PubchemFingerprinter
			PubchemFingerprinter fprinter = new PubchemFingerprinter(SilentChemObjectBuilder.getInstance());
			MACCSFingerprinter maccs 	=  new MACCSFingerprinter(SilentChemObjectBuilder.getInstance());
			IBitFingerprint maccs_fingerp = maccs.getBitFingerprint(mole);
			int[] maccs_onbits = maccs_fingerp.getSetbits();
			
			//LinkedHashMap<String, String> fpatterns = maccs.getRINFingerprintPatterns();
			
			System.out.println(Arrays.toString(maccs_onbits));
			System.out.println(maccs_onbits.length);
			
	    	CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mole.getBuilder());
	    	System.out.println(mole.getProperties());
			String molecularFeatures = featureGeneration.generateMolecularFeatures(mole);
			String[] molecularFeature = molecularFeatures.split(",");
			
//			String[] atomicFeatures = featureGeneration.generateAtomicFeatures(mole);
//			System.out.println(Arrays.toString(atomicFeatures));
			
//			String[] combinedFeature;
//			
//			String combinedAtomicFeature = atomicFeatures[0];
//			
//			for (int lenAtomicFea = 1; lenAtomicFea < atomicFeatures.length; lenAtomicFea++) {
//				
//				String eachFeature = atomicFeatures[lenAtomicFea];
//				combinedAtomicFeature = combinedAtomicFeature +","+ eachFeature ;
//				
//				
//			}
//			combinedAtomicFeature += "\n";
//			String[] combinedAtomicFeatures = combinedAtomicFeature.split(",");
			
			
//			combinedFeature = ArrayUtils.addAll(molecularFeature,combinedAtomicFeatures);
			//System.out.println(Arrays.toString(combinedFeature));
			
			// if it is predicting setting, add "?" question mark at the end for weka
			if (isPredicting == true) {
				String[] questionMark = new String[1];
				questionMark[0] = "?";
				molecularFeature = ArrayUtils.addAll(molecularFeature,questionMark);
			}
			
			
			
			
			all_Value_Array.add(ValueString);
			 
			//writer.writeNext(molecularFeature);
			
		}
		
		String[] tempAttributeArray = AttribString.split(",");
		writer.writeNext(tempAttributeArray);
		for(String single_value : all_Value_Array) {
			String temp = single_value.toString();
			String[] tempArray = temp.split(",");
			writer.writeNext(tempArray);
		}
		
		
		File checkFile = new File(tempFile);
		if(checkFile.exists()) {
			checkFile.delete();
			System.out.println("Temp File deleted");
		}
		reader.close();
		writer.close();
		
		return output_path;
	}
	    	 		
	
	     
	/*
	 * main is just for single java class testing
	 * other program will call the method of this class directly
	 */
	
    public static void main( String[] args ) throws Exception
    {
    	
    		/* detect the length of file*/
    		boolean isPredict = false;
    		
    		
    		int args_length = args.length;
    		if(args_length < 1) {
    			System.out.println("You need input the path to the file");
    			System.exit(0);
    		}
    		if(!(args[0].contains(".csv"))) {
    			System.out.println("Only Take csv file");
    			System.exit(0);
    		}
    		
    		
    		String path_input_file = args[0];  //return the path of the file
    		String output_path = generating_feature(path_input_file,isPredict);
    		
    		ConvertTOArff newConverting = new ConvertTOArff();
    		//newConverting.CSVToArff(output_path);
    		String temp_output_path = "C:\\Users\\xcao\\Desktop\\XuanMDR1TrainingSet(output).csv";
    		//ConvertTOArff.CSVToArff(temp_output_path);
    		
    		
    		
    		//or maybe just manipulate them inside java
    		
    		// call the python script to run
    		//Process p = Runtime.getRuntime().exec("python test1.py "+ path_input_file +" "+"output.csv");

    		
    		
        
    }
    
    
    
}
