package utility;
/**
 *  Communication Thread that receiver uses to receive message from connection socket
 * 
 *  @author Xincen Hao
 *  @author Bin Feng
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import entity.Message;

public class Communication implements Runnable {

	private Socket connectionSocket;	//TCP Connection socket with other process
	private Receiver receiver;	//Receiver main thread 
	
	public Communication (Socket conn, Receiver receiver) {
		this.connectionSocket = conn;
		this.receiver = receiver;
	}
	
	public void setSocket (Socket conn) {
		this.connectionSocket = conn;
	}
	
	public Socket getSocket () {
		return this.connectionSocket;
	}
	
	@Override
	public void run() {
		
		 try {
			ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
			  
	        while(true)
	        {
	        	Message receivedMessage = (Message) inFromClient.readObject();
	            if(receivedMessage != null){
	            
		            this.receiver.getMessagePasser().insertReceiveQueue(new Message(receivedMessage));
	            }
				Thread.sleep(100);
				
	        }
		} catch (IOException e) {
			System.out.println("Connection break.");
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found Exception.");
		} catch (InterruptedException e) {
			System.out.println("Interrupt Exception.");
		}
	}
}
