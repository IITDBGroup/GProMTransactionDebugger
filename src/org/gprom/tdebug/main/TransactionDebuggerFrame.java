package org.gprom.tdebug.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.gprom.tdebug.cli_process.GpromProcess;
import org.gprom.tdebug.db_connection.DBManager;
import org.gprom.tdebug.gui.mainfraim.model.DebuggerTableModel;
import org.gprom.tdebug.gui.optframe.OptimizationInternalsFrame;
import org.gprom.util.LoggerUtil;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.view.View;

import org.graphstream.ui.view.Viewer;

import timebars.eventmonitoring.model.EventInterval;
import timebars.eventmonitoring.model.EventTimeBarRow;






public class TransactionDebuggerFrame extends JFrame implements ActionListener, ComponentListener, MouseListener, TableModelListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 869036626552676746L;

	private final static int DEBUGGER_LEFT_PADDING = 135;
	private final static int DEBUGGER_CELL_WIDTH = 295;
	private final static int XFORBUTTONS = 1060;
	private final static int WIDTHFORMAINSCROLLPANE = DEBUGGER_CELL_WIDTH * (3) + 8;
	private final static int HEIGHTFORMAINSCROLLPANE = 245;
	private final static int WIDTHFORGRAPHPANEL = 893;
	private final static int HEIGHTFORGRAPHPANEL = 250; 
	private final static int ADD = 200;
	private final static int YEACHROWTABLEPANEL = 100; 

	static Logger log = Logger.getLogger(TransactionDebuggerFrame.class);
	
	private JButton reset_button = null;
	private JButton show_affected_button = null;
	private JButton show_all_button = null;
	private JButton opt_internal_button = null;
	private JButton add_stmt_button = null;
	private JButton del_stmt_button = null;
	private JButton whatif_button = null;
	
	Map<String, List<String>> nextT = new HashMap<String, List<String>>();
	Map<String, List<String>> prevT = new HashMap<String, List<String>>();
	Map<Node, Node> mapNode = new HashMap<>();
	
	
	JScrollPane main_scrollPane = null;
	JPanel panel_view = null;
	JLabel imageLabel = null;
	JPanel stmt_table_panel = null;
	
	//used to store label (SQL, table, graph) in the first column
	JPanel panel_table = null;
	JPanel panel_graph = null;
	JPanel panel_SQL = null;
	
	
	private int initialWidth = 0;
	private int initialHeight = 0;
	private List<JButton> buttons = new ArrayList<JButton>();
	private List<JTable> tables = new ArrayList<JTable>();
	private List<DebuggerTableModel> tableModels = new ArrayList<DebuggerTableModel>();
	private List<JTextArea> sqlTextAreas = new ArrayList<JTextArea>();
	private List<JLabel> sqlLabels = new ArrayList<JLabel>();
	private List<Integer> numUps = new ArrayList<Integer>();
	
	private List<String> distinctTableNames = new ArrayList<String>(); //e.g., 0->R, 1->S , used to know following list element belong to which table
	//store each list in a list, each element is for different table, e.g., (R) 0->List<JTable>, (S) 1->List<JTable> 
	private List<List<JTable>> lTables = new ArrayList<List<JTable>>();
	private List<List<DebuggerTableModel>> lTableModels = new ArrayList<List<DebuggerTableModel>>();
	//private List<List<JTextArea>> lSqlTextAreas = new ArrayList<List<JTextArea>>();
	//private List<List<JLabel>> lSqlLabels = new ArrayList<List<JLabel>>();
	private List<List<Integer>> lNumUps = new ArrayList<List<Integer>>();
	
	private EventTimeBarRow currentRow;
	
	private ResultSet rsReset;
	
	//store all old values of first table, when table data changed get matched old value from here 
	private Map<Integer, List<Object>> old = new HashMap<Integer, List<Object>>();
	private Map<Integer, List<Object>> oldMap = new HashMap<Integer, List<Object>>();
	private Map<Integer, List<Object>> newMap = new HashMap<Integer, List<Object>>();
	
	//store each list in a list, each element is for different table
//	private List<Map<Integer, List<Object>>> lOld = new ArrayList<Map<Integer, List<Object>>>();
//	private List<Map<Integer, List<Object>>> lOldMap = new ArrayList<Map<Integer, List<Object>>>();
//	private List<Map<Integer, List<Object>>> lOewMap = new ArrayList<Map<Integer, List<Object>>>();
	private Map<String, Map<Integer, List<Object>>> lOld = new HashMap<String, Map<Integer, List<Object>>>();
	private Map<String, Map<Integer, List<Object>>> lOldMap = new HashMap<String, Map<Integer, List<Object>>>();
	private Map<String, Map<Integer, List<Object>>> lNewMap = new HashMap<String, Map<Integer, List<Object>>>();
	
	private String storeSql = "";
	private Map<String, String> storeSqlList = new HashMap<String, String>();	
	private boolean clickWhatIf = false;
	private Map<String, Map<Integer, List<Object>>> lStmtMap = new HashMap<String, Map<Integer, List<Object>>>();

	
	private List<String> tableNames = new ArrayList<String>();
	private List<Boolean> tableEmptyFlagList = new ArrayList<Boolean>();
	private Map<String, Integer> tNameCount = new HashMap<String, Integer>();
	
	private static int totalNumtables = -1;

		
	public TransactionDebuggerFrame(EventTimeBarRow row, Map<String, Integer> tNameCount, List<String> tableNames, List<String> distinctTableNames )
	{
		super();
		this.currentRow = row;
	    this.tNameCount = tNameCount;
	    this.tableNames = tableNames;
	    this.distinctTableNames = distinctTableNames;
		setup();
		setupListeners();
		initialWidth = this.getWidth();
		initialHeight = this.getHeight();
	}
	
	private static List<Map<String, Object>> convertListS(ResultSet rs) throws SQLException {
	    rs.beforeFirst();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    ResultSetMetaData md = rs.getMetaData();
	    int columnCount = md.getColumnCount();
	    while (rs.next()) {
	        Map<String, Object> rowData = new HashMap<String, Object>();
	        for (int i = 1; i <= columnCount; i++) {
	            rowData.put(md.getColumnName(i), rs.getObject(i));
	        }
	        list.add(rowData);
	    }
	    rs.beforeFirst();
	    return list;
	}
	
	/*  list - each element is a map key: column number, value: object
	 *   0 (row 0) -store- <column 1, value> <column 2, value>   
	 *   1 ..
	 *   2 ..
	 */
	
	private static List<Map<Integer, Object>> convertList(ResultSet rs) throws SQLException {
		rs.beforeFirst();
		List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
	    ResultSetMetaData md = rs.getMetaData();
	    int columnCount = md.getColumnCount();
	    while (rs.next()) {
	        Map<Integer, Object> rowData = new HashMap<Integer, Object>();
	        for (int i = 1; i <= columnCount; i++) {
	            rowData.put(i, rs.getObject(i));
	        }
	        list.add(rowData);
	    }
	    rs.beforeFirst();
	    return list;
	}
	
	private void setupStatementOrder(ResultSet rs, List<Map<Integer, Object>> rsList, String tableName) throws SQLException
	{
		List<Integer> stmtIndexList = new ArrayList<Integer>();
	    for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
	    {
	    	for(int j=1; j<=currentRow.getIntervals().size(); j++)
	    	{
	    		if (Pattern.matches("PROV_U"+ j + "_(up|ins)", rs.getMetaData().getColumnName(i)))
	    		{
	    			log.info("stmt index i = "+i+" j = " + j +" col name = "+ rs.getMetaData().getColumnName(i));
	    			stmtIndexList.add(i);
	    		}
	    	}
	    }

	    //stmtMap like:		  U1 U2 U3   (key is row number)
	    // key: 1     value:   0  0  1
	    // key: 2     value:   1  0  0
	    // key: 3     value:   NULL 1 0
	    Map<Integer, List<Object>> stmtMap = new HashMap<Integer, List<Object>>();
        for(int i = 0; i < rsList.size(); i++)
        {
        	Map<Integer, Object> tempMap = rsList.get(i);
        	List<Object> tempList = new ArrayList<Object>();
        	for(int j=0; j<stmtIndexList.size(); j++)
        		tempList.add(tempMap.get(stmtIndexList.get(j)));
        	stmtMap.put(i+1, tempList);
        	
        	for(int s=0; s<tempList.size(); s++)
        		log.info("list: "+i+ " value: "+ tempList.get(s));
        }
        
        lStmtMap.put(tableName, stmtMap);
	}

	private void setup()
	{
		// load dot png
		// DotWrapper dw = new DotWrapper();
		// try {
		// dw.init();
		// } catch (URISyntaxException e1) {
		// e1.printStackTrace();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		
		totalNumtables = (currentRow.getIntervals().size() + 1) * distinctTableNames.size();
		log.info("Total number of tables is : " + totalNumtables + " Each row contains the number of tables : " + totalNumtables/tableNames.size()
		+" tableNames size: "+distinctTableNames.size());

		stmt_table_panel = new JPanel();
		stmt_table_panel
				.setPreferredSize(new Dimension(DEBUGGER_CELL_WIDTH * (currentRow.getIntervals().size() + 1), 300));
		main_scrollPane = new JScrollPane(stmt_table_panel);
		
		int heightMainScrollPanel = HEIGHTFORMAINSCROLLPANE;
		if(tNameCount.size() > 1)
			heightMainScrollPanel = heightMainScrollPanel  + ADD;	
		main_scrollPane.setBounds(DEBUGGER_LEFT_PADDING, 5, WIDTHFORMAINSCROLLPANE, heightMainScrollPanel + 70);
		
//---------------in stmt_table_panel-------------- first line (original + each statement) ----------------------------------------
		JPanel jp1 = new JPanel();
		JTextArea ja1 = new JTextArea(5, 20);
		// ja1.setFont(Bold);
		
		String originalTable_str = "\n\n Original Table";
		
		//display transaction
		for (int i = 0; i < currentRow.getIntervals().size(); i++)
		{
			EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
			String originalStmt = currentInterval.getSql();
			log.info(originalStmt);
			JPanel jp = new JPanel();
			
			JLabel jb = new JLabel();
			int updateIndex = i + 1;
			jb.setText("U"+ updateIndex);
			Font lfont = new Font("Courier", Font.BOLD, 18);
			jb.setFont(lfont);
			sqlLabels.add(jb);
			//jb.setForeground(Color.RED);
			
			JTextArea ja = new JTextArea(5, 20);
			ja.setEditable(true);
			ja.setText(originalStmt);
			ja.setLineWrap(true);
			sqlTextAreas.add(ja);
			jp.add(jb);
			jp.add(ja);
			stmt_table_panel.add(jp);
			jp.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
			jp.setBounds(DEBUGGER_CELL_WIDTH * (i + 1), 0, 300, 95);
			ja.setBounds(0, 10, 5, 20);
		}

		ja1.setText(originalTable_str);
		//ja1.setCaretColor(Color.BLUE);
		ja1.setEditable(false);
		jp1.add(ja1);

		this.setLayout(null);
		stmt_table_panel.setLayout(null);
		stmt_table_panel.add(jp1);

		jp1.setBounds(0, 0, 300, 95);
		jp1.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		ja1.setBounds(0, 10, 5, 20);

//----------stmt_table_panel--------------------- second line (each table)----------------------------------------
		
		//for(int t=0; t<tableNames.size(); t++)
		int tableRowIndex = 0;
		int yTablePanel = YEACHROWTABLEPANEL;
		numUps.clear();
		for (Entry<String, Integer> ncEntry : tNameCount.entrySet()) //how many update in result sql (number of tuples were updated)
		{
			tableRowIndex ++;
			
			// add GProm query result
			String sql = null;
			try {
				sql = GpromProcess.getTransactionIntermediateSQL(currentRow.getXID(), ncEntry.getKey());
			}
			catch (Exception e3) {
				LoggerUtil.logException(e3, log);
			}
			//String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
			ResultSet rs = DBManager.getInstance().executeQuery(sql);

			//store rs data into a list map and store column name into a list

			List<Map<Integer, Object>> rsList = null;
			List<String> rsNameList = new ArrayList<String>();
			rsNameList.add(" ");
			try {
				rsList = convertList(rs);		
				for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
				{
					rsNameList.add(rs.getMetaData().getColumnName(i));	
					log.info("name: "+rs.getMetaData().getColumnName(i));
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}		


			
			//get how many tuples in the initial table
			//num of update if update based on diff column
			int numUp = 0;
			int pos = -1;
			String nameKey = "";
			
			//get how many not null tuples in this table (numUp)
			try {
				//rs.first();
				//log.info("test: list size "+rsListS.size());
				int cont = 0;
				rs.beforeFirst();
				while(rs.next())
				{
					cont++;
					for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
					{
						log.info("row: "+cont + " name: "+ rs.getMetaData().getColumnName(i) + " value: " + rs.getString(i));
						if(Pattern.matches("PROV_(?!U|query).*", rs.getMetaData().getColumnName(i)))
						{
							pos = i;
							break;	
						}
					}
					if(pos != -1)
					{
						String v = rs.getString(pos);
						log.info("v: "+ v);
						if(v != null)
							numUp++;	
					}				
				}
				
				
				//get statement orders
				setupStatementOrder(rs, rsList, ncEntry.getKey());
								

				log.info("cont: "+cont);
				log.info("numUp: "+numUp);
				//rs.first();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				rs.close();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			log.info("current row size : "+currentRow.getIntervals().size());
			//for (int i = 0; i < currentRow.getIntervals().size() + 1; i++)
			//for(int i=0; i<ncEntry.getValue()+1;i++)
			for(int i=0; i<tableNames.size() + 1;i++)
			{
				// set up indexList
				List<Integer> indexList = new ArrayList<Integer>();
				String currentTableName = null;

				for (int j = 1; j < rsNameList.size(); j++)
				{
					if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsNameList.get(j)))
					{
						log.info("i=0, j = "+j);
						indexList.add(j);
					} 
					else if (Pattern.matches("PROV_U" + i + "__.*", rsNameList.get(j)))
					{
						log.info("i !=0, j = "+j);
						indexList.add(j);
					}		
				}

				// tablename
				currentTableName = ncEntry.getKey();


				if(i > 0)
				{
					EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
					String sqlType = currentInterval.getType();
					if(sqlType.equals("INSERT"))
						numUp++;
				}
				numUps.add(numUp);			
				//DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp, tables);
				
				//if showTableFlag, this table should be empty
				boolean showTableFlag = false;				
				if(i > 0)
					if(!currentTableName.equals(tableNames.get(i-1)))
						showTableFlag = true;
				
				tableEmptyFlagList.add(showTableFlag);
				DebuggerTableModel tm = new DebuggerTableModel(rsList, indexList, i, currentRow, numUp, rsNameList, showTableFlag, currentTableName);
				tableModels.add(tm);
				JTable table = new JTable(tm);
				table.setName(currentTableName);
			
				JPanel jp = new JPanel();
				jp.setLayout(null);
				
				log.info("tableRowIndex: "+tableRowIndex);	
				log.info("YEACHROWTABLEPANEL: "+ yTablePanel);	
				jp.setBounds(DEBUGGER_CELL_WIDTH * i, yTablePanel, 300, 205);

				jp.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
				stmt_table_panel.add(jp);
				

				table.setSelectionBackground(new Color(50, 205, 50));

				tables.add(table);
				JScrollPane scrollPane = new JScrollPane(table);
				//scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray,3));
				scrollPane.setBounds(5, 20, 290, 185);

				table.setFillsViewportHeight(true);
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				JLabel tableName = new JLabel(currentTableName, JLabel.CENTER);
				tableName.setBounds(0, 0, 300, 20);
				jp.add(tableName);
				jp.add(scrollPane);

				// setup Provenance map
				//			try
				//			{
				//				rs.first();
				//				int countRowNum = 0;
				//				while (true)
				//				{
				//					if (i >= currentRow.getIntervals().size() + 1 || i == 0)
				//						break; // i start from 0, but U" " start from 1, we only
				//								// need to add map for tables except the first
				//					log.info("test outpu4");
				//					int flag = rs.getInt("U" + (i));
				//					log.info("output" + flag + "?" + i);
				//					log.info("test output");
				//
				//					if (flag == 1)
				//					{
				//						DebuggerTableModel model = (DebuggerTableModel) table.getModel();
				//						model.setPrevTupleIndex("t" + (countRowNum + 1) + "[" + (i) + "]",
				//								"t" + (countRowNum + 1) + "[" + (i - 1) + "]");
				//						// if (tables.size() >= 2) {
				//						// DebuggerTableModel lastModel = (DebuggerTableModel)
				//						// tables.get(tables.size() - 2).getModel();
				//						// lastModel.setNextTupleIndex(targetIndex, tupleIndex);
				//						// }
				//
				//					}
				//					if (!rs.next())
				//						break;
				//					countRowNum++;
				//				}
				//				rs.first();
				//
				//			} catch (SQLException e)
				//			{
				//				log.info(e);
				//			}

				//			log.info("tableIndex: " + i);
				//			log.info("prev" + tm.getPrevRelation());
				//			log.info("next" + tm.getNextRelation());
				//			prevT = tm.getPrevRelation();
				//			nextT = tm.getNextRelation();
			}
			

			yTablePanel = yTablePanel + ADD;
			
			this.add(main_scrollPane);			
		}
		
		//set the table not in the first column is unable to edit
		int numCell = totalNumtables/tNameCount.size();
		
		for(int t=0,i=0; t<tables.size(); t++,i++)
		{
			if(i == numCell)
				i=0;
			
			if(i != 0)
				tables.get(t).setEnabled(false);
		}
		
		
		//store all old value of the first table
		for(int i=0,t=0; t<tNameCount.size(); i = i + numCell,t++)
		{
			log.info("i: "+i);
			JTable firstTable = tables.get(i);
			int storeColCount = firstTable.getColumnCount();
			int storeRowCount = firstTable.getRowCount();
			log.info("storeRowCount: " + storeRowCount + " storeColCount: " + storeColCount);

		    Map<Integer, List<Object>> old1 = new HashMap<Integer, List<Object>>();
			for(int r=0; r<storeRowCount; r++)
			{
				List<Object> l = new ArrayList<Object>();
				for(int c=2; c<storeColCount; c++)
					l.add(firstTable.getValueAt(r, c));
				old1.put(r, l);				
			}	
			lOld.put(firstTable.getName(), old1);
		}
			
		//print
		log.info("----------print old hasp map------------");
		for (Entry<String, Map<Integer, List<Object>>> entry : lOld.entrySet()) {
			String s = entry.getKey();
			Map<Integer, List<Object>> cl = entry.getValue();              	 
			log.info("table: " + s);

			for (Entry<Integer, List<Object>> entry1 : cl.entrySet()) {
				int r = entry1.getKey();
				List<Object> l = entry1.getValue();
				for(int ii=0; ii<l.size(); ii++)
					log.info("Row: "+ r +" Old v: "+ l.get(ii));   
			}              	    	
		}
		
		//print flag list
		for(int i=0; i<tableEmptyFlagList.size(); i++)
			log.info("table is empty ? " + tableEmptyFlagList.get(i));
		
		//used to show the graph view after click each row in the table
		panel_view = new JPanel();
		
		int yPanelView = 320;
		if(tableRowIndex > 1)
			yPanelView = 520;
		
		panel_view.setBounds(DEBUGGER_LEFT_PADDING, yPanelView, WIDTHFORGRAPHPANEL, 220);
		panel_view.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		this.add(panel_view);
		
//      comment out old graph					
//		GUIUtility.createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/dotFlow.png",
//				"/Users/xun/Documents/java_workspace/gprom-gui/test2.png", 300, 400);
//		File file = new File(new File(this.getClass().getResource("/").getPath()).getParent() + "/test2.png"); // use
//																												// relative
//																												// path
//		BufferedImage image;
//		try
//		{
//			image = ImageIO.read(file);
//			imageLabel = new JLabel(new ImageIcon(image));
//			imageLabel.setBounds(graphPanel.getWidth() / 2 - 300 / 2, 0, 450, 250);
//
//			// JLabel label2 = new JLabel(new ImageIcon(image));
//			// JLabel label3 = new JLabel(new ImageIcon(image));
//
////			graphPanel.add(imageLabel);
//			// jp8.add(label2);
//			// jp9.add(label3);
//
//			// label1.setBounds(0, 10, 5, 10);
//
//		} catch (IOException e1)
//		{
//			e1.printStackTrace();
//		}
		//this.add(graphPanel);

		// first column label name
		panel_SQL = new JPanel();
		panel_table = new JPanel();
		panel_graph = new JPanel();
		
		panel_table.setLayout(null);
		panel_graph.setLayout(null);
		
		panel_SQL.setBounds(35, 5, 100, 95);
		panel_SQL.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		
		if(tNameCount.size() == 1)
		{
			panel_table.setBounds(35, 100, 100, 220);		
			panel_graph.setBounds(35, 320, 100, 220);
		}
		else
		{
			panel_table.setBounds(35, 100 , 100, 220 + (tNameCount.size() - 1) * 200);		
			panel_graph.setBounds(35, 320 + (tNameCount.size() - 1) * 200, 100, 220);
		}
		
		panel_table.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		panel_graph.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		
		this.add(panel_SQL);
		this.add(panel_table);
		this.add(panel_graph);
		
		JLabel jl1 = new JLabel("SQL");
		JLabel jl2 = new JLabel("TABLE");
		JLabel jl3 = new JLabel("GRAPH");
		
		jl2.setBounds(35, 5, 100, 10);
		jl3.setBounds(35, 5, 100, 10);
		
		panel_SQL.add(jl1);
		panel_table.add(jl2);
		panel_graph.add(jl3);
		
		
		// Right buttons show_all_button
		reset_button = new JButton("Reset");
		show_affected_button = new JButton("<html>&nbsp;&nbsp;Show<br>Affected</html>");
		show_all_button = new JButton("ShowAll");
		opt_internal_button = new JButton("<html>Optimizer<br>Internals</html>");
		add_stmt_button = new JButton("<html>&nbsp;&nbsp; Add<br>Statement</html>");
		del_stmt_button = new JButton("<html>&nbsp; Delete<br>Statement</html>");
		whatif_button = new JButton("What-If");

		// jb3.setHorizontalAlignment(JButton.RIGHT);

		reset_button.setBounds(XFORBUTTONS, 10, 105, 55);
		show_affected_button.setBounds(XFORBUTTONS, 65, 105, 55);
		show_all_button.setBounds(XFORBUTTONS, 120, 105, 55);
		opt_internal_button.setBounds(XFORBUTTONS, 175, 105, 55);
		add_stmt_button.setBounds(XFORBUTTONS, 230, 105, 55);
		del_stmt_button.setBounds(XFORBUTTONS, 285, 105, 55);
		whatif_button.setBounds(XFORBUTTONS, 340, 105, 55);
		buttons.add(reset_button);
		buttons.add(show_affected_button);
		buttons.add(show_all_button);
		buttons.add(opt_internal_button);
		buttons.add(add_stmt_button);
		buttons.add(del_stmt_button);
		buttons.add(whatif_button);

		this.add(reset_button);
		this.add(show_affected_button);
		this.add(show_all_button);
		this.add(opt_internal_button);
		this.add(add_stmt_button);
		this.add(del_stmt_button);
		this.add(whatif_button);
		this.setVisible(true);
	}

	private void setupListeners()
	{
		reset_button.addActionListener(this);
		show_affected_button.addActionListener(this);
		show_all_button.addActionListener(this);
		opt_internal_button.addActionListener(this);
		add_stmt_button.addActionListener(this);
		del_stmt_button.addActionListener(this);
		whatif_button.addActionListener(this);
		this.addComponentListener(this);
		for (int i = 0; i < tables.size(); i++)
		{
			tables.get(i).addMouseListener(this);
			tables.get(i).getModel().addTableModelListener(this);
		}

	}


	@Override
	public void actionPerformed(ActionEvent e)
	{		
		if(e.getSource() == del_stmt_button)
		{
		}

		if (e.getSource() == whatif_button)
		{

			if(lNewMap.size() > 0)
			{
				clickWhatIf = true;  //user should click what-if button firstly, then user can click show affected or show all button
				log.info("-----------------------------click what if button---------------------------");

				int numCell = totalNumtables/tNameCount.size();
				log.info("numCell: "+numCell+" totalNumtables: "+totalNumtables);
				numUps.clear();
				lStmtMap.clear();
				for(int t = 0, c = 0; t<distinctTableNames.size(); c = c + numCell, t++)
				{

					log.info("numCell: "+numCell+" totalNumtables: "+totalNumtables+" tNameCount size: "+tNameCount.size());
					log.info("cc: "+c);
					String appendSql = "";
					int col1 = -1;
					int row1 = -1;
					Object s1 = null;

					//e.g., used to add "OPTIONS (NO PROVENANCE AS OF SCN 1425819) 
					//UPDATE R SET A=300,B=4 WHERE A=10 AND B=4;" in front of sql
					//			appendSql = "UPDATE ";

					List<String> colNames = new ArrayList<String>();  //store original column names: e.g., A, B

					JTable firstTable = tables.get(c);
					String tableName = firstTable.getName();				
					int colCount = firstTable.getColumnCount();	

					if(lNewMap.containsKey(tableName))
					{

						for(int i=2; i<colCount; i++)
						{
							String cName = firstTable.getModel().getColumnName(i);
							Pattern p = Pattern.compile("PROV_(\\w*)_(\\w*)"); 
							Matcher m = p.matcher(cName);
							if (!m.find())
							{
								log.info("regular expression succeed!");
							}
							else
							{
								log.info("group 1: " + m.group(1) + " group 2: "+m.group(2));
								//tableName = m.group(1);
								cName = m.group(2);
								colNames.add(cName);
							}
						}

						log.info("new map size: "+ newMap.size());

						Map<Integer, List<Object>> newMap1 = lNewMap.get(tableName);
						Map<Integer, List<Object>> oldMap1 = lOldMap.get(tableName);
						for (Entry<Integer, List<Object>> entry : newMap1.entrySet()) //how many update in result sql (number of tuples were updated)
						{
							int k = entry.getKey();
							List<Object> newV = entry.getValue();
							List<Object> oldV = oldMap1.get(k);
							log.info("new list size: "+ newV.size());
							log.info("old list size: "+ oldV.size());

							appendSql = "UPDATE ";
							//				String tableName = "";
							String setClause = "SET ";
							String wheClause = "WHERE ";

							for(int i=0; i<newV.size(); i++)
							{
								setClause = setClause + colNames.get(i) + " = " + newV.get(i);
								wheClause = wheClause + colNames.get(i) + " = " + oldV.get(i);

								if(i+1 != newV.size())
								{
									setClause = setClause + " , ";
									wheClause = wheClause + " AND ";
								}

							}

							appendSql = appendSql + tableName + " " + setClause + " " + wheClause + ";";
							log.info("sql = "+ appendSql);				
						}


						int count = tableModels.size();
						log.info("tableModels size: "+count);

						int countSqls = sqlTextAreas.size();
						log.info("countTextAreas: "+countSqls);

						String newSql = "";

						if(currentRow.getIsoLevel().equals("1"))
						{
							for(int i=0; i<countSqls; i++)
							{
								EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
								String scn_rc = currentInterval.getSCN();
								newSql = newSql + "OPTIONS (AS OF SCN " + scn_rc + ") "  + sqlTextAreas.get(i).getText() + "; ";
							}

							if(newMap1.size() != 0)
								newSql = "OPTIONS (NO PROVENANCE AS OF SCN " + currentRow.getStartSCN() + ") " + appendSql + " " + newSql;
						}
						else
						{
							for(int i=0; i<countSqls; i++)
							{
								EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
								String scn_rc = currentInterval.getSCN();
								newSql = newSql + sqlTextAreas.get(i).getText() + "; ";
							}

							if(newMap1.size() != 0)
								newSql = "OPTIONS (NO PROVENANCE) " + appendSql + " " + newSql;
						}
						log.info("new sqls: "+newSql);

						//store sql
						storeSqlList.put(tableName,newSql);

						//String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
						String sql = "";
						if(currentRow.getIsoLevel().equals("1")) {
							try {
								sql = GpromProcess.getReenactSQL(currentRow.getStartSCN(),newSql, tableName);
							}
							catch (Exception e3) {
								LoggerUtil.logException(e3, log);
							}
						}
						else //serializable //TODO construct appendsql for this one and add case if newMap size equal to 0
						{
							//if serializable, each scn same, get first one
							EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(0);
							try {
								sql = GpromProcess.getSerializableReenactSQL(currentInterval.getSCN(), newSql, tableName);
							}
							catch (Exception e3) {
								LoggerUtil.logException(e3, log);
							}
						}

						ResultSet rs = DBManager.getInstance().executeQuery(sql);
						//store rs data into a list map and store column name into a list
						//rsReset = rs;
						List<Map<Integer, Object>> rsList = null;
						List<String> rsNameList = new ArrayList<String>();
						rsNameList.add(" ");

						try {
							rsList = convertList(rs);		
							for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
							{
								rsNameList.add(rs.getMetaData().getColumnName(i));	
								log.info("name: "+rs.getMetaData().getColumnName(i));
							}
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}		

						//get how many tuples in the initial table
						//num of update if update based on diff column
						int numUp = 0;
						int pos = -1;
						String nameKey = "";

						//get how many not null tuples in this table (numUp)
						try {
							//rs.first();
							//log.info("test: list size "+rsListS.size());
							int cont = 0;
							rs.beforeFirst();
							while(rs.next())
							{
								cont++;
								for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
								{
									log.info("row: "+cont + " name: "+ rs.getMetaData().getColumnName(i) + " value: " + rs.getString(i));
									if(Pattern.matches("PROV_(?!U|query).*", rs.getMetaData().getColumnName(i)))
									{
										pos = i;
										break;	
									}
								}
								if(pos != -1)
								{
									String v = rs.getString(pos);
									log.info("v: "+ v);
									if(v != null)
										numUp++;	
								}				
							}

							//get statement orders
							setupStatementOrder(rs, rsList,tableName);

							log.info("cont: "+cont);
							log.info("numUp: "+numUp);				
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}


						try {
							rs.close();
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						log.info("current row size : "+currentRow.getIntervals().size());
						//for (int i = 0; i < currentRow.getIntervals().size() + 1; i++)
						for(int i=0; i<tableNames.size() + 1;i++)
						{
							int index = i;
							for(int m=0; m<distinctTableNames.size(); m++)
							{
								if(tableName.equals(distinctTableNames.get(m)))
								{
									index = index + m*numCell;
								}
							}

							JTable jtb = tables.get(index);
							// set up indexList
							List<Integer> indexList = new ArrayList<Integer>();
							String currentTableName = tableName;

							//log.info("test columncont + 1 = "+rsmd.getColumnCount());
							for (int j = 1; j < rsNameList.size(); j++)
							{
								if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsNameList.get(j)))
								{
									log.info("i=0, j = "+j);
									indexList.add(j);
								} 
								else if (Pattern.matches("PROV_U" + i + "__.*", rsNameList.get(j)))
								{
									log.info("i !=0, j = "+j);
									indexList.add(j);
								}		
							}

							//log.info("index: " + indexList + currentTableName);
							if(i > 0)
							{
								EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
								String sqlType = currentInterval.getType();
								if(sqlType.equals("INSERT"))
									numUp++;
							}
							numUps.add(numUp);			
							//DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp, tables);

							//if showTableFlag, this table should be empty
							boolean showTableFlag = tableEmptyFlagList.get(index);
							//					boolean showTableFlag = false;				
							//					if(i > 0)
							//						if(!currentTableName.equals(tableNames.get(i-1)))
							//							showTableFlag = true;

							DebuggerTableModel tm = new DebuggerTableModel(rsList, indexList, i, currentRow, numUp, rsNameList, showTableFlag, currentTableName);	
							//DebuggerTableModel tm = new DebuggerTableModel(rsList, indexList, i, currentRow, numUp, row1, col1, s1, rsNameList, showTableFlag, currentTableName);
							jtb.setModel(tm);
						}
					}

				}
			}
		}


		if (e.getSource() == opt_internal_button)
		{
			OptimizationInternalsFrame f = new OptimizationInternalsFrame("GProM Optimization Internals");
			//			f.setSize(950, 600);
			//			new design
			f.setSize(950, 300);
		} 

		if (e.getSource() == reset_button)
		{
			//reset what-if button
			clickWhatIf = false;
			
			//clear new and old map, sql list
			lNewMap.clear();
			lOldMap.clear();
			storeSqlList.clear();
			lStmtMap.clear();
			numUps.clear();
				
			int countSqls = sqlTextAreas.size();
			log.info("count sql: "+countSqls);

			for (int i = 0; i < countSqls; i++)
			{
				EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
				String originalStmt = currentInterval.getSql();
				JTextArea ja = sqlTextAreas.get(i);
				ja.setText(originalStmt);
			}

			int count = tableModels.size();
			log.info("count tables: "+count);
			
			for(int t=0; t<distinctTableNames.size(); t++)
			{
				//tableRowIndex ++;
				String currentTableName = distinctTableNames.get(t);
				// add GProm query result
				String sql = null;
				try {
					sql = GpromProcess.getTransactionIntermediateSQL(currentRow.getXID(), currentTableName);
				}
				catch (Exception e3) {
					LoggerUtil.logException(e3, log);
				}
				//String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
				ResultSet rs = DBManager.getInstance().executeQuery(sql);

				//store rs data into a list map and store column name into a list
		
				List<Map<Integer, Object>> rsList = null;
				List<String> rsNameList = new ArrayList<String>();
				rsNameList.add(" ");
				try {
					rsList = convertList(rs);		
					for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
					{
						rsNameList.add(rs.getMetaData().getColumnName(i));	
						log.info("name: "+rs.getMetaData().getColumnName(i));
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}		

				//get how many tuples in the initial table
				//num of update if update based on diff column
				int numUp = 0;
				int pos = -1;
				String nameKey = "";

				//get how many not null tuples in this table (numUp)
				try {
					//log.info("test: list size "+rsListS.size());
					int cont = 0;
					rs.beforeFirst();
					while(rs.next())
					{
						cont++;
						for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
						{
							log.info("row: "+cont + " name: "+ rs.getMetaData().getColumnName(i) + " value: " + rs.getString(i));
							if(Pattern.matches("PROV_(?!U|query).*", rs.getMetaData().getColumnName(i)))
							{
								pos = i;
								break;	
							}
						}
						if(pos != -1)
						{
							String v = rs.getString(pos);
							log.info("v: "+ v);
							if(v != null)
								numUp++;	
						}				
					}

					//get statement orders
					setupStatementOrder(rs, rsList, currentTableName);

					log.info("cont: "+cont);
					log.info("numUp: "+numUp);
					//rs.first();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					rs.close();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				log.info("current row size : "+currentRow.getIntervals().size());
				//for (int i = 0; i < currentRow.getIntervals().size() + 1; i++)
				int numCell = totalNumtables/tNameCount.size();
				for(int i=0; i<tableNames.size() + 1;i++)
				{
					int index = i;
					for(int m=0; m<distinctTableNames.size(); m++)
					{
						if(currentTableName.equals(distinctTableNames.get(m)))
						{
						    index = index + m*numCell;
						}
					}
											
					JTable jtb = tables.get(index);
					
					// set up indexList
					List<Integer> indexList = new ArrayList<Integer>();
					// index
					//log.info("test columncont + 1 = "+rsmd.getColumnCount());
					for (int j = 1; j < rsNameList.size(); j++)
					{
						if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsNameList.get(j)))
						{
							log.info("i=0, j = "+j);
							indexList.add(j);
						} 
						else if (Pattern.matches("PROV_U" + i + "__.*", rsNameList.get(j)))
						{
							log.info("i !=0, j = "+j);
							indexList.add(j);
						}		
					}


					if(i > 0)
					{
						EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
						String sqlType = currentInterval.getType();
						if(sqlType.equals("INSERT"))
							numUp++;
					}
					numUps.add(numUp);			
					//DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp, tables);

					//if showTableFlag, this table should be empty
					boolean showTableFlag = false;				
					if(i > 0)
						if(!currentTableName.equals(tableNames.get(i-1)))
							showTableFlag = true;

					//tableEmptyFlagList.add(showTableFlag);
					DebuggerTableModel tm = new DebuggerTableModel(rsList, indexList, i, currentRow, numUp, rsNameList, showTableFlag, currentTableName);
					jtb.setModel(tm);
				}

			}
		}


		if (e.getSource() == show_affected_button)
		{
			if(clickWhatIf)
			{
				lStmtMap.clear();
				numUps.clear();
				
				for(int t=0; t<distinctTableNames.size(); t++)
				{
					
					String tableName = distinctTableNames.get(t);
					String sql = "";
						
					if(lNewMap.containsKey(distinctTableNames.get(t)))
					{
						String newSql = storeSqlList.get(tableName);
						log.info("table name: " + tableName +" new sqls: "+newSql);
						
						if(currentRow.getIsoLevel().equals("1"))
							try {
								sql = GpromProcess.getReenactSQL(currentRow.getStartSCN(),newSql, tableName);
							}
						catch (Exception e3) {
							LoggerUtil.logException(e3, log);
						}
						else //serializable
						{
							//if serializable, each scn same, get first one
							EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(0);
							try {
								sql = GpromProcess.getSerializableReenactSQL(currentInterval.getSCN(), newSql, tableName);
							}
							catch (Exception e3) {
								LoggerUtil.logException(e3, log);
							}
						}
					}
					else
					{
						try {
							sql = GpromProcess.getTransactionIntermediateSQL(currentRow.getXID(), distinctTableNames.get(t));
						}
						catch (Exception e3) {
							LoggerUtil.logException(e3, log);
						}
					}
					//String sql = GpromProcess.getReenactSQL(newSql);
					ResultSet rs = DBManager.getInstance().executeQuery(sql);
					//ResultSetMetaData rsmd = null;

					
					
					//store rs data into a list map and store column name into a list
					//rsReset = rs;
					List<Map<Integer, Object>> rsList = null;
					List<String> rsNameList = new ArrayList<String>();
					rsNameList.add(" ");

					try {
						rsList = convertList(rs);		
						for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
						{
							rsNameList.add(rs.getMetaData().getColumnName(i));	
							log.info("name: "+rs.getMetaData().getColumnName(i));
						}
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}		
				
					//get how many tuples in the initial table
					//num of update if update based on diff column
					int numUp = 0;
					int pos = -1;
					String nameKey = "";
					
					//get how many not null tuples in this table (numUp)
					try {
						//rs.first();
						//log.info("test: list size "+rsListS.size());
						int cont = 0;
						rs.beforeFirst();
						while(rs.next())
						{
							cont++;
							for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
							{
								log.info("row: "+cont + " name: "+ rs.getMetaData().getColumnName(i) + " value: " + rs.getString(i));
								if(Pattern.matches("PROV_(?!U|query).*", rs.getMetaData().getColumnName(i)))
								{
									pos = i;
									break;	
								}
							}
							if(pos != -1)
							{
								String v = rs.getString(pos);
								log.info("v: "+ v);
								if(v != null)
									numUp++;	
							}				
						}
						
						//get statement orders
						setupStatementOrder(rs, rsList,tableName);
										
						log.info("cont: "+cont);
						log.info("numUp: "+numUp);				
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					try {
						rs.close();
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					int numCell = totalNumtables/tNameCount.size();
					log.info("current row size : "+currentRow.getIntervals().size());
					for(int i=0; i<tableNames.size() + 1;i++)
					{
						int index = i;
						for(int m=0; m<distinctTableNames.size(); m++)
						{
							if(tableName.equals(distinctTableNames.get(m)))
							{
							    index = index + m*numCell;
							}
						}
												
						JTable jtb = tables.get(index);
						// set up indexList
						List<Integer> indexList = new ArrayList<Integer>();
						String currentTableName = tableName;

						//log.info("test columncont + 1 = "+rsmd.getColumnCount());
						for (int j = 1; j < rsNameList.size(); j++)
						{
							if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsNameList.get(j)))
							{
								log.info("i=0, j = "+j);
								indexList.add(j);
							} 
							else if (Pattern.matches("PROV_U" + i + "__.*", rsNameList.get(j)))
							{
								log.info("i !=0, j = "+j);
								indexList.add(j);
							}		
						}

						//log.info("index: " + indexList + currentTableName);
						if(i > 0)
						{
							EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
							String sqlType = currentInterval.getType();
							if(sqlType.equals("INSERT"))
								numUp++;
						}
						numUps.add(numUp);			
						//DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp, tables);
						
						//if showTableFlag, this table should be empty
						boolean showTableFlag = tableEmptyFlagList.get(index);
//						boolean showTableFlag = false;				
//						if(i > 0)
//							if(!currentTableName.equals(tableNames.get(i-1)))
//								showTableFlag = true;
						
					DebuggerTableModel tm = new DebuggerTableModel(rsList, indexList, i, currentRow, numUp, rsNameList, showTableFlag, currentTableName);	
					jtb.setModel(tm);
				}
				

				}
			}
		}



		if (e.getSource() == show_all_button)
		{
			if(clickWhatIf)
			{
				lStmtMap.clear();
				numUps.clear();
				for(int t=0; t<distinctTableNames.size(); t++)
				{
					
					String tableName = distinctTableNames.get(t);
					
					String sql = "";
					if(lNewMap.containsKey(distinctTableNames.get(t)))
					{
						String newSql = storeSqlList.get(tableName);
						log.info("table name: " + tableName +" new sqls: "+newSql);
						
						if(currentRow.getIsoLevel().equals("1")) {
							try {
								sql = GpromProcess.getReenactAllSQL(currentRow.getStartSCN(),newSql,tableName);
							}
							catch (Exception e3) {
								LoggerUtil.logException(e3, log);
							}
						}
						else //serializable
						{
							//if serializable, each scn same, get first one
							EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(0);
							try {
								sql = GpromProcess.getSerializableReenactAllSQL(currentInterval.getSCN(), newSql, tableName);
							}
							catch (Exception e3) {
								LoggerUtil.logException(e3, log);
							}
						}
					}
					else
					{
						try {
							sql = GpromProcess.getWholeTransactionIntermediateSQL(currentRow.getXID(), distinctTableNames.get(t));
						}
						catch (Exception e3) {
							LoggerUtil.logException(e3, log);
						}
					}
					
					ResultSet rs = DBManager.getInstance().executeQuery(sql);


						

						//store rs data into a list map and store column name into a list
						//rsReset = rs;
						List<Map<Integer, Object>> rsList = null;
						List<String> rsNameList = new ArrayList<String>();
						rsNameList.add(" ");

						try {
							rsList = convertList(rs);		
							for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
							{
								rsNameList.add(rs.getMetaData().getColumnName(i));	
								log.info("name: "+rs.getMetaData().getColumnName(i));
							}
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}		

						//get how many tuples in the initial table
						//num of update if update based on diff column
						int numUp = 0;
						int pos = -1;
						String nameKey = "";

						//get how many not null tuples in this table (numUp)
						try {
							//rs.first();
							//log.info("test: list size "+rsListS.size());
							int cont = 0;
							rs.beforeFirst();
							while(rs.next())
							{
								cont++;
								for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
								{
									log.info("row: "+cont + " name: "+ rs.getMetaData().getColumnName(i) + " value: " + rs.getString(i));
									if(Pattern.matches("PROV_(?!U|query).*", rs.getMetaData().getColumnName(i)))
									{
										pos = i;
										break;	
									}
								}
								if(pos != -1)
								{
									String v = rs.getString(pos);
									log.info("v: "+ v);
									if(v != null)
										numUp++;	
								}				
							}

							//get statement orders
							setupStatementOrder(rs, rsList,tableName);

							log.info("cont: "+cont);
							log.info("numUp: "+numUp);				
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						try {
							rs.close();
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						int numCell = totalNumtables/tNameCount.size();
						log.info("current row size : "+currentRow.getIntervals().size());
						for(int i=0; i<tableNames.size() + 1;i++)
						{
							int index = i;
							for(int m=0; m<distinctTableNames.size(); m++)
							{
								if(tableName.equals(distinctTableNames.get(m)))
								{
									index = index + m*numCell;
								}
							}

							JTable jtb = tables.get(index);
							// set up indexList
							List<Integer> indexList = new ArrayList<Integer>();
							String currentTableName = tableName;

							//log.info("test columncont + 1 = "+rsmd.getColumnCount());
							for (int j = 1; j < rsNameList.size(); j++)
							{
								if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsNameList.get(j)))
								{
									log.info("i=0, j = "+j);
									indexList.add(j);
								} 
								else if (Pattern.matches("PROV_U" + i + "__.*", rsNameList.get(j)))
								{
									log.info("i !=0, j = "+j);
									indexList.add(j);
								}		
							}
					
							//log.info("index: " + indexList + currentTableName);
							if(i > 0)
							{
								EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
								String sqlType = currentInterval.getType();
								if(sqlType.equals("INSERT"))
									numUp++;
							}
							numUps.add(numUp);			
		
							//if showTableFlag, this table should be empty
							boolean showTableFlag = tableEmptyFlagList.get(index);
							//						boolean showTableFlag = false;				
							//						if(i > 0)
							//							if(!currentTableName.equals(tableNames.get(i-1)))
							//								showTableFlag = true;

							DebuggerTableModel tm = new DebuggerTableModel(rsList, indexList, i, currentRow, numUp, rsNameList, showTableFlag, currentTableName);	
							jtb.setModel(tm);
						}
				}
			}
		}
		
		//set the table not in the first column is unable to edit
		int numCell = totalNumtables/tNameCount.size();
		
		for(int t=0,i=0; t<tables.size(); t++,i++)
		{
			if(i == numCell)
				i=0;
			
			if(i != 0)
				tables.get(t).setEnabled(false);
		}

		//reset table listener to handle after click any button, the tableChanged function can not be activated
		for (int t = 0; t < tables.size(); t++)
		{
			if(tableEmptyFlagList.get(t) == false)
			{
				tables.get(t).addMouseListener(this);
				tables.get(t).getModel().addTableModelListener(this);
			}
		}
	}


			

	@Override
	public void componentResized(ComponentEvent e)
	{
		//update();
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
	}
	
	
	@Override
	public void tableChanged (TableModelEvent e) {
		
        int row = e.getFirstRow();
        int column = e.getColumn() + 2;
        DebuggerTableModel model = (DebuggerTableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        log.info("col name： "+columnName);
        log.info("row: "+ row + " col: " + column +" value: "+data.toString());
       
		int numCell = totalNumtables/tNameCount.size();
		log.info("numCell: "+numCell + " num tables : " + tables.size());

		for(int i=0, t=0; t<distinctTableNames.size(); i = i + numCell,t++)
		{
			log.info("i: "+i);
        	JTable fTable = tables.get(i);
        	String tName = fTable.getName();    
        	if(tName.equals(model.getTableName()))
        	{
        		int fCountCol = fTable.getColumnCount();
        		List<Object> nList = new ArrayList();
        		
        		if(!lNewMap.containsKey(tName))
        		{
        			Map<Integer, List<Object>> newMap1 = new HashMap<Integer, List<Object>>();
        			for(int c=2; c<fCountCol; c++)
            		{
            			Object d = model.getValueAt(row, c);
            			nList.add(d);
            		}   
            		newMap1.put(row, nList);
            		lNewMap.put(tName, newMap1);
        		}
        		else      			
        		{
        			Map<Integer, List<Object>> newMap1 = lNewMap.get(tName);
            		for(int c=2; c<fCountCol; c++)
            		{
            			Object d = model.getValueAt(row, c);
            			nList.add(d);
            		}     
            		newMap1.put(row, nList);
            		lNewMap.put(tName, newMap1);
        		}
        		
        		if(!lOldMap.containsKey(fTable.getName()))
        		{
        			Map<Integer, List<Object>> oldMap1 = new HashMap<Integer, List<Object>>();
        			Map<Integer, List<Object>> old = lOld.get(tName);
            		List l = old.get(row);    
            		oldMap1.put(row, l);
            		lOldMap.put(tName, oldMap1);
        		}
        		else      			
        		{
        			Map<Integer, List<Object>> oldMap1 = lOldMap.get(tName);
            		if(!oldMap.containsKey(row))
            		{
            			Map<Integer, List<Object>> old = lOld.get(tName);
            			List l = old.get(row);    
            			oldMap1.put(row, l);
            			lOldMap.put(tName, oldMap1);
            		} 
        		}
        		       		      	
		
   		//check info
		log.info("List new");
		for (Entry<String, Map<Integer, List<Object>>> entry : lNewMap.entrySet()) {
			String s = entry.getKey();
			Map<Integer, List<Object>> cl = entry.getValue();              	 
			log.info("table: " + s);

			for (Entry<Integer, List<Object>> entry1 : cl.entrySet()) {
				int r = entry1.getKey();
				List<Object> l = entry1.getValue();
				for(int ii=0; ii<l.size(); ii++)
					log.info("Row: "+ r +" New v: "+ l.get(ii));   
			}              	    	
		}
		
		log.info("List old");
		for (Entry<String, Map<Integer, List<Object>>> entry : lOldMap.entrySet()) {
			String s = entry.getKey();
			Map<Integer, List<Object>> cl = entry.getValue();              	 
			log.info("table: " + s);

			for (Entry<Integer, List<Object>> entry1 : cl.entrySet()) {
				int r = entry1.getKey();
				List<Object> l = entry1.getValue();
				for(int j=0; j<l.size(); j++)
					log.info("Row: "+ r +" Old v: "+ l.get(j));   
			}              	    	
		}

        	}
        }    
	}

	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		for (int i = 0; i < tables.size(); i++)
			tables.get(i).clearSelection();
		
		//highlight transaction statement e.g., U1..
		for (int i = 0; i < sqlLabels.size(); i++)
			sqlLabels.get(i).setForeground(Color.BLACK);
		
		JTable table = (JTable) e.getSource();
		log.info("table size:" + tables.size() );
		log.info("numUps size: "+numUps.size());
		
		int index = -1;
		int numCell = totalNumtables/tNameCount.size();
		for (int i = 0; i < tables.size(); i++)
		{
			JTable currentTable = tables.get(i); 
			if (currentTable == table)
			{
				index = i;
				break;
			}
		}
		
		int r = table.rowAtPoint(e.getPoint());		
		if(r >=0) //handle mouse click outside of the table area
		{			
			int p = index/numCell;
			int f = p*numCell;
			log.info("p: "+p + " index: "+index + " numCell: "+numCell + " f: "+f);

			for (int i = f; i <= index; i++)
			{
				if(!tableEmptyFlagList.get(i) && r < numUps.get(i)) //tableEmptyFlagList.get(i) check if this table is empty, r < numUps.get(i) check if this table contain the row r+1
					tables.get(i).setRowSelectionInterval(r, r);
			}

			int row = r + 1;
			log.info("r: "+r + " index: "+index);
			if(r >= 0 && index != -1)
				showGraph(f, index, row, numCell, table.getName());
		}
	}

	
	//old highlight
//	private void highlightTables(int currentTableIndex, String tupleIndex)
//	{
//		JTable currentTable = tables.get(currentTableIndex);
//		// get the index for row we want to highlight
//		Pattern pattern = Pattern.compile("t(\\d*)\\[(\\d*)\\]");
//		Matcher matcher = pattern.matcher(tupleIndex);
//		String rowIndex = "";
//		String tableIndex = "";
//		if (matcher.matches())
//		{
//			rowIndex = matcher.group(1);
//			tableIndex = matcher.group(2);
//			log.info(rowIndex + ", " + tableIndex);
//		}
//		// highlight table
//		currentTable.setRowSelectionInterval(Integer.parseInt(rowIndex) - 1, Integer.parseInt(rowIndex) - 1);
//		log.info("currentTupeIndex" + tupleIndex + "  tableIndex" + currentTableIndex);
//		if (currentTableIndex <= -1)
//		{
//			return;
//		}
//		List<String> provenanceList = ((DebuggerTableModel) tables.get(currentTableIndex).getModel()).getPrevRelation()
//				.get(tupleIndex);
//		if (provenanceList == null)
//		{
//			return;
//		}
//		log.info(((DebuggerTableModel) tables.get(currentTableIndex).getModel()).getPrevRelation());
//		log.info(tupleIndex);
//		log.info(provenanceList);
//
//		for (int i = 0; i < provenanceList.size(); i++)
//		{
//			String nextTupleIndex = provenanceList.get(i);
//			highlightTables(--currentTableIndex, nextTupleIndex);
//		}
//	}
	
	

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	
	
	
	//t row [level] e.g. t1[2] tuple 1 and the second update
	public void showGraph(int first, int level, int row,int numCell, String tableName) {
		//Make the label text on the label
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		//Set node style
		String styleSheet =
	            "node {" +
	            "size: 35;" +
	            "fill-mode: dyn-plain;" +
	            "fill-color: grey, red, green;" +
	            "shape: box;" +
	            "}";
		
		//Graph
		Graph graph = new DefaultGraph("ProvenanceGraph");
		//graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box; fill-color: grey;}");  // size-mode: fit
		graph.addAttribute("ui.stylesheet", styleSheet);


		           		
		//old node to pre and next;
//		Map<String, List<String>> next = new HashMap<String, List<String>>();
//		Map<String, List<String>> prev = new HashMap<String, List<String>>();
//		
//		int level = 3;
//		List<String> list = new ArrayList<String>();
//		list.add("t[1]1");
//		next.put("t[1]0", list);
//		list.clear();
//		
//		list.add("t[1]2");
//		next.put("t[1]1", list);
//		list.clear();
//		
//		list.add("t[1]3");
//	    next.put("t[1]2", list);
//		list.clear();
////		
//		list.add("t[1]0)");
//		prev.put("t[1]1", list);
//		list.clear();
////		
//		list.add("t[1]1");
//		prev.put("t[1]2", list);
//		list.clear();
////		
//		list.add("t[1]2");
//		prev.put("t[1]3", list);
//		list.clear();
//		
//		log.info(next.size());
//		log.info(next.get("t[1]0"));
		
		
		
		
		//12.1
//		HashMap<String, String> next = new HashMap<String, String>();
//		next.put("t1[0]", "t1[1]");
//		next.put("t1[1]", "t1[2]");
//		next.put("t1[2]", "t1[3]");
//		HashMap<String, String> prev = new HashMap<String, String>();
//		prev.put("t[1]1", "t[1]0");
//		prev.put("t[1]2", "t[1]1");
//		prev.put("t[1]3", "t[1]2");
		//12.1
		
		
//		Map<String, List<String>> nextT = new HashMap<String, List<String>>();
//		Map<String, List<String>> prevT = new HashMap<String, List<String>>();
//		new DebuggerTableModel().forGraphSQL(prevT, nextT);
//		prevT =new DebuggerTableModel().getPrevRelation();
		
		
		
		
		
//		Set<String> nodes = new HashSet<String>();
//		int attribute = 0;
//		int edgeNum = 0;
//		for(int i = 0; i < level; i++) {
////			log.info(i);
//			String start = "t[1]" + String.valueOf(i);
////			log.info(start);
//
//			if(!nodes.contains(start)) {
//				nodes.add(start);
//				graph.addNode(start);
//				graph.setAttribute("xy",attribute, 0);
//				attribute++;
//				graph.addAttribute("label", start);
//				List<String> n = next.get(start);
//				for(int j = 0; j < n.size(); j++) {
//					String subnode = n.get(j);
//					nodes.add(subnode);
//					
//					
//					
//				}
//				for(int j = 0; j < next.get(start).size();j++) {
//					String subnode = next.get(start).get(j);
//					log.info(subnode);
////					nodes.add(next.get(start).get(j)));
//					nodes.add(subnode);
//					graph.setAttribute("xy", attribute, 0);
//					attribute++;
//					graph.addAttribute("label", subnode);
//					graph.addEdge("u" + String.valueOf(edgeNum), subnode, start, true);
//					edgeNum++;
//				}
//			}
//			else {
//				for(int j = 0; j < next.get(start).size();j++) {
//					String subnode = next.get(start).get(j);
////					nodes.add(next.get(start).get(j)));
//					nodes.add(subnode);
//					graph.setAttribute("xy", attribute, 0);
//					attribute++;
//					graph.addAttribute("label", subnode);
//					graph.addEdge("u" + String.valueOf(edgeNum), start, subnode, true);
//					edgeNum++;
//				}
//			}
//			
//		}
		
		
//		int level = 3;
//		int attribute = 0;
//		String start = "t[1]0";
//		Node stNode = graph.addNode(start);
//		stNode.setAttribute("xy", attribute);
//		attribute++;
//		stNode.addAttribute("label", start);
//		int edgeNum = 0;
//		for(int i = 0; i < level; i++) {
//			String currName = next.get(start);
//			Node currNode = graph.addNode(currName);
//			currNode.setAttribute("xy", attribute, 0);
//			attribute++;
//			currNode.addAttribute("label", currName);
//			graph.addEdge("u" + String.valueOf(edgeNum), stNode, currNode);
//			edgeNum++;
//			stNode = currNode;
//			
//		}
//		graph.display(false);
//		String startName = "t[1]0";
//		
//		Node startNode = graph.addNode(startName);
//		int attribute = 0;
//		startNode.setAttribute("xy", attribute, 0);
//		attribute++;
//		startNode.addAttribute("label", startName);
//		
//		int edgeNum = 0;
//		for(int i = 0; i < level; i++) {
//			String currName = next.get(startName);
//			Node currNode = graph.addNode(currName);
//			
//			currNode.setAttribute("xy", attribute, 0);
//			attribute++;
//			currNode.addAttribute("label", currName);
//			graph.addEdge("u" + String.valueOf(edgeNum), startNode, currNode, true);
//			edgeNum++;
//			startNode = currNode;
//		}
//		
//		graph.display();
//		int level = 1;
//		log.info("prevT size:" + nextT.size());
		
		
		//structure tables
		//e.g.,
		// 	 		 orig     R_up1   R_up2    S_up3
        //row 1: 	 data 	  data    data     empty
		//row 2: 	 data     empty   empty    data
		
		//tableEmptyFlagList size:8
		//			  F		   F       F        T
		//            F        T       T        F
		
		//lStmtMap size 2   u1  u2     list begin from 1 (row 1)
		//  R ->  row 1:    1   0
		//    ->  row 2:    0   1
		//   			    u3
		//  S ->  row 3:    1
		
		

		ArrayList<Node> nodes = new ArrayList<Node>();
		int attribute = 0;
		
		log.info("level : "+level + " numUpSize : "+ numUps.size());
		for(int t=0; t< numUps.size(); t++)
			log.info("numUps = "+ numUps.get(t));

		int h = level/numCell;  //e.g.,  7/4 = 1 (click the table 7 (0-7), each table (one row) has 4 statements including the original one), or 3/4 = 0    
		int hh = h*numCell;     //e.g.,  1*4 = 4 (which is the first table in second row (table 4 (0-7))), or 0*4 = 0
		
		log.info("first: "+first);
		for(int i = first,j=-1; i <= level; i++)
		{
			int st = i - hh; //e.g., when click level = 7, second row last columns table, 7-4 = 3 the statement 3.(U3)
			log.info("i: "+i + " tableEmptyFlagList size: "+tableEmptyFlagList.size());
			log.info("row: "+row + " numUs: "+numUps.get(i)+ " table empty? "+ tableEmptyFlagList.get(i));
			if(row <= numUps.get(i) && !tableEmptyFlagList.get(i))
			{
				boolean addU = false;
				if(i != first)
				{
                    log.info("lStmtMap.table size: "+lStmtMap.get(tableName).size());
					log.info("row: "+row+" i: "+i + " j: "+j);
					log.info(" addU: "+lStmtMap.get(tableName).get(row).get(j));

					if(lStmtMap.get(tableName).get(row).get(j) != null)
						if(lStmtMap.get(tableName).get(row).get(j).toString().equals("1"))
							addU = true;
					log.info("addU: "+addU);
				}

				if(i != first && addU)  //or st != 0
				{
					int updateIndex = st;
					String uName = "U" + updateIndex;
					Node uNode = graph.addNode(uName);
					//set node position x and y axis
					uNode.setAttribute("xy", attribute, 0); 						
					attribute++;

					uNode.addAttribute("label", uName);
					uNode.setAttribute("ui.color", 0.5);
					nodes.add(uNode);

					sqlLabels.get(st-1).setForeground(Color.RED);
				}

				String nodeName = "t" + String.valueOf(row-1) + "[" + String.valueOf(st) + "]";
				Node tempNode = graph.addNode(nodeName);
				//set node position x and y axis
				tempNode.setAttribute("xy", attribute, 0); 						
				attribute++;

				tempNode.addAttribute("label", nodeName);
				tempNode.setAttribute("ui.color", 0.94);
				nodes.add(tempNode);
			}
			
			log.info(" table empty? "+ tableEmptyFlagList.get(i));
			if(!tableEmptyFlagList.get(i))
			{
				log.info("table not empty!! ");
				j++;
			}
			log.info("after-> i: "+i + " j: "+j);
					
		}
		
		
		if(nodes.size() > 1)
		{
			for(int i = 0; i < nodes.size()-1; i++) 
			{
				graph.addEdge("u" + String.valueOf(i), nodes.get(i), nodes.get(i + 1), true);
			}
		}
		
//	    JFrame frame = new JFrame();
//	    frame.setLayout(new GridBagLayout());
//	    JPanel panel = new JPanel();
//	    panel.setBorder(BorderFactory.createLineBorder(Color.black));
//	    panel.setPreferredSize(new Dimension(600, 600));
//	    frame.add(panel);
//	    frame.setSize(1000, 1000);
//	    frame.setVisible(true);

	    Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
	    // ...
	    View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
	    ((Component) view).setPreferredSize(new Dimension(400, 200));
		// ...
		//panel.add((Component) view);
//	    JLabel jlabel = new JLabel("This is a label");
//		//panel_test.add((Component) view);
//	    JButton getG4 = new JButton("Get Graph3");
//		getG4.setBounds(450, 50, 100, 25);
		
	    panel_view.removeAll();
	    panel_view.add((Component) view);
	    panel_view.repaint();
	    panel_view.revalidate();
		
		
		//Viewer viewer = graph.display(false);
		//graphPanel.add(viewer);
		//just hide the window not close it after click the "x"
		//viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
		//View view = viewer.getDefaultView();
		//graph.display();
		//false means the graph is not dynamic
		
		//graphPanel.add((Component) view);
		//graph.display(false);
		
//		for(int i = 0; i < level; i++) {
//			String nodeName1 = "t1[" + String.valueOf(i) + "]";
//			Node n1 = graph.addNode(nodeName1);
//			n1.addAttribute("ui.label", n1.getId());
//			String nodeName2 = "t1[" + String.valueOf(i+1) + "]";
//			Node n2 = graph.addNode(nodeName2);
//			n2.addAttribute("ui.label", n2.getId());
//			graph.addEdge("u" + String.valueOf(i), n1, n2, true);
//			//graph.addEdge("n1n2", "n1", "n2");
//		}
//		graph.display(false);
		
//		JFrame frame = new JFrame();
//		frame.setLayout(new GridBagLayout());
//		JPanel panel = new JPanel();
//		panel.setBorder(BorderFactory.createLineBorder(Color.black));
//		panel.setPreferredSize(new Dimension(600, 600));
//		JLabel jlabel = new JLabel("This is a label");
//		frame.add(panel);
//		frame.setSize(1000, 1000);
//		frame.setVisible(true);		
//		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
//		View view = viewer.addDefaultView(false);   // false indicates "no JFrame".
//		((Component) view).setPreferredSize(new Dimension(400, 200));
//		panel.add((Component) view);
		
		
		
//		String start = "t" +String.valueOf(index) + "[" + String.valueOf(i) + "]";
//		Node startNode = makeNodeMap(graph, start, atrribute++);
//		Node forStart = startNode;
//		while(!prevT.containsKey(start){
//			String curr = prevT.get(start).get(0);
//			Node currNode = makeNode(graph, cur, attribute++);
//			makeNodeMap(start, curr);
//			start = curr;
//		}
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
		
		
		
		
		
		
		
//		for(int i = 0; i < level; i++) {
//			String start = "t[1]" + String.valueOf(i);
//			nodes.add(start);
//			for(int j = 0; j < next.get(start).size(); j++) {
//				nodes.add(next.get(start).get(j));
//			}
//			
//		}
//		
//		int attribute = 0;
//		ArrayList<Node> listNode = new ArrayList<Node>();
//		for(String s : nodes) {
//			
//			Node curNode = graph.addNode(s);
//			curNode.setAttribute("xy", attribute, 0);
//			curNode.addAttribute("label", s);
//			attribute++;
//		}
//		int edgeNum = 0;
//		for(int i = 0; i < level; i++) {
//			String start = "t[1]" + String.valueOf(i);
//			
//			for(int j = 0; j < next.get(start).size(); j++) {
//				String subNode = next.get(start).get(j);
//				
//				graph.addEdge("u" + String.valueOf(edgeNum), subNode, start,true);
//				start = subNode;
//				edgeNum++;
//			}
//		}
		
//		graph.display(true);
	}
	
//	public Node makeNode(Graph graph, String name, int attribute){
//		
//		Node tempNode = graph.addNode(name);
//		tempNode.setAttribute("xy", attribute, 0);
//		tempNode.addAttribute("label", name);
//		return tempNode;
//		
//	}
//	public void makeNodeMap(Node start, Node end) {
//		mapNode.put(start, end);
//	}
//	public void makeGraph(Map<Node, Node> map, Graph graph, Node start){
//		int edge = 1;
//		while(mapNode.containsKey(start)) {
//			graph.addEdge("u" + String.valueOf(edge), start, mapNode.get(start), true);
//			edge++;
//			start = mapNode.get(start);
//		}
//	}

	
}


