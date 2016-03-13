import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class chatClient{
	JTextArea incoming;
	JTextArea outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	public static void main(String[] args){
		chatClient client = new chatClient();
		client.go();
	}

	public void go(){
		JFrame frame1 = new JFrame("Chat Client");
		JPanel panel1 = new JPanel();
		incoming = new JTextArea(15,50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextArea(15,20);
		JButton send = new JButton("Send");
		send.addActionListener(new sendListener());
		panel1.add(qScroller);
		panel1.add(outgoing);
		panel1.add(send);
		setUpNetWorking();

		Thread readerThread = new Thread(new incomingReader());
		readerThread.start();

		frame1.getContentPane().add(BorderLayout.CENTER,panel1);
		frame1.setSize(400,500);
		frame1.setVisible(true);
	}

	private void setUpNetWorking(){
		try{
			sock = new Socket("127.0.0.1",5000);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("networking established");
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	public class sendListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			try{
				writer.println(outgoing.getText());
				writer.flush();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}

	public class incomingReader implements Runnable{
		public void run(){
			String message;
			try{
				while((message = reader.readLine()) != null){
					System.out.println("read : " + message);
					incoming.append(message + "\n");
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	} 
}