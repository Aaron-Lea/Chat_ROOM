import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class chatClient{
	JTextArea incoming;
	JTextArea outgoing;
	JTextArea Beginning;
	JFrame frame1;	//聊天界面
	JFrame frame2;	//登陆界面
	JPanel panel1;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;

	public static void main(String[] args){
		chatClient client = new chatClient();
		client.go();
	}

	public void go(){
		frame1 = new JFrame("Chat Client");
		frame2 = new JFrame("Chat Client");
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();	
		Font TextFont = new Font("serif",Font.BOLD,22);
		Beginning = new JTextArea(12,1);
		Beginning = new JTextArea("\n\n\n\n\n\nDo you have an acount ? \n\n\n");
		Beginning.setFont(TextFont);
		Beginning.setLineWrap(true);
		Beginning.setWrapStyleWord(true);
		Beginning.setEditable(false);
		Font bigFont = new Font("serif",Font.BOLD,15);
		JButton SignIn = new JButton("Sign In :) ");
		SignIn.setFont(bigFont);
		SignIn.addActionListener(new SignInListener());
		JButton SignUp = new JButton("Sign Up :) ");
		SignUp.setFont(bigFont);
		SignUp.addActionListener(new SignUpListener());

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


		panel3.setBackground(Color.WHITE);
		panel3.add(SignIn);
		panel4.setBackground(Color.WHITE);
		panel4.add(SignUp);
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
		panel2.setBackground(Color.WHITE);
		panel2.add(panel3);
		panel2.add(panel4);
		setUpNetWorking();

		Thread readerThread = new Thread(new incomingReader());
		readerThread.start();

		frame2.getContentPane().add(BorderLayout.CENTER,Beginning);
		frame2.getContentPane().add(BorderLayout.EAST,panel2);
		frame1.getContentPane().add(BorderLayout.CENTER,panel1);
		frame1.setSize(700,400);
		frame2.setSize(300,500);
		frame2.setVisible(true);
		//还没有把聊天窗口打开
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

	public class SignInListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			frame1.setVisible(true);
			//登陆按钮
		

		}
	}

	public class SignUpListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
		
		//注册界面
	
		
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
					incoming.append("read" + " : " +message + "\n");
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	} 
}