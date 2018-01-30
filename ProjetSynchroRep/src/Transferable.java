import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

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
			    		line = "\t\t" + nom+ "/" + " ["+Files.size(nom)+" octets] ,last modification : "+Files.getLastModifiedTime(nom)+"\n";
			    		retour += line;
					    System.out.print(line);
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
	/*public String infoRepo(PrintWriter pw) {
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
			    		line = "\t\t" + (Files.isDirectory(nom)) != null ? nom+"/" : nom + " "+Files.size(nom)+"octets "+Files.getLastModifiedTime(nom)+"\n";
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
	}*/
	
	public void envoi(int mode, String file, PrintWriter out, BufferedReader in) {
		String message;
		File f = new File(file);
		for(File subFile : f.listFiles()) {
				if(subFile.isDirectory()) {
					out.println(subFile.getAbsolutePath() + "  dir  " + subFile.lastModified());
					out.flush();
					envoi(mode, subFile.getAbsolutePath(), out, in);
				}else {
					out.println(subFile.getAbsolutePath() + "  file  " + subFile.lastModified());
					out.flush();
					
					try {
						message = in.readLine();
						if(message.equals("PASOK")) {
							BufferedReader br = new BufferedReader(new FileReader(subFile));
							//transfert(in,out, true);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
				}
		}
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
	
	public boolean send(PrintWriter pw, BufferedReader br, int Mode) {		//permet de mettre à jour le dossier de l'interracteur (l'autre processus avec lequel celui ci discute)
		String selfInfo, otherInfo;
		selfInfo = infoRepo();
		otherInfo = null;
		if(selfInfo.equals(otherInfo)) {
			return true;
		}else {
			boolean finish = false;
			while(finish = false) {
				
			}
		}
		return true;
	}
	
	public boolean setRepo(Scanner sc) {
		System.out.println("Sélection du répertoire");
        try {
        	repository = sc.nextLine()+sc.nextLine();
        	FileSystems.getDefault().getPath(repository);
        	return true;
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }
	}
}
