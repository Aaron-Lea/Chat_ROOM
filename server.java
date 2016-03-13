import java.io.*;
import java.net.*;
import java.util.*;

public class server{
	ArrayList clientOutputStream;

	public class ClientHandler implements Runnable{
		BufferedReader reader;
		Socket sock;

		public ClientHandler(Socket clientSocket){
			try{
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		public void run(){
			String message;
			try{
				while((message = reader.readLine()) != null){
					System.out.println("reader" + message);
					tellEveryone(message);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args){
		new server().go();
	}

	public void go(){
		clientOutputStream = new ArrayList();
		try{
			ServerSocket ServerSocket = new ServerSocket(5000);

			while(true){
				Socket clientSocket = ServerSocket.accept();
				//clientSocket = new Socket("127.0.0.1",5000);
			
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				clientOutputStream.add(writer);

				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void tellEveryone(String message){
		Iterator it = clientOutputStream.iterator();
		while(it.hasNext()){
			try{
				PrintWriter writer = (PrintWriter) it.next();	
				writer.println(message);
				writer.flush();
			}catch(Exception ex){
					ex.printStackTrace();
					}
		}
	}
}