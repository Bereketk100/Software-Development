package PA3.models;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.IOException;


public class ServerThread extends Thread { // refer to Chatroom
	private ObjectInputStream is = null;
	private ObjectOutputStream os = null;
	private boolean isAvailable = true;
	public int indDrivers;
	private Server Server;
	
	public ServerThread(Socket soc, Server server) {
		this.Server = server;
		this.indDrivers = Server.drivers;
		try {
			os = new ObjectOutputStream(soc.getOutputStream());
			is = new ObjectInputStream(soc.getInputStream());
			start();		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public void sendDi(Object b) {
		try {
			if(b == null) {
				os.writeObject(null);
				os.flush();
			} else {
			os.writeObject(b);
			os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean isAvailable() {
		return isAvailable;
	}
	
	
	public void sendOrder(DeliveryInformation deliveryInformation) {
		isAvailable = false; 
		sendDi(deliveryInformation);
	}

	
	public void run() 
	{	
		sendDi(Server.drivers);
		while(indDrivers != 0) 
		{	
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(Server.drivers != this.indDrivers)
			{
				sendDi(Server.drivers);
				indDrivers = Server.drivers;
			}
		}
		while(true) {
			
			try {
				String line = (String) is.readObject();
				if(line.equals("done")) {
					{ isAvailable = true; 
					}
				}
				else if (line.equals("finished")) {
					break; 
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				 
			}
		} 
	
	}

}
