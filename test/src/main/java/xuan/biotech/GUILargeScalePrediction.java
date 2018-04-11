package xuan.biotech;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.ArrayUtils;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class GUILargeScalePrediction extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUILargeScalePrediction frame = new GUILargeScalePrediction();
					frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
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
	public GUILargeScalePrediction() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 665);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(118, 11, 571, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JTextPane txtpnFileDirectory = new JTextPane();
		txtpnFileDirectory.setBackground(new Color(240, 240, 240));
		txtpnFileDirectory.setEditable(false);
		txtpnFileDirectory.setText("File Directory: ");
		txtpnFileDirectory.setBounds(10, 11, 84, 20);
		contentPane.add(txtpnFileDirectory);
		
		JButton btnPredict = new JButton("Predict!");
		btnPredict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//file directory
				String fileDirectory = textField.getText();
				CSVReader reader = null;
				ArrayList<String> smileStrings = new ArrayList<String>();
				try {
					reader = new CSVReader(new FileReader(fileDirectory));
					String [] nextLine;
					while ((nextLine = reader.readNext()) != null) {    
					    String smile_string = nextLine[0];
					    smileStrings.add(smile_string);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				GeneratingFeatures GF = new GeneratingFeatures();
				String workingDir = System.getProperty("user.dir");
				String outputFilePath = null;
				try {
					outputFilePath = GF.generating_feature_for_largeScalePrediction(fileDirectory, false);
				} catch (Exception e) {
					GUIerrors raiseError = new GUIerrors(e.toString());
					raiseError.main(e.toString());
				}
				
				
				
				
				String arffFilePath = workingDir + "\\forTempFile\\temp.arff";
				// load CSV
				CSVLoader loader = new CSVLoader();
				Instances data = null;
				int totalInstanceNumber = 0;
				try {
					loader.setSource(new File(outputFilePath));
					data = loader.getDataSet();
					totalInstanceNumber = data.numInstances();
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
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//model path:
				String MDR1Model = workingDir + "\\wekaMachineLearningModel\\MDR1MRandomForest.model";
				// arff exist, put them in weka model to predict
				MakePrediction MP = new MakePrediction();
				String[] resultForMDR1 = new String[totalInstanceNumber];
				try {
					resultForMDR1 = MP.makePrediction_Random_Forest_GUILargeScale(MDR1Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				String ABCG2Model = workingDir + "\\wekaMachineLearningModel\\ABCG2MRandomForest.model";
				String[] resultForABCG2 = new String[totalInstanceNumber];
				try {
					resultForABCG2 = MP.makePrediction_Random_Forest_GUILargeScale(ABCG2Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String SLC22A6Model = workingDir + "\\wekaMachineLearningModel\\SLC22A6MRandomForest.model";
				String[] resultForSLC22A6 = new String[totalInstanceNumber];
				try {
					resultForSLC22A6 = MP.makePrediction_Random_Forest_GUILargeScale(SLC22A6Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String SLCO1B1Model = workingDir + "\\wekaMachineLearningModel\\SLCO1B1MRandomForest.model";
				String[] resultForSLCO1B1 = new String[totalInstanceNumber];
				try {
					resultForSLCO1B1 = MP.makePrediction_Random_Forest_GUILargeScale(SLCO1B1Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String SLC22A8Model = workingDir + "\\wekaMachineLearningModel\\SLC22A8MRandomForest.model";
				String[] resultForSLC22A8 = new String[totalInstanceNumber];
				try {
					resultForSLC22A8 = MP.makePrediction_Random_Forest_GUILargeScale(SLC22A8Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String ABCC2Model = workingDir + "\\wekaMachineLearningModel\\ABCC2MRandomForest.model";
				String[] resultForABCC2 = new String[totalInstanceNumber];
				try {
					resultForABCC2 = MP.makePrediction_Random_Forest_GUILargeScale(ABCC2Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String SLC22A1Model = workingDir + "\\wekaMachineLearningModel\\SLC22A1MRandomForest.model";
				String[] resultForSLC22A1 = new String[totalInstanceNumber];
				try {
					resultForSLC22A1 = MP.makePrediction_Random_Forest_GUILargeScale(SLC22A1Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String SLCO1A2Model = workingDir + "\\wekaMachineLearningModel\\SLCO1A2MRandomForest.model";
				String[] resultForSLCO1A2 = new String[totalInstanceNumber];
				try {
					resultForSLCO1A2 = MP.makePrediction_Random_Forest_GUILargeScale(SLCO1A2Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String SLC22A2Model = workingDir + "\\wekaMachineLearningModel\\SLC22A2MRandomForest.model";
				String[] resultForSLC22A2 = new String[totalInstanceNumber];
				try {
					resultForSLC22A2 = MP.makePrediction_Random_Forest_GUILargeScale(SLC22A2Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				String ABCC1Model = workingDir + "\\wekaMachineLearningModel\\ABCC1MRandomForest.model";
				String[] resultForABCC1 = new String[totalInstanceNumber];
				try {
					resultForABCC1 = MP.makePrediction_Random_Forest_GUILargeScale(ABCC1Model, arffFilePath, "MDR1");
				} catch (Exception e) {
					System.out.println(e);
				}
				//
				//populated the table
				
				Object[][]newtable = new Object[totalInstanceNumber][11];

				for (int tempInt = 0; tempInt < totalInstanceNumber; tempInt++) {
					String drugSmiles = smileStrings.get(tempInt);
					String MDR1Result = resultForMDR1[tempInt];
					String ABCG2Result = resultForABCG2[tempInt];
					String SLC22A6Result = resultForSLC22A6[tempInt];
					String SLCO1B1Result = resultForSLCO1B1[tempInt];
					String SLC22A8Result = resultForSLC22A8[tempInt];
					String ABCC2Result = resultForABCC2[tempInt];
					String SLC22A1Result = resultForSLC22A1[tempInt];
					String SLCO1A2Result = resultForSLCO1A2[tempInt];
					String SLC22A2Result = resultForSLC22A2[tempInt];
					String ABCC1Result = resultForABCC1[tempInt];
					Object[] tempObject = {drugSmiles,MDR1Result,ABCG2Result,SLC22A6Result,SLCO1B1Result,SLC22A8Result,
							ABCC2Result,SLC22A1Result,SLCO1A2Result,SLC22A2Result,ABCC1Result};
					newtable[tempInt] = tempObject;
				}
				
				table.setModel(new DefaultTableModel(
						newtable,
						new String[] {
							"Smiles String", "MDR1", "ABCG2", "SLC22A6", "SLCO1B1", "SLC22A8", "ABCC2", "SLC22A1", "SLCO1A2", "SLC22A2", "ABCC1"
						}
					) {
						Class[] columnTypes = new Class[] {
							String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class
						};
						public Class getColumnClass(int columnIndex) {
							return columnTypes[columnIndex];
						}
					});

				System.out.println("Finished");
				
			}
		});
		btnPredict.setBounds(600, 592, 89, 23);
		contentPane.add(btnPredict);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 679, 539);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Smiles String", "MDR1", "ABCG2", "SLC22A6", "SLCO1B1", "SLC22A8", "ABCC2", "SLC22A1", "SLCO1A2", "SLC22A2", "ABCC1"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(120);
		scrollPane.setViewportView(table);
	}
}
