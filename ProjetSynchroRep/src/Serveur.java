import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Serveur implements Runnable{
	
	private final int port = 1996;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread t=new Thread(new Serveur());
		t.start();
	}
	@Override
	public void run(){
		try {
			ServerSocket listener = new ServerSocket(8080);
			int i =0;
			while(i < 2) {
				Socket aClient = listener.accept();
				ClientManager cm = new ClientManager(aClient);
				cm.start();
				/*InputStream in = aClient.getInputStream();
				BufferedReader pout = new BufferedReader(new InputStreamReader(in));
				OutputStream out = aClient.getOutputStream();
				System.out.print("test");
				String line = pout.readLine();
				
				BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(out));
				wr.write(line);
				if(line == "STOP") {
					i++;
					aClient.close();
				}*/
			}
			listener.close();
		}catch(UnknownHostException e) {
			System.out.println(e.toString());
		}catch(IOException e) {
			System.out.println(e.toString());
		}
	}
	
	
}
