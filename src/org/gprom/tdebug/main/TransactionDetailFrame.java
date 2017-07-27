package org.gprom.tdebug.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = -1610954370493488724L;
	
	private EventTimeBarRow currentRow = null;
	private JButton startButton = null;
	private TimeBarViewer _tbv = null;
	private List<String> tableNames = new ArrayList<String>();
	
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

		JButton startBt = new JButton("Debug Transaction");

		startBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TransactionDebuggerFrame tdf = new TransactionDebuggerFrame(currentRow, tableNames);
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
		
		String isoLevel = "";
		if(currentRow.getIsoLevel().equals("1"))
			isoLevel = "READ COMMITTED";
		else
			isoLevel = "SERIALIZABLE";
		
		ED = "3/4/16 12:26:36";
		String str = "OS UserName: "+osName+"\nSessionId: "+sessionid + "\nStart Time: " + ST
				+ "\nCommit Time: " + ED + "\nIsolation Level: " + isoLevel;
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

		tf3.setBounds(0, 70, 520, 100);
		jlSt.setBounds(0,170,520,30);
		jlSt.setBorder(BorderFactory.createLineBorder(Color.gray,3));

		this.add(startBt);
		this.add(jlSy);
		this.add(tf3);
		this.add(jlSt);

		//System.out.println("size of the interval list: " + currentRow.getIntervals().size());
		for(int i=0; i < currentRow.getIntervals().size(); i++){
			EventInterval interval = (EventInterval) currentRow
					.getIntervals().get(i);

			sql = interval.getSql();
			tns = currentRow.getRowHeader().getLabel();
			//System.out.println(sql);

			beginTime = interval.getBegin()
					.toDisplayStringDate()
					+ " "
					+ interval.getBegin().toDisplayStringTime(true);

			TextArea tf2 = new TextArea();
			//tf2.setText("start time:"+beginTime +"\n os username:"+osName+"\n session id:"+sessionid);
			tf2.setText(" SQL:" + sql + "\n Start Time:"+beginTime );
			tf2.setEditable(false);
			// tf.multiline = true;
			//tf2.setPreferredSize(new Dimension(500, 100));

			tf2.setBounds(10, 90, 100, 100);
			jpTns.add(tf2);
			
			//add table name: used for two table case
			Pattern p = Pattern.compile("(?i)update (\\w*) .*|(?i)insert into (\\w*) .*");
			Matcher m = p.matcher(sql);
			if (m.find())
			{
				if(m.group(1) != null)
				{
				   if(!tableNames.contains(m.group(1)))
						tableNames.add(m.group(1));
				   System.out.println("find table: "+m.group(0)+ " "+m.group(1));
				}
				else
				{
					if(!tableNames.contains(m.group(2)))
						tableNames.add(m.group(2));
					System.out.println("find table: "+m.group(0)+ " "+m.group(2));
				}
			}
		}
		
		//print table name list (diff tables in this transaction)
		for(int i=0; i<tableNames.size(); i++)
			System.out.println("tableNames: "+ tableNames.get(i));
		
		JScrollPane scrollPane = new JScrollPane(jpTns);
		jpTns.setPreferredSize(new Dimension(520, currentRow.getIntervals().size() * 100));
		scrollPane.setBounds(0, 200, 520, 350);

		this.add(scrollPane);
	}




	private void setupListeners() {

	}




	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
