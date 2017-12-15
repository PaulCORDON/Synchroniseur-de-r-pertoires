import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Serveur implements Runnable{
	
	private final int port = 1996;
	
	@Override
	public void run(){
		try {
			ServerSocket listener = new ServerSocket(6060);
			int i =0;
			while(i < 2) {
				Socket aClient = listener.accept();
				ClientManager cm = new ClientManager();
				
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
