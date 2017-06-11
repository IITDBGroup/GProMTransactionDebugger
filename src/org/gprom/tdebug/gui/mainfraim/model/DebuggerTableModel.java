package org.gprom.tdebug.gui.mainfraim.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

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
	
	public DebuggerTableModel(ResultSet rs, List<Integer> indexList, int stmtIndex, EventTimeBarRow currentRow) {
		super();
		this.rs = rs;
		try {
			rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.indexList = indexList;
		
		this.stmtIndex = stmtIndex;
		this.currentRow = currentRow;
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
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return 0;
	}
	
    public String getColumnName(int col) {
        try {
        	if (col == 0) {
        		return "Tuple Index";
        	}
        	else if (col == 1)
        		return "TransID";
			//return rsmd.getColumnName(indexList.get(--col)); // -- make index start from 0
        	col = col - 2; 
        	log.info("col = "+(col));
        	return rsmd.getColumnName(indexList.get(col));
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
		
		if (columnIndex == 1) {
			String tid = currentRow.getXID();
			return (Object)(tid);	
		}
		
//		if (rowIndex > 1) {
//			return (Object)"Dummy text";
//		}
		Object result = null;
//		log.info("get Value called" + rowIndex + "??" +columnIndex);
		columnIndex = columnIndex - 2;
		columnIndex = indexList.get(columnIndex);// -- make index start from 0
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
