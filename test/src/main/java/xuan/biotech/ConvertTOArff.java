package xuan.biotech;




import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
 
import java.io.File;
 
public class ConvertTOArff {
  /**
   * takes 2 arguments:
   * - CSV input file
   * - ARFF output file
   */
	public static void CSVToArff(String filePath) throws Exception {
		
		
		String arffFilePath = "C:\\Users\\xcao\\Desktop\\Output.arff";
		// load CSV
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File(filePath));
		Instances data = loader.getDataSet();
 
		// save ARFF
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(arffFilePath));
		//saver.setDestination(new File(arffFilePath));
		saver.writeBatch();
	}
	/**
	 * 
	 * @param filePath csv file that contain calculated value
	 * @param OriginalFilePath original file with smiles string and association 1:yes ; 0： no
	 * @throws Exception
	 */
	public static void addASSO(String filePath, String OriginalFilePath) throws Exception{
		
	}
	
	public static void addAttribute(String filePath) throws Exception {
		String attribute = "XlogP，Apol，HbondAcceptor，HbondDonor	MofIn，RotatableBond，TPSA，Weight	XlogP，ASA，Ass\n";
		
	}
}