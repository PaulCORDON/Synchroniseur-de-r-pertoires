import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Transferable {
	
	protected String repository;
	
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
