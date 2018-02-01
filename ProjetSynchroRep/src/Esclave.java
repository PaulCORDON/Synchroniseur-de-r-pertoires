import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Esclave extends Transferable implements Runnable{
	String id;
	BufferedReader br;
	PrintWriter bw;
	Socket _socket;
	OutputStream buffOut;
	InputStream buffInf;
	String message = "";	
	String nom;
	String type;
	String derniereModif;
	Long lm;
	File racine;

	public Esclave(String st,BufferedReader r,PrintWriter w,Socket s) {		
		id=st;
		br=r;
		bw=w;
		_socket=s;
		try {
			buffOut= _socket.getOutputStream();
			buffInf= _socket.getInputStream();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	public void run(){
		byte[] data = new byte[1024];
		boolean enCour = true;
		int taille;
		File f= new File("H:\\Mes documents\\ProgReseauProjet\\racine");
		f.mkdirs();
		Scanner sc= new Scanner(System.in);		
		System.out.println("Voulez-vous :\n1 : Récuperer un fichier en mode supression \n2 : Recuperer un fichier en mode watchdog\n3 : Recuperer un fichier en mode ecrasement");
		
		int y=sc.nextInt();
		try {
		switch (y) {
		case 1:
			Client.s.acquire();
			Thread.sleep(10000);
			bw.print(y);
			bw.flush();
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
		case 2:
			Client.s.acquire();
			bw.print(y);
			bw.flush();
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
		case 3:
			Client.s.acquire();
			bw.print(y);
			bw.flush();
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
		default:
			break;
		}	
		sc.close();
		
			_socket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
