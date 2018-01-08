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
	
	//affiche les infos sur son r�pertoire
	public void infoRepo(){
		try{
			//Path chemin = Paths.get(repository);
			
			Path roots = FileSystems.getDefault().getPath(repository);
			//Maintenant, il ne nous reste plus qu'� parcourir
			
			  System.out.println(roots);
			  //Pour lister un r�pertoire, il faut utiliser l'objet DirectoryStream
			  //L'objet Files permet de cr�er ce type d'objet afin de pouvoir l'utiliser
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
	
	//envoie les infos sur son r�pertoire � son processus interlocuteur
	public void infoRepo(PrintWriter pw) {						
		try{
			//Path chemin = Paths.get(repository);
			
			Path roots = FileSystems.getDefault().getPath(repository);
			//Maintenant, il ne nous reste plus qu'� parcourir
			
			  pw.println(roots);
			  //Pour lister un r�pertoire, il faut utiliser l'objet DirectoryStream
			  //L'objet Files permet de cr�er ce type d'objet afin de pouvoir l'utiliser
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
			    pw.println("end");
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//askRepo demande au processus avec lequel il discute les informations sur son r�pertoire
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
}
