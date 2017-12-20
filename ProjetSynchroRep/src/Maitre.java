import java.io.BufferedReader;
import java.io.File;
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
		Scanner sc= new Scanner(System.in);		
		System.out.println("Path Repo :");
		repository=sc.nextLine();		
		sc.close();		
		infoRepo();
		askRepo(bw,br);
		
		
		
		
		
		
	}
	
	
	public static void transfert(InputStream in, OutputStream out, boolean closeOnExit) throws IOException  {
  
        byte buf[] = new byte[1024];
        
        int n;
        while((n=in.read(buf))!=-1) {
        	out.write(buf,0,n);
        }
            
        
        if (closeOnExit){
            in.close();
            out.close();
        }
    }
 /* 
	private void push(File f)throws IOException {
	
	  
        Commun.transfert(
                new FileInputStream("D:\\test.jpg"),
                _socket.getOutputStream(),
                true);
        
        _socket.close();
	}

	private void pull() {
		
	}*/
}
