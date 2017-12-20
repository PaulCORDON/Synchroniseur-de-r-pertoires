import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Maitre  extends Transferable implements Runnable{
	
	String id;
	BufferedReader br;
	PrintWriter bw;
	Socket _socket;
	
	
	Maitre(String st,BufferedReader r,PrintWriter w,Socket s) {		
		id=st;
		br=r;
		bw=w;
		_socket=s;
	}
	
	public void run(){
		File fenv= new File("H:/Mes documents/4A/TestReseau/Client/aEnvoyer.odt");
		
		
		try {
			push(fenv);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	

 
	private void push(File f)throws IOException {	  
        transfert(new FileInputStream(f),_socket.getOutputStream(),true);
	}

	private void pull(File f)throws IOException {        
        transfert(_socket.getInputStream(),new FileOutputStream(f),true); 
	}
}
