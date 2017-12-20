import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Esclave extends Transferable implements Runnable{
	String id;
	BufferedReader br;
	PrintWriter bw;
	Socket _socket;
	public Esclave(String st,BufferedReader r,PrintWriter w,Socket s) {		
		id=st;
		br=r;
		bw=w;
		_socket=s;
	}
	public void run(){
		Scanner sc= new Scanner(System.in);		
		System.out.println("Voulez-vous :\n1 : Récupere un fichier en mode supression \n2 : Recuperer un fichier en mode watchdog\n3 : Recuperer un fichier en mode ecrasemen");
		switch (sc.nextInt()) {
		case 1:
			bw.println("1");
			bw.flush();
			/*try {
				
				
				push(fenv);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			break;
		case 2:
			bw.println("2");
			bw.flush();
			/*try {
				
				pull(frec);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			*/
			break;		
		case 3:
			bw.println("3");
			bw.flush();
			break;	
		default:
			break;
		}	
		sc.close();

	}
}
