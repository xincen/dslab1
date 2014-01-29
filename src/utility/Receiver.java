package utility;

/**
 * Receiver Thread used by MessagePasser
 * 
 * @author Xincen Hao
 * @author Bin Feng
 */
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

import entity.Message;

public class Receiver implements Runnable {

	private int serverPort;
	private MessagePasser messagePasser;
	private Queue<Message> delayQueue;
	private Queue<Message> messageQueue;
	private boolean delayBlocked;
	
	public boolean isDelayBlocked() {
		return delayBlocked;
	}

	public void setDelayBlocked(boolean delayBlocked) {
		this.delayBlocked = delayBlocked;
	}

	public Queue<Message> getDelayQueue() {
		return delayQueue;
	}
	
	public Queue<Message> getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(Queue<Message> messageQueue) {
		this.messageQueue = messageQueue;
	}
	
	public Receiver(int serverPort, MessagePasser messagePasser) {
		super();
		this.serverPort = serverPort;
		this.messagePasser = messagePasser;
		this.delayQueue = new LinkedList<Message>();
		this.messageQueue = new LinkedList<Message>();
		this.delayBlocked = false;
	}

	public MessagePasser getMessagePasser() {
		return this.messagePasser;
	}
	
	public void clearDelayQueue() {
		while (!this.delayQueue.isEmpty()) {
			Message m = this.delayQueue.poll();
			this.messageQueue.add(m);
		}
	}
	
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public void run() {
		// be getting messages from the socket as they are delivered
		try {
			
	        ServerSocket welcomeSocket = new ServerSocket(serverPort);
	        System.out.println("-----Server open at port: " + serverPort);

	        while (true) {
	        	// Waiting for new connection
	        	Socket connectionSocket = welcomeSocket.accept();
	        	InetAddress dest = connectionSocket.getInetAddress();
	        	System.out.println("Set up connection with : " + dest.getHostAddress());
	        	
	        	// Set up new connection socket and start new communication thread
	        	Communication com = new Communication(connectionSocket, this);
	        	new Thread(com).start();
	        }
	       
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
