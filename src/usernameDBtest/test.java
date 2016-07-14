package usernameDBtest;

import java.sql.ResultSet;
import java.sql.SQLException;

import dbConnection.DBConnection;

public class test {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		ResultSet sets= DBConnection.getData();
		System.out.println(sets.getRow());
		while(sets.next()){
//			System.out.println(sets.g)
			System.out.println(sets.getString(sets.findColumn("DBUSERNAME")));
		}	
	}
}
