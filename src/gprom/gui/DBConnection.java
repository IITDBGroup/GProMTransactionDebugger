package gprom.gui;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.jaret.util.ui.timebars.*;




public class DBConnection {
	
   
    
    public static ResultSet getData(){
    	Connection connection = getConnection();
    	Statement statement = null;
    	String query;
    	ResultSet resultSet = null;
    	
		try {
			statement = connection.createStatement();
			query ="SELECT * FROM SYS.fga_log$ WHERE ROWNUM <=20 ORDER BY scn ASC";
        	resultSet = statement.executeQuery(query);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultSet;
    }

    private static Connection getConnection() {
    	Connection connection= null;
    	try {
             	Class.forName("oracle.jdbc.driver.OracleDriver");
             	connection = DriverManager.getConnection(
                                "jdbc:oracle:thin:@ ligeti.cs.iit.edu:1521:orcl", "fga_user", "fga");
    	} catch (ClassNotFoundException e) {
             	e.printStackTrace();
    	} catch (SQLException e) {
    			e.printStackTrace();
    	}
    	return connection;
	}
}