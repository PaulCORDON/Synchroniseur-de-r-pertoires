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
	ArrayList<utilisateur> comptes=new ArrayList<utilisateur>();

	String message = "";	
	String nom;
	String type;
	String derniereModif;
	Long lm;
	File racine;
	public ClientManager(Socket cl) {
		_socket=cl;		
	}
	
	public void run(){
		int compteur=1;
		byte[] data = new byte[1024];
		boolean enCour = true;
		File f= new File("H:\\Mes documents\\ProgReseauProjet\\racine");
		
		int taille;
		/*On remplit la liste de compte utilisateur*/
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
				racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				viderDossier(racine);
				
				racine.mkdirs();
				
				do
				{
					System.out.println("la");
					
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
									System.out.println("ici");
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
				
				racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				racine.mkdirs();
				
				do
				{
					System.out.println("la");
					
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
									System.out.println("ici");
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
				
				
				racine = new File("H:\\Mes documents\\ProgReseauProjet\\racine");
				racine.mkdirs();
				
				do
				{
					System.out.println("la");
					
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
									System.out.println("ici");
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


	private void viderDossier(File f2) {
		for (File f: f2.listFiles())
		{
			if(f.isDirectory()) viderDossier(f);
			f.delete();
		}	
	}

	
}