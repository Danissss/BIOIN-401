package xuan.biotech;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.UIManager;

public class GUIMoreInfo extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main() {
		try {
			GUIMoreInfo dialog = new GUIMoreInfo();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public GUIMoreInfo() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{

			JTextArea txtrThis = new JTextArea();
			
			txtrThis.setLineWrap(true);
			txtrThis.setBackground(UIManager.getColor("Button.light"));
			txtrThis.setText("Description: predicting drug transporter associations\n"
					  +	 "Author: Xuan Cao\n"
					  +  "Open sources: Weka; CDK; CasFire\n"
					  +  "Dataset resources: Drugbank, Uniprot, PubChem\n"
					  +  "Smiles string: Isomeric smiles string\n"
					  +  "Paper:");
			txtrThis.setBounds(0, 0, 427, 181);
			txtrThis.setEditable(false);
			Color newColor = new Color(240,240,240);
			txtrThis.setBackground(newColor);
			contentPanel.add(txtrThis);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
