package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import timebars.eventmonitoring.model.EventInterval;
import timebars.eventmonitoring.model.EventTimeBarRow;

public class TransactionDetailFrame extends JFrame implements ActionListener{
	private EventTimeBarRow currentRow = null;
	private JButton startButton = null;
	private TimeBarViewer _tbv = null;
	public TransactionDetailFrame(EventTimeBarRow row, TimeBarViewer _tbv) {
		super();
		this._tbv = _tbv;
		this.currentRow = row;
		setup();
		setupListeners();
	}
	
	
	

	private void setup() {

					
					// EventInterval interval = (EventInterval)
					// flatModel.getRow(0).getIntervals().get(0);
					String sql = "";
					String tns = "Transactions";
					String osName = "";
					String sessionid = "";
					String beginTime = "";
					this.setTitle("Transactions Details");
					//this.setBounds(100, 50, 520, 800);	
					this.setLayout(null);
					//this.setLayout(new FlowLayout());
					//Container container = this.getContentPane();
					//JPanel jp = new JPanel(new GridLayout(8,1));
					//JPanel jp = new JPanel();
//					jp.setPreferredSize(new Dimension(500, 100));

					JButton startBt = new JButton("Get Provence");
					
					startBt.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							TransactionDebuggerFrame tdf = new TransactionDebuggerFrame(currentRow);
							tdf.setTitle("Debug Panel");
							tdf.setSize(1200, 600);
							tdf.setVisible(true);
						}
						
					});
					//TODO get provence frame
					JPanel jpTns = new JPanel(new GridLayout(currentRow.getIntervals().size(),1));
					
					
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
					String ST = currentRow.getIntervals().get(0).getBegin()
							.toDisplayStringDate()
							+ " "
							+ currentRow.getIntervals().get(0).getBegin().toDisplayStringTime(true);
					
					String ED = currentRow.getIntervals().get(currentRow.getIntervals().size() - 1).getEnd()
							.toDisplayStringDate()
							+ " "
							+ currentRow.getIntervals().get(currentRow.getIntervals().size() - 1).getEnd().toDisplayStringTime(true);
					
					//ajin
					EventInterval interval2 = (EventInterval) currentRow
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
			        
			        //this.add(jpSy);
			        this.add(startBt);
			        this.add(jlSy);
			        this.add(tf3);
			        this.add(jlSt);
					
					
//					
//                    Label tl2 = new Label();
//                    tl2.setText("Statement");
//                    jp.add(tl2);
					
					
					
					
					
					int m = 1;
//					System.out.println("size of the interval list: " + currentRow.getIntervals().size());
					for(int i=0; i < currentRow.getIntervals().size(); i++){
						EventInterval interval = (EventInterval) currentRow
								.getIntervals().get(i);
//						sql = sql + "\n" + interval.getTitle();
//						scn = scn + " " + currentRow.getRowHeader().getLabel();
//						osName = osName + " " + interval.getOsName();
//						sessionid = sessionid + " " + interval.getSessionId();
						sql = interval.getSql();
						tns = currentRow.getRowHeader().getLabel();
//						System.out.println(sql);
					
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
					
//					jpTns.setBounds(0,200,520, currentRow.getIntervals().size() * 300);
					JScrollPane scrollPane = new JScrollPane(jpTns);
					jpTns.setPreferredSize(new Dimension(520, currentRow.getIntervals().size() * 100));
					scrollPane.setBounds(0, 200, 520, 350);
					
//					jpTns.revalidate();
					this.add(scrollPane);
					//JScrollPane scrollPane=new JScrollPane(jp);
//					scrollPane.setViewportView(jp);
//					scrollPane.setViewportBorder(BorderFactory
//							.createBevelBorder(BevelBorder.LOWERED));
//					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					//container.add(scrollPane,BorderLayout.CENTER);
//					 JScrollPane scr1=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//						
//					 container.add(scr1);
					 
//					EventInterval interval = (EventInterval) currentRow
//							.getIntervals().get(0);

					
					// 在这里添加弹出窗口界面。。。
					// JOptionPane.showMessageDialog(null,
					// "在对话框内显示的描述性的文字"+currentRow.getIntervals().get(0).getBegin(),
					// "标题条文字串", JOptionPane.ERROR_MESSAGE);
//					String sql = interval.getTitle();
//					String scn = currentRow.getRowHeader().getLabel();
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
					
					
					
					
					
					
					
					
					
					
//					JFrame this = new JFrame(scn);
//					this.setBounds(100, 50, 520, 280);
//					this.setVisible(true);
//					this.setLayout(new FlowLayout());
//					Container container = this.getContentPane();
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
					
				
	}




	private void setupListeners() {
		
	}




	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
