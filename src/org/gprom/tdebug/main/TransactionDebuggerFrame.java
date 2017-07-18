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
//
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
	private final static int HEIGHTFORGRAPHPANEL = 250; //

	static Logger log = Logger.getLogger(TransactionDebuggerFrame.class);
	
	private JButton reset_button = null;
	private JButton show_affected_button = null;
	private JButton show_all_button = null;
	private JButton opt_internal_button = null;
	private JButton add_stmt_button = null;
	private JButton del_stmt_button = null;
	private JButton whatif_button = null;
	
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
	private List<JLabel> sqlLabels = new ArrayList<JLabel>();
	private List<Integer> numUps = new ArrayList<Integer>();
	
	private EventTimeBarRow currentRow;
	
	private ResultSet rsReset;
	
	//store all old values of first table, when table data changed get matched old value from here 
	private Map<Integer, List<Object>> old = new HashMap<Integer, List<Object>>();
	private Map<Integer, List<Object>> oldMap = new HashMap<Integer, List<Object>>();
	private Map<Integer, List<Object>> newMap = new HashMap<Integer, List<Object>>();
	
	private String seriSql = "";
	private String commSql = "";
	private boolean clickWhatIf = false;

	public TransactionDebuggerFrame(EventTimeBarRow row)
	{
		super();
		this.currentRow = row;
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
		List<Map<Integer, Object>> rsList = null;
		List<Map<String, Object>> rsListS = null;
		try {
			//rsListS = convertListS(rs);
			//rs.first();
			rsList = convertList(rs);
			//rs.first();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		numUps.clear();
		//get how many tuples in the initial table
		//num of update if update based on diff column
		int numUp = 0;
		int pos = -1;
		String nameKey = "";
		try {
			//rs.first();
			//log.info("test: list size "+rsListS.size());
			int cont = 0;
			rs.beforeFirst();
			while(rs.next())
			{
			cont++;
			//for (int i=0; i<rsListS.size(); i++)
			//{
				for(int i=1; i< rs.getMetaData().getColumnCount()+1; i++)
				//for(Entry<String, Object> entry : rsListS.get(i).entrySet())
				{
					
					//log.info("test: i "+i + " key is "+entry.getKey() + " value is "+entry.getValue());
					log.info("row: "+cont + " name: "+ rs.getMetaData().getColumnName(i) + " value: " + rs.getString(i));
					if(Pattern.matches("PROV_(?!U|query).*", rs.getMetaData().getColumnName(i)))
					//if(Pattern.matches("PROV_(?!U|query).*", entry.getKey()))
					{
//						log.info("test: key is "+entry.getKey());
//						log.info("test: value is "+entry.getValue());
						pos = i;
						//nameKey = entry.getKey();
						break;	
					}
				}
				if(pos != -1)
				{
					String v = rs.getString(pos);
					//String v = (String) rsListS.get(i).get(nameKey);
					log.info("v: "+ v);
					if(v != null)
						numUp++;	
				}				
			}
			log.info("cont: "+cont);
			log.info("numUp: "+numUp);
			//rs.first();
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
			numUps.add(numUp);			
			//DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i, currentRow, numUp, tables);
			DebuggerTableModel tm = new DebuggerTableModel(rsList,rs, indexList, i, currentRow, numUp, tables);

			tableModels.add(tm);
			JPanel jp = new JPanel();
			jp.setLayout(null);
			jp.setBounds(DEBUGGER_CELL_WIDTH * i, 100, 300, 205);
			jp.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
			stmt_table_panel.add(jp);
			JTable table = new JTable(tm);
			table.setSelectionBackground(new Color(50, 205, 50));
            //tm.isCellEditable(1, 1);
			
            //tm.addTableModelListener();
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
		
		//store all old value of the first table
		JTable firstTable = tables.get(0);
		int storeColCount = firstTable.getColumnCount();
		int storeRowCount = firstTable.getRowCount();
		log.info("storeRowCount: " + storeRowCount + " storeColCount: " + storeColCount);
			
		for(int r=0; r<storeRowCount; r++)
		{
			List<Object> l = new ArrayList<Object>();
			for(int c=2; c<storeColCount; c++)
			{
				l.add(firstTable.getValueAt(r, c));			
			}
			old.put(r, l);
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
		// jb1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		// jb2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
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
		//		getG1.addActionListener(this);
		//		getG2.addActionListener(this);
		//		getG3.addActionListener(this);

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
	//				int index = currentTable.rowAtPoint(e.getPoint());
	//								
	////				if(i != 0)
	////					showGraph(i);
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
	//				
	//				 for (int j = 0; j < i + 1; j++) {
	//				 tables.get(j).setRowSelectionInterval(index, index);
	//				 }
	//			}
	//		}
	//	}
	//    tables.get(2).addMouseListener(new MouseAdapter(){    //鼠标事件
	//        public void mouseClicked(MouseEvent e){
	//            int selectedRow = table.getSelectedRow(); //获得选中行索引
	//            Object oa = tableModel.getValueAt(selectedRow, 0);
	//            Object ob = tableModel.getValueAt(selectedRow, 1);
	//            aTextField.setText(oa.toString());  //给文本框赋值
	//            bTextField.setText(ob.toString());
	//        }
	//    });

	//    addKeyListener(new KeyAdapter() {
	//        @Override
	//        public void keyReleased(KeyEvent e) {
	//            event();
	//        }
	//    });

	@Override
	public void actionPerformed(ActionEvent e)
	{		
		//		int col = tables.get(2).getSelectedColumn();
		//		int row = tables.get(2).getSelectedRow();
		//		tables.get(2).getCellEditor(row, col).stopCellEditing();
		//		System.out.println(col + " " + row + " " + tables.get(2).getValueAt(row, col));
		//		

		//		TableCellListener tcl = (TableCellListener)e.getSource();
		//        System.out.println("Row   : " + tcl.getRow());
		//        System.out.println("Column: " + tcl.getColumn());
		//        System.out.println("Old   : " + tcl.getOldValue());
		//        System.out.println("New   : " + tcl.getNewValue());

		if(e.getSource() == del_stmt_button)
		{


		}

		if (e.getSource() == whatif_button)
		{

			clickWhatIf = true;  //user should click what-if button firstly, then user can click show affected or show all button

			String appendSql = "";
			int col1 = -1;
			int row1 = -1;
			Object s1 = null;

			//e.g., used to add "OPTIONS (NO PROVENANCE AS OF SCN 1425819) 
			//UPDATE R SET A=300,B=4 WHERE A=10 AND B=4;" in front of sql
			//			appendSql = "UPDATE ";
			String tableName = "";
			//			String setClause = "SET ";
			//			String wheClause = "WHERE ";
			List<String> colNames = new ArrayList<String>();  //store column names

			JTable firstTable = tables.get(0);
			int colCount = firstTable.getColumnCount();			 
			for(int i=2; i<colCount; i++)
			{
				String cName = firstTable.getModel().getColumnName(i);
				//Pattern p = Pattern.compile("PROV_U.*__(\\w*)_(\\w*)");
				Pattern p = Pattern.compile("PROV_(\\w*)_(\\w*)"); 
				Matcher m = p.matcher(cName);
				if (!m.find())
				{
					log.info("regular expression succeed!");
				}
				else
				{
					log.info("group 1: " + m.group(1) + " group 2: "+m.group(2));
					tableName = m.group(1);
					cName = m.group(2);
					colNames.add(cName);
				}
			}


			log.info("new map size: "+ newMap.size());
			for (Entry<Integer, List<Object>> entry : newMap.entrySet()) //how many update in result sql (number of tuples were updated)
			{
				int k = entry.getKey();
				List<Object> newV = entry.getValue();
				List<Object> oldV = oldMap.get(k);
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



			//			for (int i = 0; i < tables.size(); i++)
			//			{
			//				ListSelectionModel selModel = tables.get(i).getSelectionModel();
			//				if(! selModel.isSelectionEmpty())
			//				{
			//					//int row = selModel.getAnchorSelectionIndex();
			//					//log.info("row id is "+row);
			//					log.info("table id is "+ i);
			//					JTable ct = tables.get(i);
			//					//int rcount = ct.getRowCount();
			//					int ccount = ct.getColumnCount();
			//					col1 = ct.getSelectedColumn();
			//					row1 = ct.getSelectedRow();
			//					log.info("table id is "+ i+" with "+ ct.getRowCount() + " rows and "
			//							+ ct.getColumnCount() + " columns.");
			//					log.info("current row is "+row1 +" current column is "+col1);
			//					
			//					//e.g., used to add "OPTIONS (NO PROVENANCE AS OF SCN 1425819) 
			//					//UPDATE R SET A=300,B=4 WHERE A=10 AND B=4;" in front of sql
			//					appendSql = "UPDATE ";
			//					String tableName = "";
			//					String sClause = "SET ";
			//					String cClause = "WHERE ";
			//					for(int j = 2; j < ccount; j++)
			//					{
			//						String cName = ct.getModel().getColumnName(j);
			//   					    //Pattern p = Pattern.compile("PROV_U.*__(\\w*)_(\\w*)");
			//   					    Pattern p = Pattern.compile("PROV_(\\w*)_(\\w*)"); 
			//   					    Matcher m = p.matcher(cName);
			//						if (!m.find())
			//						{
			//							log.info("regular expression succeed!");
			//						}
			//						else
			//						{
			//							log.info("group 1: " + m.group(1) + " group 2: "+m.group(2));
			//							tableName = m.group(1);
			//							cName = m.group(2);
			//						}
			//						//Object s1 = ct.getCellEditor(row1, j).getCellEditorValue();
			//						if(j != col1){			
			//							Object s = ct.getModel().getValueAt(row1, j);
			//							log.info("num "+ j + " is " +s.toString());
			//							sClause = sClause + cName + "=" + s.toString();
			//							if(j+1 < ccount)
			//								sClause = sClause + " , ";
			//						}
			//						else
			//						{
			//							ct.getCellEditor(row1, col1).stopCellEditing();
			//							s1 = ct.getCellEditor(row1, col1).getCellEditorValue();
			//							sClause = sClause + cName + "=" + s1.toString();
			//							if(j+1 < ccount)
			//								sClause = sClause + " , ";
			//							
			////							//used to update the statment
			////							//e.g., if changed B from 4 to 40
			////							//first find "B =" and 4
			////							//Second construct "B = 4" and "B = 40"
			////							//Third replace "B = 4" to "B = 40"
			////							String kw2 = cName + " = ";
			////							log.info("key word: "+ kw2);
			////	   					    Pattern p2 = Pattern.compile(".*"+kw2+"(\\w*).*");
			////	   						JTextArea ja2 = sqlTextAreas.get(i-1);
			////	   						String smt2 = ja2.getText();
			////	   						log.info("matched statment: "+ smt2);
			////	   						String newKw2 = kw2;
			////	   						newKw2 = newKw2 + s1.toString();
			////	   						log.info("matched new key word: "+ newKw2);
			////
			////	   					    Matcher m2 = p2.matcher(smt2);
			////							if (!m2.find())
			////							{
			////								log.info("regular expression succeed!");
			////							}
			////							else
			////							{
			////								log.info("matched statment group 0: "+ m2.group(0));
			////								log.info("matched statment group 1: "+ m2.group(1));
			////								
			////								kw2 = kw2 + m2.group(1);
			////								log.info("matched need to replace: "+ kw2);
			////								smt2 = smt2.replaceAll(kw2, newKw2);
			////								log.info("matched new statment: "+ smt2);
			////								ja2.setText(smt2);
			////							}
			//						}
			//					}
			//					log.info("sql set = "+ sClause);
			//					
			//					for(int k=0; k<=i; k++)
			//					{
			//						JTable pt = tables.get(k);
			//						int pRowCount = pt.getRowCount();
			//						if(row1 <= pRowCount)
			//						{
			//							for(int m=2; m<ccount; m++)
			//							{
			//								String sName = pt.getModel().getColumnName(m);
			//		   					    Pattern ps = Pattern.compile("PROV_.*_(\\w*)");
			//		   					    Matcher ms = ps.matcher(sName);
			//								if (!ms.find())
			//								{
			//									log.info("regular expression succeed!");
			//								}
			//								else
			//								{
			//									log.info("group 1: " + ms.group(1));
			//									//tableName = ms.group(1);
			//									sName = ms.group(1);
			//								}
			//				
			//								Object c1 = pt.getModel().getValueAt(row1, m);
			//								log.info("num "+ m + " is " +c1.toString());
			//								cClause = cClause + sName + "=" + c1.toString();
			//								if(m+1 < ccount)
			//									cClause = cClause + " AND ";	
			//							}
			//						}
			//					}
			//					log.info("sql where = "+ cClause);
			//					
			//					appendSql = appendSql + tableName + " " + sClause + " " + cClause + ";";
			//					log.info("sql = "+ appendSql);
			//
			//				}
			//			}

			int count = tableModels.size();
			log.info("count: "+count);

			int countSqls = sqlTextAreas.size();
			log.info("countTextAreas: "+countSqls);
			String newSql = "";
			for(int i=0; i<countSqls; i++)
				newSql = newSql + sqlTextAreas.get(i).getText() + ";";
			log.info("new sqls: "+newSql);

			String newSql_rc = "";
			for(int i=0; i<countSqls; i++)
			{
				EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
				String scn_rc = currentInterval.getSCN();
				//newSql_rc = newSql_rc + sqlTextAreas.get(i).getText() + "; AS OF SCN " + scn_rc + " ";
				newSql_rc = newSql_rc + "OPTIONS (AS OF SCN " + scn_rc + ") "  + sqlTextAreas.get(i).getText() + "; ";
			}

			if(newMap.size() != 0)
				newSql_rc = "OPTIONS (NO PROVENANCE AS OF SCN " + currentRow.getStartSCN() + ") " + appendSql + " " + newSql_rc;

			log.info("new sqls: "+newSql_rc);

			//store sql
			commSql = newSql_rc;
			seriSql = newSql;

			//String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
			String sql = "";
			if(currentRow.getIsoLevel().equals("1"))
				sql = GpromProcess.getReenactSQL(currentRow.getStartSCN(),newSql_rc);
			else //serializable //TODO construct appendsql for this one and add case if newMap size equal to 0
			{
				//if serializable, each scn same, get first one
				EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(0);
				sql = GpromProcess.getSerializableReenactSQL(currentInterval.getSCN(), newSql);
			}

			//String sql = GpromProcess.getReenactSQL(newSql);
			ResultSet rs = DBManager.getInstance().executeQuery(sql);
			ResultSetMetaData rsmd = null;

			numUps.clear();
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

				numUps.add(numUp);
				List<Map<Integer, Object>> rsList = null;
				try {
					rsList = convertList(rs);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				DebuggerTableModel tm = new DebuggerTableModel(rsList, rs, indexList, i, currentRow, numUp, row1, col1, s1,tables);
				jtb.setModel(tm);			
			}

			//			int col1 = tables.get(2).getSelectedColumn();
			//			int row1 = tables.get(2).getSelectedRow();
			//			DebuggerTableModel tm = (DebuggerTableModel) tables.get(2).getModel();
			//			tables.get(2).getCellEditor(row1, col1).stopCellEditing();
			//			
			//			CellEditorListener l = null;
			//			
			//			tables.get(2).getCellEditor(row1, col1).addCellEditorListener(l);
			//			//tables.get(2).getCellEditor(row1, col1).getCellEditorValue();
			//			//Object ob = tables.get(2).getModel().getValueAt(row1, col1);
			//			tm.setValueAt(tables.get(2).getCellEditor(row1, col1).getCellEditorValue(), row1, col1);
			//			tables.get(2).setModel(tm);
			//			Object s1 = tables.get(2).getCellEditor(row1, col1).getCellEditorValue();
			//			boolean b1 = tables.get(2).isCellEditable(row1, col1);
			//			System.out.println(col1 + " " + row1 + " " + tables.get(2).getValueAt(row1, col1)+ " " + s1.toString() + " Edit: "+b1 );
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
			//			newMap.clear();
			//			oldMap.clear();
			//			
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

			numUps.clear();
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

				numUps.add(numUp);
				List<Map<Integer, Object>> rsList = null;
				try {
					rsList = convertList(rs);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				DebuggerTableModel tm = new DebuggerTableModel(rsList, rs, indexList, i, currentRow, numUp,tables);
				jtb.setModel(tm);

			}


		}


		if (e.getSource() == show_affected_button)
		{
			if(clickWhatIf)
			{
				int count = tableModels.size();
				//			log.info("count: "+count);
				//			
				//			int countSqls = sqlTextAreas.size();
				//			log.info("countTextAreas: "+countSqls);
				//			String newSql = "";
				//			for(int i=0; i<countSqls; i++)
				//				newSql = newSql + sqlTextAreas.get(i).getText() + ";";
				//			log.info("new sqls: "+newSql);
				//			
				//			String newSql_rc = "";
				//			for(int i=0; i<countSqls; i++)
				//			{
				//				EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
				//                String scn_rc = currentInterval.getSCN();
				//				//newSql_rc = newSql_rc + sqlTextAreas.get(i).getText() + "; AS OF SCN " + scn_rc + " ";
				//				newSql_rc = newSql_rc + "OPTIONS (AS OF SCN " + scn_rc + ") "  + sqlTextAreas.get(i).getText() + "; ";
				//			}
				//			log.info("new sqls: "+newSql_rc);

				String newSql = seriSql;
				String newSql_rc = commSql;
				log.info("new sqls: "+newSql_rc);
				log.info("new sqls: "+newSql);

				//String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
				String sql = "";
				if(currentRow.getIsoLevel().equals("1"))
					sql = GpromProcess.getReenactSQL(currentRow.getStartSCN(),newSql_rc);
				else //serializable
				{
					//if serializable, each scn same, get first one
					EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(0);
					sql = GpromProcess.getSerializableReenactSQL(currentInterval.getSCN(), newSql);
				}

				//String sql = GpromProcess.getReenactSQL(newSql);
				ResultSet rs = DBManager.getInstance().executeQuery(sql);
				ResultSetMetaData rsmd = null;

				numUps.clear();
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

					numUps.add(numUp);
					List<Map<Integer, Object>> rsList = null;
					try {
						rsList = convertList(rs);
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					DebuggerTableModel tm = new DebuggerTableModel(rsList, rs, indexList, i, currentRow, numUp,tables);
					jtb.setModel(tm);

				}
			}
		}



		if (e.getSource() == show_all_button)
		{
			if(clickWhatIf)
			{

				int count = tableModels.size();
				//			log.info("count: "+count);
				//			
				//			int countSqls = sqlTextAreas.size();
				//			log.info("countTextAreas: "+countSqls);
				//			
				//			String newSql = "";
				//			for(int i=0; i<countSqls; i++)
				//				newSql = newSql + sqlTextAreas.get(i).getText() + ";";
				//			log.info("new sqls: "+newSql);
				//			
				//			String newSql_rc = "";
				//			for(int i=0; i<countSqls; i++)
				//			{
				//				EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(i);
				//                String scn_rc = currentInterval.getSCN();
				//				//newSql_rc = newSql_rc + sqlTextAreas.get(i).getText() + "; AS OF SCN " + scn_rc + " ";
				//				newSql_rc = newSql_rc + "OPTIONS (AS OF SCN " + scn_rc + ") "  + sqlTextAreas.get(i).getText() + "; ";
				//			}

				String newSql = seriSql;
				String newSql_rc = commSql;
				log.info("new sqls: "+newSql_rc);
				log.info("new sqls: "+newSql);

				//String sql = GpromProcess.getReenactSQL("UPDATE R SET A = 100 WHERE B = 3;");
				String sql = "";
				if(currentRow.getIsoLevel().equals("1"))
					sql = GpromProcess.getReenactAllSQL(currentRow.getStartSCN(),newSql_rc);
				else //serializable
				{
					//if serializable, each scn same, get first one
					EventInterval currentInterval = (EventInterval) currentRow.getIntervals().get(0);
					sql = GpromProcess.getSerializableReenactAllSQL(currentInterval.getSCN(), newSql);
				}

				//String sql = GpromProcess.getReenactAllSQL(newSql);
				ResultSet rs = DBManager.getInstance().executeQuery(sql);
				ResultSetMetaData rsmd = null;

				numUps.clear();
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

					numUps.add(numUp);
					List<Map<Integer, Object>> rsList = null;
					try {
						rsList = convertList(rs);
					} catch (SQLException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					DebuggerTableModel tm = new DebuggerTableModel(rsList,rs, indexList, i, currentRow, numUp,tables);
					jtb.setModel(tm);

				}
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
	
	
//	@Override
//	public void tableChanged (TableModelEvent e) {
//	    AbstractTableModel model = (AbstractTableModel) e.getSource();
//	    TableModelListener[] listeners = model.getTableModelListeners();
//	    for (TableModelListener listener : listeners) {
//	        if (listener instanceof JTable) {
//	            System.out.println(((JTable)listener).getName());
//	        }
//	    }
//	}
	
	@Override
	public void tableChanged (TableModelEvent e) {
		
        int row = e.getFirstRow();
        int column = e.getColumn() + 2;
        DebuggerTableModel model = (DebuggerTableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        System.out.println("col name： "+columnName);
        System.out.println("row: "+ row + " col: " + column +" value: "+data.toString());
        
        JTable fTable = tables.get(0);
        int fCountCol = fTable.getColumnCount();
        List<Object> nList = new ArrayList();
        for(int c=2; c<fCountCol; c++)
        {
        	Object d = model.getValueAt(row, c);
        	nList.add(d);
        }     
        newMap.put(row, nList);
        
        if(!oldMap.containsKey(row))
        {
        	List l = old.get(row);    
        	oldMap.put(row, l);
        }    
        
        //check info
        for (Entry<Integer, List<Object>> entry : newMap.entrySet()) {
        	int r = entry.getKey();
        	List cl = entry.getValue();
        	
        	log.info("New Row: " + r);
        	for(int i=0; i<cl.size(); i++)
        		log.info("New v: "+ cl.get(i));        	
        }
        
        for (Entry<Integer, List<Object>> entry : oldMap.entrySet()) {
        	int r = entry.getKey();
        	List cl = entry.getValue();
        	
        	log.info("Old Row: " + r);
        	for(int i=0; i<cl.size(); i++)
        		log.info("Old v: "+ cl.get(i));        	
        }
        
	}

	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			tables.get(i).clearSelection();
		}
		
		for (int i = 0; i < sqlLabels.size(); i++)
		{
			sqlLabels.get(i).setForeground(Color.BLACK);
		}
		
		JTable table = (JTable) e.getSource();
		for (int i = 0; i < tables.size(); i++)
		{
			JTable currentTable = tables.get(i); 
			if (currentTable == table)
			{
				int index = currentTable.rowAtPoint(e.getPoint());
				int row = currentTable.rowAtPoint(e.getPoint()) + 1;	
				//if(i != 0)
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
	            "fill-mode: dyn-plain;" +
	            "fill-color: grey, red;" +
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
		
		log.info("level : "+level + " numUpSize : "+ numUps.size());
		for(int t=0; t< numUps.size(); t++)
			log.info("numUps = "+ numUps.get(t));

		for(int i = 0; i <= level; i++) 
		{
			if(row <= numUps.get(i))
			{
				boolean addU = false;
				if(i > 0)
				{
					JTable tableC = tables.get(i); 
					JTable tableP = tables.get(i-1); 
					int colLen = tableC.getColumnCount();
					log.info("num column tableC: " + colLen);
					log.info("num column tableP: " + tableP.getColumnCount());
					for(int c=2; c<colLen; c++)
					{
						log.info("row: " + row);
						log.info("valueC: " + tableC.getValueAt(row-1, c));
						log.info("valueP: " + tableP.getValueAt(row-1, c));
						if(!tableC.getValueAt(row-1, c).equals(tableP.getValueAt(row-1, c)))
							addU = true;
					}
					
				}
				
				if(i != 0 && addU)
				{
					int updateIndex = i;
					String uName = "U" + updateIndex;
					Node uNode = graph.addNode(uName);
					//set node position x and y axis
					uNode.setAttribute("xy", attribute, 0); 						
					attribute++;

					uNode.addAttribute("label", uName);
					uNode.setAttribute("ui.color", 0.5);
					nodes.add(uNode);
					
					sqlLabels.get(i-1).setForeground(Color.RED);
				}
				
				String nodeName = "t" + String.valueOf(row) + "[" + String.valueOf(i) + "]";
				Node tempNode = graph.addNode(nodeName);
				//set node position x and y axis
				tempNode.setAttribute("xy", attribute, 0); 						
				attribute++;

				tempNode.addAttribute("label", nodeName);
				nodes.add(tempNode);
				
//				if(i != level)
//				{
//					int updateIndex = i + 1;
//					String uName = "U" + updateIndex;
//					Node uNode = graph.addNode(uName);
//					//set node position x and y axis
//					uNode.setAttribute("xy", attribute, 0); 						
//					attribute++;
//
//					uNode.addAttribute("label", uName);
//					nodes.add(uNode);
//				}
					
			}
		}
		
//		log.info(nodes.size());
		
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


