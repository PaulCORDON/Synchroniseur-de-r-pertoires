import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Esclave extends Transferable implements Runnable{
	String id;
	BufferedReader br;
	PrintWriter bw;
	Socket _socket;
	public Esclave(String st,BufferedReader r,PrintWriter w,Socket s) {		
		id=st;
		br=r;
		bw=w;
		_socket=s;
	}
	public void run(){
		Scanner sc= new Scanner(System.in);		
		System.out.println("Path Repo :");
		repository=sc.nextLine();
		sc.close();
		infoRepo();
	}
}
