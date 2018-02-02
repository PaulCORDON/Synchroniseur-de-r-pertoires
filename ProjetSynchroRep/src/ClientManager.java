import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ClientManager extends Transferable implements Runnable {
	/*
	 * liste de tous les utilisateur
	 */
	ArrayList<utilisateur> comptes=new ArrayList<utilisateur>();

	/*
	 * string permetant de récupérer les messages que nous envera l'interlocuteur
	 */
	String message = "";	
	
	/*
	 * string permetant de récupérer le nom des fichiers que nous envera l'interlocuteur
	 */
	String nom;
	
	/*
	 * string qui contiendra le type de client qu'est l'interlocuteur (esclave,maitre,inconnu)
	 */
	String type;
	
	/*
	 * string qui contiendra la date de dernière modification d'un fichier d'un client  
	 */
	String derniereModif;
	/*
	 * long  qui contiendra la date de dernière modification d'un fichier d'un client
	 */
	Long lm;
	
	/*
	 * répertoire racine
	 */
	File racine;
	
	/**
	 * Constructeur du client manager
	 * @param cl socket client
	 */
	public ClientManager(Socket cl) {
		_socket=cl;		
	}
	
	public void run(){
		/*
		 * compteur de profondeur de récursivité  
		 */
		int compteur=1;
		
		byte[] data = new byte[1024];
		
		/*
		 * booléen qui permet de savoir si l'envoi est terminé vrai = en cour d'envoi faux = fin d'envoi
		 */
		boolean enCour = true;
		
		/*
		 * chemin du répertoire racine
		 */
		File f= new File("H:\\Mes documents\\ProgReseauProjet\\racine");
		
		int taille;
		/*On remplit la liste d'utilisateur de compte utilisateur*/
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document= builder.parse(new File("ID.xml"));
			final Element racine = document.getDocumentElement();
			final NodeList utilisateurs = racine.getChildNodes();
			final int nbUtilisateur = utilisateurs.getLength();
			for (int i = 0; i<nbUtilisateur; i++) {
				 if(utilisateurs.item(i).getNodeType() == Node.ELEMENT_NODE) {
					 final Element personne = (Element) utilisateurs.item(i);
					 utilisateur u=new utilisateur(personne.getAttribute("pseudo"),personne.getAttribute("type"));
					 comptes.add(u);
				 }
			}
		}

		catch (final ParserConfigurationException e) {

			e.printStackTrace();

		}
		catch (final SAXException e) {

		    e.printStackTrace();

		}

		catch (final IOException e) {

		    e.printStackTrace();

		}

		/*On recupere le pseudo de l'utilisateur*/
		int choix;
		String lu=null;
		String type="inconnu";
		try{
			
			/*On recupere le pseudo de l'utilisateur*/
			InputStream input=_socket.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(input));
			lu=br.readLine();
			System.out.println(lu);
			for(utilisateur u:comptes){
				if(u.id.equals(lu)){
					type=u.type;
				}
			}
			/*On lui retourne maitre esclave ou inconnue*/
			OutputStream output = _socket.getOutputStream();	
			PrintWriter rep =new PrintWriter(new OutputStreamWriter(output));			
			rep.println(type);
			rep.flush();
			
			/*
			 * on récupère le choix du client.
			 */
			choix=br.read()-48;
				
			switch (choix) {
			case 1:
				envoi(f,output,input,compteur);
				break;
				
			case 2:
				envoi(f,output,input,compteur);
				break;
			case 3:
				envoi(f,output,input,compteur);
				break;
			case 4:
				/*
				 * récuperation des données en mode suppression dans racine
				 */
				racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				
				/*
				 * on vide le dossier racine
				 */
				viderDossier(racine);
				
				racine.mkdirs();
				/*
				 * on remplit le dossier racine avec ce qu'il y a dans le dossier racine du maitre
				 */
				do
				{
					while(input.available()<=0);
					taille=input.read(data);
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
								output.write("OK".getBytes());
								output.flush();
							}
							else
							{
								System.out.println("PASOK");
								output.write("PASOK".getBytes());
								output.flush();
								
								FileOutputStream fos= new FileOutputStream(f);
								do
								{
									while(input.available()<=0);
									taille=input.read(data);
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
				
				break;
			case 5:
				/*
				 * Récupération du contenu du maitre en mode watchdog
				 */
				
				
				/*
				 * On se place dans le répertoire racine
				 */
				racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				racine.mkdirs();
				
				do
				{
					while(input.available()<=0);
					taille=input.read(data);
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
								output.write("OK".getBytes());
								output.flush();
							}
							else
							{
								System.out.println("PASOK");
								output.write("PASOK".getBytes());
								output.flush();
								
								FileOutputStream fos= new FileOutputStream(f);
								do
								{
									while(input.available()<=0);
									taille=input.read(data);
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

				break;
			
			case 6:
				/*
				 * Récupération du contenue du répertoire du maitre en mode écrasement
				 */
				
				racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				racine.mkdirs();
				
				do
				{					
					while(input.available()<=0);
					taille=input.read(data);
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
								output.write("OK".getBytes());
								output.flush();
							}
							else
							{
								System.out.println("PASOK");
								output.write("PASOK".getBytes());
								output.flush();
								
								FileOutputStream fos= new FileOutputStream(f);
								do
								{
									while(input.available()<=0);
									taille=input.read(data);
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
				
				break;
			case 7:
				
				break;
			case 8:
				
				break;
			case 9:
				
				break;
			
			default:
				System.out.println("choix client : " + choix);
				break;
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		
	}

/**
 * méthode servant à vider le répertoir en paramètre
 * @param f2 le répertoire à vider
 */
	private void viderDossier(File f2) {
		for (File f: f2.listFiles())
		{
			if(f.isDirectory()) viderDossier(f);
			f.delete();
		}	
	}

	
}