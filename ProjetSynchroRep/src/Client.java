import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private String id;
	private static int    _port;
	private static Socket _socket;
	
	Client(String st){
		id=st;
	}
	
	
	
	public static void main(String[] args) throws InterruptedException{	
		
		Scanner sc=new Scanner(System.in);
		System.out.println("Quel est votre ID?");
		
		Client cl= new Client(sc.nextLine());
		
		InputStream input   = null;
		OutputStream output=null;
	
		try
		{
			_port   = (args.length == 1) ? Integer.parseInt(args[0]) : 8080;
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
				System.out.println("D�conection du serveur");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}