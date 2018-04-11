package xuan.biotech;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GUIViewTransporter extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIViewTransporter frame = new GUIViewTransporter();
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
	public GUIViewTransporter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 242);
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
		btnHome.setBounds(645, 176, 89, 23);
		contentPane.add(btnHome);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 625, 188);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"MDR1", "P-glycoprotein 1", "Chr5:8.66–8.75 Mb", "Major efflux transporter", "ABCB1"},
				{"ABCG2", "ATP-binding cassette sub-family G member 2", "Chr 4: 88.09 – 88.23 Mb", 
					"exporter for renal, extrarenal urate excretion", ""},
				{"SLC22A6", "Solute carrier family 22 member 6","Chr 11: 62.94 – 62.98 Mb" , 
						"renal elimination of endogenous and exogenous organic anions", "organic anion transporter 1"},
				{"SLCO1B1", "Solute carrier organic anion transporter family member 1B1", "Chr 12: 21.13 – 21.24 Mb",
							"Mediates the Na+-independent uptake of organic anions", null},
				{"SLC22A8", "Solute carrier family 22 member 8", "Chr 11: 62.99 – 63.02 Mb", 
								"excretion/detoxification of endogenous and exogenous organic anions", "OAT3"},
				{"ABCC2", "Canalicular multispecific organic anion transporter 1", "Chr 10: 99.78 – 99.85 Mb", 
									"excretion of numerous organic anions", "Multidrug resistance-associated protein 2"},
				{"SLC22A1", "Solute carrier family 22 member 1", "Chr 6: 160.12 – 160.16 Mb", 
										"Translocates a broad array of organic cations", "OCT1"},
				{"SLCO1A2", "Solute carrier organic anion transporter family member 1A2", "Chr 12: 21.26 – 21.42 Mb", 
											"transport of organic anions", "OATP1A2"},
				{"SLC22A2", "Solute carrier family 22 member 2", "Chr 6: 160.17 – 160.28 Mb", 
												"uptake of organic compounds from circulation", "OCT2"},
				{"ABCC1", "Multidrug resistance-associated protein 1", "Chr 16: 15.95 – 16.14 Mb",
													"export of organic anions and drugs from the cytoplasm", null},
			},
			new String[] {
				"Transporter Gene", "Transporter Name", "Allele", "Description", "Symnons "
			}
		));
		scrollPane.setViewportView(table);
	}
}
