
public class Maitre  extends Thread{
	String id;
	
	Maitre(String st) {		
		id=st;
	}
	public void run(){
		System.out.println("Je suis un maitre");
	}
}
