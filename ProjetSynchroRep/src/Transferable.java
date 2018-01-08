import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Transferable {
	
	protected String repository;
	
	//affiche les infos sur son repertoire
	public String infoRepo(){
		String line= null;
		String retour = null;
		try{
			//Path chemin = Paths.get(repository);
			
			Path roots = FileSystems.getDefault().getPath(repository);
			//Maintenant, il ne nous reste plus qu'a parcourir
			
			  System.out.println(roots);
			  //Pour lister un repertoire, il faut utiliser l'objet DirectoryStream
			  //L'objet Files permet de creer ce type d'objet afin de pouvoir l'utiliser
			  try(DirectoryStream<Path> listing = Files.newDirectoryStream(roots)){
			    int i = 0;
			    for(Path nom : listing){
			    	if(Files.size(nom)>0){
			    		line = "\t\t" + (Files.isDirectory(nom)) != null ? nom+"/" : nom + " ["+Files.size(nom)+" octets] ,last modification : "+Files.getLastModifiedTime(nom)+"\n";
			    		retour += line;
					    System.out.print(" ,last modification : "+Files.getLastModifiedTime(nom)+"\n");
					    i++;
					    if(i%4 == 0)System.out.println("\n");
			    	}
			    }
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			return retour;
		}
	}
	
	//envoie les infos sur son repertoire a son processus interlocuteur
	public String infoRepo(PrintWriter pw) {
		String retour = null;
		String line=null;
		try{
			//Path chemin = Paths.get(repository);
			
			Path roots = FileSystems.getDefault().getPath(repository);
			//Maintenant, il ne nous reste plus qu'a parcourir
			
			  pw.println(roots);
			  //Pour lister un repertoire, il faut utiliser l'objet DirectoryStream
			  //L'objet Files permet de creer ce type d'objet afin de pouvoir l'utiliser
			  try(DirectoryStream<Path> listing = Files.newDirectoryStream(roots)){
			    int i = 0;
			    for(Path nom : listing){
			    	if(Files.size(nom)>0){
			    		line = "\t\t" + (Files.isDirectory(nom)) != null ? nom+"/" : nom + " ["+Files.size(nom)+" octets] ,last modification : "+Files.getLastModifiedTime(nom)+"\n";
			    		retour += line;
					    pw.print(line);
					    i++;
					    if(i%4 == 0)System.out.println("\n");
			    	}
			    }
			    pw.println("end");
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			return retour;
		}
	}
	
	//askRepo demande au processus avec lequel il discute les informations sur son repertoire
	public String askRepo(PrintWriter pw, BufferedReader br) {		
		pw.print("askRepo");
		String total = null;
		String line = "";
		while(line != "end") {
			try {
				line = br.readLine();
				total += line; 
				System.out.println(line);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return total;
	}
	
	public void push(OutputStream os) {
		//transfert(new FileInputStream(repository))
	}
	
	 public void transfert(InputStream in, OutputStream out, boolean closeOnExit) throws IOException
	    {
	        byte buf[] = new byte[1024];
	        
	        int n;
	        while((n=in.read(buf))!=-1)
	            out.write(buf,0,n);
	        
	        if (closeOnExit)
	        {
	            in.close();
	            out.close();
	        }
	    }
	
	//attend des commandes de la part du processus avec qui il discute
	public void listen(PrintWriter pw, BufferedReader br) throws InterruptedException {
		String cmd = null;
		while(cmd != "STOP") {
			try {
				System.out.println("coucou1");
				if(br.ready()) {
					cmd = br.readLine();
								
					System.out.println("coucou2");
					switch(cmd) {
						case "askRepo" : {
							infoRepo(pw);
							break;
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public boolean send(PrintWriter pw, BufferedReader br, int Mode) {		//permet de mettre à jour le dossier de l'interracteur (l'autre processus avec lequel celui ci discute)
		String selfInfo, otherInfo;
		selfInfo = infoRepo();
		otherInfo = askRepo(pw,br);
		
		return true;
	}
	
	public boolean setRepo() {
		System.out.println("Sélection du répertoire");
        JFileChooser jfc = new JFileChooser();
        System.out.print("TEST PASSAGE");
        jfc.setCurrentDirectory(new File(repository));
        int result = jfc.showOpenDialog(new JFrame());
        if(result == JFileChooser.APPROVE_OPTION) {
        	File selectedFile = jfc.getSelectedFile();
        	repository = selectedFile.getAbsolutePath(); 
        	return true;
        }
		return false;
	}
}
