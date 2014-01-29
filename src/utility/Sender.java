package utility;

/**
 * Sender Class that used by MessagePasser to send messages
 * 
 * @author Xincen Hao
 * @author Bin Feng
 * 
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import entity.Host;
import entity.Message;

public class Sender {

	private String hostname;
	private InetAddress senderIP;
	private Object sendingObject;
	private Queue<Message> delayQueue;	// Queue that stores delayed message
	private boolean delayBlocked;
	private HashMap<String, Socket> connections;
	private HashMap<String, ObjectOutputStream> output;
	private HashMap<String, Host> hostMap;
	
	public Sender(String hostname, HashMap<String, Host> hostMap) {
		super();
		this.hostname = hostname;
		this.hostMap = hostMap;
		this.delayQueue = new LinkedList<Message>();
		this.connections = new HashMap<String, Socket>();
		this.output = new HashMap<String, ObjectOutputStream>();
		this.delayBlocked = false;
	}
	
	public boolean isDelayBlocked() {
		return delayBlocked;
	}

	public void setDelayBlocked(boolean delayBlocked) {
		this.delayBlocked = delayBlocked;
	}

	public Queue<Message> getDelayQueue() {
		return delayQueue;
	}

	public void setDelayQueue(Queue<Message> delayQueue) {
		this.delayQueue = delayQueue;
	}

	public Object getSendingObject() {
		return sendingObject;
	}

	public void setSendingObject(Object sendingObject) {
		this.sendingObject = sendingObject;
	}

	public InetAddress getSenderIP() {
		return senderIP;
	}

	public void setSenderIP(InetAddress senderIP) {
		this.senderIP = senderIP;
	}

	/*
	 * Setup TCP connection socket and send message
	 * 
	 */
	public void sendMessage(Message message) {
		String dest = message.getDest();
		Socket clientSocket = null;
		ObjectOutputStream outToServer = null;
		
		// Get connection socket and OutputStream
		if (connections.get(dest) != null) {
			// Get socket and ObjectOutputStrem if already stored for the destination
			clientSocket = connections.get(dest);
			outToServer = output.get(dest);
		} else {
			InetAddress destIP;
			// Create new socket for a new destination
			try {
				destIP = InetAddress.getByName(hostMap.get(dest).getIp());
				int destPort = hostMap.get(dest).getPort();
				
				clientSocket = new Socket(destIP, destPort); 
				outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
				
				// Store socket and ObjectOutputStream for later use
				connections.put(dest, clientSocket);
				output.put(dest, outToServer);
			} catch (UnknownHostException e) {
				System.out.println("Host name is unkown!");
				e.printStackTrace();
			
			} catch (ConnectException e) {
				System.out.println("Cannot setup TCP connection");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Write message to OutputStream
		if (outToServer != null) {
			try {
				System.out.print("-----" + message.getSrc().toUpperCase() + " Send to " + message.getDest().toUpperCase());
				System.out.println(": " +message.isDuplicateFlag() + " " + message.getMessageKind() + " " + message.getSequenceNum());
				outToServer.writeObject(message);
			} catch (Exception e) {
				// Release socket if connection break
				System.out.println("Cannot deliver message, perhaps pipe is broken");
				try {
					output.get(dest).close();
				} catch (IOException e1) {
					System.out.println("Cannot close the socket");
				}
				output.remove(dest);
				connections.remove(dest);
			}
		}
	}
	
	/*
	 * Clear the delayed queue
	 */
	public void clearDelayQueue() {
		while(!delayBlocked && !delayQueue.isEmpty()){
			Message blockedMessage = delayQueue.remove();
			this.sendMessage(blockedMessage);
		}
	}
	
}
