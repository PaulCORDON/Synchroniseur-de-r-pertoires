
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;



public class Transferable {
	
	protected String repository;
	Socket _socket;
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
			    
			  //attrapage d'exception
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
			  
			//attrapage d'exception
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			return retour;
		}
	}

	
	public boolean setRepo(Scanner sc) {
		System.out.println("Sélection du répertoire");
        try {
        	repository = sc.nextLine()+sc.nextLine();
        	FileSystems.getDefault().getPath(repository);
        	return true;
        	
        	//attrapage d'exception
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }
	}
	public static void envoi(File f, OutputStream out, InputStream in,int compte) throws IOException {
		int compteurcourrant = compte++;
		
		int taille;
		String message;
		byte[] data = new byte[1024];
		FileInputStream fis;

		File[] list = f.listFiles();
		
		if(list.length>0) 
		{
			for(int i=0; i<list.length; i++) 
			{
				if(list[i].isDirectory())
				{
					message = list[i].getAbsolutePath() + "  dir  " + list[i].lastModified();
					out.write(message.getBytes());
					out.flush();
					
					envoi(list[i], out,in,compte);
				}
				else 
				{
					fis= new FileInputStream(list[i]);
					
					message = list[i].getAbsolutePath() + "  file  " + list[i].lastModified();
					out.write(message.getBytes());
					out.flush();
					
					while(in.available()<=0);
					taille=in.read(data);
					message = "";
					for (int j = 0 ;j<taille;j++)
					{
						message += (char)data[j];
					}
					System.out.println(message);
					
					if(message.equals("PASOK"))
					{	
						while(fis.available()>0) 
						{
							taille=fis.read(data);
							out.write(data,0,taille);
							out.flush();
						}
						
						message = "null";
						
						try {
							Thread.sleep(500);
							
							//attrapage d'exception
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
						out.write(message.getBytes());
						out.flush();
						
						try {
							Thread.sleep(500);
							
							//attrapage d'exception
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					fis.close();
				}			
			}
		}
		System.out.println("fin de l'envoi " + compteurcourrant);
		if(compteurcourrant!=1) 
		{
			message = "null";
			out.write(message.getBytes());
			out.flush();
			try {
				Thread.sleep(500);
				
				//attrapage d'exception
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		else 
		{
			System.out.println("finRacine"+compteurcourrant);
			message = "finRacine"+compteurcourrant;
			out.write(message.getBytes());
			out.flush();
			in.close();
			out.close();
		}
}
	
}
