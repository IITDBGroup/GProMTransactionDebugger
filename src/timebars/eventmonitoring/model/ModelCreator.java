/*
 *  File: ModelCreator.java 
 *  Copyright (c) 2004-2008  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package timebars.eventmonitoring.model;

import java.sql.ResultSet;
import java.sql.Timestamp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.gprom.tdebug.db_connection.DBManager;
import org.gprom.util.LoggerUtil;

import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.AddingTimeBarNode;
import de.jaret.util.ui.timebars.model.DefaultHierarchicalTimeBarModel;
import de.jaret.util.ui.timebars.model.DefaultRowHeader;
import de.jaret.util.ui.timebars.model.DefaultTimeBarModel;
import de.jaret.util.ui.timebars.model.DefaultTimeBarNode;
import de.jaret.util.ui.timebars.model.HierarchicalTimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarNode;

/**
 * Simple model creator creating a hierachcial or a flat model and adds some
 * events.
 * 
 * @author kliem
 * @version $Id: ModelCreator.java 801 2008-12-27 22:44:54Z kliem $
 */
public class ModelCreator {

	static Logger log = Logger.getLogger(ModelCreator.class); 
	
	static Random _random = new Random(12345);
	
	static List<String> tables = new ArrayList<String>();

	public static HierarchicalTimeBarModel createHierarchicalModel() {

		JaretDate start = new JaretDate();
		start.setDateTime(1, 1, 2009, 0, 0, 0);
		JaretDate end = new JaretDate();
		end.setDateTime(1, 2, 2009, 0, 0, 0);

		DefaultRowHeader header = new DefaultRowHeader("root");
		TimeBarNode node;
		node = new AddingTimeBarNode(header);

		CollectingTimeBarNode kat1 = new CollectingTimeBarNode(
				new DefaultRowHeader("cat1"));
		node.addNode(kat1);
		CollectingTimeBarNode kat2 = new CollectingTimeBarNode(
				new DefaultRowHeader("Cat2"));
		node.addNode(kat2);
		CollectingTimeBarNode kat3 = new CollectingTimeBarNode(
				new DefaultRowHeader("Cat3"));
		node.addNode(kat3);
		CollectingTimeBarNode kat4 = new CollectingTimeBarNode(
				new DefaultRowHeader("Cat4"));
		node.addNode(kat4);

		// kat1
		EventInterval interval = new EventInterval(start.copy(), end.copy());
		interval.setTitle("long running");
		kat1.addInterval(interval);

		DefaultTimeBarNode n = new DefaultTimeBarNode(new DefaultRowHeader(""));
		interval = new EventInterval(start.copy().advanceHours(10), start
				.copy().advanceHours(15));
		interval.setTitle("short1");
		n.addInterval(interval);
		kat1.addNode(n);

		// kat2
		n = new DefaultTimeBarNode(new DefaultRowHeader(""));
		interval = new EventInterval(start.copy().advanceHours(11), start
				.copy().advanceHours(14));
		interval.setTitle("short2");
		n.addInterval(interval);
		kat2.addNode(n);

		// kat3
		n = new DefaultTimeBarNode(new DefaultRowHeader(""));
		for (int i = 0; i < 20; i++) {
			interval = new EventInterval(start.copy().advanceHours(i * 3),
					start.copy().advanceHours(i * 3 + 1));
			interval.setTitle("short" + i);
			n.addInterval(interval);
		}
		kat3.addNode(n);

		HierarchicalTimeBarModel model = new DefaultHierarchicalTimeBarModel(
				node);

		return model;

	}

	@SuppressWarnings("deprecation")
	public static TimeBarModel createFlatModel() {

		JaretDate start = new JaretDate();
		start.setDateTime(1, 1, 2009, 0, 0, 0);
		JaretDate end = new JaretDate();
		end.setDateTime(1, 2, 2009, 0, 0, 0);
		//使用sql获取数据库信息
		ResultSet resultSet = null;
		ResultSet resultSetCommit = null;
		ResultSet resultSetIsoLevel = null;
		try {
			resultSet = DBManager.getInstance().getData();
			//resultSetCommit = DBManager.getInstance().getDataCommit();
			resultSetIsoLevel = DBManager.getInstance().getIsolationLevel();
		}
		catch (Exception e1) {
			LoggerUtil.logException(e1, log);
		}


		DefaultTimeBarModel model = new DefaultTimeBarModel();
		

			
		
		TransactionNode node;

		ArrayList<TransactionNode> nodes = new ArrayList<TransactionNode>();
		try {
			
			while (resultSet.next()) {


				//used for UNIFIED_AUDIT_TRAIL
//				byte byBuffer[] =  resultSet.getBytes(resultSet
//						.findColumn("TRANSACTION_ID"));
				
				//used for SYS.FGA_LOG$
//				byte byBuffer[] =  resultSet.getBytes(resultSet
//						.findColumn("XID"));
//
//				StringBuilder sb=new StringBuilder("");
//				
//				for (byte element: byBuffer )
//				{
//					sb.append(String.valueOf(element));
//				}
//
//				String strRead=sb.toString();
//				log.info(strRead);

				
				//添加需要的属性
				//第一个属性找到sql对应的属性名  dbusername换sql对应属性
				//used for UNIFIED_AUDIT_TRAIL
//				node = new TransactionNode(resultSet.getString(resultSet
//						.findColumn("SQL_TEXT")), strRead,
//						resultSet.getTimestamp("EVENT_TIMESTAMP"),
//						resultSet.getString("OS_USERNAME"),
//						resultSet.getString("SESSIONID"), 
//						resultSet.getString("TRANSACTION_ID"),
//						resultSet.getString("ACTION_NAME"));

				//get table name
				String tableName = resultSet.getString("OBJ$NAME");
				if(!tables.contains(tableName))
					tables.add(tableName);
				
				
				//used for SYS.FGA_LOG$
				String stmtType = resultSet.getString("STMT_TYPE");
				String actionName = null;
                if (stmtType.equals("1"))
                	actionName = "SELECT";
                else if(stmtType.equals("2"))
                	actionName = "INSERT";
                else if(stmtType.equals("4"))
                	actionName = "UPDATE";
                else if(stmtType.equals("8"))
                	actionName = "DELETE";
				
                String lsql = "";
                if(resultSet.getString("LSQL") == null)
                	lsql = resultSet.getString("LSQLTEXT");
                else
                	lsql = resultSet.getString("LSQL");
				
				node = new TransactionNode(
						//resultSet.getString(resultSet.findColumn("LSQLTEXT")), 
						//resultSet.getString("LSQLTEXT"), 
						lsql,
						resultSet.getString("SCN"),
						resultSet.getTimestamp("SCNTIME"),
						resultSet.getString("OSUID"),
						resultSet.getString("SESSIONID"), 
						resultSet.getString("XID"),
						actionName);
				
				
				nodes.add(node);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LoggerUtil.logException(e,log);
		}
		
		//print table name
		for(int i=0; i<tables.size(); i++)
			log.info("Table: " + tables.get(i));
		
		//print nodes
		for(int i=0; i<nodes.size(); i++)
			log.info("TID: "+nodes.get(i).getTransactionId() + " Time: "+ nodes.get(i).getTimeStamp());
		
		try {
			resultSetCommit = DBManager.getInstance().getDataCommit(tables);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		TransactionNode nodeCommit;
		ArrayList<TransactionNode> nodesCommit = new ArrayList<TransactionNode>();
try {

			while (resultSetCommit.next()) {
//				node = new TransactionNode(resultSet.getString(resultSet
//						.findColumn("LSQLTEXT")), resultSet.getInt(resultSet
//						.findColumn("SCN")),
//						resultSet.getTimestamp("NTIMESTAMP#"));
//				byte byBuffer1[] =  resultSetCommit.getBytes(resultSetCommit
//						.findColumn("TRANSACTION_ID"));
//				StringBuilder sb1=new StringBuilder("");
//				for (byte element: byBuffer1 )
//				{
//				sb1.append(String.valueOf(element));
//				}
//				
//				String strReadCommit=sb1.toString();
//				log.info(strRead);
				
				//添加需要的属性
				//第一个属性找到sql对应的属性名  dbusername换sql对应属性
				nodeCommit = new TransactionNode(
						resultSetCommit.getTimestamp("VERSIONS_STARTTIME"),
						resultSetCommit.getString("VERSIONS_XID"),
						resultSetCommit.getString("VERSIONS_STARTSCN"));
				nodesCommit.add(nodeCommit);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LoggerUtil.logException(e,log);
		}
		/**flash_back_nodes*/
        log.info("commit node list size: "+nodesCommit.size());
        for(int i=0; i<nodesCommit.size(); i++)
        {
        	log.info("TID: "+nodesCommit.get(i).getTransactionId() + " commit time: "+nodesCommit.get(i).getTimeStamp());
        }
        
		HashMap<String, TransactionNode> map1 = new HashMap<String, TransactionNode>();
		for(int k = 0; k < nodesCommit.size(); k++){   //Insert Transaction nodes into the hashMap.
			map1.put(nodesCommit.get(k).getTransactionId(), nodesCommit.get(k));
		}
		

		for (String key : map1.keySet()){
			log.info("flash_back_transaction_ID:bbb" + key);
		}
		
		
		
		TransactionNode nodeIsoLevel;
		ArrayList<TransactionNode> nodesIsoLevel = new ArrayList<TransactionNode>();
try {

			while (resultSetIsoLevel.next()) {				

				nodeIsoLevel = new TransactionNode(
						resultSetIsoLevel.getString("xid"),
						resultSetIsoLevel.getString("readCommmit"));
				nodesIsoLevel.add(nodeIsoLevel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LoggerUtil.logException(e,log);
		}
		/**flash_back_nodes*/
		HashMap<String, String> map2 = new HashMap<String, String>();
		for(int k = 0; k < nodesIsoLevel.size(); k++){   //Insert Transaction nodes into the hashMap.
			map2.put(nodesIsoLevel.get(k).getTransactionId(), nodesIsoLevel.get(k).getIsoLevel());
		}
		
		for (String key : map2.keySet()){
			log.info("flash_back_transaction_ID:bbb" + key);
		}
		
		
				
		/**audit_log_nodes*/
		HashMap<String, ListNode> map = new LinkedHashMap<String, ListNode>();
		for(int k = 0; k < nodes.size(); k++){   //Insert Transaction nodes into the hashMap.
			log.info("loop nodes: "+nodes.get(k).getTransactionId() + " Time: "+ nodes.get(k).getTimeStamp());
			if(!map.containsKey(nodes.get(k).getTransactionId())){
				map.put(nodes.get(k).getTransactionId(), new ListNode());
			}
			ListNode ln = map.get(nodes.get(k).getTransactionId());
			ln.AddNode(nodes.get(k));	
		}
		
		for (Entry<String, ListNode> entry : map.entrySet()) 
		{
			String key = entry.getKey();
			ListNode v = entry.getValue();
			
			if(map2.containsKey(key) && map1.containsKey(key))
			{
				if(!map2.get(key).equals("1"))
				{
					Timestamp tc = map1.get(key).getTimeStamp();
					Timestamp tb = v.GetNode(0).getTimeStamp();
					long milliseconds = tc.getTime() - tb.getTime();
					int seconds = (int) milliseconds / 1000;
					
					seconds = seconds / v.Size();
					// calculate hours minutes and seconds
					//int hours = seconds / 3600;
				    int minutes = (seconds % 3600) / 60;
				    seconds = (seconds % 3600) % 60;
				    
				    for(int i=0; i<v.Size(); i++)
				    {
				    	TransactionNode n = v.GetNode(i);
				    	Timestamp t = n.getTimeStamp();
				    	if(i > 0)
				    	{
				    		t.setMinutes(v.GetNode(i-1).getTimeStamp().getMinutes() + minutes);
				    		t.setSeconds(v.GetNode(i-1).getTimeStamp().getSeconds() + seconds);
				    	}
				    }
				}
			}
			
			
		}
		
		
		for (String key : map.keySet())
		{
			log.info("map tid: "+ key);
		}
		
//		Iterator iter = map.entrySet().iterator();
//		　　while (iter.hasNext()) {
//		　　Map.Entry entry = (Map.Entry) iter.next();
//		　　Object key = entry.getKey();
//		　　Object val = entry.getValue();
		
//		Iterator iter = map.entrySet().iterator();
		
//		for (Integer key : map.keySet()) {  
//			  
//		    Integer value = map.get(key);  
//		  
//		    log.info("Key = " + key + ", Value = " + value);  
//		  
//		} 
		
		for (String key : map.keySet())
		{
						
			log.info("begin: ");
			if(map1.containsKey(key)){
				log.info("True" + " TID is " + key );
			}
			else{
				log.info("False" + " TID is "+ key);
			}
			
			if(map1.containsKey(key)){
				
				DefaultRowHeader header = new DefaultRowHeader("T_ID "
						+ key);
				EventTimeBarRow row = new EventTimeBarRow(header);
				
				log.info("audit_log_transaction_ID: " + key);
				row.setXID(key);
				row.setStartSCN(map1.get(key).getStartSCN());
				
				if(map2.containsKey(key)){
					row.setIsoLevel(map2.get(key));
					log.info("key : isolation level -> "+ key + " : " + map2.get(key));
				}
				
//				int c1 = 0;
//				int c2 = 1;
//				
//							
				if (map.get(key).Size() == 1)
				{
					
					log.info("Only one statement!");
					log.info("transaction start time is " + map.get(key).GetNode(map.get(key).Size() - 1).getTimeStamp().toString());
					log.info("transaction commit time is " + map1.get(key).getTimeStamp().toString());
					
					
					start.setDateTime(map.get(key).GetNode(map.get(key).Size() - 1).getTimeStamp().getDate(),
							(map.get(key).GetNode(map.get(key).Size() - 1).getTimeStamp().getMonth() + 1), map.get(key).GetNode(map.get(key).Size() - 1).getTimeStamp().getYear() + 1900,
							map.get(key).GetNode(map.get(key).Size() - 1).getTimeStamp().getHours(), map.get(key).GetNode(map.get(key).Size() - 1).getTimeStamp().getMinutes(), map.get(key).GetNode(map.get(key).Size() - 1).getTimeStamp().getSeconds());
					
					end.setDateTime(map1.get(key).getTimeStamp().getDate(),
							(map1.get(key).getTimeStamp().getMonth() + 1), map1.get(key).getTimeStamp().getYear() + 1900,
							map1.get(key).getTimeStamp().getHours(), map1.get(key).getTimeStamp().getMinutes(), map1.get(key).getTimeStamp().getSeconds());					
					
					
//					EventInterval interval = new EventInterval(start.copy().advanceHours(c1), end.copy().advanceHours(c2));
				    EventInterval interval = new EventInterval(start.copy().advanceHours(0), end.copy().advanceHours(0));
					interval.setSql(map.get(key).GetNode(map.get(key).Size() - 1).getQuery());
					interval.setTitle(map.get(key).GetNode(map.get(key).Size() - 1).getActionName());
				    interval.setOsName(map.get(key).GetNode(map.get(key).Size() - 1).getOsUser());
					interval.setSessionId(map.get(key).GetNode(map.get(key).Size() - 1).getSessionId());
					interval.setType(map.get(key).GetNode(map.get(key).Size() - 1).getActionName());
					interval.setSCN(map.get(key).GetNode(map.get(key).Size() - 1).getSCN());
					row.addInterval(interval);
				}
//	04000C00F9090000			
				else
				{
					log.info("More statements!");
					for (int g = 0; g < map.get(key).Size() - 1; g++)
					{
						log.info("transaction node " + key);
						log.info("The " + g + " start time is " + map.get(key).GetNode(g).getTimeStamp().toString());
						log.info("The " + g + " end   time is " + map.get(key).GetNode(g + 1).getTimeStamp().toString());
						log.info("The commit time is " + map1.get(key).getTimeStamp().toString());						
						
						start.setDateTime(map.get(key).GetNode(g).getTimeStamp().getDate(),
								(map.get(key).GetNode(g).getTimeStamp().getMonth() + 1), map.get(key).GetNode(g).getTimeStamp().getYear() + 1900,
								map.get(key).GetNode(g).getTimeStamp().getHours(), map.get(key).GetNode(g).getTimeStamp().getMinutes(), map.get(key).GetNode(g).getTimeStamp().getSeconds());
						
						end.setDateTime(map.get(key).GetNode(g + 1).getTimeStamp().getDate(),
								(map.get(key).GetNode(g + 1).getTimeStamp().getMonth() + 1), map.get(key).GetNode(g + 1).getTimeStamp().getYear() + 1900,
								map.get(key).GetNode(g + 1).getTimeStamp().getHours(), map.get(key).GetNode(g + 1).getTimeStamp().getMinutes(), map.get(key).GetNode(g + 1).getTimeStamp().getSeconds());
						
					//	EventInterval interval = new EventInterval(start.copy().advanceHours(c1), end.copy().advanceHours(c2));
						EventInterval interval = new EventInterval(start.copy().advanceHours(0), end.copy().advanceHours(0));
//						c1++;
//						c2++;
						interval.setSql(map.get(key).GetNode(g).getQuery());
						interval.setTitle(map.get(key).GetNode(g).getActionName());
						interval.setOsName(map.get(key).GetNode(g).getOsUser());
						interval.setSessionId(map.get(key).GetNode(g).getSessionId());
						interval.setType(map.get(key).GetNode(g).getActionName());
						interval.setSCN(map.get(key).GetNode(g).getSCN());
						row.addInterval(interval);
					}
					start.setDateTime(map1.get(key).getTimeStamp().getDate(),
					      (map1.get(key).getTimeStamp().getMonth() + 1), map1.get(key).getTimeStamp().getYear() + 1900,
					       map1.get(key).getTimeStamp().getHours(), map1.get(key).getTimeStamp().getMinutes(), map1.get(key).getTimeStamp().getSeconds());
					
					//EventInterval interval = new EventInterval(end.copy().advanceHours(c1), start.copy().advanceHours(c2));
					EventInterval interval = new EventInterval(end.copy().advanceHours(0), start.copy().advanceHours(0));
					interval.setSql(map.get(key).GetNode(map.get(key).Size() - 1).getQuery());
					interval.setTitle(map.get(key).GetNode(map.get(key).Size() - 1).getActionName());
					interval.setOsName(map.get(key).GetNode(map.get(key).Size() - 1).getOsUser());
					interval.setSessionId(map.get(key).GetNode(map.get(key).Size() - 1).getSessionId());
					interval.setType(map.get(key).GetNode(map.get(key).Size() - 1).getActionName());
					interval.setSCN(map.get(key).GetNode(map.get(key).Size() - 1).getSCN());
					row.addInterval(interval);
				}
				model.addRow(row);
				
		}
		}
		
//		for (Map.Entry<String, ListNode> entry : map.entrySet() ){
//			
//			DefaultRowHeader header = new DefaultRowHeader("T_ID "
//					+ entry.getKey());
//			EventTimeBarRow row = new EventTimeBarRow(header);
//			log.info("aaa");
//			for (int g = 0; g < entry.getValue().Size() - 1; g++){
//				log.info(entry.getValue().GetNode(g).getTimeStamp().toString());
//				log.info(entry.getValue().GetNode(g + 1).getTimeStamp().toString());
//				
//				start.setDateTime(entry.getValue().GetNode(g).getTimeStamp().getDate(),
//						(entry.getValue().GetNode(g).getTimeStamp().getMonth() + 1), entry.getValue().GetNode(g).getTimeStamp().getYear() + 1900,
//						entry.getValue().GetNode(g).getTimeStamp().getHours(), entry.getValue().GetNode(g).getTimeStamp().getMinutes(), 0);
//				
//				end.setDateTime(entry.getValue().GetNode(g + 1).getTimeStamp().getDate(),
//						(entry.getValue().GetNode(g + 1).getTimeStamp().getMonth() + 1), entry.getValue().GetNode(g + 1).getTimeStamp().getYear() + 1900,
//						entry.getValue().GetNode(g + 1).getTimeStamp().getHours(), entry.getValue().GetNode(g + 1).getTimeStamp().getMinutes(), 0);
//				
//				EventInterval interval = new EventInterval(start.copy().advanceHours(-2), end.copy().advanceHours(-2));
//				interval.setTitle(entry.getValue().GetNode(g).getQuery());
//				interval.setOsName(entry.getValue().GetNode(g).getOsUser());
//				interval.setSessionId(entry.getValue().GetNode(g).getSessionId());
//				row.addInterval(interval);
//			}
//			
//			EventInterval interval = new EventInterval(end.copy().advanceHours(-2), end.copy().advanceHours(-3));
//			interval.setTitle(entry.getValue().GetNode(entry.getValue().Size() - 1).getQuery());
//			interval.setOsName(entry.getValue().GetNode(entry.getValue().Size() - 1).getOsUser());
//			interval.setSessionId(entry.getValue().GetNode(entry.getValue().Size() - 1).getSessionId());
//			row.addInterval(interval);	
//			
//			model.addRow(row);
//		}
//		

		
		
		
		
////
//		Map<Integer, Integer> map = new HashMap<Integer, Integer>();  
//		  
//		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {  
//		  
//		    log.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
//		  
//		} 
//		map.put("123", new ListNode());
//		map.put("234", new ListNode());
//		ListNode ln = map.get("123");
//		ListNode ln2 = map.get("234");
//		ln.PrintList();
//		ln.AddNode(new TransactionNode( ));
//		TransactionNode n1 = ln.GetNode(0);
//		n1.PrintNode();
//		log.info(ln.Remove(n1));
//		log.info(ln.IsEmpty());
		
		
		
		
		
		
		
		
		
//
//		for (int i = 0; i < nodes.size(); i++) {
//			DefaultRowHeader header = new DefaultRowHeader("T_ID "
//					+ nodes.get(i).getSCN());
//			EventTimeBarRow row = new EventTimeBarRow(header);
//			
//			for (int j = i; j < nodes.size(); j++) {
//				if (nodes.get(i).getSCN() == nodes.get(j).getSCN()) {
//		
//					//设置date
//					
////					nodes.get(j).getTimeStamp()
////					log.info("dd:"+nodes.get(j).getTimeStamp());
////					log.info("ee:"+nodes.get(j).getTimeStamp().getDate());
//					
//					start.setDateTime(nodes.get(j).getTimeStamp().getDate(),
//							(nodes.get(j).getTimeStamp().getMonth() + 1), nodes
//									.get(j).getTimeStamp().getYear() + 1900,
//							nodes.get(j).getTimeStamp().getHours(), nodes
//									.get(j).getTimeStamp().getMinutes(), 0);
//					
//					EventInterval interval = new EventInterval(start.copy()
//							.advanceHours(-2), start.copy().advanceHours(5));
//					
//					
////					start.setDateTime(nodes.get(j).getTimeStamp().;
//					
//					
////					start.setDate(nodes.get(j).getTimeStamp(1));
////					start.setDate(nodes.get(j).getTimeStamp().getTime());
////					start.setDate(nodes.get(j).getTimeStamp().toLocalDateTime());
////					start.setDate(new java.sql.Timestamp(new java.util.Date().getTime()));
////					
//					
////					EventInterval interval = new EventInterval(start.copy()
////							.advanceHours(10), start.copy().advanceHours(15));
//					
//					interval.setTitle(nodes.get(j).getQuery());
//					interval.setOsName(nodes.get(j).getOsUser());
//					interval.setSessionId(nodes.get(j).getSessionId());
//					row.addInterval(interval);
////					nodes.remove(j);
//				}
//			}
//			model.addRow(row);
//		}
		return model;
	}

}
