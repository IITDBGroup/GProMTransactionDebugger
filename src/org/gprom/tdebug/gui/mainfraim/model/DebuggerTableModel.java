package org.gprom.tdebug.gui.mainfraim.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import timebars.eventmonitoring.model.EventInterval;
import timebars.eventmonitoring.model.EventTimeBarRow;

public class DebuggerTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(DebuggerTableModel.class);
	

	private int stmtIndex;
	private List<Integer> indexList = null;
	private Map<String, List<String>> prevRelation = new HashMap<String, List<String>>();
	private Map<String, List<String>> nextRelation = new HashMap<String, List<String>>();
	private EventTimeBarRow currentRow;
	private int numRows;

	List<Map<Integer, Object>> rsList;
	List<String> rsNameList = new ArrayList<String>();
	List<String> rsRealNameList = new ArrayList<String>();
	private boolean showTableFlag;
	private String tableName = "";
	
	public DebuggerTableModel(List<Map<Integer, Object>> rsList, 
							  List<Integer> indexList, 
							  int stmtIndex, 
							  EventTimeBarRow currentRow, 
							  int numRows, 
							  List<String> rsNameList, 
							  List<String> rsRealNameList,
							  boolean showTableFlag, 
							  String tableName) {
		super();

		this.indexList = indexList;
		this.rsList = rsList;
		this.rsNameList = rsNameList;
		this.rsRealNameList = rsRealNameList;
		this.showTableFlag = showTableFlag;
		this.tableName = tableName;
		
		this.stmtIndex = stmtIndex;
		this.currentRow = currentRow;
		this.numRows = numRows;
	}
	
	public DebuggerTableModel(List<Map<Integer, Object>> rsList, 
							  List<Integer> indexList, 
							  int stmtIndex, 
							  EventTimeBarRow currentRow, 
							  int numRows,
							  int cRow, 
							  int cCol, 
							  Object cValue, 
							  List<String> rsNameList,
							  List<String> rsRealNameList,
							  boolean showTableFlag, 
							  String tableName) {
		super();

		this.indexList = indexList;
		this.rsList = rsList;
		this.rsNameList = rsNameList;
		this.rsRealNameList = rsRealNameList;
		this.showTableFlag = showTableFlag;
		this.tableName = tableName;
		
		this.stmtIndex = stmtIndex;
		this.currentRow = currentRow;
		this.numRows = numRows;
		
	}

	public void setPrevTupleIndex(String targetIndex, String tupleIndex) {
		List<String> list = prevRelation.get(targetIndex);
		if (list == null) {
			list = new ArrayList<String>();
			prevRelation.put(targetIndex, list);
		}
		list.add(tupleIndex);

	}
	
	
	public void setNextTupleIndex(String targetIndex, String tupleIndex) {
		List<String> list = nextRelation.get(targetIndex);
		if (list == null) {
			list = new ArrayList<String>();
			nextRelation.put(targetIndex, list);
			
		}
		list.add(tupleIndex);

	}
	
	public void forGraphSQL(Map<String, List<String>>myHashPre, Map<String, List<String>>myHashNext) {
		myHashPre = prevRelation;
		myHashNext = nextRelation;
	}
	
	
	@Override
	public int getRowCount(){
		
		return rsList.size();
	}

	@Override
	public int getColumnCount() {

			return indexList.size() + 2; // because we only need to access the part of result set
	}
	
    public String getColumnName(int col) {
    	
    	if(numRows == 0 || showTableFlag)
    	{
    		if (col == 0) 
    			return "";
    		else if (col == 1)
    			return "";
    	}
    	else
    	{
    		if (col == 0) 
    			return "Tuple Index";        		
    		else if (col == 1)
    			return "TransID";
    	}

    	col = col - 2; 
    	log.info("col = "+(col) + " List index = "+indexList.get(col) 
    	+ "Prov Name = " + rsNameList.get(indexList.get(col))
    	+ "Real Name = " + rsRealNameList.get(indexList.get(col)));

    	//return rsNameList.get(indexList.get(col));
    	return rsRealNameList.get(indexList.get(col));
    }
    
    
    public String getProvColumnName(int col) {
    	
    	if(numRows == 0 || showTableFlag)
    	{
    		if (col == 0) 
    			return "";
    		else if (col == 1)
    			return "";
    	}
    	else
    	{
    		if (col == 0) 
    			return "Tuple Index";        		
    		else if (col == 1)
    			return "TransID";
    	}

    	col = col - 2; 
    	log.info("col = "+(col) + " List index = "+indexList.get(col) 
    	+ "Prov Name = " + rsNameList.get(indexList.get(col))
    	+ "Real Name = " + rsRealNameList.get(indexList.get(col)));

    	return rsNameList.get(indexList.get(col));
    }


	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {

		
		if (columnIndex == 0) {
		}
		else if (columnIndex == 1) {
		}
		else
		{
			columnIndex = columnIndex - 2;
			Map<Integer, Object> map = rsList.get(rowIndex);
			map.put(indexList.get(columnIndex), value);
		}
		
		//used for key is name
//		Map<String, Object> map = rsList.get(rowIndex);
//		int c = 0;
//		for(Entry<String, Object> entry : map.entrySet())
//		{
//			if(c == columnIndex)
//			{
//				String cName = "";
//				try {
//					cName = rsmd.getColumnName(indexList.get(columnIndex));
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				map.put(cName, value);
//			}			
//			c++;
//		}
		
	    fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		
		if(showTableFlag)
		{
			if (columnIndex == 0) {
				return (Object)("");	
			}

			if (columnIndex == 1) {
				String tid = "";
				return (Object)(tid);	
			}
		}
		else
		{
			if(rowIndex < numRows)
			{
				if (columnIndex == 0) {
					return (Object)("t" + (rowIndex) + "[" + stmtIndex + "]");	
				}

				if (columnIndex == 1) {
					String tid = currentRow.getXID();
					return (Object)(tid);	
				}
			}
			else
			{
				if (columnIndex == 0) {
					return (Object)("");	
				}

				if (columnIndex == 1) {
					String tid = "";
					return (Object)(tid);	
				}
			}
		}
		
		columnIndex = columnIndex - 2;
		
		Map<Integer, Object> map = rsList.get(rowIndex);
		Object result = map.get(indexList.get(columnIndex));
	
		return result;
	}

	
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
    	return true;
    }
    
	public int getStmtIndex() {
		return stmtIndex;
	}
	
	public Map<String, List<String>> getPrevRelation() {
		return prevRelation;
	}

	public Map<String, List<String>> getNextRelation() {
		return nextRelation;
	}

	public void addColumn(String string, Object[] array) {
		// TODO Auto-generated method stub
		this.addColumn(string, array);
	}
	
	public String getTableName() {
		// TODO Auto-generated method stub
		return this.tableName;
	}
}
