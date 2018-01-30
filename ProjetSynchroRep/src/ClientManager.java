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
	Socket client;
	ArrayList<utilisateur> comptes=new ArrayList<utilisateur>();

	String message;	
	String nom;
	String type;
	String derniereModif;
	Long lm;

	public ClientManager(Socket cl) {
		client=cl;		
	}
	
	public void run(){
		File repert= new File("H:/Mes documents/4A/TestReseau/Serveur");
		
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
			InputStream input=client.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(input));
			lu=br.readLine();
			System.out.println(lu);
			for(utilisateur u:comptes){
				if(u.id.equals(lu)){
					type=u.type;
				}
			}
			/*On lui retourne maitre esclave ou inconnue*/
			OutputStream output = client.getOutputStream();	
			PrintWriter rep =new PrintWriter(new OutputStreamWriter(output));			
			rep.println(type);
			rep.flush();
			
			
			choix=br.read()-48;
				
			switch (choix) {
			case 1:
				pull(repert);
				System.out.println("reception");
				break;

			case 2:
				push(repert);
				System.out.println("envois");
				break;
			case 3:
				push(repert);
				System.out.println("envois");
				break;
			case 4:
				viderDossier(repert);
				
				repert.mkdirs();
				
				boolean enCour = true;
				do
				{
					System.out.println("la");
					message=br.readLine();
					System.out.println(message);
					if(message!=null && message.equals("finRacine1"))
						enCour = false;
					else if (message!=null && !message.equals("null"))
					{	
						nom=message.split("  ")[0];
						type=message.split("  ")[1];
						derniereModif = message.split("  ")[2];
						lm=Long.valueOf(derniereModif);
						
						if(type.equals("dir"))
						{
							File f = new File(nom);
							f.mkdir();
						}
						else
						{
							File f = new File(nom);
							f.createNewFile();
							if(f.lastModified()!=lm)
							{
								System.out.println("OK");
								rep.println("OK");
								rep.flush();
							}
							else
							{
								System.out.println("PASOK");
								rep.println("PASOK");
								rep.flush();
								pull(f);
								
								f.setLastModified(lm);
							}
						}
					}
					System.out.println(enCour);
					
				}while(enCour);
				
				break;
			case 5:
				push(repert);
				System.out.println("envois");
				break;
			case 6:
				push(repert);
				System.out.println("envois");
				break;
			case 7:
				push(repert);
				System.out.println("envois");
				break;
			case 8:
				push(repert);
				System.out.println("envois");
				break;
			case 9:
				push(repert);
				System.out.println("envois");
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

	private void pull(File f)throws IOException {        
		transfert(client.getInputStream(),new FileOutputStream(f),true); 
	}
	private void push(File f)throws IOException {	  
        transfert(new FileInputStream(f),client.getOutputStream(),true);
	}
}