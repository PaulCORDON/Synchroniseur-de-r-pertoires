import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Serveur{

	private static int           _port;
	private static ServerSocket  _socket;
	

	public static void main(String[] args)
	{
		
		try
		{
			_port   = (args.length == 1) ? Integer.parseInt(args[0]) : 8099;
			_socket = new ServerSocket(_port);

			System.out.println("TCP server is running on " + _port + "...");

			while (true)
			{
				// Accept new TCP client
				Socket client       = _socket.accept();
				ClientManager th= new ClientManager(client);
				Thread thread = new Thread(th);
				System.out.println("New client, address " + client.getInetAddress() + " on " + client.getPort() + ".");
				thread.start();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				_socket.close();
		

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
