package xuan.biotech;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;

import com.mysql.fabric.xmlrpc.base.Array;
import com.opencsv.CSVWriter;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GUIPredictionClass extends JFrame{

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;
	private JTable table_1;
	private String[] firstTable = {"","","","","",""};
	private String[] secondTable = {"","","","","",""};
	private ArrayList<String> firstTableData;
	private ArrayList<String> secondTableData;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIPredictionClass frame = new GUIPredictionClass();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIPredictionClass(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 482, 328);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton Predict = new JButton("Predict!");
		Predict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				
				
				////////////////////////////////////////////////////////////////////////////////////////
				// this the main part for prediction
				// smiles string CAN'T HAVE EMPTY SPACE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				String smilesString = textField.getText();
				smilesString = smilesString.replaceAll(" ", "");
				
				String workingDir = System.getProperty("user.dir");
				
				String locationForSDF = workingDir + "\\forTempFile\\temp.sdf";
				String locationForCSV = workingDir + "\\forTempFile\\temp.csv";
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
					GUIerrors raiseError = new GUIerrors(e.toString());
					raiseError.main(e.toString());
				}
				 StructureDiagramGenerator sdg = new StructureDiagramGenerator();
		 		 sdg.setMolecule(atom_container);
		 		 try {
					sdg.generateCoordinates();
				} catch (CDKException e) {
					GUIerrors raiseError = new GUIerrors(e.toString());
					raiseError.main(e.toString());
				}
		 		 IAtomContainer mole = sdg.getMolecule();
		 		try {
					sdw.write(mole);
				} catch (CDKException e) {
					GUIerrors raiseError = new GUIerrors(e.toString());
					raiseError.main(e.toString());
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
				GeneratingFeatures GF = new GeneratingFeatures();
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
				String arffFilePath = workingDir + "\\forTempFile\\temp.arff";
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
					System.out.println("Temp File deleted");
				}
				File checkFileCSV = new File(locationForCSV);
				if(checkFileCSV.exists()) {
					checkFileCSV.delete();
					System.out.println("Temp File deleted");
				}
				
				
				/////////////////////////////////////////////////////////////////////////////////////////////////////
				//model path:
				String MDR1Model = workingDir + "\\wekaMachineLearningModel\\MDR1MRandomForest.model";
				String ABCG2Model = workingDir + "\\wekaMachineLearningModel\\ABCG2MRandomForest.model";
				String SLC22A6Model = workingDir + "\\wekaMachineLearningModel\\SLC22A6MRandomForest.model";
				String SLCO1B1Model = workingDir + "\\wekaMachineLearningModel\\SLCO1B1MRandomForest.model";
				String SLC22A8Model = workingDir + "\\wekaMachineLearningModel\\SLC22A8MRandomForest.model";
				
				String ABCC2Model = workingDir + "\\wekaMachineLearningModel\\ABCC2MRandomForest.model";
				String SLC22A1Model = workingDir + "\\wekaMachineLearningModel\\SLC22A1MRandomForest.model";
				String SLCO1A2Model = workingDir + "\\wekaMachineLearningModel\\SLCO1A2MRandomForest.model";
				String SLC22A2Model = workingDir + "\\wekaMachineLearningModel\\SLC22A2MRandomForest.model";
				String ABCC1Model = workingDir + "\\wekaMachineLearningModel\\ABCC1MRandomForest.model";
				// arff exist, put them in weka model to predict
				MakePrediction MP = new MakePrediction();
				
				String resultForMDR1 = null;
				try {
					resultForMDR1 = MP.makePrediction_Random_Forest_GUI(MDR1Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String Result_for_MDR1 = resultForMDR1.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForABCG2 = null;
				try {
					resultForABCG2 = MP.makePrediction_Random_Forest_GUI(ABCG2Model, arffFilePath, "ABCG2");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_ABCG2 = resultForABCG2.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForSLC22A6 = null;
				try {
					resultForSLC22A6 = MP.makePrediction_Random_Forest_GUI(SLC22A6Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_SLC22A6 = resultForSLC22A6.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForSLCO1B1 = null;
				try {
					resultForSLCO1B1 = MP.makePrediction_Random_Forest_GUI(SLCO1B1Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_SLCO1B1 = resultForSLCO1B1.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForSLC22A8 = null;
				try {
					resultForSLC22A8 = MP.makePrediction_Random_Forest_GUI(SLC22A8Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_SLC22A8 = resultForSLC22A8.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForABCC2 = null;
				try {
					resultForABCC2 = MP.makePrediction_Random_Forest_GUI(ABCC2Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_ABCC2 = resultForABCC2.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForSLC22A1 = null;
				try {
					resultForSLC22A1 = MP.makePrediction_Random_Forest_GUI(SLC22A1Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_SLC22A1 = resultForSLC22A1.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForSLCO1A2 = null;
				try {
					resultForSLCO1A2 = MP.makePrediction_Random_Forest_GUI(SLCO1A2Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_SLCO1A2 = resultForSLCO1A2.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForSLC22A2 = null;
				try {
					resultForSLC22A2 = MP.makePrediction_Random_Forest_GUI(SLC22A2Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_SLC22A2 = resultForSLC22A2.split(" ")[0];
				//////////////////////////////////////////////////////////////////////////////////////////////////////
				String resultForABCC1 = null;
				try {
					resultForABCC1 = MP.makePrediction_Random_Forest_GUI(ABCC1Model, arffFilePath, "SLC22A6");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
				String result_For_ABCC1 = resultForABCC1.split(" ")[0];
				
				//this table only for previous five transporter
				firstTable[0] = smilesString;
				firstTable[1] = Result_for_MDR1;
				firstTable[2] = result_For_ABCG2;
				firstTable[3] = result_For_SLC22A6;
				firstTable[4] = result_For_SLCO1B1;
				firstTable[5] = result_For_SLC22A8;
				secondTable[0] = smilesString;
				secondTable[1] = result_For_ABCC2;
				secondTable[2] = result_For_SLC22A1;
				secondTable[3] = result_For_SLCO1A2;
				secondTable[4] = result_For_SLC22A2;
				secondTable[5] = result_For_ABCC1;
				//re-define the table here.
				
				table.setModel(new DefaultTableModel(
						new Object[][] {
							{firstTable[0], firstTable[1], firstTable[2], firstTable[3], firstTable[4], firstTable[5]},
						},
						new String[] {
							"DrugSmiles", "MDR1", "ABCG2", "SLC22A6", "SLCO1B1", "SLC22A8"
						}
					) {
						private static final long serialVersionUID = 1L;
						Class[] columnTypes = new Class[] {
							String.class, String.class, String.class, String.class, String.class, String.class
						};
						public Class getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
					});
				//same for second table
				table_1.setModel(new DefaultTableModel(
						new Object[][] {
							{secondTable[0], secondTable[1], secondTable[2], secondTable[3], secondTable[4], secondTable[5]},
						},
						new String[] {
							"DrugSmiles", "ABCC2", "SLC22A1", "SLCO1A2", "SLC22A2", "ABCC1"
						}
					) {
						private static final long serialVersionUID = 1L;
						Class[] columnTypes = new Class[] {
							String.class, String.class, String.class, String.class, String.class, String.class
						};
						public Class getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
					});
				
				
				
				
				
				File checkFileARFF = new File(arffFilePath);
				if(checkFileARFF.exists()) {
					checkFileARFF.delete();
					System.out.println("Temp File deleted");
				}
				System.out.println("finished");
				
				
				
			}
			
		});
		Predict.setBounds(371, 227, 89, 23);
		contentPane.add(Predict);
		
		JButton btnHome = new JButton("Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUIMainPage window = new GUIMainPage();
				window.frmDrugtransporterprediction.setVisible(true);
				setVisible(false);
				dispose();

			}
		});
		btnHome.setBounds(371, 255, 89, 23);
		contentPane.add(btnHome);
		
		textField = new JTextField();
		textField.setBounds(10, 33, 352, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblSmilesStringisomeric = new JLabel("Smiles String (Isomeric)");
		lblSmilesStringisomeric.setBounds(10, 11, 142, 23);
		contentPane.add(lblSmilesStringisomeric);

		
		JLabel lblResultyesThereExists = new JLabel("Result(Yes: There exists association; No: no association)");
		lblResultyesThereExists.setBounds(10, 64, 352, 23);
		contentPane.add(lblResultyesThereExists);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 98, 450, 57);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setRowHeight(34);
		
		
		
		
		DefaultTableModel table1Model = new DefaultTableModel();
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{firstTable[0], firstTable[1], firstTable[2], firstTable[3], firstTable[4], firstTable[5]},
			},
			new String[] {
				"DrugSmiles", "MDR1", "ABCG2", "SLC22A6", "SLCO1B1", "SLC22A8"
			}
		) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scrollPane.setViewportView(table);
	

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 166, 450, 57);
		contentPane.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setRowHeight(34);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
			},
			new String[] {
				"DrugSmiles", "ABCC2", "SLC22A1", "SLCO1A2", "SLC22A2", "ABCC1"
			}
		));
		scrollPane_1.setViewportView(table_1);
		
		JButton btnNewButton = new JButton("Upload File");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				GUILargeScalePrediction largePred = new GUILargeScalePrediction();
				largePred.main();
				
				
			}
		});
		btnNewButton.setBounds(371, 32, 89, 23);
		contentPane.add(btnNewButton);
	}
}
