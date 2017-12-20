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
	File frec= new File("H:/Mes documents/4A/TestReseau/Serveur/Recu.odt");
	

	public ClientManager(Socket cl) {
		client=cl;	
		repository="H:/";	
	}
	
	public void run(){
		
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
			System.out.println(type);	
			pull(frec);
			
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		
	}


	private void pull(File f)throws IOException {        
		transfert(client.getInputStream(),new FileOutputStream(f),true); 
	}
}