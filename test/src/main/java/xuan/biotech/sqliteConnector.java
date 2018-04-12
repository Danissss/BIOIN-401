package xuan.biotech;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;




public class sqliteConnector {
	
	private static Connection conn = null;
	
	public static Connection dbConnector() {
		
		
		try {
			
			String workingDir = System.getProperty("user.dir");
			workingDir += "\\DatabaseFolder\\DrugPorter.db";
			String url = "jdbc:sqlite:"+ workingDir;
			
			conn = DriverManager.getConnection(url);
			System.out.println("successfully connect to db");
			return conn;
			
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null,e);
			return null;
		}
	}

}
