package xuan.testForNodejs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.fingerprint.IBitFingerprint;
import org.openscience.cdk.fingerprint.MACCSFingerprinter;
import org.openscience.cdk.fingerprint.PubchemFingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import com.opencsv.CSVWriter;

import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import xuan.testForNodejs.FeatureGeneration;
import xuan.testForNodejs.MoleculeExplorer;
import xuan.testForNodejs.ChemSearcher;

/**
 * Hello world!
 *
 */
public class App 
{
	
    public  String makePrediction_Random_Forest_GUI(String ModelPath, String predictedFilePath, String TransporterName) throws Exception{
    	
		
//		System.out.println(ModelPath);
//		System.out.println(predictedFilePath);
//		System.out.println(TransporterName);
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

	return results;
	
	
    }
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
	public static String getPredictedValue(String smileString){
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		// this the main part for prediction
		// smiles string CAN'T HAVE EMPTY SPACE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		String smilesString = smileString;
		smilesString = smilesString.replaceAll(" ", "");
		
		String workingDir = System.getProperty("user.dir");
		
		String locationForSDF = workingDir + "/forTempFile/temp.sdf";
		String locationForCSV = workingDir + "/forTempFile/temp.csv";
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(locationForCSV));
		} catch (IOException e1) {

			System.out.println(e1);
		}
		SDFWriter sdw = null;
		try {
			sdw  = new SDFWriter(new FileWriter(locationForSDF));
		} catch (IOException e) {

			System.out.println("can't find file");
		}
		SmilesParser temp_smiles = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer atom_container = null;
		try {
			atom_container   = temp_smiles.parseSmiles(smilesString);
		} catch (InvalidSmilesException e) {
			System.out.println(e);
		}
		 StructureDiagramGenerator sdg = new StructureDiagramGenerator();
 		 sdg.setMolecule(atom_container);
 		 try {
			sdg.generateCoordinates();
		} catch (CDKException e) {
			System.out.println(e);
		}
 		 IAtomContainer mole = sdg.getMolecule();
 		try {
			sdw.write(mole);
		} catch (CDKException e) {
			System.out.println(e);
		}
 		try {
			sdw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
 		
 		FeatureGeneration featureGeneration = new FeatureGeneration();
 		IAtomContainerSet moleSet = null;
		try {
			moleSet = featureGeneration.readFile(locationForSDF);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		/////////////////////////////////////////////////////////////////////////////////////////
		//sdf file generated successfully, convert to final atomcontainer and
		// make the csv file
		App GF = new App();
		IAtomContainer mole2 = moleSet.getAtomContainer(0);
		
		String values = null;
		try {
			values = GF.generateOneinstance(mole2,"molecularFeatures");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		String Attributes = null;
		try {
			Attributes = GF.generateAllFeatures(mole2,"molecularFeatures");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		String[] Values = values.split(",");
		String[] Attributess = Attributes.split(",");
		writer.writeNext(Attributess);
		writer.writeNext(Values);
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		
		///////////////////////////////////////////////////////////////////////////////////////
		// csv file successfully created, convert to arff file to feed weka
		//locationForCSV
		String arffFilePath = workingDir + "/forTempFile/temp.arff";
		// load CSV
		CSVLoader loader = new CSVLoader();
		Instances data = null;
		try {
			loader.setSource(new File(locationForCSV));
			data = loader.getDataSet();
			FastVector<String> association = new FastVector<String>();
			association.addElement("Yes");
			association.addElement("No");
			data.insertAttributeAt(new Attribute("Association",association), data.numAttributes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		// save ARFF
		ArffSaver saver = new ArffSaver();
		
		saver.setInstances(data);
		try {
			saver.setFile(new File(arffFilePath));
			saver.writeBatch();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		File checkFileSDF = new File(locationForSDF);
		if(checkFileSDF.exists()) {
			checkFileSDF.delete();
			
		}
		File checkFileCSV = new File(locationForCSV);
		if(checkFileCSV.exists()) {
			checkFileCSV.delete();
			
		}
		
		
		
		//model path:
		String MDR1Model = workingDir + "/wekaMachineLearningModel/MDR1_molecularFeatures.model";
		
		// arff exist, put them in weka model to predict
		App MP = new App();
		String resultForMDR1 = null;
		try {
			resultForMDR1 = MP.makePrediction_Random_Forest_GUI(MDR1Model, arffFilePath, "MDR1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		String ACBG2Model = workingDir + "/wekaMachineLearningModel/ABCG2Model_M.model";
		
		// arff exist, put them in weka model to predict
		
		String resultForACBG2 = null;
		try {
			resultForACBG2 = MP.makePrediction_Random_Forest_GUI(ACBG2Model, arffFilePath, "ACBG2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		String Result_for_MDR1 = resultForMDR1.split(" ")[0];
		String Result_for_ACBG2 = resultForACBG2.split(" ")[0];
		
		String AllResult = Result_for_MDR1+","+Result_for_ACBG2;
		File checkFileARFF = new File(arffFilePath);
		if(checkFileARFF.exists()) {
			checkFileARFF.delete();
			
		}
		
		
		return AllResult;
		
	}
	
	
    public static void main( String[] args )
    {	
    		String smileString = args[0];
    		String toNodejs = getPredictedValue(smileString);
    		System.out.println(toNodejs);
    		
    }
}
