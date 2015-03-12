package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class TalkerThread extends Thread {
	private String msg, host;
	private int port;
	private ArrayBlockingQueue<String> channel;
	private boolean going;
	
	public TalkerThread(String msg, String host, int port, ArrayBlockingQueue<String> channel) {
		this.msg = msg.endsWith("\n") ? msg : msg + "\n";
		this.host = host;
		this.port = port;
		this.channel = channel;
		going = true;
	}
	
	public synchronized boolean isGoing() {
		return going;
	}

	@Override
	public void run() {
		Socket s = null;
		try {
			s = new Socket(host, port);
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			writer.println(msg);
			writer.flush();

			BufferedReader responses = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (going) {
				while (going && !responses.ready());
				String line = responses.readLine();
				if ( line != null ) {channel.put(line);}
			}
			going = false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
            try {
                if (s != null) {s.close();}
            } catch (IOException ioe) {
                System.out.println("error closing socket");
            }
        }		
	}
	
	public synchronized void halt() {
		going = false;
	}
}
