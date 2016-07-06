/*
 *  File: EventMonitoringExample.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
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
package timebars.eventmonitoring.swing;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import timebars.eventmonitoring.model.CollectingTimeBarNode;
import timebars.eventmonitoring.model.EventInterval;
import timebars.eventmonitoring.model.EventTimeBarRow;
import timebars.eventmonitoring.model.ModelCreator;
import timebars.eventmonitoring.swing.renderer.EventMonitorHeaderRenderer;
import timebars.eventmonitoring.swing.renderer.EventRenderer;
import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.model.HierarchicalTimeBarModel;
import de.jaret.util.ui.timebars.model.HierarchicalViewStateListener;
import de.jaret.util.ui.timebars.model.ISelectionRectListener;
import de.jaret.util.ui.timebars.model.ITimeBarChangeListener;
import de.jaret.util.ui.timebars.model.ITimeBarViewState;
import de.jaret.util.ui.timebars.model.TBRect;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarNode;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.strategy.IIntervalSelectionStrategy;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultHierarchyRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTitleRenderer;

/**
 * Example showing events (non modificable).
 * 
 * @author Peter Kliem
 * @version $Id: EventMonitoringExample.java 1073 2010-11-22 21:25:33Z kliem $
 */
public class EventMonitoringExample {
	TimeBarViewer _tbv;
	TimeBarMarkerImpl _tm;
	
	
	private TimeBarRow currentRow = null; 
	private final static int DEBUGGER_LEFT_PADDING =  135;
	private final static int DEBUGGER_CELL_WIDTH =  295;
	private final static boolean HIERARCHICAL = false;

	public static void main(String[] args) {
		EventMonitoringExample example = new EventMonitoringExample();
		example.run();
	}

	public EventMonitoringExample() {

	}
	
	//PNG image compress method
	public static void createThumbnail(String src, String dist, float width,
            float height) {
        try {
            File srcfile = new File(src);
            if (!srcfile.exists()) {
                System.out.println("需要被压缩的文件不存在");
                return;
            }
            BufferedImage image = ImageIO.read(srcfile);

            // 获得缩放的比例
            double ratio = 1.0;
            // 判断如果高、宽都不大于设定值，则不处理
            if (image.getHeight() > height || image.getWidth() > width) {
                if (image.getHeight() > image.getWidth()) {
                    ratio = height / image.getHeight();
                } else {
                    ratio = width / image.getWidth();
                }
            }
            // 计算新的图面宽度和高度
            int newWidth = (int) (image.getWidth() * ratio);
            int newHeight = (int) (image.getHeight() * ratio);

            BufferedImage bfImage = new BufferedImage(newWidth, newHeight,
                    BufferedImage.TYPE_INT_RGB);
            bfImage.getGraphics().drawImage(
                    image.getScaledInstance(newWidth, newHeight,
                            Image.SCALE_SMOOTH), 0, 0, null);

            FileOutputStream os = new FileOutputStream(dist);
            ImageIO.write(bfImage, "PNG", os);

            // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
            // encoder.encode(bfImage);
            os.close();
            System.out.println("创建缩略图成功");
        } catch (Exception e) {
            System.out.println("创建缩略图发生异常" + e.getMessage());
        }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public void run() {
		//JFrame f = new JFrame(EventMonitoringExample.class.getName());
		JFrame f = new JFrame("GProM Transaction Debugger");
		f.setSize(1400, 600);
		f.getContentPane().setLayout(new BorderLayout());
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// 添加菜单
		JMenuBar menubar = new JMenuBar();
		//JMenu LoginMenu = new JMenu("System");
//		JMenu UserMangeMenu = new JMenu("User");
		//JMenu Graphic = new JMenu("Graphic");
		//JMenuItem userLoginMenu = new JMenuItem("Provence");
		//LoginMenu.add(userLoginMenu);
		//menubar.add(LoginMenu);
//		menubar.add(UserMangeMenu);
		//menubar.add(Graphic);

		// Container content = this.getContentPane();
		f.add(menubar, BorderLayout.NORTH);

		HierarchicalTimeBarModel hierarchicalModel = ModelCreator
				.createHierarchicalModel();
		final TimeBarModel flatModel = ModelCreator.createFlatModel();

		_tbv = new TimeBarViewer();

		if (HIERARCHICAL) {
			_tbv.setModel(hierarchicalModel);
			_tbv.setHierarchyRenderer(new DefaultHierarchyRenderer());
			_tbv.setHierarchyWidth(100);
		} else {
			_tbv.setModel(flatModel);
			_tbv.setHierarchyWidth(0);
		}
		_tbv.setTimeScalePosition(TimeBarViewer.TIMESCALE_POSITION_TOP);
		_tbv.setYAxisWidth(100);
		f.getContentPane().add(_tbv, BorderLayout.CENTER);

		// allow marker grabbing in the diagram area
		_tbv.setMarkerDraggingInDiagramArea(true);

		// enable region selection
		_tbv.setRegionRectEnable(true);

		// draw row grid
		_tbv.setDrawRowGrid(true);

		// setup header renderer
		_tbv.setHeaderRenderer(new EventMonitorHeaderRenderer());

		// set a name for the viewer and setup the default title renderer
		_tbv.setName("Transaction History");
		
		_tbv.setTitleRenderer(new DefaultTitleRenderer());

		// selection strategy: shortest first
		_tbv.getDelegate().setIntervalSelectionStrategy(
				new IIntervalSelectionStrategy() {
					public Interval selectInterval(List<Interval> intervals) {
						Interval result = null;
						for (Interval interval : intervals) {
							if (result == null
									|| interval.getSeconds() < result
											.getSeconds()) {
								result = interval;
							}
						}
						return result;
					}
				});

		if (HIERARCHICAL) {
			int i = 0;
			// set the overlap drawing property for the nodes on the second
			// level
			for (TimeBarNode node : hierarchicalModel.getRootNode()
					.getChildren()) {
				_tbv.getTimeBarViewState().setDrawOverlapping(node, true);
			}

			// add a listener that toggles the collect childIntervals property
			// of expanded/collapsed nodes
			_tbv.getHierarchicalViewState().addHierarchicalViewstateListener(
					new HierarchicalViewStateListener() {
						public void nodeExpanded(TimeBarNode node) {
							if (node instanceof CollectingTimeBarNode) {
								CollectingTimeBarNode ctbn = (CollectingTimeBarNode) node;
								ctbn.setCollectChildIntervals(false);
							}
						}

						public void nodeFolded(TimeBarNode node) {
							if (node instanceof CollectingTimeBarNode) {
								CollectingTimeBarNode ctbn = (CollectingTimeBarNode) node;
								ctbn.setCollectChildIntervals(true);
							}
						}
					});
		} else {
			// in general draw overlapping
			_tbv.setDrawOverlapping(true);

			// allow different row heights
			_tbv.getTimeBarViewState().setUseVariableRowHeights(true);

			// add a double click listener for checking on the header
			_tbv.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					Point origin2 = e.getPoint();
					TimeBarRow row2 = _tbv.getRowForXY(origin2.x, origin2.y);
					currentRow  = row2;
					//if click empty row , then return
					if (row2 == null) {
						return;
					}
					
					// EventInterval interval = (EventInterval)
					// flatModel.getRow(0).getIntervals().get(0);
					String sql = "";
					String tns = "Transactions";
					String osName = "";
					String sessionid = "";
					String beginTime = "";
					//TODO give these statements a scrollable pane
					JFrame jf1 = new JFrame(tns);
					jf1.setSize(520, 600);
					//jf1.setBounds(100, 50, 520, 800);
					jf1.setVisible(true);
					jf1.setLayout(null);
					//jf1.setLayout(new FlowLayout());
					//Container container = jf1.getContentPane();
					//JPanel jp = new JPanel(new GridLayout(8,1));
					//JPanel jp = new JPanel();
//					jp.setPreferredSize(new Dimension(500, 100));
//					jp
					JButton startBt = new JButton("Get Provence");
					
					startBt.addActionListener(new ActionListener()
					{
					  public void actionPerformed(ActionEvent e)
					  {
						  DotWrapper dw = new DotWrapper();
						  try {
							dw.init();
						} catch (URISyntaxException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}

//						Icon c = Icon(new ImageIcon("images\\background.jpg")).getImage());  
//					    bgp.setBounds(0,0,400,300); 

						
						  
//						dw.runDot(commands);
						  
					    // display/center the jdialog when the button is pressed
//					    JDialog d = new JDialog(frame, "Hello", true);
//						  JFrame jf = new JFrame();
//						  jf.setSize(600, 800);
//						  jf.setVisible(true);
//						  
//						  createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/dotout.png", "/Users/xun/Documents/java_workspace/gprom-gui/test1.png", 100, 100);
//					        
//						  
//						  
//						  File file = new File("/Users/xun/Documents/java_workspace/gprom-gui/test1.png");
//					       BufferedImage image;
//						try {
//							image = ImageIO.read(file);
//							JLabel label = new JLabel(new ImageIcon(image));
//							jf.add(label);
//						} catch (IOException e1) {
//							e1.printStackTrace();
//						}
//					
//						
//						JTextArea txt1 = new JTextArea(2,4);
//						JTextArea txt2 = new JTextArea(2,4);
//						JTextArea txt3 = new JTextArea(2,4);
//						txt1.setBorder(BorderFactory.createLineBorder(Color.gray,1));
////						JLabel txt1 = new JLabel("title");
////						JLabel txt2 = new JLabel("title");
////						JLabel txt3 = new JLabel("title");
////						JLabel txt4 = new JLabel("title");
////						txt4.setBounds(10, 10, 5, 5);
//						
//						
////						JLabel txt5 = new JLabel("title");
////						JLabel txt6 = new JLabel("title");
////						JLabel txt7 = new JLabel("title");
////						JLabel txt8 = new JLabel("title");
////						JLabel txt9 = new JLabel("title");
//						String str1 = "\n \n \n UPDATE employee \n SET name = 'Alice'\n WHERE ID = 1";
//						String str2 = "\n \n \n UPDATE employee \n SET name = 'Bob'\n WHERE ID = 2";
//						String str3 = "\n \n \n UPDATE employee \n SET name = 'Mike'\n WHERE ID = 3";
//						//update employee set name = 'Alice' where ID = 1 
//						txt1.setText(str1);
//						txt2.setText(str2);
//						txt3.setText(str3);
//						//txt1.setText(" SQL:"  + "\n StartTime: "+"\n beginTime" );
//						txt1.setEditable(false);
//						// tf.multiline = true;//设置多行显示
////						txt1.setPreferredSize(new Dimension(5, 5));
////						pan.add(tf2);
//					
////						scrollPane.add(tf2);
//						
//						//txt1.setBounds(10, 10, 3, 3);
//						
////						JTextField txt4 = new JTextField(5);
////						JTextField txt5 = new JTextField(5);
////						JTextField txt6 = new JTextField(5);
////						JTextField txt7 = new JTextField(5);
////						JTextField txt8 = new JTextField(5);
////						JTextField txt9 = new JTextField(5);
////						JTextField txt10 = new JTextField(5);
////						JTextField txt11 = new JTextField(5);
////						JTextField txt12 = new JTextField(5);
//						//JTextArea txt4 = new JTextArea(2,4);					
//						//JTextArea txt5 = new JTextArea(2,4);
//						//JTextArea txt6 = new JTextArea(2,4);
//						JPanel jp4 = new JPanel();
//						JPanel jp5 = new JPanel();
//						JPanel jp6 = new JPanel();
//						
//						jp4.setPreferredSize(new Dimension(5, 4));
//						//jp5.setPreferredSize(new Dimension(10, 4));
//						//jp6.setPreferredSize(new Dimension(10, 4));
//						
//						String[] cn4 = {"Id","Name","Salary"};
//						String[] cn5 = {"Id","Name","Salary"};
//						String[] cn6 = {"Id","Name","Salary"};
//						
//						Object[][] cd4 = {{"1","Alice","3600"},{"1","Alice","3600"},{"1","Alice","3600"}};
//						Object[][] cd5 = {{"1","Alice","3600"},{"1","Alice","3600"},{"1","Alice","3600"}};
//						Object[][] cd6 = {{"1","Alice","3600"},{"1","Alice","3600"},{"1","Alice","3600"}};
//						
//						JTable txt4 = new JTable(cd4,cn4);
//						JTable txt5 = new JTable(cd5,cn5);
//						JTable txt6 = new JTable(cd6,cn6);
//						
////						Rectangle r = new Rectangle(0, 0);
////						r.height = 1;
////						r.width = 2;
////						txt4.setBounds(r);
////						txt5.setBounds(r);
//						
////						txt4.setPreferredScrollableViewportSize(new Dimension(5, 4));
//						txt4.setPreferredSize(new Dimension(40, 10));
//						
//						jp4.setLayout(new FlowLayout());
//						jp5.setLayout(new FlowLayout());
//						jp6.setLayout(new FlowLayout());
//						
////						jp4.add(txt4,BorderLayout.CENTER);
//						jp4.add(txt4);
//						jp5.add(txt5);
//						jp6.add(txt6);
////						jp5.add(txt5,BorderLayout.CENTER);
////						jp6.add(txt6,BorderLayout.CENTER);
//						jp4.setBorder(BorderFactory.createLineBorder(Color.black));
//						//jp5.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						//jp6.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						
//						JTextArea txt7 = new JTextArea(2,4);
//						JTextArea txt8 = new JTextArea(2,4);
//						JTextArea txt9 = new JTextArea(2,4);
//						txt2.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						txt3.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						txt4.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						txt5.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						txt6.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						txt7.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						txt8.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//						txt9.setBorder(BorderFactory.createLineBorder(Color.gray,1));
//
//						
//						
////						JTextArea ta = new JTextArea(15, 10);
//						
//
//						JTable ta = new JTable(2, 2);
//						
//						JPanel grid = new JPanel();
//
////							grid.setLayout(new GridLayout(4, 3));
//
//							grid.setLayout(new GridLayout(3, 3));
//						
//			//		grid.setLayout(new FlowLayout(FlowLayout.LEFT ));
//
//							grid.add(txt1);
//							grid.add(txt2);
//							grid.add(txt3);
//							grid.add(jp4);
//							grid.add(jp5);
//							grid.add(jp6);
//							grid.add(txt7);
//							grid.add(txt8);
//							grid.add(txt9);
//							//grid.add(txt10);
//							//grid.add(txt11);
//							//grid.add(txt12);
//							
//
////							ta.setText("This is a text area");
////							jf.add(grid);
////							jf.add(ta, BorderLayout.SOUTH); 
						  
						  
						  
						  
						  
						  
						    JFrame debuggerFrame = new JFrame("Debug Panel");
					        debuggerFrame.setSize(1200, 600);
					        
					        JPanel jp1 = new JPanel();
//					        JPanel jp2 = new JPanel();
//					        JPanel jp3 = new JPanel();
					        JTextArea ja1=new JTextArea(5,20);
//					        JTextArea ja2=new JTextArea(5,20);
//					        JTextArea ja3=new JTextArea(5,20);
					        
//					        ja1.setFont(Bold);
					        
					       
					        String originalTable_str = "\n\n Original Table";
					        //TODO display transaction
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
					        	debuggerFrame.add(jp);
					        	jp.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        	jp.setBounds(DEBUGGER_LEFT_PADDING + (i + 1) * (DEBUGGER_CELL_WIDTH), 5, 300, 95);
					        	ja.setBounds(0, 10, 5, 20);
					        	
					        	if (i == 3) {
					        		break;
					        	}
					        }
//					        String str2 = "\n UPDATE account \n SET bal=bal-35 \n WHERE cust='Alice' AND typ='Checking'";
//					        String str3 = "\n UPDATE account \n SET bal=bal+35 \n WHERE cust='Alice' AND typ='Savings'";
					        ja1.setText(originalTable_str);
//					        ja2.setText(str2);
//					        ja3.setText(str3);
					        
//					        ja1.setCaretColor(Color.BLUE);
//					        ja2.setCaretColor(Color.BLUE);
//					        ja3.setCaretColor(Color.BLUE);
					        
					        ja1.setEditable(false);
//					        ja2.setEditable(false);
//					        ja3.setEditable(false);
					        
					        jp1.add(ja1);
//					        jp2.add(ja2);
//					        jp3.add(ja3);
					        
					        
					        debuggerFrame.setLayout(null);
					        
					        debuggerFrame.add(jp1);
//					        f.add(jp2);
//					        f.add(jp3);
					        
					        jp1.setBounds(DEBUGGER_LEFT_PADDING, 5, 300, 95);
					        jp1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//					        jp2.setBounds(430, 5, 300, 95);
//					        jp2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//					        jp3.setBounds(728, 5, 300, 95);
//					        jp3.setBorder(BorderFactory.createLineBorder(Color.gray,3));

					        ja1.setBounds(0, 10, 5, 20);
//					        ja2.setBounds(0, 10, 5, 20);
//					        ja3.setBounds(0, 10, 5, 20);
					        
					        
					        
					        //second line
					        JPanel jp4 = new JPanel();
					        JPanel jp5 = new JPanel();
					        JPanel jp6 = new JPanel();
					        jp4.setBounds(135, 100, 300, 100);
					        jp4.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        jp5.setBounds(430, 100, 300, 100);
					        jp5.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        jp6.setBounds(728, 100, 300, 100);
					        jp6.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        debuggerFrame.add(jp4);
					        debuggerFrame.add(jp5);
					        debuggerFrame.add(jp6);
					      
					        //f.getContentPane().add(jp4);
					        //f.getContentPane().add(jp5);
					        //f.getContentPane().add(jp6);
					        //TODO add GProm query result
					        String[] cn4 = {"cust","typ","bal"};
					        String[] cn5 = {"cust","typ","bal"};
					        String[] cn6 = {"cust","typ","bal"};
					        Object[][] cd4 = {{"Alice","Checkings","50"},{"Alice","Savings","30"},{"Peter","Savings","60"},{"Mike","Checking","45"}};
					        Object[][] cd5 = {{"Alice","Checkings","15"},{"Alice","Savings","30"},{"Peter","Savings","60"},{"Mick","Checking","45"}};
					        Object[][] cd6 = {{"Alice","Checkings","15"},{"Alice","Savings","45"},{"Peter","Savings","60"},{"Mick","Checking","45"}};
					        
					        JTable ta4 = new JTable(cd4,cn4);
					        JTable ta5 = new JTable(cd5,cn5);
					        JTable ta6 = new JTable(cd6,cn6);
					        //set first panel jp4
					        JLabel lb41 = new JLabel("t1[0]");
					        JLabel lb42 = new JLabel("t2[0]");
					        JLabel lb43 = new JLabel("t3[0]");
					        JLabel ta4Name = new JLabel("account", JLabel.CENTER);
	
					        jp4.setLayout(null);
					        lb41.setBounds(260, 40, 30, 20);
					        lb42.setBounds(260, 56, 30, 20);
					        lb43.setBounds(260, 73, 30, 20);
                            ta4.setBounds(8,40, 245, 50);
                            ta4.getTableHeader().setBounds(8,25,245,15);
                            ta4Name.setBounds(8,2,245,20);
					        
					        jp4.add(lb41);
					        jp4.add(lb42);
					        jp4.add(lb43);
					        jp4.add(ta4);
					        jp4.add(ta4.getTableHeader());
					        jp4.add(ta4Name);
					        
					        
					        //set second panel jp5
					        JLabel lb51 = new JLabel("t1[1]");
					        JLabel lb52 = new JLabel("t2[1]");
					        JLabel lb53 = new JLabel("t3[1]");
					        JLabel ta5Name = new JLabel("account", JLabel.CENTER);
					        
					        jp5.setLayout(null);
					        lb51.setBounds(260, 40, 30, 20);
					        lb52.setBounds(260, 56, 30, 20);
					        lb53.setBounds(260, 73, 30, 20);
                            ta5.setBounds(8,40, 245, 50);
                            ta5.getTableHeader().setBounds(8,25,245,15);
                            ta5Name.setBounds(8,2,245,20);
					        
                            jp5.add(lb51);
					        jp5.add(lb52);
					        jp5.add(lb53);					        
					        jp5.add(ta5);
					        jp5.add(ta5.getTableHeader());
					        jp5.add(ta5Name);
					        
					        //set second panel jp5
					        JLabel lb61 = new JLabel("t1[2]");
					        JLabel lb62 = new JLabel("t2[2]");
					        JLabel lb63 = new JLabel("t3[2]");
					        JLabel ta6Name = new JLabel("account", JLabel.CENTER);
					        
					        jp6.setLayout(null);
					        lb61.setBounds(260, 40, 30, 20);
					        lb62.setBounds(260, 56, 30, 20);
					        lb63.setBounds(260, 73, 30, 20);
                            ta6.setBounds(8,40, 245, 50);
                            ta6.getTableHeader().setBounds(8,25,245,15);
                            ta6Name.setBounds(8,2,245,20);
					        
                            jp6.add(lb61);
					        jp6.add(lb62);
					        jp6.add(lb63);	
					        jp6.add(ta6);
					        jp6.add(ta6.getTableHeader());
					        jp6.add(ta6Name);
					        
					        JPanel jpF = new JPanel();
					        jpF.setBounds(135, 200, 893, 200);
					        jpF.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        debuggerFrame.add(jpF);
					        //third line
//					        JPanel jp7 = new JPanel();
//					        JPanel jp8 = new JPanel();
//					        JPanel jp9 = new JPanel();
//					        jp7.setBounds(135, 165, 260, 200);
//					        jp7.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//					        jp8.setBounds(390, 165, 260, 200);
//					        jp8.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//					        jp9.setBounds(648, 165, 260, 200);
//					        jp9.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        
					createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/dotFlow.png", "/Users/xun/Documents/java_workspace/gprom-gui/test2.png", 300, 400);
					File file = new File(new File(this.getClass().getResource("/").getPath()).getParent() + "/test2.png"); //use relative path
					BufferedImage image;
							try {
								image = ImageIO.read(file);
								JLabel label1 = new JLabel(new ImageIcon(image));
//								JLabel label2 = new JLabel(new ImageIcon(image));
//								JLabel label3 = new JLabel(new ImageIcon(image));
								
								jpF.add(label1);
//								jp8.add(label2);
//								jp9.add(label3);
								
								//label1.setBounds(0, 10, 5, 10);
								
							} catch (IOException e1) {
								e1.printStackTrace();
							}
					        
					        
					               
					        
//					        f.add(jp7);
//					        f.add(jp8);
//					        f.add(jp9);
					        
					        
					        //first column
					        JPanel jp11 = new JPanel();
					        JPanel jp12 = new JPanel();
					        JPanel jp13 = new JPanel();
					        jp11.setBounds(35, 5, 100, 95);
					        jp11.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        jp12.setBounds(35, 100, 100, 100);
					        jp12.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        jp13.setBounds(35, 200, 100, 200);
					        jp13.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        debuggerFrame.add(jp11);
					        debuggerFrame.add(jp12);
					        debuggerFrame.add(jp13);
					        JLabel jl1 = new JLabel("SQL");
					        JLabel jl2 = new JLabel("TABLE");
					        JLabel jl3 = new JLabel("GRAPH");
					        jp11.add(jl1);
					        jp12.add(jl2);
					        jp13.add(jl3);
					        
					        //Right buttons
					        JButton jb1 = new JButton("Refresh");
					        JButton jb2 = new JButton("<html>Optimizer<br>Internals</html>");
					        JButton jb3 = new JButton("<html>&nbsp;&nbsp; Add<br>Statement</html>");
					        JButton jb4 = new JButton("<html>&nbsp; Delete<br>Statement</html>");
					        JButton jb5 = new JButton("<html>Show/Hide<br>Unaffected<br> &nbsp;&nbsp;&nbsp;Rows</html>");

					        //jb3.setHorizontalAlignment(JButton.RIGHT);
					        
					        jb1.setBounds(1060, 30, 105, 55);
					        jb2.setBounds(1060, 85, 105, 55);
					        jb3.setBounds(1060, 140, 105, 55);
					        jb4.setBounds(1060, 195, 105, 55);
					        jb5.setBounds(1060, 250, 105, 55);
					        
					        //jb1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        //jb2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
					        debuggerFrame.add(jb1);
					        debuggerFrame.add(jb2);
					        debuggerFrame.add(jb3);
					        debuggerFrame.add(jb4);
					        debuggerFrame.add(jb5);
					        
							jb2.addActionListener(new ActionListener()
							{
							  public void actionPerformed(ActionEvent e)
							  {
								  JFrame f = new JFrame("GProM Optimization Internals");
							        f.setSize(950, 600);
							        
							       
							        //JPanel jp1 = new JPanel();
							        //JPanel jp2 = new JPanel();
							        //JPanel jp3 = new JPanel();
							        //JTextArea ja1=new JTextArea(5,20);
							        JTextArea ja1=new JTextArea();							        
							        JTextArea ja2=new JTextArea();
							        JTextArea ja3=new JTextArea();
							        JScrollPane jp1=new JScrollPane(ja1);
							        JScrollPane jp2=new JScrollPane(ja2);
							        JScrollPane jp3=new JScrollPane(ja3);
							        //jp1.setLayout(new BorderLayout());
							        
//							        ja1.setFont(Bold);
                                    String str1 = "WITH temp_view_of_0 AS ("
+"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, (CASE  WHEN ((F0.CUST = 'Alice') AND (F0.TYP = 'Savings')) THEN (F0.BAL - 70) ELSE F0.BAL END) AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM (("
+"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM (("
+"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS \"PROV_ACCOUNT_1_CUST\", F0.TYP AS \"PROV_ACCOUNT_1_TYP\", F0.BAL AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM (ACCOUNT AS OF SCN 1478778490 F0)) F0)) F0))"
+"\nSELECT F0.CUST AS CUST, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM ((("
+"\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\", NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM (("
+"\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.CUST AS PROV_OVERDRAFT_CUST, F0.BAL AS PROV_OVERDRAFT_BAL"
+"\nFROM (OVERDRAFT AS OF SCN 1478778490 F0)) F0)) UNION ALL ("
+"\nSELECT F0.CUST AS CUST, F0.\"(BAL+BAL)\" AS \"(BAL+BAL)\", NULL AS PROV_OVERDRAFT_CUST, NULL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM (("
+"\nSELECT F0.CUST AS CUST, (F0.BAL + F0.BAL) AS \"(BAL+BAL)\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM (("
+"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F1.CUST AS CUST, F1.TYP AS TYP, F1.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F1.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F1.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
+"\nFROM (((temp_view_of_0) F0) CROSS JOIN ((temp_view_of_0) F1))) F0)"
+"\nWHERE ((((F0.CUST = 'Alice') AND (F0.CUST = F0.CUST)) AND (F0.TYP != F0.TYP)) AND ((F0.BAL + F0.BAL) < 0))) F0))) F0);";
							     
                                    String str2 =   "WITH temp_view_of_0 AS ("
                                    		        +"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    		        +"\nFROM (("
                                    		        +"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, (CASE  WHEN ((F0.CUST = 'Alice') AND (F0.TYP = 'Savings')) THEN (F0.BAL - 70) ELSE F0.BAL END) AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    		        +"\nFROM (("
                                    		        +"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    				+"\nFROM (("
                                    				+"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS \"PROV_ACCOUNT_1_CUST\", F0.TYP AS \"PROV_ACCOUNT_1_TYP\", F0.BAL AS \"PROV_ACCOUNT_1_BAL\""
                                    			    +"\nFROM (ACCOUNT AS OF SCN 1478778490 F0)) F0)) F0)) F0)"
                                    			    +"\nWHERE (F0.PROV_ACCOUNT_BAL = F0.\"PROV_ACCOUNT_1_BAL\"))"
                                    			    +"\nSELECT F0.CUST AS CUST, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    			    +"\nFROM ((("
                                    			    +"\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.PROV_OVERDRAFT_CUST AS PROV_OVERDRAFT_CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\", NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\""
                                    			    +"\nFROM (("
                                    			    +"\nSELECT F0.CUST AS CUST, F0.BAL AS BAL, F0.CUST AS PROV_OVERDRAFT_CUST, F0.BAL AS PROV_OVERDRAFT_BAL"
                                    		        +"\nFROM (OVERDRAFT AS OF SCN 1478778490 F0)) F0)) UNION ALL ("
                                    		        +"\nSELECT F0.CUST AS CUST, F0.\"(BAL+BAL)\" AS \"(BAL+BAL)\", NULL AS PROV_OVERDRAFT_CUST, NULL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    		        +"\nFROM (("
                                    		        +"\nSELECT F0.CUST AS CUST, (F0.BAL + F0.BAL) AS \"(BAL+BAL)\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    		        +"\nFROM (("
                                    		        +"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F1.CUST AS CUST, F1.TYP AS TYP, F1.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F1.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F1.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F1.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F1.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F1.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    		        +"\nFROM (((temp_view_of_0) F0) CROSS JOIN ((temp_view_of_0) F1))"
                                    		        +"\nWHERE (((F0.CUST = 'Alice') AND (F0.PROV_ACCOUNT_CUST = 'Alice')) AND (F0.\"PROV_ACCOUNT_1_CUST\" = 'Alice'))) F0)"
                                    		        +"\nWHERE ((F0.TYP != F0.TYP) AND ((F0.BAL + F0.BAL) < 0))) F0)"
                                    		        +"\nWHERE (F0.PROV_ACCOUNT_TYP = F0.\"PROV_ACCOUNT_1_TYP\"))) F0)";        
                                    
                                    String str3 =   "WITH temp_view_of_0 AS ("
                                    		        +"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, (CASE  WHEN ((F0.CUST = 'Alice') AND (F0.TYP = 'Savings')) THEN (F0.BAL - 70) ELSE F0.BAL END) AS BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.CUST AS PROV_ACCOUNT_CUST, F0.TYP AS PROV_ACCOUNT_TYP, F0.BAL AS PROV_ACCOUNT_BAL, F0.CUST AS \"PROV_ACCOUNT_1_CUST\", F0.TYP AS \"PROV_ACCOUNT_1_TYP\", F0.BAL AS \"PROV_ACCOUNT_1_BAL\""
                                    				+"\nFROM (ACCOUNT AS OF SCN 1478778490 F0)"
                                    				+"\nWHERE (F0.BAL = F0.BAL))"
                                    		        +"\nSELECT F0.CUST AS CUST, F0.PROV_OVERDRAFT_BAL AS PROV_OVERDRAFT_CUST, F0.PROV_ACCOUNT_CUST AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_TYP\", F0.PROV_ACCOUNT_CUST AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_TYP\", F0.PROV_ACCOUNT_CUST AS \"PROV_ACCOUNT_1_BAL\""
                                    				+"\nFROM ((("
                                    				+"\nSELECT F0.CUST AS CUST, F0.CUST AS PROV_OVERDRAFT_CUST, F0.BAL AS PROV_OVERDRAFT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\", NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS PROV_ACCOUNT_CUST, NULL AS PROV_ACCOUNT_TYP, NULL AS PROV_ACCOUNT_BAL, NULL AS \"PROV_ACCOUNT_1_CUST\", NULL AS \"PROV_ACCOUNT_1_TYP\", NULL AS \"PROV_ACCOUNT_1_BAL\""
                                    				+"\nFROM (OVERDRAFT AS OF SCN 1478778490 F0)) UNION ALL ("
                                    				+"\nSELECT F0.CUST AS CUST, NULL AS PROV_OVERDRAFT_CUST, NULL AS PROV_OVERDRAFT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    				+"\nFROM (("
                                    				+"\nSELECT F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.CUST AS CUST, F0.TYP AS TYP, F0.BAL AS BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\", F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.PROV_ACCOUNT_CUST AS PROV_ACCOUNT_CUST, F0.PROV_ACCOUNT_TYP AS PROV_ACCOUNT_TYP, F0.PROV_ACCOUNT_BAL AS PROV_ACCOUNT_BAL, F0.\"PROV_ACCOUNT_1_CUST\" AS \"PROV_ACCOUNT_1_CUST\", F0.\"PROV_ACCOUNT_1_TYP\" AS \"PROV_ACCOUNT_1_TYP\", F0.\"PROV_ACCOUNT_1_BAL\" AS \"PROV_ACCOUNT_1_BAL\""
                                    				+"\nFROM (((temp_view_of_0) F0) CROSS JOIN ((temp_view_of_0) F1))"
                                    			    +"\nWHERE (((F0.TYP != F0.TYP) AND ((F0.BAL + F0.BAL) < 0)) AND (((F0.CUST = 'Alice') AND (F0.PROV_ACCOUNT_CUST = 'Alice')) AND (F0.\"PROV_ACCOUNT_1_CUST\" = 'Alice')))) F0)"
                                    				+"\nWHERE (F0.PROV_ACCOUNT_TYP = F0.\"PROV_ACCOUNT_1_TYP\"))) F0)";
                                    
                                    
							        //String str1 = "\n UPDATE employee \n SET name = 'Alice'\n WHERE ID = 1";
							        //String str2 = "\n UPDATE employee \n SET name = 'Bob'\n WHERE ID = 2";
							        //String str3 = "\n UPDATE employee \n SET name = 'Mike'\n WHERE ID = 3";
							        ja1.setText(str1);
							        ja2.setText(str2);
							        ja3.setText(str3);
							        
//							        ja1.setCaretColor(Color.BLUE);
//							        ja2.setCaretColor(Color.BLUE);
//							        ja3.setCaretColor(Color.BLUE);
							        
							        ja1.setEditable(false);
							        ja2.setEditable(false);
							        ja3.setEditable(false);
							        
							        //jp1.add(ja1);
							        //jp2.add(ja2);
							        //jp3.add(ja3);
							        
							        
							        f.setLayout(null);
							        
							        f.add(jp1);
							        f.add(jp2);
							        f.add(jp3);

							        jp1.setBounds(135, 35, 260, 95);
							        jp1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jp2.setBounds(390, 35, 260, 95);
							        jp2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jp3.setBounds(648, 35, 260, 95);
							        jp3.setBorder(BorderFactory.createLineBorder(Color.gray,3));

							        ja1.setBounds(0, 10, 5, 20);
							        ja2.setBounds(0, 10, 5, 20);
							        ja3.setBounds(0, 10, 5, 20);
							        
							        
							        
							        //second line
//							        JPanel jp4 = new JPanel();
//							        JPanel jp5 = new JPanel();
//							        JPanel jp6 = new JPanel();
//							        jp4.setBounds(135, 100, 260, 65);
//							        jp4.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//							        jp5.setBounds(390, 100, 260, 65);
//							        jp5.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//							        jp6.setBounds(648, 100, 260, 65);
//							        jp6.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//							        f.add(jp4);
//							        f.add(jp5);
//							        f.add(jp6);
//							        
//							        
//							        String[] cn4 = {"Id","Name","Salary"};
//							        String[] cn5 = {"Id","Name","Salary"};
//							        String[] cn6 = {"Id","Name","Salary"};
//							        Object[][] cd4 = {{"1","Alice","3600"},{"2","Alex","4300"},{"3","Peter","5000"}};
//							        Object[][] cd5 = {{"1","Alice","3600"},{"2","Bob","4300"},{"3","Peter","5000"}};
//							        Object[][] cd6 = {{"1","Alice","3600"},{"2","Bob","4300"},{"3","Mike","5000"}};
//							        JTable ta4 = new JTable(cd4,cn4);
//							        JTable ta5 = new JTable(cd5,cn5);
//							        JTable ta6 = new JTable(cd6,cn6);
//
//							        jp4.add(ta4);
//							        jp5.add(ta5);
//							        jp6.add(ta6);
							        
							        //JPanel jpF = new JPanel();
							        //jpF.setBounds(135, 165, 773, 200);
							        //jpF.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        //f.add(jpF);
							        //third line
							        JPanel jp7 = new JPanel();
							        JPanel jp8 = new JPanel();
							        JPanel jp9 = new JPanel();
							        jp7.setBounds(135, 130, 260, 300);
							        jp7.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jp8.setBounds(390, 130, 260, 300);
							        jp8.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jp9.setBounds(648, 130, 260, 300);
							        jp9.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        
//							        dotFlow.png
							        
							createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/0_beforeOpt.png", "/Users/xun/Documents/java_workspace/gprom-gui/test1.png", 300, 280);
							File file1 = new File("/Users/xun/Documents/java_workspace/gprom-gui/test1.png");
							createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/1_selectionMoveAround.png", "/Users/xun/Documents/java_workspace/gprom-gui/test22.png", 300, 280);
							File file2 = new File("/Users/xun/Documents/java_workspace/gprom-gui/test22.png");
							createThumbnail("/Users/xun/Documents/python_workspace/SqlGui/src/5_mgAdProSel.png", "/Users/xun/Documents/java_workspace/gprom-gui/test3.png", 300, 280);
							File file3 = new File("/Users/xun/Documents/java_workspace/gprom-gui/test3.png");
							
							BufferedImage image1;
							BufferedImage image2;
							BufferedImage image3;
									try {
										image1 = ImageIO.read(file1);
										image2 = ImageIO.read(file2);
										image3 = ImageIO.read(file3);
										JLabel label1 = new JLabel(new ImageIcon(image1));
										JLabel label2 = new JLabel(new ImageIcon(image2));
										JLabel label3 = new JLabel(new ImageIcon(image3));
										
										jp7.add(label1);
										jp8.add(label2);
										jp9.add(label3);
										
										//label1.setBounds(0, 10, 5, 10);
										
									} catch (IOException e1) {
										e1.printStackTrace();
									}
							        
							        
							               
							        
							        f.add(jp7);
							        f.add(jp8);
							        f.add(jp9);
							        
							        
							        //first column
							        JPanel jp11 = new JPanel();
							        JPanel jp12 = new JPanel();
							        JPanel jp13 = new JPanel();
							        jp11.setBounds(35, 35, 100, 95);
							        jp11.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//							        jp12.setBounds(35, 100, 100, 65);
//							        jp12.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jp13.setBounds(35, 130, 100, 300);
							        jp13.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        f.add(jp11);
							        f.add(jp12);
							        f.add(jp13);
							        JLabel jl1 = new JLabel("SQL");
							        JLabel jl2 = new JLabel("TABLE");
							        JLabel jl3 = new JLabel("GRAPH");
							        jp11.add(jl1);
							        jp12.add(jl2);
							        jp13.add(jl3);
							        
							        //first label line
							        //first column
							        JPanel jpl1 = new JPanel();
							        JPanel jpl2 = new JPanel();
							        JPanel jpl3 = new JPanel();
							        JPanel jpl4 = new JPanel();
							        jpl1.setBounds(35, 5, 100, 30);
							        jpl1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jpl2.setBounds(135, 5, 260, 30);
							        jpl2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jpl3.setBounds(390, 5, 260, 30);
							        jpl3.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        jpl4.setBounds(648, 5, 260, 30);
							        jpl4.setBorder(BorderFactory.createLineBorder(Color.gray,3));
							        f.add(jpl1);
							        f.add(jpl2);
							        f.add(jpl3);
							        f.add(jpl4);
							        JLabel jll1 = new JLabel("Methods");
							        JLabel jll2 = new JLabel("Before Optimization");
							        JLabel jll3 = new JLabel("Selection Move Around");
							        JLabel jll4 = new JLabel("Merge");
							        jpl1.add(jll1);
							        jpl2.add(jll2);
							        jpl3.add(jll3);
							        jpl4.add(jll4);
							        
							        //Right buttons
//							        Button jb1 = new Button("Refresh");
//							        JButton jb2 = new JButton("Optimizer");
//							        jb1.setBounds(920, 30, 100, 40);
//							        jb2.setBounds(920, 70, 100, 40);
//							        //jb1.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//							        //jb2.setBorder(BorderFactory.createLineBorder(Color.gray,3));
//							        f.add(jb1);
//							        f.add(jb2);
							        f.setVisible(true);
							  }
							});
					        
					        
					        debuggerFrame.setVisible(true);
					  }
					});
					

					//TODO get provence frame
					JPanel jpTns = new JPanel(new GridLayout(row2.getIntervals().size(),1));
					//startBt.setPreferredSize(new Dimension(100, 20));
//					container.add(startBt);
					
//					JScrollPane scr = new JScrollPane();
//					container.add(scr);
//					container.setVerticalScrollBarPolicy (JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//					container.
//					JPanel pan=new JPanel();
//					pan.setLayout(new FlowLayout());
//					container.setLayout(new FlowLayout());
					
					
					
					
				//ST means that the start time of the transaction.
               //ED means that the end time of the transaction.				
					String ST = row2.getIntervals().get(0).getBegin()
							.toDisplayStringDate()
							+ " "
							+ row2.getIntervals().get(0).getBegin().toDisplayStringTime(true);
					
					String ED = row2.getIntervals().get(row2.getIntervals().size() - 1).getEnd()
							.toDisplayStringDate()
							+ " "
							+ row2.getIntervals().get(row2.getIntervals().size() - 1).getEnd().toDisplayStringTime(true);
					
					//ajin
					EventInterval interval2 = (EventInterval) row2
							.getIntervals().get(0);
					osName = interval2.getOsName();
					sessionid = interval2.getSessionId();
					ED = "3/4/16 12:26:36";
					String str = "OS UserName:"+osName+"\nSessionId:"+sessionid + "\nStart Time:" + ST
							+ "\nCommit Time:" + ED;
					TextArea tf3 = new TextArea(5,20);
					tf3.setText(str);
					//tf3.setPreferredSize(new Dimension(500, 55));
					tf3.setEditable(false);
					
					//summary window (panel -> label + textarea)
					JPanel jpSy = new JPanel();
					JLabel jlSy = new JLabel("Summary");
					JLabel jlSt = new JLabel("Statements");
					//jpSy.add(jlSy);
					//jpSy.add(tf3);
					
					startBt.setBounds(180, 10, 150, 30);					
			        jlSy.setBorder(BorderFactory.createLineBorder(Color.gray,3));
			        jlSy.setBounds(0, 40, 520, 30);
			        
			        //jpSy.setBounds(0, 70, 520, 80);
			        //jpSy.setBorder(BorderFactory.createLineBorder(Color.gray,3));
			        //tf3.setBounds(0, 10, 5, 20);
			        //jpSy.add(tf3);
			        tf3.setBounds(0, 70, 520, 100);
			        jlSt.setBounds(0,170,520,30);
			        jlSt.setBorder(BorderFactory.createLineBorder(Color.gray,3));
			        
			        //jf1.add(jpSy);
			        jf1.add(startBt);
			        jf1.add(jlSy);
			        jf1.add(tf3);
			        jf1.add(jlSt);
					
					
//					
//                    Label tl2 = new Label();
//                    tl2.setText("Statement");
//                    jp.add(tl2);
					
					
					
					
					
					int m = 1;
					System.out.println("size of the interval list: " + row2.getIntervals().size());
					for(int i=0; i < row2.getIntervals().size(); i++){
						EventInterval interval = (EventInterval) row2
								.getIntervals().get(i);
//						sql = sql + "\n" + interval.getTitle();
//						scn = scn + " " + row2.getRowHeader().getLabel();
//						osName = osName + " " + interval.getOsName();
//						sessionid = sessionid + " " + interval.getSessionId();
						sql = interval.getSql();
						tns = row2.getRowHeader().getLabel();
						System.out.println(sql);
					
						// String id = interval.get
//						beginTime = beginTime + " " + interval.getBegin()
//								.toDisplayStringDate()
//								+ " "
//								+ interval.getBegin().toDisplayStringTime();
						beginTime = interval.getBegin()
								.toDisplayStringDate()
								+ " "
								+ interval.getBegin().toDisplayStringTime(true);
//								+"   " + "EndTime: "
//								+ interval.getEnd()
//								.toDisplayStringDate()
//								+ " "
//								+ interval.getEnd().toDisplayStringTime(true);
//						TextArea tf = new TextArea();
//						//sql 
//						tf.setText(sql);
//						
//						tf.setEditable(false);
//						// tf.multiline = true;//设置多行显示
//						tf.setPreferredSize(new Dimension(500, 100));
//						container.add(tf);
//						tf.setBounds(10, 90, 100, 100);

						
//						if (m == 1){
//							sql = "update account set bal=bal-35 where cust='Alice' and typ='Checking'";
//							m++;
//						}
//						else if(m == 2)
//						{
//							sql = "update account set bal=bal+35 where cust='Alice' and typ='Savings'";
//							m++;
//						}
						
						
						
						TextArea tf2 = new TextArea();
//						tf2.setText("start time:"+beginTime +"\n os username:"+osName+"\n session id:"+sessionid);
						tf2.setText(" SQL:" + sql + "\n Start Time:"+beginTime );
						tf2.setEditable(false);
						// tf.multiline = true;//设置多行显示
						//tf2.setPreferredSize(new Dimension(500, 100));
//						pan.add(tf2);
					
//						scrollPane.add(tf2);
						
						tf2.setBounds(10, 90, 100, 100);
						jpTns.add(tf2);
						
//						if(m == 3)
//							break;
					}
					
//					jpTns.setBounds(0,200,520, row2.getIntervals().size() * 300);
					JScrollPane scrollPane = new JScrollPane(jpTns);
					jpTns.setPreferredSize(new Dimension(520, row2.getIntervals().size() * 100));
					scrollPane.setBounds(0, 200, 520, 350);
					
//					jpTns.revalidate();
					jf1.add(scrollPane);
					//JScrollPane scrollPane=new JScrollPane(jp);
//					scrollPane.setViewportView(jp);
//					scrollPane.setViewportBorder(BorderFactory
//							.createBevelBorder(BevelBorder.LOWERED));
//					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					//container.add(scrollPane,BorderLayout.CENTER);
//					 JScrollPane scr1=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//						
//					 container.add(scr1);
					 
//					EventInterval interval = (EventInterval) row2
//							.getIntervals().get(0);

					
					// 在这里添加弹出窗口界面。。。
					// JOptionPane.showMessageDialog(null,
					// "在对话框内显示的描述性的文字"+row2.getIntervals().get(0).getBegin(),
					// "标题条文字串", JOptionPane.ERROR_MESSAGE);
//					String sql = interval.getTitle();
//					String scn = row2.getRowHeader().getLabel();
//					String osName = interval.getOsName();
//					String sessionid = interval.getSessionId();
//					
//					
//					
//					// String id = interval.get
//					String beginTime = interval.getBegin()
//							.toDisplayStringDate()
//							+ " "
//							+ interval.getBegin().toDisplayStringTime();
					// String endTime =
					// interval.getEnd().toDisplayStringDate()+" "+interval.getEnd().toDisplayStringTime();
					// 显示一个dialog

					// JOptionPane.showConfirmDialog(null,
					// "sql:"+sql+"\n\nbegin:"+beginTime, "information",
					// JOptionPane.YES_NO_OPTION);
					
					
					
					
					
					
					
					
					
					
//					JFrame jf1 = new JFrame(scn);
//					jf1.setBounds(100, 50, 520, 280);
//					jf1.setVisible(true);
//					jf1.setLayout(new FlowLayout());
//					Container container = jf1.getContentPane();
//
//					Button startBt = new Button("Set Provence");
//					//startBt.setPreferredSize(new Dimension(100, 20));
//					container.add(startBt);

//					Button startBt2 = new Button("After");
//					//startBt2.setPreferredSize(new Dimension(100, 10));
//					container.add(startBt2);
//
//					Button startBt3 = new Button("Update");
//					//startBt2.setPreferredSize(new Dimension(100, 10));
//					container.add(startBt3);

//					TextField tf = new TextField();
//					TextArea tf = new TextArea();
//					//sql 
//					tf.setText(sql);
//					
//					tf.setEditable(false);
//					// tf.multiline = true;//设置多行显示
//					tf.setPreferredSize(new Dimension(500, 100));
//					container.add(tf);
//					tf.setBounds(10, 90, 100, 100);
//
//					
//					
//					TextArea tf2 = new TextArea();
//					tf2.setText("start time:"+beginTime +"\n os username:"+osName+"\n session id:"+sessionid);
//					tf2.setEditable(false);
//					// tf.multiline = true;//设置多行显示
//					tf2.setPreferredSize(new Dimension(500, 100));
//					container.add(tf2);
//					tf2.setBounds(10, 90, 100, 100);
					
					if (e.getClickCount() == 2) {
						Point origin = e.getPoint();
						if (_tbv.getDelegate().getYAxisRect().contains(origin)) {
							TimeBarRow row = _tbv.getRowForXY(origin.x,
									origin.y);
							if (row != null) {
								if (row instanceof EventTimeBarRow) {
									EventTimeBarRow erow = (EventTimeBarRow) row;

									if (!erow.isExpanded()) {
										// expand
										_tbv.getTimeBarViewState()
												.setDrawOverlapping(row, false);
										_tbv.getTimeBarViewState()
												.setRowHeight(
														row,
														calculateRowHeight(
																_tbv.getDelegate(),
																_tbv.getTimeBarViewState(),
																row));
										erow.setExpanded(true);
									} else {

										// fold
										_tbv.getTimeBarViewState()
												.setDrawOverlapping(row, true);
										_tbv.getTimeBarViewState()
												.setRowHeight(
														row,
														_tbv.getTimeBarViewState()
																.getDefaultRowHeight());
										erow.setExpanded(false);
									}
								}
							}

						}
					}
				}

				/**
				 * Calculate the optimal row height
				 * 
				 * @param delegate
				 * @param timeBarViewState
				 * @param row
				 * @return
				 */
				public int calculateRowHeight(TimeBarViewerDelegate delegate,
						ITimeBarViewState timeBarViewState, TimeBarRow row) {
					int maxOverlap = timeBarViewState.getDefaultRowHeight();
					int height = delegate.getMaxOverlapCount(row) * maxOverlap;
					return height;
				}

			});

		}

		// change listener
		_tbv.addTimeBarChangeListener(new ITimeBarChangeListener() {

			public void intervalChangeCancelled(TimeBarRow row,
					Interval interval) {
				System.out.println("CHANGE CANCELLED " + row + " " + interval);
			}

			public void intervalChangeStarted(TimeBarRow row, Interval interval) {
				System.out.println("CHANGE STARTED " + row + " " + interval);
			}

			public void intervalChanged(TimeBarRow row, Interval interval,
					JaretDate oldBegin, JaretDate oldEnd) {
				System.out.println("CHANGE DONE " + row + " " + interval);
			}

			public void intervalIntermediateChange(TimeBarRow row,
					Interval interval, JaretDate oldBegin, JaretDate oldEnd) {
				System.out.println("CHANGE INTERMEDIATE " + row + " "
						+ interval);
			}

			public void markerDragStarted(TimeBarMarker marker) {
				System.out.println("Marker drag started " + marker);
			}

			public void markerDragStopped(TimeBarMarker marker) {
				System.out.println("Marker drag stopped " + marker);
			}

		});

		// sample property listener
		_tbv.addPropertyChangeListener(_tbv.PROPERTYNAME_STARTDATE,
				new PropertyChangeListener() {

					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						System.out.println("Start changed to "
								+ evt.getNewValue());

					}

				});

		// Do not allow any modifications - do not add an interval modificator!
		// _tbv.addIntervalModificator(new DefaultIntervalModificator());

		// do not allow row selections
		_tbv.getSelectionModel().setRowSelectionAllowed(false);

		// register additional renderer
		_tbv.registerTimeBarRenderer(EventInterval.class, new EventRenderer());

		// add a marker
		_tm = new TimeBarMarkerImpl(true, _tbv.getModel().getMinDate().copy()
				.advanceHours(3));
		_tm.setDescription("Timebarmarker");
		_tbv.addMarker(_tm);

		// do not show the root node
		_tbv.setHideRoot(true);

		// add a popup menu for EventIntervals
		Action action = new AbstractAction("IntervalAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		JPopupMenu pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.registerPopupMenu(EventInterval.class, pop);

		// add a popup menu for the body
		final Action bodyaction = new AbstractAction("BodyAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(bodyaction);
		pop.add(new RunMarkerAction(_tbv));

		// add the zoom action
		pop.add(new ZoomAction(_tbv));
		// add the rem selection action
		pop.add(new ResetRegionSelectionAction(_tbv));

		_tbv.setBodyContextMenu(pop);

		// sample: check enablement of action in a popup
		pop.addPopupMenuListener(new PopupMenuListener() {

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				System.out.println(_tbv.getPopUpInformation().getLeft());
				System.out.println(_tbv.getPopUpInformation().getRight()
						.toDisplayString());
				//System.out.println("222222222");

				if (_tbv.getPopUpInformation().getRight().getHours() > 9) {
					bodyaction.setEnabled(false);
				} else {
					bodyaction.setEnabled(true);
				}
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}

			public void popupMenuCanceled(PopupMenuEvent e) {
				// TODO Auto-generated method stub

			}
		});

		// add a popup menu for the hierarchy
		action = new AbstractAction("HierarchyAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};

		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setHierarchyContextMenu(pop);

		// add a popup menu for the header
		action = new AbstractAction("HeaderAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setHeaderContextMenu(pop);

		// add a popup menu for the time scale
		action = new AbstractAction("TimeScaleAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};

		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setTimeScaleContextMenu(pop);

		// add a popup menu for the title area
		action = new AbstractAction("TitleAction") {
			public void actionPerformed(ActionEvent e) {
				System.out.println("run " + getValue(NAME));
			}
		};
		pop = new JPopupMenu("Operations");
		pop.add(action);
		_tbv.setTitleContextMenu(pop);

		// add dnd support
		DragSource dragSource = DragSource.getDefaultDragSource();
		DragGestureListener dgl = new TimeBarViewerDragGestureListener();
		DragGestureRecognizer dgr = dragSource
				.createDefaultDragGestureRecognizer(_tbv._diagram,
						DnDConstants.ACTION_COPY, dgl);

		// add the control panel
		EventMonitoringControlPanel cp = new EventMonitoringControlPanel(_tbv,
				_tm, 100); // TODO
		f.getContentPane().add(cp, BorderLayout.SOUTH);

		// make sure the marker is in a certain area when zooming
		// relative display range the marker should be in after zooming
		final double min = 0.3;
		final double max = 0.7;

		_tbv.addPropertyChangeListener(_tbv.PROPERTYNAME_PIXELPERSECOND,
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						// if not displayed set the viewer to display the marker
						// at the min position
						if (!isInRange(_tm.getDate(), min, max)) {
							int secondsDisplayed = _tbv.getSecondsDisplayed();
							JaretDate startDate = _tm.getDate().copy()
									.advanceSeconds(-min * secondsDisplayed);
							// _tbv.setStartDate(startDate);
						}
					}

				});

		// go!
		f.setVisible(true);
	}

	public void setEndDate(TimeBarViewer tbv, JaretDate endDate) {
		int secondsDisplayed = tbv.getSecondsDisplayed();
		JaretDate startDate = endDate.copy().advanceSeconds(-secondsDisplayed);
		tbv.setStartDate(startDate);
	}

	boolean isInRange(JaretDate date, double min, double max) {
		int secondsDisplayed = _tbv.getSecondsDisplayed();
		JaretDate minDate = _tbv.getStartDate().copy()
				.advanceSeconds(min * secondsDisplayed);
		JaretDate maxDate = _tbv.getStartDate().copy()
				.advanceSeconds(max * secondsDisplayed);
		return minDate.compareTo(date) > 0 && maxDate.compareTo(date) < 0;
	}

	class TimeBarViewerDragGestureListener implements DragGestureListener {
		public void dragGestureRecognized(DragGestureEvent e) {
			Component c = e.getComponent();
			System.out.println("component " + c);
			System.out.println(e.getDragOrigin());

			boolean markerDragging = _tbv.getDelegate()
					.isMarkerDraggingInProgress();
			if (markerDragging) {
				return;
			}

			List<Interval> intervals = _tbv.getDelegate().getIntervalsAt(
					e.getDragOrigin().x, e.getDragOrigin().y);
			if (intervals.size() > 0) {
				Interval interval = intervals.get(0);
				e.startDrag(null, new StringSelection("Drag "
						+ ((EventInterval) interval).getSql()));
				return;
			}
			Point origin = e.getDragOrigin();
			if (_tbv.getDelegate().getYAxisRect().contains(origin)) {
				TimeBarRow row = _tbv.getRowForXY(origin.x, origin.y);
				if (row != null) {
					e.startDrag(null, new StringSelection("Drag ROW " + row));
				}
			}

		}
	}

	/**
	 * Simple zoom action.
	 * 
	 * @author kliem
	 * @version $Id: EventMonitoringExample.java 1073 2010-11-22 21:25:33Z kliem
	 *          $
	 */
	class ZoomAction extends AbstractAction implements ISelectionRectListener {
		TimeBarViewer _tbv;

		public ZoomAction(TimeBarViewer tbv) {
			super("Zoom to selection");
			_tbv = tbv;
			setEnabled(false);
			_tbv.addSelectionRectListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			if (_tbv.getRegionRect() != null) {
				TBRect tbrect = _tbv.getRegionRect();
				JaretDate startDate = tbrect.startDate;
				int seconds = tbrect.endDate.diffSeconds(tbrect.startDate);
				int pixel = _tbv.getDelegate().getDiagramRect().width;
				double pps = ((double) pixel) / ((double) seconds);
				_tbv.clearRegionRect();
				_tbv.setPixelPerSecond(pps);
				_tbv.setStartDate(startDate);
				// TODO row scaling
			}
		}

		public void regionRectChanged(TimeBarViewerDelegate delegate,
				TBRect tbrect) {
			setEnabled(true);
		}

		public void regionRectClosed(TimeBarViewerDelegate delegate) {
			setEnabled(false);
		}

		public void selectionRectChanged(TimeBarViewerDelegate delegate,
				JaretDate beginDate, JaretDate endDate, List<TimeBarRow> rows) {
			// TODO Auto-generated method stub

		}

		public void selectionRectClosed(TimeBarViewerDelegate delegate) {
			// TODO Auto-generated method stub

		}

	}

	class ResetRegionSelectionAction extends AbstractAction {
		TimeBarViewer _tbv;

		public ResetRegionSelectionAction(TimeBarViewer tbv) {
			super("Remove selection");
			_tbv = tbv;
		}

		public void actionPerformed(ActionEvent e) {
			_tbv.clearRegionRect();
		}

	}

	class RunMarkerAction extends AbstractAction {
		TimeBarViewer _tbv;

		public RunMarkerAction(TimeBarViewer tbv) {
			super("Run marker");
			_tbv = tbv;
		}

		public void actionPerformed(ActionEvent e) {
			_tm.setDate(_tbv.getModel().getMinDate().copy());

			final Timer timer = new Timer(40, null);
			ActionListener al = new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					_tm.setDate(_tm.getDate().copy().advanceMillis(40));
					if (_tm.getDate().compareTo(_tbv.getModel().getMaxDate()) > 0) {
						timer.stop();
					}
				}
			};
			timer.addActionListener(al);
			timer.setRepeats(true);
			timer.setDelay(40);
			timer.start();
		}

	}

}






