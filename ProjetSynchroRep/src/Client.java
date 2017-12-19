import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.*;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

	private String id;
	private static int    _port;
	private static Socket _socket;
	private String repository = "H:/Mes documents/4A";
	
	
	Client(String st){
		id=st;
	}
	
	
	
	public static void main(String[] args) throws InterruptedException{	
		
		//new Client("50").recep(0);
		
		Scanner sc=new Scanner(System.in);
		System.out.println("Quel est votre ID?");
		
		Client cl= new Client(sc.nextLine());
		
		InputStream input  =null;
		OutputStream output=null;
		String lu="";
		try
		{
			_port   = (args.length == 1) ? Integer.parseInt(args[0]) : 8099;
			_socket = new Socket((String) null, _port);
			
			// Open stream
			input = _socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			output=_socket.getOutputStream();
			PrintWriter bw=new PrintWriter(new OutputStreamWriter(output));
			
			bw.println(cl.id);
			bw.flush();
			Thread.sleep(1000);
			lu=br.readLine();
			System.out.println(lu);
			if(lu.equals("maitre")) {
				Maitre maitre=new Maitre(cl.id);
				maitre.start();
			}
			else if(lu.equals("esclave")) {
				Esclave esclave=new Esclave(cl.id);
				esclave.start();
			}
			else{
				System.out.println("Vous n'etes pas client");
			}
					
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sc.close();
				input.close();
				_socket.close();
				System.out.println("Déconection du serveur");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void recep(int mode){
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
}
