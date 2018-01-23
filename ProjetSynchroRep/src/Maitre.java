import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Maitre  extends Transferable implements Runnable{
	static int compteur=1;
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
		File f= new File("H:\\Mes documents\\4A\\TestReseau\\Maitre");
		f.mkdirs();
		Scanner sc=new Scanner(System.in);
		
		System.out.println("Voulez-vous :\n1 : R�cuperer un fichier en mode supression \n2 : Recuperer un fichier en mode watchdog\n3 : Recuperer un fichier en mode ecrasement\n4 : Envoyer un fichier en mode supression\n5 : Envoyer un fichier en mode watchdog\n6 : Envoyer un fichier en mode ecrasement\n7 : Afficher des informations sur le r�pertoire s�lectionn�\n8 : S�lectionner le r�pertoire\n9 : Afficher les informations de l'autre r�pertoire");

		int i=sc.nextInt();
		bw.print(i);
		bw.flush();
		switch (i) {
		case 1: 	// R�cuperer un fichier en mode supression
			
			/*try {
				
				
				push(fenv);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			break;
		case 2:		//Recuperer un fichier en mode watchdog

			/*try {
				
				pull(f);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			*/
			break;		
		case 3:		//Recuperer un fichier en mode ecrasement

			break;
			
		case 4:
			try {
				envoi(f,bw,br);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;	
			
		case 5:
			try {
				envoi(f,bw,br);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;	
			
		case 6:
			try {
				envoi(f,bw,br);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;	
			
		case 7 :	//Afficher des informations sur le r�pertoire s�lectionn�
			infoRepo();
			break;
			
		case 8 :	//S�lectionner le r�pertoire
			setRepo(sc);
			System.out.println(repository);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
			
		case 9 :	//Afficher les informations de l'autre r�pertoire
			
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
	
	
	public static void envoi(File f, PrintWriter out, BufferedReader in) throws IOException 
	{
		int nbBouclesRecursives = compteur++;
		
		String rep;
		BufferedReader br;

		File[] list = f.listFiles();
		
		if(list.length>0) 
		{
			for(int i=0; i<list.length; i++) 
			{
				if(list[i].isDirectory())
				{
					out.println(list[i].getAbsolutePath() + "  dir  " + list[i].lastModified());
					out.flush();
					
					envoi(list[i], out,in);
				}
				else 
				{
					br= new BufferedReader(new FileReader(list[i]));
					
					out.println(list[i].getAbsolutePath() + "  file  " + list[i].lastModified());
					out.flush();
					
					rep = in.readLine();
					
					if(rep.equals("PASOK"))
					{	
						while((rep=br.readLine())!=null) 
						{
							out.println(rep);
							out.flush();
						}
				
						out.println("null");
						out.flush();
					}
					br.close();
				}			
			}
		}
		System.out.println("fin de l'envoi " + nbBouclesRecursives);
		if(nbBouclesRecursives!=1) 
		{
			out.println("null");
			out.flush();
			
		}
		else 
		{
			System.out.println("finRacine"+nbBouclesRecursives);
			out.println("finRacine"+nbBouclesRecursives);
			out.flush();
			in.close();
			out.close();
		}
}
 
	private void push(File f)throws IOException {	  
        transfert(new FileInputStream(f),_socket.getOutputStream(),true);
	}

	private void pull(File f)throws IOException {        
        transfert(_socket.getInputStream(),new FileOutputStream(f),true); 
	}
}
