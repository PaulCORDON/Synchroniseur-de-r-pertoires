import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
		File frec= new File("H:/Mes documents/4A/TestReseau/Serveur/Recu.odt");
		Scanner sc=new Scanner(System.in);
		
		System.out.println("Voulez-vous :\n1 : Envoyer un fichier\n2 : Recuperer un fichier\n3 : \n4 : \n");
		switch (sc.nextInt()) {
		case 1:
			bw.println("1");
			bw.flush();
			try {
				
				
				push(fenv);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case 2:
			bw.println("2");
			bw.flush();
			try {
				
				pull(frec);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
			break;		
			
		default:
			break;
		}	
		sc.close();	
	}
	
	

 
	private void push(File f)throws IOException {	  
        transfert(new FileInputStream(f),_socket.getOutputStream(),true);
	}

	private void pull(File f)throws IOException {        
        transfert(_socket.getInputStream(),new FileOutputStream(f),true); 
	}
}
