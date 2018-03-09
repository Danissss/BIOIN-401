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
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.exception.CDKException;





public class GeneratingFeatures
{
	/*
	 * read_csv
	 * @function: parse the csv file into java object
	 * @input:  path to csv_file in string
	 * @output: java object
	 * @notes: nextLine[n]; n might be change due to the table
	 */
	public static String generating_feature(String csv_file_path) throws Exception
	{
		
		CSVReader reader = new CSVReader(new FileReader(csv_file_path));
		String output_path = "/Users/xuan/Desktop/output.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(output_path));
		
		
	 	String tempFile = "/Users/xuan/Desktop/testcsv.sdf";
	 	SDFWriter sdw  = new SDFWriter(new FileWriter(tempFile));
	     String [] nextLine;
	     while ((nextLine = reader.readNext()) != null) {    
	    	    String smile_string = nextLine[4];    //contain smile string
	    	    
	    	    
	    	    
 	 		SmilesParser temp_smiles = new SmilesParser(DefaultChemObjectBuilder.getInstance());
 	 		IAtomContainer atom_container   = temp_smiles.parseSmiles(smile_string);
	 		StructureDiagramGenerator sdg = new StructureDiagramGenerator();
	 		sdg.setMolecule(atom_container);
	 		sdg.generateCoordinates();
	 		IAtomContainer mole = sdg.getMolecule();   
	
	        sdw.write(mole);
	     }
	     sdw.close();

		FeatureGeneration featureGeneration = new FeatureGeneration();
		IAtomContainerSet moleSet = featureGeneration.readFile(tempFile);
		for(int i = 0 ; i < moleSet.getAtomContainerCount(); i++) {
			
			IAtomContainer mole = moleSet.getAtomContainer(i);
	    	 	CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mole.getBuilder());
			String testString = featureGeneration.generateMolecularFeatures(mole);
			String[] ary = testString.split(",");
			writer.writeNext(ary);
	    	 			
	    	 	
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
	    	 		

	     
	
	
    public static void main( String[] args ) throws Exception
    {
    	
    		/* detect the length of file*/
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
    		String output_path = generating_feature(path_input_file);
    		
    		
    		
    		//or maybe just manipulate them inside java
    		
    		// call the python script to run
    		Process p = Runtime.getRuntime().exec("python test1.py "+ path_input_file +" "+"output.csv");

    		
    		
        
    }
    
    
    
}
