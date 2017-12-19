import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


public class Transferable {
	
	protected String repository;
	
	//affiche les infos sur son répertoire
	public void infoRepo(){
		try{
			//Path chemin = Paths.get(repository);
			
			Path roots = FileSystems.getDefault().getPath(repository);
			//Maintenant, il ne nous reste plus qu'à parcourir
			
			  System.out.println(roots);
			  //Pour lister un répertoire, il faut utiliser l'objet DirectoryStream
			  //L'objet Files permet de créer ce type d'objet afin de pouvoir l'utiliser
			  try(DirectoryStream<Path> listing = Files.newDirectoryStream(roots)){
			    int i = 0;
			    for(Path nom : listing){
			    	if(Files.size(nom)>0){
					    System.out.print("\t\t" + ((Files.isDirectory(nom)) ? nom+"/" : nom));
					    System.out.println(" ["+Files.size(nom)+" octets]");
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
		}
	}
	
	//envoie les infos sur son répertoire à son processus interlocuteur
	public void infoRepo(PrintWriter pw) {						
		try{
			//Path chemin = Paths.get(repository);
			
			Path roots = FileSystems.getDefault().getPath(repository);
			//Maintenant, il ne nous reste plus qu'à parcourir
			
			  pw.println(roots);
			  //Pour lister un répertoire, il faut utiliser l'objet DirectoryStream
			  //L'objet Files permet de créer ce type d'objet afin de pouvoir l'utiliser
			  try(DirectoryStream<Path> listing = Files.newDirectoryStream(roots)){
			    int i = 0;
			    for(Path nom : listing){
			    	if(Files.size(nom)>0){
					    pw.print("\t\t" + ((Files.isDirectory(nom)) ? nom+"/" : nom));
					    pw.println(" ["+Files.size(nom)+" octets]");
					    pw.print(" ,last modification : "+Files.getLastModifiedTime(nom)+"\n");
					    i++;
					    if(i%4 == 0)System.out.println("\n");
			    	}
			    }
			    System.out.println("end");
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//askRepo demande au processus avec lequel il discute les informations sur son répertoire
	public void askRepo(PrintWriter pw, BufferedReader br) {		
		pw.print("askRepo");
		String line = "";
		while(line != "end") {
			try {
				line = br.readLine();
				System.out.println(line);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	//attend des commandes de la part du processus avec qui il discute
	public void listen(PrintWriter pw, BufferedReader br) {			
		String cmd = null;
		while(cmd != "STOP") {
			try {
				cmd = br.readLine();
			
				switch(cmd) {
				case "askRepo" : {
					infoRepo(pw);
				}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
