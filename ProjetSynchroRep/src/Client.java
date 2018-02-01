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
import java.util.concurrent.Semaphore;

public class Client {

	private String id;
	private static int    _port;
	private static Socket _socket;
	Client(String st){
		id=st;
	}
	static Semaphore s = new Semaphore(1);
	static Semaphore semaphoreBloquantLesNouveauxArrivant= new Semaphore(1);
	
	public static void main(String[] args) throws InterruptedException{	
		
		
		
		Scanner sc=new Scanner(System.in);
		System.out.println("Quel est votre ID?");
		
		Client cl= new Client(sc.nextLine());
		
		InputStream input  =null;
		OutputStream output=null;
		String lu="";
		try
		{
			_port   = (args.length == 1) ? Integer.parseInt(args[0]) : 8099;
			_socket = new Socket("172.18.50.114", _port);
			
			// Open stream
			input = _socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			output=_socket.getOutputStream();
			PrintWriter bw=new PrintWriter(new OutputStreamWriter(output));
			
			bw.println(cl.id);
			bw.flush();
			Thread.sleep(500);
			lu=br.readLine();
			System.out.println(lu);
			semaphoreBloquantLesNouveauxArrivant.acquire();
			semaphoreBloquantLesNouveauxArrivant.release();
			if(lu.equals("maitre")) {
				Maitre maitre=new Maitre(cl.id,br,bw,_socket,s,semaphoreBloquantLesNouveauxArrivant);
				Thread thread = new Thread(maitre);
				
				thread.start();
			}
			else if(lu.equals("esclave")) {
				Esclave esclave=new Esclave(cl.id,br,bw,_socket,s);
				Thread thread = new Thread(esclave);
				thread.start();
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

	}
	
	
}
