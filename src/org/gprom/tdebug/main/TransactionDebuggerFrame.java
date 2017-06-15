package org.gprom.tdebug.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
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
//
import org.graphstream.ui.view.Viewer;

import timebars.eventmonitoring.model.EventInterval;
import timebars.eventmonitoring.model.EventTimeBarRow;








public class TransactionDebuggerFrame extends JFrame implements ActionListener, ComponentListener, MouseListener
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
	private final static int HEIGHTFORGRAPHPANEL = 250; //

	static Logger log = Logger.getLogger(TransactionDebuggerFrame.class);
	
	private JButton reset_button = null;
	private JButton refresh_button = null;
	private JButton show_all_button = null;
	private JButton opt_internal_button = null;
	private JButton add_stmt_button = null;
	private JButton del_stmt_button = null;
	private JButton show_hide_button = null;
	
//	private JButton getG1 = null;
//	private JButton getG2 = null;
//	private JButton getG3 = null;
	
	
	Map<String, List<String>> nextT = new HashMap<String, List<String>>();
	Map<String, List<String>> prevT = new HashMap<String, List<String>>();
	Map<Node, Node> mapNode = new HashMap<>();
	
	
	JScrollPane main_scrollPane = null;
//	JScrollPane main_scrollPane1 = null;
//	JPanel graphPanel = null;
	JPanel panel_view = null;
	JLabel imageLabel = null;
	JPanel stmt_table_panel = null;
//	JPanel stmt_table_panel1 = null;
	
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

	
	private EventTimeBarRow currentRow;
	
	private ResultSet rsReset;
	

	public TransactionDebuggerFrame(EventTimeBarRow row)
	{
		super();
		this.currentRow = row;
		setup();
		setupListeners();
		initialWidth = this.getWidth();
		initialHeight = this.getHeight();
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

		setTitle("Debug Panel");
		setSize(1200, 600);

		stmt_table_panel = new JPanel();
		stmt_table_panel
				.setPreferredSize(new Dimension(DEBUGGER_CELL_WIDTH * (currentRow.getIntervals().size() + 1), 300));
		main_scrollPane = new JScrollPane(stmt_table_panel);
		main_scrollPane.setBounds(DEBUGGER_LEFT_PADDING, 5, WIDTHFORMAINSCROLLPANE, HEIGHTFORMAINSCROLLPANE + 70);
		
//		stmt_table_panel1 = new JPanel();
//		main_scrollPane1 = new JScrollPane(stmt_table_panel1);
//		main_scrollPane1.setBounds(DEBUGGER_LEFT_PADDING, 105, WIDTHFORMAINSCROLLPANE, HEIGHTFORMAINSCROLLPANE);
//		
		JPanel jp1 = new JPanel();
		JTextArea ja1 = new JTextArea(5, 20);
		// ja1.setFont(Bold);
		
		String originalTable_str = "\n\n Original Table";
		// display transaction
		for (int i = 0; i < currentRow.getIntervals().size(); i++)
		{
			EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
			String originalStmt = currentInterval.getSql();
			log.info(originalStmt);
			JPanel jp = new JPanel();
			JTextArea ja = new JTextArea(5, 20);
			ja.setEditable(true);
			ja.setText(originalStmt);
			ja.setLineWrap(true);
			sqlTextAreas.add(ja);
			jp.add(ja);
			stmt_table_panel.add(jp);
			jp.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
			jp.setBounds(DEBUGGER_CELL_WIDTH * (i + 1), 0, 300, 95);
			ja.setBounds(0, 10, 5, 20);

			// if (i == 3) {
			// break;
			// }
		}
		// String str2 = "\n UPDATE account \n SET bal=bal-35 \n WHERE
		// cust='Alice' AND typ='Checking'";
		// String str3 = "\n UPDATE account \n SET bal=bal+35 \n WHERE
		// cust='Alice' AND typ='Savings'";
		ja1.setText(originalTable_str);
		// ja1.setCaretColor(Color.BLUE);
		ja1.setEditable(false);
		jp1.add(ja1);

		this.setLayout(null);
		stmt_table_panel.setLayout(null);
		stmt_table_panel.add(jp1);

		jp1.setBounds(0, 0, 300, 95);
		jp1.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		ja1.setBounds(0, 10, 5, 20);

		// second line
		// add GProm query result
		String sql = GpromProcess.getTransactionIntermediateSQL(currentRow.getXID());
	    //String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
		ResultSet rs = DBManager.getInstance().executeQuery(sql);
		
		//set reset rs
		rsReset = rs;
		
		//get how many tuples in the initial table
		//num of update if update based on diff column
		int numUp = 0;
		int pos = -1;
		try {
			while(rs.next())
			{
				for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
				{
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
			log.info("numUp: "+numUp);
			rs.first();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ResultSetMetaData rsmd = null;
		try
		{
			rsmd = rs.getMetaData();
		} catch (SQLException e)
		{
			LoggerUtil.logException(e,log);
		}
		
		log.info("current row size : "+currentRow.getIntervals().size() );
		for (int i = 0; i < currentRow.getIntervals().size() + 1; i++)
		{
			// set up indexList
			List<Integer> indexList = new ArrayList<Integer>();
			String currentTableName = null;
			try
			{
				// index
				log.info("test columncont + 1 = "+rsmd.getColumnCount());
				for (int j = 1; j < rsmd.getColumnCount() + 1; j++)
				{
					if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsmd.getColumnName(j)))
					{
						log.info("i=0, j = "+j);
						indexList.add(j);
					} 
					else if (Pattern.matches("PROV_U" + i + ".*", rsmd.getColumnName(j)))
					{
						log.info("i !=0, j = "+j);
						indexList.add(j);
					}
				}

				// tablename
				if (currentTableName == null)
				{
					Pattern p = Pattern.compile("PROV_(?!U)(\\w*)_.*|PROV_U" + i + "__(\\w*)_.*");
					//log.info("indexList size: "+indexList.size());
					if(indexList.size() != 0)
					{
						Matcher m = p.matcher(rsmd.getColumnName(indexList.get(0)));
						if (!m.find())
						{
							log.info("regular expression succeed!");
						}
						else if (i == 0)
						{
							currentTableName = m.group(1);
							log.info("group1: " + m.group(0));
							log.info("group1: " + m.group(1));
							log.info("group1: " + m.group(2));
						} else
						{
							currentTableName = m.group(2);
							log.info("group2: "+ m.group(0));
							log.info("group2: " + m.group(1));
							log.info("group2: " + m.group(2));
						}
					}
					else
						currentTableName = "";
				}

			} 
			catch (SQLException e)
			{
				LoggerUtil.logException(e,log);
			}
			// log.info("index: " + indexList + currentTableName);
			if(i > 0)
			{
				EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
				String sqlType = currentInterval.getType();
				if(sqlType.equals("INSERT"))
					numUp++;
			}
						
			DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp);
			tableModels.add(tm);
			JPanel jp = new JPanel();
			jp.setLayout(null);
			jp.setBounds(DEBUGGER_CELL_WIDTH * i, 100, 300, 205);
			jp.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
			stmt_table_panel.add(jp);
			JTable table = new JTable(tm);

//			//test add additional column
//			List colData = new ArrayList(table.getRowCount());		
//			tm.addColumn("Col3", colData.toArray());
					
			tables.add(table);
			JScrollPane scrollPane = new JScrollPane(table);
			// scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray,3));
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
		
		this.add(main_scrollPane);
		//this.add(main_scrollPane1);

//		graphPanel = new JPanel();
//		graphPanel.setLayout(null);
//		graphPanel.setBounds(DEBUGGER_LEFT_PADDING, 200, WIDTHFORGRAPHPANEL, 100);
//		graphPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 3));

		//used to show the graph view after click each row in the table
		panel_view = new JPanel();
		panel_view.setBounds(DEBUGGER_LEFT_PADDING, 320, WIDTHFORGRAPHPANEL, 220);
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

		// f.add(jp7);
		// f.add(jp8);
		// f.add(jp9);

		// first column label name
		panel_SQL = new JPanel();
		panel_table = new JPanel();
		panel_graph = new JPanel();
		
		panel_table.setLayout(null);
		panel_graph.setLayout(null);
		
		panel_SQL.setBounds(35, 5, 100, 95);
		panel_SQL.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		panel_table.setBounds(35, 100, 100, 220);
		panel_table.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
		panel_graph.setBounds(35, 320, 100, 220);
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
		
		//for graph
//		getG1 = new JButton("Get Graph1");
//		getG2 = new JButton("Get Graph2");
//		getG3 = new JButton("Get Graph3");
		
//		getG1.setBounds(150, 50, 100, 25);
//		getG2.setBounds(300, 50, 100, 25);
//		getG3.setBounds(450, 50, 100, 25);

//		graphPanel.add(getG1);
//		graphPanel.add(getG2);
//		graphPanel.add(getG3);
		
		
		
		// Right buttons show_all_button
		reset_button = new JButton("Reset");
		refresh_button = new JButton("Refresh");
		show_all_button = new JButton("ShowAll");
		opt_internal_button = new JButton("<html>Optimizer<br>Internals</html>");
		add_stmt_button = new JButton("<html>&nbsp;&nbsp; Add<br>Statement</html>");
		del_stmt_button = new JButton("<html>&nbsp; Delete<br>Statement</html>");
		show_hide_button = new JButton("<html>Show/Hide<br>Unaffected<br> &nbsp;&nbsp;&nbsp;Rows</html>");

		// jb3.setHorizontalAlignment(JButton.RIGHT);

		reset_button.setBounds(XFORBUTTONS, 10, 105, 55);
		refresh_button.setBounds(XFORBUTTONS, 65, 105, 55);
		show_all_button.setBounds(XFORBUTTONS, 120, 105, 55);
		opt_internal_button.setBounds(XFORBUTTONS, 175, 105, 55);
		add_stmt_button.setBounds(XFORBUTTONS, 230, 105, 55);
		del_stmt_button.setBounds(XFORBUTTONS, 285, 105, 55);
		show_hide_button.setBounds(XFORBUTTONS, 330, 105, 55);
		buttons.add(reset_button);
		buttons.add(refresh_button);
		buttons.add(show_all_button);
		buttons.add(opt_internal_button);
		buttons.add(add_stmt_button);
		buttons.add(del_stmt_button);
		buttons.add(show_hide_button);
		// jb1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// jb2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		this.add(reset_button);
		this.add(refresh_button);
		this.add(show_all_button);
		this.add(opt_internal_button);
		this.add(add_stmt_button);
		this.add(del_stmt_button);
		this.add(show_hide_button);
		this.setVisible(true);
	}

	private void setupListeners()
	{
//		getG1.addActionListener(this);
//		getG2.addActionListener(this);
//		getG3.addActionListener(this);
		
		reset_button.addActionListener(this);
		refresh_button.addActionListener(this);
		show_all_button.addActionListener(this);
		opt_internal_button.addActionListener(this);
		add_stmt_button.addActionListener(this);
		del_stmt_button.addActionListener(this);
		show_hide_button.addActionListener(this);
		this.addComponentListener(this);
		for (int i = 0; i < tables.size(); i++)
		{
			tables.get(i).addMouseListener(this);
			
//			ListSelectionModel selModel = tables.get(i).getSelectionModel();
//			//selModel; 
////            int row = tables.get(i).getSelectedRow();
////            
////            log.info("row id is "+row);
//			//tables.get(i).getSelectionModel().

		}
	}
//	@Override
//	public void valueChanged(ListSelectionEvent e) {
//		// TODO Auto-generated method stub
////		for (int i = 0; i < tables.size(); i++)
////		{
////			ListSelectionModel selModel = tables.get(i).getSelectionModel();
////			if(! selModel.isSelectionEmpty())
////			{
////				int row = selModel.getAnchorSelectionIndex();
////				log.info("row id is "+row);
////			}
////		}
//		for (int i = 0; i < tables.size(); i++)
//		{
//			tables.get(i).clearSelection();
//		}
//		JTable table = (JTable) e.getSource();
//		for (int i = 0; i < tables.size(); i++)
//		{
//			JTable currentTable = tables.get(i);
//			ListSelectionModel selModel = currentTable.getSelectionModel();
//			if (! selModel.isSelectionEmpty())
//			{
//				//int index = currentTable.rowAtPoint(e.getPoint());
//								
//				if(i != 0)
//					showGraph(i);
//				
////				if(i != 0) {
////					showGraph(i, index);
////				}
//				
////				currentTable.setRowSelectionInterval(index, index);
////
////				// get the index for next table that need to be highlighted
////
////				index++;
////				log.info("t" + index + "[" + i + "]");
////				// index--;
////
////				highlightTables(i, "t" + (index) + "[" + i + "]");
//
//				//
//				// for (int j = 0; j < i + 1; j++) {
//				// tables.get(j).setRowSelectionInterval(index, index);
//				// }
//			}
//		}
//	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		if (e.getSource() == opt_internal_button)
		{
			OptimizationInternalsFrame f = new OptimizationInternalsFrame("GProM Optimization Internals");
//			f.setSize(950, 600);
//			new design
			f.setSize(950, 300);
		} 
		
		if (e.getSource() == reset_button)
		{
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
			
			//get how many tuples in the initial table
			//num of update if update based on diff column
			String sql = GpromProcess.getTransactionIntermediateSQL(currentRow.getXID());
		    //String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
			ResultSet rs = DBManager.getInstance().executeQuery(sql);
			
			int numUp = 0;
			int pos = -1;
			try {
				while(rs.next())
				{
					for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
					{
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
				log.info("numUp: "+numUp);
				rs.first();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ResultSetMetaData rsmd = null;
			try
			{
				rsmd = rs.getMetaData();
			} catch (SQLException e1)
			{
				LoggerUtil.logException(e1,log);
			}
			
			for (int i = 0; i < count; i++) 
			{
				JTable jtb = tables.get(i);

				List<Integer> indexList = new ArrayList<Integer>();
				try
				{
					// index
					log.info("columncont + 1 = "+rsmd.getColumnCount());
					for (int j = 1; j < rsmd.getColumnCount() + 1; j++)
					{
						if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsmd.getColumnName(j)))
						{
							log.info("i=0, j = "+j);
							indexList.add(j);
						} 
						else if (Pattern.matches("PROV_U" + i + ".*", rsmd.getColumnName(j)))
						{
							log.info("i !=0, j = "+j);
							indexList.add(j);
						}
					}
				} 
				catch (SQLException e1)
				{
					LoggerUtil.logException(e1,log);
				}
				// log.info("index: " + indexList + currentTableName);
				
				
				if(i > 0)
				{
					EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
					String sqlType = currentInterval.getType();
					if(sqlType.equals("INSERT"))
						numUp++;
				}
				
				DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp);
				jtb.setModel(tm);
				
			}
			
			
		}
		
		
		if (e.getSource() == refresh_button)
		{
			int count = tableModels.size();
			log.info("count: "+count);
			
			int countSqls = sqlTextAreas.size();
			log.info("countTextAreas: "+countSqls);
			String newSql = "";
			for(int i=0; i<countSqls; i++)
				newSql = newSql + sqlTextAreas.get(i).getText() + ";";
			log.info("new sqls: "+newSql);
			
		    //String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
			String sql = GpromProcess.getReenactSQL(newSql);
			ResultSet rs = DBManager.getInstance().executeQuery(sql);
			ResultSetMetaData rsmd = null;
			
			//get how many tuples in the initial table
			//num of update if update based on diff column
			int numUp = 0;
			int pos = -1;
			try {
				while(rs.next())
				{
					for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
					{
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
				log.info("numUp: "+numUp);
				rs.first();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try
			{
				rsmd = rs.getMetaData();
			} catch (SQLException e1)
			{
				LoggerUtil.logException(e1,log);
			}
			
			for (int i = 0; i < count; i++) 
			{
				JTable jtb = tables.get(i);
				
				List<Integer> indexList = new ArrayList<Integer>();
				try
				{
					// index
					log.info("columncont + 1 = "+rsmd.getColumnCount());
					for (int j = 1; j < rsmd.getColumnCount() + 1; j++)
					{
						if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsmd.getColumnName(j)))
						{
							log.info("i=0, j = "+j);
							indexList.add(j);
						} 
						else if (Pattern.matches("PROV_U" + i + ".*", rsmd.getColumnName(j)))
						{
							log.info("i !=0, j = "+j);
							indexList.add(j);
						}
					}

				} 
				catch (SQLException e1)
				{
					LoggerUtil.logException(e1,log);
				}
				// log.info("index: " + indexList + currentTableName);
				
				if(i > 0)
				{
					EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
					String sqlType = currentInterval.getType();
					if(sqlType.equals("INSERT"))
						numUp++;
				}
				
				DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp);
				jtb.setModel(tm);
				
			}
		}
		
		
		
		if (e.getSource() == show_all_button)
		{
			int count = tableModels.size();
			log.info("count: "+count);
			
			int countSqls = sqlTextAreas.size();
			log.info("countTextAreas: "+countSqls);
			String newSql = "";
			for(int i=0; i<countSqls; i++)
				newSql = newSql + sqlTextAreas.get(i).getText() + ";";
			log.info("new sqls: "+newSql);
			
		    //String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
			String sql = GpromProcess.getReenactAllSQL(newSql);
			ResultSet rs = DBManager.getInstance().executeQuery(sql);
			ResultSetMetaData rsmd = null;
			
			//get how many tuples in the initial table
			//num of update if update based on diff column
			int numUp = 0;
			int pos = -1;
			try {
				while(rs.next())
				{
					for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
					{
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
				log.info("numUp: "+numUp);
				rs.first();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try
			{
				rsmd = rs.getMetaData();
			} catch (SQLException e1)
			{
				LoggerUtil.logException(e1,log);
			}
			
			for (int i = 0; i < count; i++) 
			{
				JTable jtb = tables.get(i);
				
				List<Integer> indexList = new ArrayList<Integer>();
				try
				{
					// index
					log.info("columncont + 1 = "+rsmd.getColumnCount());
					for (int j = 1; j < rsmd.getColumnCount() + 1; j++)
					{
						if (i == 0 && Pattern.matches("PROV_(?!U|query).*", rsmd.getColumnName(j)))
						{
							log.info("i=0, j = "+j);
							indexList.add(j);
						} 
						else if (Pattern.matches("PROV_U" + i + ".*", rsmd.getColumnName(j)))
						{
							log.info("i !=0, j = "+j);
							indexList.add(j);
						}
					}

				} 
				catch (SQLException e1)
				{
					LoggerUtil.logException(e1,log);
				}
				// log.info("index: " + indexList + currentTableName);
				
				if(i > 0)
				{
					EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i-1);
					String sqlType = currentInterval.getType();
					if(sqlType.equals("INSERT"))
						numUp++;
				}
				
				DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp);
				jtb.setModel(tm);
				
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

//	@Override
//	public void mouseClicked(MouseEvent e)
//	{
//		for (int i = 0; i < tables.size(); i++)
//		{
//			tables.get(i).clearSelection();
//		}
//		JTable table = (JTable) e.getSource();
//		for (int i = 0; i < tables.size(); i++)
//		{
//			JTable currentTable = tables.get(i);
//			int row = currentTable.getSelectedRow();
//			log.info("row : "+row);
//			TableModel selModel = currentTable.getModel();
//			if (currentTable == table)
//			{
//				
//				String id = selModel.getValueAt(row, 0).toString();
//				log.info("id : "+id);
//				String id1 = selModel.getValueAt(row, 1).toString();
//				log.info("id1 : "+id1);
////				String id2 = selModel.getValueAt(row, 2).toString();
////				log.info("id2 : "+id2);
//				
//				int index = currentTable.rowAtPoint(e.getPoint());
//								
//				if(i != 0)
//					showGraph(i);
//				
////				if(i != 0) {
////					showGraph(i, index);
////				}
//				
//				currentTable.setRowSelectionInterval(index, index);
//
//				// get the index for next table that need to be highlighted
//
//				index++;
//				log.info("t" + index + "[" + i + "]");
//				// index--;
//
//				highlightTables(i, "t" + (index) + "[" + i + "]");
//
//				//
//				// for (int j = 0; j < i + 1; j++) {
//				// tables.get(j).setRowSelectionInterval(index, index);
//				// }
//			}
//		}
//
//	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			tables.get(i).clearSelection();
		}
		JTable table = (JTable) e.getSource();
		for (int i = 0; i < tables.size(); i++)
		{
			JTable currentTable = tables.get(i);
			if (currentTable == table)
			{
				int index = currentTable.rowAtPoint(e.getPoint());
				int row = currentTable.rowAtPoint(e.getPoint()) + 1;	
				if(i != 0)
					showGraph(i, row);

//				if(i != 0)
//					showGraph(i);
				
//				if(i != 0) {
//					showGraph(i, index);
//				}
				
				currentTable.setRowSelectionInterval(index, index);

				// get the index for next table that need to be highlighted

//				index++;
//				log.info("t" + index + "[" + i + "]");
				// index--;


//				highlightTables(i, "t" + (index) + "[" + i + "]");

				//
				// for (int j = 0; j < i + 1; j++) {
				// tables.get(j).setRowSelectionInterval(index, index);
				//}
			}
		}

	}

	private void highlightTables(int currentTableIndex, String tupleIndex)
	{
		JTable currentTable = tables.get(currentTableIndex);
		// get the index for row we want to highlight
		Pattern pattern = Pattern.compile("t(\\d*)\\[(\\d*)\\]");
		Matcher matcher = pattern.matcher(tupleIndex);
		String rowIndex = "";
		String tableIndex = "";
		if (matcher.matches())
		{
			rowIndex = matcher.group(1);
			tableIndex = matcher.group(2);
			log.info(rowIndex + ", " + tableIndex);
		}
		// highlight table
		currentTable.setRowSelectionInterval(Integer.parseInt(rowIndex) - 1, Integer.parseInt(rowIndex) - 1);
		log.info("currentTupeIndex" + tupleIndex + "  tableIndex" + currentTableIndex);
		if (currentTableIndex <= -1)
		{
			return;
		}
		List<String> provenanceList = ((DebuggerTableModel) tables.get(currentTableIndex).getModel()).getPrevRelation()
				.get(tupleIndex);
		if (provenanceList == null)
		{
			return;
		}
		log.info(((DebuggerTableModel) tables.get(currentTableIndex).getModel()).getPrevRelation());
		log.info(tupleIndex);
		log.info(provenanceList);

		for (int i = 0; i < provenanceList.size(); i++)
		{
			String nextTupleIndex = provenanceList.get(i);
			highlightTables(--currentTableIndex, nextTupleIndex);
		}
	}
	
	

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
	public void showGraph(int level, int row) {
		//Make the label text on the label
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		//Set node style
		String styleSheet =
	            "node {" +
	            "size: 35;" +
	            "fill-color: grey;" +
	            "shape: box;" +
	            "}";
		//Graph
		Graph graph = new DefaultGraph("ProvenanceGraph");
		//graph.addAttribute("ui.stylesheet", "node { size: 35; shape: box; fill-color: grey;}");  // size-mode: fit
		graph.addAttribute("ui.stylesheet", styleSheet);


		            
		
//		Node a = graph.addNode("t2[0]");
//		Node b = graph.addNode("t2[1]");
//		Node c = graph.addNode("t2[2]");
//		
//		
//		a.setAttribute("xy",0,0);
//		b.setAttribute("xy",1,0);
//		c.setAttribute("xy",2,0);
//		
//		a.addAttribute("label", "t2[0]");
//		b.addAttribute("label", "t2[1]");
//		c.addAttribute("label", "t2[2]");
//		
//		
//		
//		graph.addEdge("u1", b, a,true);
//		graph.addEdge("u2", c, b,true);
//		
//		graph.display(false);		//false is to disable automatic placement of the nodes
		
		
//		graph.addAttribute("ui.sreeenshot", "/home/david/export.jpg");
		
		
		
		
		
		//node to pre and next;
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

		ArrayList<Node> nodes = new ArrayList<Node>();
		int attribute = 0;
		for(int i = 0; i <= level; i++) {
			String nodeName = "t" + String.valueOf(row) + "[" + String.valueOf(i) + "]";
			Node tempNode = graph.addNode(nodeName);
			//set node position x and y axis
			tempNode.setAttribute("xy", attribute, 0); 						
			attribute++;
			
			tempNode.addAttribute("label", nodeName);
			nodes.add(tempNode);
		}
//		log.info(nodes.size());
		
		for(int i = 0; i < level; i++) {
			graph.addEdge("u" + String.valueOf(i), nodes.get(i), nodes.get(i + 1), true);
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
