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

import weka.core.Attribute;

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
import org.apache.commons.lang3.StringUtils;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.fingerprint.IBitFingerprint;
import org.openscience.cdk.fingerprint.MACCSFingerprinter;
import org.openscience.cdk.fingerprint.PubchemFingerprinter;
import org.openscience.cdk.exception.CDKException;





public class GeneratingFeatures
{
	
	
	
	public String generateAllFeatures(IAtomContainer mole, String CalculatingType) throws Exception{
		
		 ArrayList<Attribute> atts = new ArrayList<Attribute>();
		 //IAtomContainerSet set = readFile(pathToInputFile);
		 //Add attribute names
		 //String names = "InChiKey\tPubChemID\tHMDB\tDrugBank\t1A2\t2A6\t2B6\t2C8\t2C9\t2C19\t2D6\t2|E1\t3A4\tName\tIsomericSmiles";
		 String moleculeFeatures = "nHBAcc\tnHBDon\tnaAromAtom\tnAtomP\tnB\tnAromBond\tnRotB\tALogP\tALogp2\tAMR\tXLogP\tMLogP\tapol\tTopoPSA\tMW\tbpol\tATSc1\tATSc2\tATSc3\tATSc4\tATSc5\tATSm1\tATSm2\tATSm3\tATSm4\tATSm5\tnAcid\tnBase\tMOMI-X\tMOMI-Y\tMOMI-Z\tMOMI-XY\tMOMI-XZ\tMOMI-YZ\tMOMI-R\tAllSurfaceArea";
		 String newMoleculeFeatures = moleculeFeatures.replaceAll("\t", ",");
		 LinkedHashMap<String, String> fpatterns = ChemSearcher.getRINFingerprintPatterns();
		 String[] labels = fpatterns.keySet().toArray(new String[fpatterns.size()]);
		 String rinFPnames = "\t" + StringUtils.join(labels,"\t");
		 
		 String rinFPnames2 = StringUtils.join(labels,"\t");
		 String newrinFPnames = rinFPnames2.replaceAll("\t", ",");
		
		 String firstNames = moleculeFeatures+rinFPnames;
		 String[] fnames = firstNames.split("\t");
		 
//		 for(int j = 0; j<fnames.length; j++){
//			 fnames[j] = fnames[j].replace(",", "-");
//			 Attribute Attribute = new Attribute(fnames[j]);
//			 atts.add(Attribute);
//		 }
//		 for(int h = 0; h < 881; h++){
//				
//			 Attribute Attribute = new Attribute(String.format("pubchem_f%03d", h+1));
//			 atts.add(Attribute);
//		 }
//
//		 for(int h = 0; h < 166; h++){
//			 Attribute Attribute = new Attribute(String.format("maccs_k%03d", h+1));
//			 atts.add(Attribute);
//		}
		if(CalculatingType == "fingerprint") {
			//1197
			//String[] leng = labelStringPrint.split(",");
			//System.out.println("from generateAllFeatures: " + leng.length);
			
//			String final_attribute = Arrays.toString(labels);
//			String final_attribute1 = final_attribute.replace("[", "");
//			String final_attribute2 = final_attribute1.replace("]", "");
//			System.out.println(final_attribute2);
			return "no";
		}
		else {
			
			return newMoleculeFeatures;
		}
		
	}
	
	
	/**
	 * Given an IAtomContainer of a molecule, generate a string that contains all raw feature values for that molecule
	 * @param IAtomContainer molecule
	 * @return IAtomContainerSet that contains all molecules in the sdf file        
	 * @throws Exception
	 * @author Siyan Tian
	 */
	
	
	public String generateOneinstance(IAtomContainer mole,String featureType ) throws Exception {
		StringBuffer sb = new StringBuffer();
		ChemSearcher cs = new ChemSearcher();
		PubchemFingerprinter pbf 	= new PubchemFingerprinter(SilentChemObjectBuilder.getInstance());
		MACCSFingerprinter maccs 	=  new MACCSFingerprinter(SilentChemObjectBuilder.getInstance());

		LinkedHashMap<String, String> fpatterns = cs.getRINFingerprintPatterns();
		FeatureGeneration fgen = new FeatureGeneration();
		
		IAtomContainer container = mole;
		
	
		IAtomContainer prepContainer = MoleculeExplorer.preprocessContainer(container);
		String[] gg = fgen.generateExtendedMolecularFeatures(prepContainer).split(",");
		
		//this is molecular featuresm
		String extendedFeatures = StringUtils.join(fgen.generateExtendedMolecularFeatures(prepContainer).split(","), "\t");
		String molecularFeatures = StringUtils.join(fgen.generateExtendedMolecularFeatures(prepContainer).split(","), ",");
		//String[] length = molecularFeatures.split(",");
		//System.out.println(length.length);
		
		
		// nonBitFeature contains the feature that don't have bit feature
		String nonBitFeature = extendedFeatures;
		String[] nonBitFeatures = nonBitFeature.split("\t");
		
		ArrayList<Double> bioTransformerFingerprint_bits = cs.generateClassyfireFingerprintAsDouble(prepContainer, fpatterns).getBitValues();
		
		//print bioTransformerFingerprint_bits separated by comma
		String bioTFinger_bits = "";
		for(int x = 0; x < bioTransformerFingerprint_bits.size(); x++){
			bioTFinger_bits =  bioTFinger_bits + String.valueOf(bioTransformerFingerprint_bits.get(x)) + ",";
		}
		
		//bioTFinger_bits
		bioTFinger_bits = bioTFinger_bits.substring(0, bioTFinger_bits.length()-1);
		
		
		//extendedFeatures = molecular Features + bioTFinger_bits
		for(int x = 0; x < bioTransformerFingerprint_bits.size(); x++){
			extendedFeatures =  extendedFeatures + "\t" + String.valueOf(bioTransformerFingerprint_bits.get(x));

		}
		
		
		ArrayList<Double> fingerprint_bits = new ArrayList<Double>();
		IBitFingerprint fingerp	= pbf.getBitFingerprint(prepContainer);

		int[] onbits = fingerp.getSetbits();

		for(int kp = 0; kp < 881; kp++){
			fingerprint_bits.add(0.0);
		}
		for(int o = 0; o < onbits.length; o++){
			fingerprint_bits.set(onbits[o], 1.0);
		}
		
		extendedFeatures =  extendedFeatures + "\t" + StringUtils.join(fingerprint_bits,"\t");
			
		ArrayList<Double> maccs_fingerprint_bits = new ArrayList<Double>();
		IBitFingerprint maccs_fingerp		= maccs.getBitFingerprint(prepContainer);
			
		int[] maccs_onbits = maccs_fingerp.getSetbits();
			
		for(int kp = 0; kp < 166; kp++){
			maccs_fingerprint_bits.add(0.0);
		}
		for(int o = 0; o < maccs_onbits.length; o++){
			maccs_fingerprint_bits.set(maccs_onbits[o], 1.0);
		}
		
		//System.out.println("4::"+extendedFeatures);
		extendedFeatures =  extendedFeatures + "\t" + StringUtils.join(maccs_fingerprint_bits,"\t");
		
		String finalFeatureValues = extendedFeatures;
		String[] temp = extendedFeatures.split("\t");
		
		
		//select which to return:
		if(featureType == "fingerprint") {
			//1197
			return bioTFinger_bits;
		}
		else {
			return molecularFeatures;
		}
	
	}
	

	/**
	 * take single smile string to get the arff file for weka prediction.
	 * @param: smiles_String
	 * @return: the file_path that contain the value for predicting on the model (class)
	 * @author xuan Cao
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
	 
	 public static String generating_fingerPrint(String csv_file_path, boolean isPredicting) throws Exception{
		 
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
	     	// featureGeneration.readFile(tempFile) will read the sdf file (with all sdf molecule
	  		// then pass them to IAtomContainerSet moleSet
	     	FeatureGeneration featureGeneration = new FeatureGeneration();
			IAtomContainerSet moleSet = featureGeneration.readFile(tempFile);
			
			ArrayList<String[]> all_Value_Array = new ArrayList<String[]>();
			for(int i = 0 ; i < moleSet.getAtomContainerCount(); i++) {
				
				GeneratingFeatures GF = new GeneratingFeatures();
				IAtomContainer mole = moleSet.getAtomContainer(i);
				
				String values = GF.generateOneinstance(mole,"fingerprint");
				String[] Values = values.split(",");
				all_Value_Array.add(Values);
				
				System.out.println(mole.getProperties());
				

			}
			
			LinkedHashMap<String, String> fpatterns = ChemSearcher.getRINFingerprintPatterns();
			String[] labels = fpatterns.keySet().toArray(new String[fpatterns.size()]);
			writer.writeNext(labels);
			
			for(int single_value = 0; single_value < all_Value_Array.size(); single_value++) {
				//System.out.println("number of Value: " + all_Value_Array.get(single_value).length);
				writer.writeNext(all_Value_Array.get(single_value));
			}
			
			File checkFile = new File(tempFile);
			if(checkFile.exists()) {
				checkFile.delete();
				System.out.println("Temp File deleted");
			}
			reader.close();
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
	     IAtomContainerSet moleSet = featureGeneration.readFile(tempFile);
	     ArrayList<String[]> molecularFeatureList = new ArrayList<String[]>();
	     String Attributes = null;
	     for(int i = 0 ; i < moleSet.getAtomContainerCount(); i++) {
			IAtomContainer mole = moleSet.getAtomContainer(i);
	    	CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mole.getBuilder());
	    	GeneratingFeatures GF = new GeneratingFeatures();
			String FingerPrint = "fingerprint";
			Attributes = GF.generateAllFeatures(mole,"molecularFeatures");
			String Features = GF.generateOneinstance(mole,"molecularFeatures");
	    	System.out.println(mole.getProperties());
	    	// molecular features
			String[] molecularFeature = Features.split(",");
			molecularFeatureList.add(molecularFeature);
			
			
//			if it is predicting setting, add "?" question mark at the end for weka
			if (isPredicting == true) {
				String[] questionMark = new String[1];
				questionMark[0] = "?";
				molecularFeature = ArrayUtils.addAll(molecularFeature,questionMark);
			}
			
			//writer.writeNext(molecularFeature);
		}
	    String[] AttributesArray = Attributes.split(",");
	    writer.writeNext(AttributesArray);
	    
		for(int singleMoleFeatureArr = 0; singleMoleFeatureArr < molecularFeatureList.size(); singleMoleFeatureArr++) {
			writer.writeNext(molecularFeatureList.get(singleMoleFeatureArr));
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
    		//String output_path = generating_feature(path_input_file,isPredict);
    		String output_path = generating_fingerPrint(path_input_file,isPredict);
    		
    		ConvertTOArff newConverting = new ConvertTOArff();
    		//newConverting.CSVToArff(output_path);
    		String temp_output_path = "C:\\Users\\xcao\\Desktop\\Output.csv";
    		//ConvertTOArff.CSVToArff(temp_output_path);
    		
    		
    		
    		//or maybe just manipulate them inside java
    		
    		// call the python script to run
    		//Process p = Runtime.getRuntime().exec("python test1.py "+ path_input_file +" "+"output.csv");

    		
    		
        
    }
    
    
    
}
