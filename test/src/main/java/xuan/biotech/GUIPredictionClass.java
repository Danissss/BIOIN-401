package xuan.biotech;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;

import com.opencsv.CSVWriter;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;

public class GUIPredictionClass extends JFrame{

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;

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
	public GUIPredictionClass() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton Predict = new JButton("Predict!");
		Predict.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0){
				
				// this the main part for prediction
				String smilesString = textField.getText();
				File tempFiles = File.createTempFile("temp.sdf");
				CSVWriter writer = new CSVWriter(new FileWriter("/predicting.csv"));
				//GeneratingFeatures GF = new GeneratingFeatures();
				
				try {
					//TODO: create temp file 
					File tempFiles = File.createTempFile("temp.sdf");
					String tempFile = "/temp.sdf";
					SDFWriter sdw  = new SDFWriter(new FileWriter(tempFile));
					SmilesParser temp_smiles = new SmilesParser(DefaultChemObjectBuilder.getInstance());
					IAtomContainer atom_container   = temp_smiles.parseSmiles(smilesString);
					StructureDiagramGenerator sdg = new StructureDiagramGenerator();
					sdg.setMolecule(atom_container);
					sdg.generateCoordinates();
					IAtomContainer mole = sdg.getMolecule();
					HashMap<Object,Object> properties = new HashMap<Object,Object>();
					properties.put("SMILES", smilesString);
					mole.addProperties(properties);
					try {
						sdw.write(mole);
					} catch (Exception e) {
						String errorMessage = "Couldn't parse this smiles string";
						GUIerrors error = new GUIerrors();
						error.main();
						
					}
				}
				catch (Exception e) {
					System.out.println(smilesString);
				}
				
				FeatureGeneration featureGeneration = new FeatureGeneration();
			    IAtomContainerSet moleSet = featureGeneration.readFile(tempFile);
			    IAtomContainer mole = moleSet.getAtomContainer(0);
			    CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mole.getBuilder());
		    	GeneratingFeatures GF = new GeneratingFeatures();
				String FingerPrint = "fingerprint";
				String Attributes = GF.generateAllFeatures(mole,"molecularFeatures");
				String Features = GF.generateOneinstance(mole,"molecularFeatures");
				String AttributesWithPred = Attributes + ", Association";
				String FeaturesWithPred = Features +",?";
				String[] AttributesList = AttributesWithPred.split(",");
				String[] FeaturesList = FeaturesWithPred.split(",");
				
				writer.writeNext(AttributesList);
				writer.writeNext(FeaturesList);
				
				//need to change csvtoarff path (create new method)
				ConvertTOArff newConverting = new ConvertTOArff();
				ConvertTOArff.CSVToArff("/temp.csv");
				
				//need to delete two files 
				File checkFile = new File(tempFile);
				if(checkFile.exists()) {
					checkFile.delete();
				}
		 		
				
				
				
			}
		});
		Predict.setBounds(371, 272, 89, 23);
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
		btnHome.setBounds(371, 301, 89, 23);
		contentPane.add(btnHome);
		
		textField = new JTextField();
		textField.setBounds(10, 33, 450, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblSmilesStringisomeric = new JLabel("Smiles String (Isomeric)");
		lblSmilesStringisomeric.setBounds(10, 11, 142, 23);
		contentPane.add(lblSmilesStringisomeric);
		
		table = new JTable();
		table.setBounds(10, 98, 450, 163);
		contentPane.add(table);
		
		JLabel lblResultyesThereExists = new JLabel("Result(Yes: There exists association; No: no association)");
		lblResultyesThereExists.setBounds(10, 64, 352, 23);
		contentPane.add(lblResultyesThereExists);
	}
}
