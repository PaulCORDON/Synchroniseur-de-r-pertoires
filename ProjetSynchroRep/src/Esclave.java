
public class Esclave  extends Thread{
	String id;
	
	public Esclave(String st) {		
		id=st;
	}
	public void run(){
		System.out.println("Je suis un esclave");
	}
}
