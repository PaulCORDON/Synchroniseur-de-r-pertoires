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
		
		System.out.println("Voulez-vous :\n1 : Récuperer un fichier en mode supression \n2 : Recuperer un fichier en mode watchdog\n3 : Recuperer un fichier en mode ecrasement\n4 : Envoyer un fichier en mode supression\n5 : Envoyer un fichier en mode watchdog\n6 : Envoyer un fichier en mode ecrasement");

		int i=sc.nextInt();

		switch (i) {
		case 1:
			bw.print(i);
			bw.flush();
			/*try {
				
				
				push(fenv);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			break;
		case 2:
			bw.println(i);
			bw.flush();
			/*try {
				
				pull(frec);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} */
			
			break;		
		case 3:
			bw.println("3");
			bw.flush();
			break;
			
		case 4:
			bw.println("4");
			bw.flush();
			break;	
			
		case 5:
			bw.println("5");
			bw.flush();
			break;	
			
		case 6:
			bw.println("6");
			bw.flush();
			break;	
			
		default:
			System.out.println(i);
			break;
		}	
		sc.close();	
		try {
			_socket.close();
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
