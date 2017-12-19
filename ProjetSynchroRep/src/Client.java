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
		
		//new Client("50").infoRepo();
		
		Scanner sc=new Scanner(System.in);
		System.out.println("Quel est votre ID?");
		
		Client cl= new Client(sc.nextLine());
		
		InputStream input   = null;
		OutputStream output=null;
	
		try
		{
			_port   = (args.length == 1) ? Integer.parseInt(args[0]) : 8099;
			_socket = new Socket((String) null, _port);
			
			// Open stream
			input = _socket.getInputStream();
			output=_socket.getOutputStream();
			PrintWriter bw=new PrintWriter(new OutputStreamWriter(output));
			
			bw.print(cl.id);
			bw.flush();
			
			
					
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
	
	
}
