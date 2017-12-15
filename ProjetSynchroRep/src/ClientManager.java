import java.io.*;
import java.net.Socket;

public class ClientManager extends Thread {
	Socket client;
	
	public ClientManager(Socket cl) {
		client=cl;		
	}
	
	public void run(){
		
		try{
			System.out.println("coucou");
				
			InputStream input=client.getInputStream();
			String lu = new BufferedReader(new InputStreamReader(input)).readLine();
			System.out.println(lu);

		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
