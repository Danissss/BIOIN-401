package xuan.biotech;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JLabel;

public  class GUIMainPage {

	JFrame frmDrugtransporterprediction;
	private JButton btnViewDrugs;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIMainPage window = new GUIMainPage();
					window.frmDrugtransporterprediction.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIMainPage() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDrugtransporterprediction = new JFrame();
		frmDrugtransporterprediction.setTitle("DrugPorter");
		frmDrugtransporterprediction.setBounds(100, 100, 486, 374);
		frmDrugtransporterprediction.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDrugtransporterprediction.getContentPane().setLayout(null);
		
		JButton btnViewTransporter = new JButton("View Transporters");
		btnViewTransporter.setBackground(Color.LIGHT_GRAY);
		btnViewTransporter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmDrugtransporterprediction.dispose();
				GUIViewTransporter viewTrans = new GUIViewTransporter();
				viewTrans.setVisible(true);
				
				
			}
		});
		btnViewTransporter.setToolTipText("");
		btnViewTransporter.setBounds(270, 35, 173, 79);
		frmDrugtransporterprediction.getContentPane().add(btnViewTransporter);
		
		JButton btnMakePrediction = new JButton("Make Prediction");
		btnMakePrediction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmDrugtransporterprediction.dispose();
				GUIPredictionClass newPrediction = new GUIPredictionClass();
				newPrediction.setVisible(true);
				
				
			}
		});
		btnMakePrediction.setToolTipText("");
		btnMakePrediction.setBackground(Color.LIGHT_GRAY);
		btnMakePrediction.setBounds(270, 215, 173, 79);
		frmDrugtransporterprediction.getContentPane().add(btnMakePrediction);
		
		JButton btnViewDrugs = new JButton("View Drugs");
		btnViewDrugs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmDrugtransporterprediction.dispose();
				GUIViewDrugs viewDrugs = new GUIViewDrugs();
				viewDrugs.setVisible(true);
			}
		});
		btnViewDrugs.setToolTipText("");
		btnViewDrugs.setBackground(Color.LIGHT_GRAY);
		btnViewDrugs.setBounds(270, 125, 173, 79);
		frmDrugtransporterprediction.getContentPane().add(btnViewDrugs);
		
		JButton btnMoreInfo = new JButton("MoreInfo");
		btnMoreInfo.setToolTipText("More Information");
		Image questionMark = new ImageIcon(this.getClass().getResource("/questionMark.png")).getImage();
		Image scaledImage = questionMark.getScaledInstance(13, 13, questionMark.SCALE_DEFAULT);
		btnMoreInfo.setIcon(new ImageIcon(scaledImage));
		btnMoreInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GUIMoreInfo MI = new GUIMoreInfo();
				MI.main();
			}
		});
		btnMoreInfo.setBounds(354, 305, 89, 23);
		frmDrugtransporterprediction.getContentPane().add(btnMoreInfo);
		
		JLabel lblNewLabel = new JLabel("");
		Image HomePageImage = new ImageIcon(this.getClass().getResource("/geneDrug.jpg")).getImage();
		lblNewLabel.setIcon(new ImageIcon(HomePageImage));
		lblNewLabel.setBounds(21, 35, 228, 259);
		frmDrugtransporterprediction.getContentPane().add(lblNewLabel);
	}
}
