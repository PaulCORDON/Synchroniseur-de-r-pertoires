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

public class ClientManager extends Thread {
	Socket client;
	ArrayList<utilisateur> comptes=new ArrayList<utilisateur>();
	
	

	public ClientManager(Socket cl) {
		client=cl;		
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

		/*on recupere le pseudo de l'utilisateur*/
		String lu=null;
		String type="inconnu";
		try{
			System.out.println("test");
			/*on recupere le pseudo de l'utilisateur*/
			InputStream input=client.getInputStream();
			lu = new BufferedReader(new InputStreamReader(input)).readLine();
			System.out.println(lu);
			for(utilisateur u:comptes){
				if(u.id.equals(lu)){
					type=u.type;
				}
			}
			
			
			OutputStream output = client.getOutputStream();	
			PrintWriter rep =new PrintWriter(new OutputStreamWriter(output));			
			rep.println(type);
			rep.flush();
			System.out.println(type);
			
			
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		
	}
}
