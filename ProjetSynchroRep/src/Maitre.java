import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Maitre  extends Transferable implements Runnable{
	static int compteur=1;
	String id;
	BufferedReader br;
	PrintWriter bw;
	OutputStream buffOut;
	InputStream buffInf;
	String message = "";	
	String nom;
	String type;
	String derniereModif;
	Long lm;
	File racine;

	Maitre(String st,BufferedReader r,PrintWriter w,Socket s) {		
		id=st;
		br=r;
		bw=w;
		_socket=s;
		try {
			buffOut= _socket.getOutputStream();
			buffInf= _socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run(){
		byte[] data = new byte[1024];
		boolean enCour = true;
		int taille;
		File f= new File("H:\\Mes documents\\ProgReseauProjet\\racine");
		f.mkdirs();
		Scanner sc=new Scanner(System.in);
		
		System.out.println("Voulez-vous :"
				+ "\n1 : Récuperer un fichier en mode supression "
				+ "\n2 : Recuperer un fichier en mode watchdog"
				+ "\n3 : Recuperer un fichier en mode ecrasement"
				+ "\n4 : Envoyer un fichier en mode supression"
				+ "\n5 : Envoyer un fichier en mode watchdog"
				+ "\n6 : Envoyer un fichier en mode ecrasement"
				+ "\n7 : Afficher des informations sur le répertoire sélectionné"
				+ "\n8 : Sélectionner le répertoire"
				+ "\n9 : Afficher les informations de l'autre répertoire");

		int y=sc.nextInt();
		bw.print(y);
		bw.flush();	
		try {
		switch (y) {
		case 1: 	// Récuperer un fichier en mode supression
			Client.s.acquire();
			racine =  new File("H:\\Mes documents\\ProgReseauProjet\\racine");
			viderDossier(racine);
			
			racine.mkdirs();
		
			do
			{
				System.out.println("la");
				
				
					while(buffInf.available()<=0);
				 
				taille=buffInf.read(data);
				message = "";
				for (int i = 0;i<taille;i++)
				{
					message += (char)data[i];
				}
				System.out.println(message);
				
				if(message.equals("finRacine1"))
					enCour = false;
				
				else if (!message.equals("null"))
				{	
					nom=message.split("  ")[0];
					type=message.split("  ")[1];
					derniereModif = message.split("  ")[2];
					lm=Long.valueOf(derniereModif);
					
					if(type.equals("dir"))
					{
						f = new File(nom);
						f.mkdirs();
					}
					else
					{
						f = new File(nom);
						f.createNewFile();
						if(f.lastModified()==lm)
						{
							System.out.println("OK");
							buffOut.write("OK".getBytes());
							buffOut.flush();
						}
						else
						{
							System.out.println("PASOK");
							buffOut.write("PASOK".getBytes());
							buffOut.flush();
							
							FileOutputStream fos= new FileOutputStream(f);
							do
							{
								System.out.println("ici");
								while(buffInf.available()<=0);
								taille=buffInf.read(data);
								message = "";
								for (int i = 0;i<taille;i++)
								{
									message += (char)data[i];
								}
								System.out.println(message);
								
								if(!message.equals("null"))
								{
										fos.write(data,0,taille);
										fos.flush();
								}
							}while(!message.equals("null"));
							fos.close();
							
							f.setLastModified(lm);
						}
					}
				}
				System.out.println(enCour);
				
			}while(enCour);
			Client.s.release();
			break;

			
		case 2:		//Recuperer un fichier en mode watchdog
			Client.s.acquire();
			racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
			racine.mkdirs();
			
			do
			{
				System.out.println("la");
				
				while(buffInf.available()<=0);
				taille=buffInf.read(data);
				message = "";
				for (int i = 0;i<taille;i++)
				{
					message += (char)data[i];
				}
				System.out.println(message);
				
				if(message.equals("finRacine1"))
					enCour = false;
				
				else if (!message.equals("null"))
				{	
					nom=message.split("  ")[0];
					type=message.split("  ")[1];
					derniereModif = message.split("  ")[2];
					lm=Long.valueOf(derniereModif);
					
					if(type.equals("dir"))
					{
						f = new File(nom);
						f.mkdirs();
					}
					else
					{
						f = new File(nom);
						f.createNewFile();
						if(f.lastModified()>=lm)
						{
							System.out.println("OK");
							buffOut.write("OK".getBytes());
							buffOut.flush();
						}
						else
						{
							System.out.println("PASOK");
							buffOut.write("PASOK".getBytes());
							buffOut.flush();
							
							FileOutputStream fos= new FileOutputStream(f);
							do
							{
								System.out.println("ici");
								while(buffInf.available()<=0);
								taille=buffInf.read(data);
								message = "";
								for (int i = 0;i<taille;i++)
								{
									message += (char)data[i];
								}
								System.out.println(message);
								
								if(!message.equals("null"))
								{
										fos.write(data,0,taille);
										fos.flush();
								}
							}while(!message.equals("null"));
							fos.close();
							
							f.setLastModified(lm);
						}
					}
				}
				System.out.println(enCour);
				
			}while(enCour);
			Client.s.release();
			break;		
		case 3:		//Recuperer un fichier en mode ecrasement
			Client.s.acquire();
			racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
			racine.mkdirs();
			
			do
			{
				System.out.println("la");
				
				while(buffInf.available()<=0);
				taille=buffInf.read(data);
				message = "";
				for (int i = 0;i<taille;i++)
				{
					message += (char)data[i];
				}
				System.out.println(message);
				
				if(message.equals("finRacine1"))
					enCour = false;
				
				else if (!message.equals("null"))
				{	
					nom=message.split("  ")[0];
					type=message.split("  ")[1];
					derniereModif = message.split("  ")[2];
					lm=Long.valueOf(derniereModif);
					
					if(type.equals("dir"))
					{
						f = new File(nom);
						f.mkdirs();
					}
					else
					{
						f = new File(nom);
						f.createNewFile();
						if(f.lastModified()==lm)
						{
							System.out.println("OK");
							buffOut.write("OK".getBytes());
							buffOut.flush();
						}
						else
						{
							System.out.println("PASOK");
							buffOut.write("PASOK".getBytes());
							buffOut.flush();
							
							FileOutputStream fos= new FileOutputStream(f);
							do
							{
								System.out.println("ici");
								while(buffInf.available()<=0);
								taille=buffInf.read(data);
								message = "";
								for (int i = 0;i<taille;i++)
								{
									message += (char)data[i];
								}
								System.out.println(message);
								
								if(!message.equals("null"))
								{
										fos.write(data,0,taille);
										fos.flush();
								}
							}while(!message.equals("null"));
							fos.close();
							
							f.setLastModified(lm);
						}
					}
				}
				System.out.println(enCour);
				
			}while(enCour);
			Client.s.release();
			break;
			
		case 4:
			try {
				Client.semaphoreBloquantLesNouveauxArrivant.acquire();
				Client.s.acquire();
				envoi(f,buffOut,buffInf,compteur);
				Client.s.release();
				Client.semaphoreBloquantLesNouveauxArrivant.release();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;	
			
		case 5:
			try {
				Client.semaphoreBloquantLesNouveauxArrivant.acquire();
				Client.s.acquire();
				envoi(f,buffOut,buffInf,compteur);
				Client.s.release();
				Client.semaphoreBloquantLesNouveauxArrivant.release();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;	
			
		case 6:
			try {
				Thread.sleep(2000);
				System.out.println("j'attend");
				Thread.sleep(2000);
				Client.semaphoreBloquantLesNouveauxArrivant.acquire();
				System.out.println("je rattend");
				Client.s.acquire();
				envoi(f,buffOut,buffInf,compteur);				
				Client.s.release();
				System.out.println("j'attend plus");
				Client.semaphoreBloquantLesNouveauxArrivant.release();
				System.out.println("je rattend plus");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;	
			
		case 7 :	//Afficher des informations sur le répertoire sélectionné
			infoRepo();
			System.out.println("coucou");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;
			
		case 8 :	//Sélectionner le répertoire
			setRepo(sc);
			System.out.println(repository);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
			
		case 9 :	//Afficher les informations de l'autre répertoire
			
			break;
			
		default:
			System.out.println(y);
			break;
		}	
		
		sc.close();	
		
			_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	}
	private void viderDossier(File f2) {
		for (File f: f2.listFiles())
		{
			if(f.isDirectory()) viderDossier(f);
			f.delete();
		}	
	}
	
}
