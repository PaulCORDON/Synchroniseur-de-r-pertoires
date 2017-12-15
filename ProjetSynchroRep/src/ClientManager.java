import java.io.*;
import java.net.Socket;

public class ClientManager extends Thread {
	
	public void run(Socket s){
		try{
			InputStream in = s.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String line = bf.readLine();
			System.out.println(line);
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
