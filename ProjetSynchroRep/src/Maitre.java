import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Maitre extends Transferable implements Runnable{
	
	//----------------------//
	//Cr�ation des variables//
	//----------------------//
	
	static int compteur=1;
	String id;
	BufferedReader br;		//lecteur bufferis� pour la discussion avec le serveur 
	PrintWriter bw;			//lecteur bufferis� pour la discussion avec le serveur 
	OutputStream buffOut;
	InputStream buffInf;
	
	String message = "";	//contient le code de desciption du fichier en cours d'inspection qu'on parse pour obtenir :
	String nom;					// - le nom du fichier
	String type;				// - le type du fichier
	String derniereModif;		// - la date de derni�re modification du fichier
	Long lm;						//la date de derni�re modification sous forme de long int
	
	File racine;			//le fichier, avec son chemin, qui subit des modifications

	
	//------------//
	//Constructeur//
	//------------//
	
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
	
	//----------------------//
	//Description du thread //
	//----------------------//
	
	public void run(){
		
		byte[] data = new byte[1024];	//r�cup�re la donn�e qui est sous forme de byte (taille du fichier)
		boolean enCour = true;			//condition faisant tourner la boucle while
		int taille;						//la taille du fichier inspect�
		File f= new File("H:\\Mes documents\\ProgReseauProjet\\racine");
		
		f.mkdirs();						//cr�e le fichier f et tout les r�pertoires n�cessaire d'apr�s le chemin de f
		Scanner sc=new Scanner(System.in);	//sert � lire les entr�es du clavier
		
		System.out.println("Voulez-vous :"
				+ "\n1 : R�cuperer un fichier en mode supression "
				+ "\n2 : Recuperer un fichier en mode watchdog"
				+ "\n3 : Recuperer un fichier en mode ecrasement"
				+ "\n4 : Envoyer un fichier en mode supression"
				+ "\n5 : Envoyer un fichier en mode watchdog"
				+ "\n6 : Envoyer un fichier en mode ecrasement");
				//+ "\n7 : Afficher des informations sur le r�pertoire s�lectionn�"
				//+ "\n8 : S�lectionner le r�pertoire"
				//+ "\n9 : Afficher les informations de l'autre r�pertoire");

		int y=sc.nextInt();		//lecture de la r�ponse donn�e au clavier
		
		bw.print(y);			//envoi de cette r�ponse au client Manager
		bw.flush();	
		
		
		try {
			
		//------------------------------------//
		//Traitement suivant l'option choisie //
		//------------------------------------//
		switch (y) {
		// R�cuperer un fichier en mode supression
		case 1: 	
			Client.s.acquire();		//assure gr�ce � un s�maphore d'�tre le seul thread pouvant avoir acc�s aux fichiers du serveur
			racine =  new File("H:\\Mes documents\\ProgReseauProjet\\racine");
			
			viderDossier(racine);	//supression de ce qui existe d�j�
			
			racine.mkdirs();		//(re)cr�ation du chemin racine
		
			do
			{
				System.out.println("la");
				
				
					while(buffInf.available()<=0);	//on attend de recevoir un message
				
				
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
					//parsing du message qui contient les infos sur le fichier 
					nom=message.split("  ")[0];
					type=message.split("  ")[1];
					derniereModif = message.split("  ")[2];
					lm=Long.valueOf(derniereModif);
					
					if(type.equals("dir"))	//si le fichier inspect� est un r�pertoire, on cr�er un fichier identique 
					{
						f = new File(nom);
						f.mkdirs();
					}
					else					//si c'est un fichier
					{
						f = new File(nom);
						f.createNewFile();
						if(f.lastModified()==lm)	//si le fichier inspect� est pr�sent
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

		//Recuperer un fichier en mode watchdog	
		case 2:		
			Client.s.acquire();//assure gr�ce � un s�maphore d'�tre le seul thread pouvant avoir acc�s aux fichiers du serveur
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
			
		//Recuperer un fichier en mode ecrasement
		case 3:		
			Client.s.acquire();	//assure gr�ce � un s�maphore d'�tre le seul thread pouvant avoir acc�s aux fichiers du serveur
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
		
		//Envoi de fichier en mode suppression
		case 4:
			try {
				Client.semaphoreBloquantLesNouveauxArrivant.acquire();
				Client.s.acquire();	//assure gr�ce � un s�maphore d'�tre le seul thread pouvant avoir acc�s aux fichiers du serveur
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
		
		//Envoi de fichier en mode watchdog
		case 5:
			try {
				Client.semaphoreBloquantLesNouveauxArrivant.acquire();
				Client.s.acquire();	//assure gr�ce � un s�maphore d'�tre le seul thread pouvant avoir acc�s aux fichiers du serveur
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
			
		//Envoi de fichier en mode �crasement
		case 6:
			try {
				Thread.sleep(2000);
				System.out.println("j'attend");
				Thread.sleep(2000);
				Client.semaphoreBloquantLesNouveauxArrivant.acquire();
				System.out.println("je rattend");
				Client.s.acquire();	//assure gr�ce � un s�maphore d'�tre le seul thread pouvant avoir acc�s aux fichiers du serveur
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
		/*	
		case 7 :	//Afficher des informations sur le r�pertoire s�lectionn�
			infoRepo();
			System.out.println("coucou");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
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
			*/
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
