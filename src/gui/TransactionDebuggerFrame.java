package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import gui.transactionDebuggerFrame.OptimizationInternalsFrame;
import gui.transactionDebuggerFrame.model.DebuggerTableModel;
import process.DotWrapper;
import process.GpromProcess;
import timebars.eventmonitoring.model.EventInterval;
import timebars.eventmonitoring.model.EventTimeBarRow;

public class TransactionDebuggerFrame extends JFrame implements ActionListener, ComponentListener, MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 869036626552676746L;
	
	private final static int DEBUGGER_LEFT_PADDING =  135;
	private final static int DEBUGGER_CELL_WIDTH =  295;
	private final static int XFORBUTTONS = 1060;
	private final static int WIDTHFORMAINSCROLLPANE = DEBUGGER_CELL_WIDTH * (3) + 8;
	private final static int HEIGHTFORMAINSCROLLPANE = 195;
	private final static int WIDTHFORGRAPHPANEL = 893;
	private final static int HEIGHTFORGRAPHPANEL = 250; //
	
	private JButton refresh_button = null;
    private JButton opt_internal_button = null;
    private JButton add_stmt_button = null;
    private JButton del_stmt_button = null;
    private JButton show_hide_button = null;
    JScrollPane main_scrollPane = null;
    JPanel graphPanel = null;
    JLabel imageLabel = null;
    JPanel panel_table = null;
    JPanel panel_graph = null; 
	private int initialWidth = 0;
	private int initialHeight = 0;
	private List<JButton> buttons = new ArrayList<JButton>();
	private List<JTable>	tables = new ArrayList<JTable>();
	
	private EventTimeBarRow currentRow;
	public TransactionDebuggerFrame(EventTimeBarRow row) {
		super();
		this.currentRow = row;
		setup();
		setupListeners();
		initialWidth = this.getWidth();
		initialHeight = this.getHeight();
	}
	
	
	
	
	private void setup() {
	//load dot png
		DotWrapper dw = new DotWrapper();
		try {
			dw.init();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
  
		setTitle("Debug Panel");
		setSize(1200, 600);
				
				JPanel stmt_table_panel = new JPanel();
				stmt_table_panel.setPreferredSize(new Dimension(DEBUGGER_CELL_WIDTH * (currentRow.getIntervals().size() + 1), 300));
				main_scrollPane = new JScrollPane(stmt_table_panel);
				main_scrollPane.setBounds(DEBUGGER_LEFT_PADDING, 5, WIDTHFORMAINSCROLLPANE, HEIGHTFORMAINSCROLLPANE);
		        JPanel jp1 = new JPanel();
//		        JPanel jp2 = new JPanel();
//		        JPanel jp3 = new JPanel();
		        JTextArea ja1=new JTextArea(5,20);
//		        JTextArea ja2=new JTextArea(5,20);
//		        JTextArea ja3=new JTextArea(5,20);
		        
//		        ja1.setFont(Bold);
		        
		       
		        String originalTable_str = "\n\n Original Table";
		        // display transaction
		        for (int i = 0; i < currentRow.getIntervals().size(); i++) {
		        	EventInterval currentInterval = (EventInterval)currentRow.getIntervals().get(i);
		        	String originalStmt = currentInterval.getSql();
		        	System.out.println(originalStmt);
		        	JPanel jp = new JPanel();
		        	JTextArea ja=new JTextArea(5,20);
		        	ja.setEditable(false);
		        	ja.setText(originalStmt);
		        	ja.setLineWrap(true);
		        	jp.add(ja);
		        	stmt_table_panel.add(jp);
		        	jp.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        	jp.setBounds(DEBUGGER_CELL_WIDTH * (i + 1), 0, 300, 95);
		        	ja.setBounds(0, 10, 5, 20);
		        	
//		        	if (i == 3) {
//		        		break;
//		        	}
		        }
//		        String str2 = "\n UPDATE account \n SET bal=bal-35 \n WHERE cust='Alice' AND typ='Checking'";
//		        String str3 = "\n UPDATE account \n SET bal=bal+35 \n WHERE cust='Alice' AND typ='Savings'";
		        ja1.setText(originalTable_str);
//		        ja2.setText(str2);
//		        ja3.setText(str3);
		        
//		        ja1.setCaretColor(Color.BLUE);
//		        ja2.setCaretColor(Color.BLUE);
//		        ja3.setCaretColor(Color.BLUE);
		        
		        ja1.setEditable(false);
//		        ja2.setEditable(false);
//		        ja3.setEditable(false);
		        
		        jp1.add(ja1);
//		        jp2.add(ja2);
//		        jp3.add(ja3);
		        
		        
		        this.setLayout(null);
		        stmt_table_panel.setLayout(null);
		        stmt_table_panel.add(jp1);
//		        f.add(jp2);
//		        f.add(jp3);
		        
		        jp1.setBounds(0, 0, 300, 95);
		        jp1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//		        jp2.setBounds(430, 5, 300, 95);
//		        jp2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//		        jp3.setBounds(728, 5, 300, 95);
//		        jp3.setBorder(BorderFactory.createLineBorder(Color.gray,3));

		        ja1.setBounds(0, 10, 5, 20);
//		        ja2.setBounds(0, 10, 5, 20);
//		        ja3.setBounds(0, 10, 5, 20);
		        
		        
		        
		        //second line
		        // add GProm query result
		        String sql = GpromProcess.getTransactionIntermediateSQL(currentRow.getXID());
//		        System.out.println(sql);
		        ResultSet rs = GpromProcess.getTransactionIntermediateSQLOutput(sql);
		        ResultSetMetaData rsmd = null;
		        try {
					rsmd = rs.getMetaData();
				} catch (SQLException e) {
					e.printStackTrace();
				}


		        for (int i = 0; i < currentRow.getIntervals().size() + 1; i++) {
			        //set up indexList 

		        	List<Integer> indexList = new  ArrayList<Integer>();
		        	String currentTableName = null;
		        	try {
						//index
		        		for(int j = 1; j < rsmd.getColumnCount() + 1; j++) {
							if ( i == 0 && Pattern.matches("PROV_(?!U).*" ,rsmd.getColumnName(j))) {
								indexList.add(j);
							} else if (Pattern.matches("PROV_U" + i + ".*" ,rsmd.getColumnName(j))) {
								indexList.add(j);
							}
						}
		        		
						//tablename
						if (currentTableName == null) {
							Pattern p=Pattern.compile("PROV_(?!U)(\\w*)_.*|PROV_U" + i + "__(\\w*)_.*"); 
							Matcher m=p.matcher(rsmd.getColumnName(indexList.get(0))); 
							if (!m.find()) {
								System.out.println("regular expression succeed!");
							}
							if (i == 0) {
								currentTableName = m.group(1);	
							} else {
								currentTableName = m.group(2);
							}
							
						}
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
//		        	System.out.println("index: " + indexList  + currentTableName);
		        	DebuggerTableModel tm = new DebuggerTableModel(rs, indexList, i);
		        	JPanel jp = new JPanel();
		        	jp.setLayout(null);
		        	jp.setBounds(DEBUGGER_CELL_WIDTH * i , 100, 300, 205);
		        	jp.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        	stmt_table_panel.add(jp);
		        	JTable table = new JTable(tm);
		        	tables.add(table);
		        	JScrollPane scrollPane = new JScrollPane(table);
//		        	scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        	scrollPane.setBounds(5, 20, 290, 185);
		        	
		        	table.setFillsViewportHeight(true);
		        	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		        	JLabel tableName = new JLabel(currentTableName, JLabel.CENTER);
		        	tableName.setBounds(0, 0, 300, 20);
		        	jp.add(tableName);
		        	jp.add(scrollPane);
		        
		        	//setup Provenance map
		        	try {
		        		rs.first();
		        		int countRowNum = 0;
		        		while(true) {
		        			if (i >= currentRow.getIntervals().size() + 1|| i == 0) break; // i start from 0, but U" " start from 1, we only need to add map for tables except the first
		        			int flag = rs.getInt("U" + (i));
			        		System.out.println("output" + flag + "?" + i);
			        		
			        		if (flag == 1) {
			        			DebuggerTableModel model = (DebuggerTableModel) table.getModel();
			        			model.setPrevTupleIndex("t" + (countRowNum + 1) + "[" + (i) + "]", "t" + (countRowNum + 1) + "[" + (i - 1) + "]");
//			        			if (tables.size() >= 2) {
//			        				DebuggerTableModel lastModel = (DebuggerTableModel) tables.get(tables.size() - 2).getModel();
//					        		lastModel.setNextTupleIndex(targetIndex, tupleIndex);
//			        			}
			        			
			        		}
			        		if (!rs.next()) break;
			        		countRowNum++;
		        		}
		        		rs.first();
	
		        	} catch(SQLException e) {
		        		System.out.println(e);
		        	}
		        	System.out.println("tableIndex: " + i);
		        	System.out.println("prev" + tm.getPrevRelation());
		        	System.out.println("next" + tm.getNextRelation());
		        	
		        }
		       this.add(main_scrollPane);
//		        JPanel jp4 = new JPanel();
//		        JPanel jp5 = new JPanel();
//		        JPanel jp6 = new JPanel();
//		        jp4.setBounds(135, 100, 300, 100);
//		        jp4.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//		        jp5.setBounds(430, 100, 300, 100);
//		        jp5.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//		        jp6.setBounds(728, 100, 300, 100);
//		        jp6.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//		        this.add(jp4);
//		        this.add(jp5);
//		        this.add(jp6);
		      
		        //f.getContentPane().add(jp4);
		        //f.getContentPane().add(jp5);
		        //f.getContentPane().add(jp6);
		       

//		        String[] cn4 = {"cust","typ","bal"};
//		        String[] cn5 = {"cust","typ","bal"};
//		        String[] cn6 = {"cust","typ","bal"};
//		        Object[][] cd4 = {{"Alice","Checkings","50"},{"Alice","Savings","30"},{"Peter","Savings","60"},{"Mike","Checking","45"}};
//		        Object[][] cd5 = {{"Alice","Checkings","15"},{"Alice","Savings","30"},{"Peter","Savings","60"},{"Mick","Checking","45"}};
//		        Object[][] cd6 = {{"Alice","Checkings","15"},{"Alice","Savings","45"},{"Peter","Savings","60"},{"Mick","Checking","45"}};
//		        
//		        JTable ta4 = new JTable(cd4,cn4);
//		        JTable ta5 = new JTable(cd5,cn5);
//		        JTable ta6 = new JTable(tm);
//		        //set first panel jp4
//		        JLabel lb41 = new JLabel("t1[0]");
//		        JLabel lb42 = new JLabel("t2[0]");
//		        JLabel lb43 = new JLabel("t3[0]");
//		        JLabel ta4Name = new JLabel("account", JLabel.CENTER);
//
//		        jp4.setLayout(null);
//		        lb41.setBounds(260, 40, 30, 20);
//		        lb42.setBounds(260, 56, 30, 20);
//		        lb43.setBounds(260, 73, 30, 20);
//                ta4.setBounds(8,40, 245, 50);
//                ta4.getTableHeader().setBounds(8,25,245,15);
//                ta4Name.setBounds(8,2,245,20);
//		        
//		        jp4.add(lb41);
//		        jp4.add(lb42);
//		        jp4.add(lb43);
//		        jp4.add(ta4);
//		        jp4.add(ta4.getTableHeader());
//		        jp4.add(ta4Name);
//		        
//		        
//		        //set second panel jp5
//		        JLabel lb51 = new JLabel("t1[1]");
//		        JLabel lb52 = new JLabel("t2[1]");
//		        JLabel lb53 = new JLabel("t3[1]");
//		        JLabel ta5Name = new JLabel("account", JLabel.CENTER);
//		        
//		        jp5.setLayout(null);
//		        lb51.setBounds(260, 40, 30, 20);
//		        lb52.setBounds(260, 56, 30, 20);
//		        lb53.setBounds(260, 73, 30, 20);
//                ta5.setBounds(8,40, 245, 50);
//                ta5.getTableHeader().setBounds(8,25,245,15);
//                ta5Name.setBounds(8,2,245,20);
//		        
//                jp5.add(lb51);
//		        jp5.add(lb52);
//		        jp5.add(lb53);					        
//		        jp5.add(ta5);
//		        jp5.add(ta5.getTableHeader());
//		        jp5.add(ta5Name);
//		        
//		        //set second panel jp5
//		        JLabel lb61 = new JLabel("t1[2]");
//		        JLabel lb62 = new JLabel("t2[2]");
//		        JLabel lb63 = new JLabel("t3[2]");
//		        JLabel ta6Name = new JLabel("account", JLabel.CENTER);
//		        
//		        jp6.setLayout(null);
//		        lb61.setBounds(260, 40, 30, 20);
//		        lb62.setBounds(260, 56, 30, 20);
//		        lb63.setBounds(260, 73, 30, 20);
//                ta6.setBounds(8,40, 245, 50);
//                ta6.getTableHeader().setBounds(8,25,245,15);
//                ta6Name.setBounds(8,2,245,20);
//		        
//                jp6.add(lb61);
//		        jp6.add(lb62);
//		        jp6.add(lb63);	
//		        jp6.add(ta6);
//		        jp6.add(ta6.getTableHeader());
//		        jp6.add(ta6Name);
		        
		        graphPanel = new JPanel();
		        graphPanel.setLayout(null);
		        graphPanel.setBounds(DEBUGGER_LEFT_PADDING, 200, WIDTHFORGRAPHPANEL, HEIGHTFORGRAPHPANEL);
		        graphPanel.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        
		        //third line
//		        JPanel jp7 = new JPanel();
//		        JPanel jp8 = new JPanel();
//		        JPanel jp9 = new JPanel();
//		        jp7.setBounds(135, 165, 260, 200);
//		        jp7.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//		        jp8.setBounds(390, 165, 260, 200);
//		        jp8.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//		        jp9.setBounds(648, 165, 260, 200);
//		        jp9.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        
		GUIUtility.createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/dotFlow.png", "/Users/xun/Documents/java_workspace/gprom-gui/test2.png", 300, 400);
		File file = new File(new File(this.getClass().getResource("/").getPath()).getParent() + "/test2.png"); //use relative path
		BufferedImage image;
				try {
					image = ImageIO.read(file);
					imageLabel = new JLabel(new ImageIcon(image));
					imageLabel.setBounds(graphPanel.getWidth() / 2 - 300 / 2, 0, 450, 250);
					
					
//					JLabel label2 = new JLabel(new ImageIcon(image));
//					JLabel label3 = new JLabel(new ImageIcon(image));
					
					graphPanel.add(imageLabel);
//					jp8.add(label2);
//					jp9.add(label3);
					
					//label1.setBounds(0, 10, 5, 10);
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				this.add(graphPanel);
		        
		               
		        
//		        f.add(jp7);
//		        f.add(jp8);
//		        f.add(jp9);
		        
		        
		        //first column
		        JPanel jp11 = new JPanel();
		        panel_table  = new JPanel();
		        panel_graph = new JPanel();
		        panel_table.setLayout(null);
		        panel_graph.setLayout(null);
		        jp11.setBounds(35, 5, 100, 95);
		        jp11.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        panel_table.setBounds(35, 100, 100, 100);
		        panel_table.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        panel_graph.setBounds(35, 200, 100, 200);
		        panel_graph.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        this.add(jp11);
		        this.add(panel_table);
		        this.add(panel_graph);
		        JLabel jl1 = new JLabel("SQL");
		        JLabel jl2 = new JLabel("TABLE");
		        JLabel jl3 = new JLabel("GRAPH");
		        jl2.setBounds(35,5,100,10);
		        jl3.setBounds(35,5,100,10);
		        jp11.add(jl1);
		        panel_table.add(jl2);
		        panel_graph.add(jl3);
		        
		        //Right buttons
		        refresh_button = new JButton("Refresh");
		        opt_internal_button = new JButton("<html>Optimizer<br>Internals</html>");
		        add_stmt_button = new JButton("<html>&nbsp;&nbsp; Add<br>Statement</html>");
		        del_stmt_button = new JButton("<html>&nbsp; Delete<br>Statement</html>");
		        show_hide_button = new JButton("<html>Show/Hide<br>Unaffected<br> &nbsp;&nbsp;&nbsp;Rows</html>");

		        //jb3.setHorizontalAlignment(JButton.RIGHT);
		        
		        refresh_button.setBounds(XFORBUTTONS, 30, 105, 55);
		        opt_internal_button.setBounds(XFORBUTTONS, 85, 105, 55);
		        add_stmt_button.setBounds(XFORBUTTONS, 140, 105, 55);
		        del_stmt_button.setBounds(XFORBUTTONS, 195, 105, 55);
		        show_hide_button.setBounds(XFORBUTTONS, 250, 105, 55);
		        buttons.add(refresh_button);
		        buttons.add(opt_internal_button);
		        buttons.add(add_stmt_button);
		        buttons.add(del_stmt_button);
		        buttons.add(show_hide_button);
		        //jb1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        //jb2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
		        this.add(refresh_button);
		        this.add(opt_internal_button);
		        this.add(add_stmt_button);
		        this.add(del_stmt_button);
		        this.add(show_hide_button);
		        this.setVisible(true);
	}




	private void setupListeners() {
		 refresh_button.addActionListener(this);
	     opt_internal_button.addActionListener(this);
	     add_stmt_button.addActionListener(this);
	     del_stmt_button.addActionListener(this);
	     show_hide_button.addActionListener(this);
	     this.addComponentListener(this);
	     for (int i = 0; i < tables.size(); i++) {
	    	 tables.get(i).addMouseListener(this);
	     }
	}




	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == opt_internal_button) {
			  OptimizationInternalsFrame f = new OptimizationInternalsFrame("GProM Optimization Internals");
		      f.setSize(950, 600);
		}
	}


	private void update() {
		System.out.println("x: " + this.getWidth() + "y: " + this.getHeight());
		if (this.getWidth() > initialWidth) {
			int newXforButtons = XFORBUTTONS + this.getWidth() - initialWidth;
			System.out.println("offset: " + newXforButtons);
			for (int i = 0; i < buttons.size(); i++) {
				JButton button = buttons.get(i);
//				this.remove(button);
				button.setBounds(newXforButtons, 30 + i * (55), 105, 55);
//				this.revalidate();
//				this.add(button);
			}
			int newWidthForMSP = WIDTHFORMAINSCROLLPANE + this.getWidth() - initialWidth;
			main_scrollPane.setBounds(DEBUGGER_LEFT_PADDING, 5, newWidthForMSP, main_scrollPane.getHeight());
			int newWidthForGP = WIDTHFORGRAPHPANEL + this.getWidth() - initialWidth;
			graphPanel.setBounds(DEBUGGER_LEFT_PADDING, 200, newWidthForGP, graphPanel.getHeight());
			imageLabel.setBounds(graphPanel.getWidth() / 2 - 300 / 2, imageLabel.getY(), 300, 200);
			panel_table.setBounds(35, 100, 100 + this.getWidth() - initialWidth, panel_table.getHeight());
			panel_graph.setBounds(35, 200, 100 + this.getWidth() - initialWidth, panel_graph.getHeight());
		}
		
		if (this.getHeight() > initialHeight) {
			int newHeightForMSP = HEIGHTFORGRAPHPANEL + this.getHeight() - initialHeight;
			main_scrollPane.setBounds(DEBUGGER_LEFT_PADDING, 5, main_scrollPane.getWidth(), newHeightForMSP);
			int newHeightForGP = HEIGHTFORGRAPHPANEL + this.getHeight() - initialHeight;
			graphPanel.setBounds(DEBUGGER_LEFT_PADDING, 200  + this.getHeight() - initialHeight, graphPanel.getWidth(), HEIGHTFORGRAPHPANEL);
	        panel_table.setBounds(35, 100, panel_table.getWidth(), 100 + this.getHeight() - initialHeight);
	        panel_graph.setBounds(35, 200 + this.getHeight() - initialHeight, panel_graph.getWidth(), 200);
		}
//		this.revalidate();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		update();
	}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentHidden(ComponentEvent e) {}




	@Override
	public void mouseClicked(MouseEvent e) {
		for(int i = 0; i < tables.size(); i++) {
			tables.get(i).clearSelection();
		}
		JTable table = (JTable) e.getSource();
		for(int i = 0; i < tables.size(); i++) {
			JTable currentTable = tables.get(i);
			if (currentTable == table) {
				int index = currentTable.rowAtPoint(e.getPoint());
				currentTable.setRowSelectionInterval(index, index);
				
				// get the index for next table that need to be highlighted
				
				index++;		
				System.out.println("t" + index+ "[" + i + "]");
//				index--;
				
				highlightTables( i, "t" + (index) + "[" + i + "]");
				
//				
//				for (int j = 0; j < i + 1; j++) {
//					tables.get(j).setRowSelectionInterval(index, index);
//				}
			}
		}
		
		
	}


	private void highlightTables(int currentTableIndex, String tupleIndex) {
		JTable currentTable = tables.get(currentTableIndex);
		//get the index for row we want to highlight
		Pattern pattern = Pattern.compile("t(\\d*)\\[(\\d*)\\]");
		Matcher matcher = pattern.matcher(tupleIndex);
		String rowIndex = "";
	    String tableIndex = "";
		if (matcher.matches()) {
		    rowIndex = matcher.group(1);
		    tableIndex = matcher.group(2);
		    System.out.println(rowIndex + ", " + tableIndex);
		}
		//highlight table
		currentTable.setRowSelectionInterval(Integer.parseInt(rowIndex) - 1, Integer.parseInt(rowIndex) - 1);
		System.out.println("currentTupeIndex" + tupleIndex + "  tableIndex" + currentTableIndex);
		if (currentTableIndex <= -1) {
			return;
		}
		List<String> provenanceList = ((DebuggerTableModel)tables.get(currentTableIndex).getModel()).getPrevRelation().get(tupleIndex);
		if (provenanceList == null) {
			return;
		}
		System.out.println(((DebuggerTableModel)tables.get(currentTableIndex).getModel()).getPrevRelation());
		System.out.println(tupleIndex);
		System.out.println(provenanceList);


		for (int i = 0; i < provenanceList.size(); i++) {
			String nextTupleIndex = provenanceList.get(i);
			highlightTables(--currentTableIndex, nextTupleIndex);
		}
	}




	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { }
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) { }

}
