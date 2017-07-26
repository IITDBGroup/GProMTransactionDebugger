package timebars.eventmonitoring.swing;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.gprom.tdebug.main.*;
import org.gprom.util.LoggerUtil;

import timebars.events.swing.SwingEventExample;
class EventMonitoring {
	
	static Logger log = Logger.getLogger(EventMonitoring.class);
	
	public static void main(String[] args) {
    	JFrame f = new JFrame(SwingEventExample.class.getName());
        f.setSize(800, 500);
        f.getContentPane().setLayout(new GridLayout(10, 1));
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addLaunchButtons(f.getContentPane());
        
        f.setVisible(true);
    }
	
	private static void addLaunchButtons(Container contentPane) {
		JButton b = new JButton();
		b.setText("Event");
		b.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) {
				 try {
					GTDB.main(new String[0]);
				}
				catch (Exception e1) {
					LoggerUtil.logException(e1, log);
				}
			}
		});
		contentPane.add(b);
	}
}



