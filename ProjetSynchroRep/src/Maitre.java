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
		repository = "H:\\Mes documents\\4A\\TestReseau\\Client";
		File fenv= new File("H:/Mes documents/4A/TestReseau/Client/aEnvoyer.odt");
		File frec= new File("H:/Mes documents/4A/TestReseau/Serveur/Recu.odt");
		Scanner sc=new Scanner(System.in);
		
		System.out.println("Voulez-vous :\n1 : Récuperer un fichier en mode supression \n2 : Recuperer un fichier en mode watchdog\n3 : Recuperer un fichier en mode ecrasement\n4 : Envoyer un fichier en mode supression\n5 : Envoyer un fichier en mode watchdog\n6 : Envoyer un fichier en mode ecrasement\n7 : Afficher des informations sur le répertoire sélectionné\n8 : Sélectionner le répertoire\n9 : Afficher les informations de l'autre répertoire");

		int i=sc.nextInt();
		bw.print(i);
		bw.flush();
		switch (i) {
		case 1:
			
			/*try {
				
				
				push(fenv);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			break;
		case 2:

			/*try {
				
				pull(frec);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} */
			
			break;		
		case 3:

			break;
			
		case 4:

			break;	
			
		case 5:

			break;	
			
		case 6:

			break;	
			
		case 7 :
			infoRepo();
			break;
			
		case 8 :
			setRepo(sc);
			System.out.println(repository);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
			
		case 9 :
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
