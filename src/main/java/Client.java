import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{

	Socket socketClient;
	ObjectOutputStream out;
	ObjectInputStream in;
	public BaccaratInfo clientInfo;
	int port;
	String address;
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call, int p, String ip ){
		this.port = p;
		this.address = ip;
		callback = call;
	}
	
	public void run() {
		try {
			socketClient= new Socket(address, port);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
			callback.accept("connected");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		while(true) {
			try {
				clientInfo = (BaccaratInfo)in.readObject(); //NUL POINTER EXCEPTION HERE!!!!!!!!
				callback.accept(clientInfo); //read in object and convert to info class object 
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
    }
	
	public void send(BaccaratInfo clientInfo) {
		try {
			out.writeObject(clientInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
