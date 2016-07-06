package gui.transactionDebuggerFrame.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import dbConnection.DBManager;

public class DebuggerTableModel extends AbstractTableModel {

	private ResultSet rs = null;
	private ResultSetMetaData rsmd = null;
	private DBManager dbManager = null;
	private int stmtIndex;
	private ArrayList<Integer> indexArray = null;
	
	
	public DebuggerTableModel(ResultSet rs) {
		super();
		this.rs = rs;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		dbManager = DBManager.getInstance();
		if (dbManager.getConnection() == null) {
			System.out.println("connect oracle database failed！");
		} else {
//			System.out.println("connect oracle database succeed！");
		}
//		this.sql = sql;
		
		
	}
	
	public DebuggerTableModel(ResultSet rs, int stmtIndex) {
		super();
		this.rs = rs;
		this.stmtIndex = stmtIndex;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbManager = DBManager.getInstance();
		if (dbManager.getConnection() == null) {
			System.out.println("connect oracle database failed！");
		} else {
//			System.out.println("connect oracle database succeed！");
		}
//		this.sql = sql
		
	}
	
	@Override
	public int getRowCount(){
		int rowCount = 0;
		try {
			rs.last();
			rowCount = rs.getRow();
//			System.out.println("row count:" + rowCount);
			rs.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}

//		return rowCount;
		return 9999999;
	}

	@Override
	public int getColumnCount() {
		try {
//			System.out.println("column count:" + rsmd.getColumnCount());
			return rsmd.getColumnCount() + 1; //+ 1 because we need to add the index of tuple
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
    public String getColumnName(int col) {
        try {
        	if (col == 0) {
        		return "Tuple Index";
        	}
			return rsmd.getColumnName(col);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return "";
    }



	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		rowIndex = rowIndex + 1;
		
		if (columnIndex == 0) {
			return (Object)("t" + (rowIndex) + "[" + stmtIndex + "]");
			
		}
		if (rowIndex > 1) {
			return (Object)"Dummy text";
		}
		Object result = null;
//		System.out.println("get Value called" + rowIndex + "??" +columnIndex);
		try {
			rs.absolute(rowIndex);
			result = rs.getObject(columnIndex);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		
		return result;
	}

	
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
    	return true;
    }
}
