package xuan.biotech;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.freehep.graphicsbase.util.Value;

import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class GUIViewDrugs extends JFrame {

	private JPanel contentPane;
	
	private Connection sqlconnection = null;
	private String getAllDrugMDR1 = "";
	
	private ResultSet resultForRowNum;
	private Statement statement;
	private JTextField textField;
	private JTable table;
	static String[] columnName = new String[] {"Drug", "Smiles", "Function", "Transporter"};
	
	
	public ClassForPassInfo newContainer = new ClassForPassInfo();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIViewDrugs frame = new GUIViewDrugs();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public boolean isCellEditable(int row, int column) {
        return false;
    }

	/**
	 * Create the frame.
	 */
	public GUIViewDrugs() {
		
		
		///this part is about sqlite
		sqlconnection = sqliteConnector.dbConnector();
		getAllDrugMDR1 += "select * from Drugs";
		Object[][] dbData = null;
		try {
		    statement = sqlconnection.createStatement();
			resultForRowNum = statement.executeQuery(getAllDrugMDR1);
			//get length of row:
			int SizeOf_result = 0;
			while (resultForRowNum.next()) {
					SizeOf_result++; 
				}
			
			dbData = new Object[SizeOf_result][4];
			ResultSet resultForData = statement.executeQuery(getAllDrugMDR1);
			int tempInt = 0;
			while (resultForData.next()) {
				  String drugName = resultForData.getString("drugName");
				  String drugSmiles = resultForData.getString("drugsmiles");
				  String action = resultForData.getString("action");
				  String transporter = resultForData.getString("transporter");
				  Object[] tempObject = {drugName,drugSmiles,action,transporter};
				  dbData[tempInt] = tempObject;
				  tempInt++; 
				}
			System.out.println("finished");
		}catch (SQLException e) {
			GUIerrors raiseError = new GUIerrors(e.toString());
			raiseError.main(e.toString());
		}
		
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 486, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 358, 313);
		contentPane.add(scrollPane);
		
		table =  new JTable();
		
		table.setModel(new DefaultTableModel(
				dbData,columnName
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, Object.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scrollPane.setViewportView(table);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUISearchByDrug search = new GUISearchByDrug();
				search.setVisible(true);
				
			}
		});
		btnSearch.setBounds(371, 275, 89, 23);
		contentPane.add(btnSearch);
		
		String[] transporter = { "MDR1", "ABCG2", "SLC22A6", "SLCO1B1", "SLC22A8", "ABCC2", "SLC22A1", "SLCO1A2", "SLC22A2", "ABCC1" };
		final JComboBox comboBox = new JComboBox(transporter);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = (String) comboBox.getSelectedItem();
				switch (s) {//check for a match
                	case "MDR1":
                		
                		
                		break;
                	case "ABCG2":
//                		getAllDrugMDR1 += " where transporter = 'ABCG2';";
//					try {
//						statement = sqlconnection.createStatement();
//						resultForRowNum = statement.executeQuery(getAllDrugMDR1);
//						int SizeOf_result = 0;
//						while (resultForRowNum.next()) {
//								SizeOf_result++; 
//							}
//						dbData = new Object[SizeOf_result][4];
//						ResultSet resultForData = statement.executeQuery(getAllDrugMDR1);
//						int tempInt = 0;
//						while (resultForData.next()) {
//							  String drugName = resultForData.getString("drugName");
//							  String drugSmiles = resultForData.getString("drugsmiles");
//							  String action = resultForData.getString("action");
//							  String transporter = resultForData.getString("transporter");
//							  Object[] tempObject = {drugName,drugSmiles,action,transporter};
//							  dbData[tempInt] = tempObject;
//							  tempInt++; 
//							}
//						
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						System.out.println(e);
//					}
            			
                		break;
                	case "SLC22A6":
                		
                		System.out.println(s);
                		break;
                	case "SLCO1B1":
                		
                		System.out.println(s);
                		break;
                	case "SLC22A8":
                		
                		System.out.println(s);
                		break;
                	case "ABCC2":
                		System.out.println(s);
                		break;
                	case "SLC22A1":
                		
                		System.out.println(s);
                		break;
                	case "SLCO1A2":
                		
                		System.out.println(s);
                		break;
                	case "SLC22A2":
                		
                		System.out.println(s);
                		break;
                	case "ABCC1":
                		
                		System.out.println(s);
                		break;
                		
                	default:
                		
                		System.out.println(s);
                		break;
                		
            }
			}
		});
		comboBox.setBounds(371, 11, 89, 20);
		contentPane.add(comboBox);
		
		JButton btnSearches = new JButton("Searches");
		btnSearches.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String queryDrug = textField.getText();
				String queryForDrugs = "select * from Drugs where drugName = '" + queryDrug + "';";
				Object[][] newData = null;
				try {
				    statement = sqlconnection.createStatement();
					resultForRowNum = statement.executeQuery(queryForDrugs);
					//get length of row:
					int SizeOf_result = 0;
					while (resultForRowNum.next()) {
							SizeOf_result++; 
						}
					newData = new Object[SizeOf_result][4];
					ResultSet resultForData = statement.executeQuery(queryForDrugs);
					int tempInt = 0;
					while (resultForData.next()) {
						  String drugName = resultForData.getString("drugName");
						  String drugSmiles = resultForData.getString("drugsmiles");
						  String action = resultForData.getString("action");
						  String transporter = resultForData.getString("transporter");
						  Object[] tempObject = {drugName,drugSmiles,action,transporter};
						  newData[tempInt] = tempObject;
						  tempInt++; 
						}
					System.out.println("finished");
				}catch (SQLException e) {
					GUIerrors raiseError = new GUIerrors(e.toString());
					raiseError.main(e.toString());
				}
				
				//However, when using logic to "refresh" a table it is much easier to do:
				//table.setModel( your updated TableModel );
				DefaultTableModel tableModel = new DefaultTableModel(newData,columnName);
				table.setModel(tableModel);
				
			}
		});
		btnSearches.setBounds(371, 241, 89, 23);
		contentPane.add(btnSearches);
		
		textField = new JTextField();
		textField.setBounds(371, 216, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
	}
}
