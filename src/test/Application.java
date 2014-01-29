package test;

/**
 * Application Program sending thread
 * 
 * @author Xincen Hao
 * @author Bin Feng
 */
import java.util.Scanner;

import utility.MessagePasser;
import entity.Message;

public class Application implements Runnable{
	
	private MessagePasser messagepasser;
	private String localhost;
	
	public Application(String host, MessagePasser mp) {
		this.messagepasser = mp;
		this.localhost = localhost;
	}
	
	public void run() {
		Scanner scanner = new Scanner(System.in);
		
		/*
		 * Test cases for sending rules
		 */
		
		
		while (true) {
			System.out.println("Select a receiver to send your message: [eg: alice bob charlie daphnie]");
			String receiverName = scanner.nextLine();
			System.out.println("Please input message kind[Ack, Lookup, Seq]:");
			String messageKind = scanner.nextLine();
			System.out.println("Please input message you wanna send:");
			String message = scanner.nextLine();
			messagepasser.send(new Message(receiverName, messageKind, message));
		}
	}
}
