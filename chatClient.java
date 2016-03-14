import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class chatClient{
	JTextArea incoming,outgoing,Beginning;
	JFrame frame4,frame1,frame2;	//f1开始界面,f2.登陆界面，f4聊天界面 
	JLabel label1,label2;	//user name，password
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;


	public class First{			//开始界面
		public void go(){
		frame1 = new JFrame("Chat Client");
		frame1 = new JFrame("Chat Client");
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();

		Font TextFont = new Font("serif",Font.BOLD,22);
		Beginning = new JTextArea(12,1);
		Beginning = new JTextArea("\n\n\n\n\nDo you have an acount ? \n\n\n");
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

		panel3.setBackground(Color.WHITE);
		panel3.add(SignIn);
		panel4.setBackground(Color.WHITE);
		panel4.add(SignUp);
		panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
		panel2.setBackground(Color.WHITE);
		panel2.add(panel3);
		panel2.add(panel4);

		frame1.getContentPane().add(BorderLayout.CENTER,Beginning);
		frame1.getContentPane().add(BorderLayout.EAST,panel2);
		frame1.setSize(300,500);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setVisible(true);
		}
	}

	public class Second{		//登陆界面
		public void go(){
			frame1.setVisible(false);
			frame2 = new JFrame("Chat Client");
			JPanel panel8 = new JPanel();
			JPanel panel5 = new JPanel();
			JPanel panel6 = new JPanel();
			JPanel panel7 = new JPanel();
			JTextField nameField = new JTextField(10);
			JTextField passedField = new JTextField(10);
			Font labelFont = new Font("serif",Font.BOLD,15);
			label1 = new JLabel("User name :");
			label1.setFont(labelFont);
			label2 = new JLabel("Password : ");
			label2.setFont(labelFont);
			JButton confirm = new JButton("confirm");
			confirm.setFont(labelFont);
			confirm.addActionListener(new confirmListener());
			JButton exit = new JButton("Exit");
			exit.setFont(labelFont);
			exit.addActionListener(new exitListener());
			
			panel6.setLayout(new BoxLayout(panel6,BoxLayout.X_AXIS));
			panel6.add(confirm);
			panel6.add(exit);

			panel5.add(label1);
			panel5.add(nameField);

			panel7.add(label2);
			panel7.add(passedField);

			panel8.setLayout(new BoxLayout(panel8,BoxLayout.Y_AXIS));
			panel8.add(panel5);
			panel8.add(panel7);			
			frame2.setSize(300,500);
			frame2.getContentPane().add(BorderLayout.NORTH,panel8);
			frame2.getContentPane().add(BorderLayout.CENTER,panel6);
			frame2.setVisible(true);
		}
	}

	public class Forth{			//聊天窗口 The chat
		public void go(){
		frame2.setVisible(false);
		frame4 = new JFrame("Chat Client");
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

		frame4.getContentPane().add(BorderLayout.CENTER,panel1);
		frame4.setSize(700,400);
		
		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame4.setVisible(true);
		}
	}
	public static void main(String[] args){
		chatClient client = new chatClient();
		client.go();
	}

	public void go(){
		new First().go();
		
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
			new Second().go();
		}
	}

	public class confirmListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			new Forth().go();
			//确认登陆
			//密码发送服务器
			//密码比对
			//if(true)登陆、
			//else 重新输入
		}
	}
	public class exitListener implements ActionListener{
		public void actionPerformed(ActionEvent EV){
			frame2.setVisible(false);
			System.out.println("Thanks for using me!");
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
