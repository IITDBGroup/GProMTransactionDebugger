package timebars.eventmonitoring.swing;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import timebars.events.swing.SwingEventExample;
import gui.*;
class EventMonitoring {
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
				 EventMonitoringMain.main(new String[0]);
			}
		});
		contentPane.add(b);
	}
}



