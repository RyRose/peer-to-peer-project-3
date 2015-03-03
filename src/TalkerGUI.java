package sockdemo;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import java.awt.event.*;
import java.util.concurrent.ArrayBlockingQueue;

@SuppressWarnings("serial")
public class TalkerGUI extends JFrame {
	private JTextArea outgoing, incoming;
	private JTextField hostName, port;
	private JButton send;
	private ArrayBlockingQueue<String> channel;
	private TalkThread talker;
	
	public TalkerGUI() {
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Talker");
		
		setLayout(new BorderLayout());
		JPanel top = new JPanel();
		send = new JButton("Send");
		top.add(send);
		top.add(new JLabel("Host"));
		hostName = new JTextField(10);
		top.add(hostName);
		top.add(new JLabel("Port"));
		port = new JTextField(10);
		top.add(port);
		add(top, BorderLayout.NORTH);

		JPanel center = new JPanel();
		center.setLayout(new GridLayout(2, 1));
		outgoing = makeTextArea("Outgoing", center);
		incoming = makeTextArea("Incoming", center);
		add(center, BorderLayout.CENTER);
		
		channel = new ArrayBlockingQueue<String>(2, true);
		
		send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				send(outgoing.getText(), hostName.getText(), Integer.parseInt(port.getText()));
			}});
	}
	
	private void send(String msg, String host, int port) {
		if (talker != null && talker.isGoing()) {
			talker.halt();
		}
		talker = new TalkThread(msg, host, port, channel);
		new Receiver().start();
		talker.start();		
	}
	
	private JTextArea makeTextArea(String title, JPanel where) {
		JTextArea t = new JTextArea();
		JScrollPane scroller = new JScrollPane(t);
		scroller.setBorder(BorderFactory.createTitledBorder(title));
		where.add(scroller);
		return t;
	}
	
	public static void main(String[] args) {
		new TalkerGUI().setVisible(true);
	}
	
	private class Receiver extends Thread {
		public void run() {
			while (talker.isGoing()) {
				String line;
				try {
					line = channel.take();
					incoming.setText(incoming.getText() + "\n" + line);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
