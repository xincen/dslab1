package test;

/**
 * This is the entry of the class.
 * It starts MessagePasser class once and use Application class to test.
 * @author Xincen Hao
 * @author Bin Feng
 */
import java.util.Scanner;

import entity.Message;

import utility.MessagePasser;

public class Main {

	public static void main(String[] args){
		
		System.out.println("Input your name:[eg: alice bob charlie daphnie]");
		Scanner scanner = new Scanner(System.in);
		String localName = scanner.nextLine();		// eg: alice
		
		MessagePasser mp = new MessagePasser("config.yaml", localName);

		// Create new thread to send message
		Application app = new Application(localName, mp);
		new Thread(app).start();
		
		// Main thread that receive messages
		while(true) {
			try {
				Thread.sleep(2000);
				Message message = mp.receive();
				if (message != null) {
					System.out.println("Receive message from: " + message.getSrc() + " " + message.getSequenceNum() + " "+ message.getObject());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

