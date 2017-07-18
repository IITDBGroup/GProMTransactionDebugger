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
	
	private ResultSet rs = null;
	private ResultSetMetaData rsmd = null;
	private int stmtIndex;
	private List<Integer> indexList = null;
	private Map<String, List<String>> prevRelation = new HashMap<String, List<String>>();
	private Map<String, List<String>> nextRelation = new HashMap<String, List<String>>();
	private EventTimeBarRow currentRow;
	private int numRows;
	private int cRow; //used for change data
	private int cCol;
	private Object cValue;
	private List<JTable> tables = new ArrayList<JTable>();
	List<Map<Integer, Object>> rsList;
	
	public DebuggerTableModel(List<Map<Integer, Object>> rsList, ResultSet rs, List<Integer> indexList, int stmtIndex, EventTimeBarRow currentRow, int numRows, List<JTable> tables) {
		super();
		this.rs = rs;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.indexList = indexList;
		this.tables = tables;
		this.rsList = rsList;
		
		this.stmtIndex = stmtIndex;
		this.currentRow = currentRow;
		this.numRows = numRows;
	}
	
	public DebuggerTableModel(List<Map<Integer, Object>> rsList, ResultSet rs, List<Integer> indexList, int stmtIndex, EventTimeBarRow currentRow, int numRows,
			int cRow, int cCol, Object cValue, List<JTable> tables) {
		super();
		this.rs = rs;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.indexList = indexList;
		this.tables = tables;
		this.rsList = rsList;
		
		this.stmtIndex = stmtIndex;
		this.currentRow = currentRow;
		this.numRows = numRows;
		this.cRow = cRow;
		this.cCol = cCol;
		this.cValue = cValue;
	}

	public void setPrevTupleIndex(String targetIndex, String tupleIndex) {
		List<String> list = prevRelation.get(targetIndex);
		if (list == null) {
			list = new ArrayList<String>();
			prevRelation.put(targetIndex, list);
		}
		list.add(tupleIndex);
//		log.info("prev" + prevRelation);

	}
	
	

	
	public void setNextTupleIndex(String targetIndex, String tupleIndex) {
		List<String> list = nextRelation.get(targetIndex);
		if (list == null) {
			list = new ArrayList<String>();
			nextRelation.put(targetIndex, list);
			
		}
		list.add(tupleIndex);
//		log.info("next" + nextRelation);
	}
public void forGraphSQL(Map<String, List<String>>myHashPre, Map<String, List<String>>myHashNext) {
		myHashPre = prevRelation;
		myHashNext = nextRelation;
	}
	
	
	@Override
	public int getRowCount(){
		int rowCount = 0;
		try {
			rs.last();
			rowCount = rs.getRow();
//			log.info("row count:" + rowCount);
			rs.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return rowCount;
//		return 999999;
	}

	@Override
	public int getColumnCount() {
//		try {
//			log.info("column count:" + rsmd.getColumnCount());
//			return rsmd.getColumnCount() + 1; //+ 1 because we need to add the index of tuple
			return indexList.size() + 2; // because we only need to access the part of result set
			//return indexList.size(); // because we only need to access the part of result set

			//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return 0;
	}
	
    public String getColumnName(int col) {
        try {
        	if(numRows == 0)
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
			//return rsmd.getColumnName(indexList.get(--col)); // -- make index start from 0
        	col = col - 2; 
        	log.info("col = "+(col));
        	return rsmd.getColumnName(indexList.get(col));
        } catch (SQLException e) {
			e.printStackTrace();
		}
    	
//        try {
//			//return rsmd.getColumnName(indexList.get(--col)); // -- make index start from 0
//        	log.info("col = "+(col));
//        	return rsmd.getColumnName(indexList.get(col));
//        } catch (SQLException e) {
//			e.printStackTrace();
//		}
    	
        return "";
    }


	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {

		
		if (columnIndex == 0) {
		}
		else if (columnIndex == 1) {
		}
		else{
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
		
		//log.info("row: "+rowIndex + "----------------numRows :  "+numRows);
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
		
		columnIndex = columnIndex - 2;
		
		Map<Integer, Object> map = rsList.get(rowIndex);
		Object result = map.get(indexList.get(columnIndex));
				
		//used for key is name
//		Map<String, Object> map = rsList.get(rowIndex);
//		String cName = "";
//		try {
//			cName = rsmd.getColumnName(indexList.get(columnIndex));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Object result = map.get(cName);
		
		//old
//		Object result = null;
//		
//		rowIndex = rowIndex + 1;
//
//		if(rowIndex+1 > numRows)
//		{
//			if (columnIndex == 0) {
//				return (Object)("");	
//			}
//
//			if (columnIndex == 1) {
//				String tid = "";
//				return (Object)(tid);	
//			}
//		}
//		else
//		{
//			if (columnIndex == 0) {
//				return (Object)("t" + (rowIndex+1) + "[" + stmtIndex + "]");	
//			}
//
//			if (columnIndex == 1) {
//				String tid = currentRow.getXID();
//				return (Object)(tid);	
//			}
//		}
//		
//		columnIndex = columnIndex - 2;
//		columnIndex = indexList.get(columnIndex);// -- make index start from 0
//		try {
//			rs.absolute(rowIndex);
//			result = rs.getObject(columnIndex);
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		log.info("cRow: "+cRow+" cCol: "+cCol);
//		log.info("rowIndex: "+ rowIndex+" columnIndex: "+ columnIndex);
//		if(rowIndex - 1 == cRow && columnIndex - 1 == cCol)
//		{
//			log.info("cRow: "+cRow+" cCol: "+cCol);
//			result = cValue;
//		}

//        
//		
		
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
	

	
//	public void getGraph(int level) {
//		log.info(x);
//	}
	
}
