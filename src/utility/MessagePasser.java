package utility;

/**
 * Message passer acts as the controller class in this project.
 * This class will be instantiated only once.
 * MessagePasser will parse the configuration file and set up sockets
 * for communicating with all processes listed in the configuration 
 * section of the file.
 * 
 * MessagePasser will use send method to check messages against send rules
 * and send them.
 * MessagePasser also use receive method to receive messages and check
 * against receive rules.
 *
 * @author Xincen Hao
 * @author Bin Feng
 */

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import entity.Configuration;
import entity.Host;
import entity.Message;
import entity.Rule;

public class MessagePasser{

	private int serverPort = 10010;
	
	private String configFile;
	private String localName;
	
	private Sender sender;
	private Receiver receiver;
	
	private HashMap<String, Host> hostMap;
	ArrayList<Rule> receiveRules;
	ArrayList<Rule> sendRules;
	
	private String localIP;
	
	public HashMap<String, Host> getHostMap() {
		return hostMap;
	}
	
	/*
	 *  configuration_filename: YAML config file,
	 * local_name: name string of this particular process
	 * 
	 */
	public MessagePasser(String configFile, String localName) {
		this.configFile = configFile;
		this.localName = localName;
		
		// Set configuration, sendRules, receiveRules from yaml configuration file
		updateYaml();
		
		try {
			this.localIP = hostMap.get(localName).getIp();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			setup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Set up the sender and receiver
	 */
	public void setup() throws Exception {
		
		serverPort = hostMap.get(localName).getPort();
		receiver = new Receiver(serverPort, this);
		new Thread(receiver).start();
		System.out.println("Start listening on port " + serverPort);
		
		sender = new Sender(localName, hostMap);
	}
	
	/* 
	 * send messages
	 * to other processes on (potentially) other hosts.
	 */
	public void send(Message message) {
		/* check the message against any SendRules before delivering the message
		 * to the socket
		 */
		Boolean duplicate = false;

		message.setNextSequenceNum();
		message.setSrc(localName);
		message.setDuplicateFlag(false);
		
		for(Rule rule : sendRules){
			if(rule.getSrc()==null || (rule.getSrc()!=null && rule.getSrc().equalsIgnoreCase(localName))){
				if(rule.getKind()==null || (rule.getKind().equalsIgnoreCase(message.getMessageKind()))) { 
					if(	(rule.getDest()==null || rule.getDest().equalsIgnoreCase(message.getDest()))){
						if (rule.getSeqNum() == 0 || (rule.getSeqNum() == message.getSequenceNum())) {
							if(rule.getAction().equalsIgnoreCase("drop")){
								System.out.println("Rule: Message dropped!");
								return;
							}else if(rule.getAction().equalsIgnoreCase("duplicate")){
								System.out.println("Rule: Message duplicated!");
								sender.setDelayBlocked(false);
								duplicate = true;
								break;
							}else if(rule.getAction().equalsIgnoreCase("delay")){
								System.out.println("Rule: Message delayed!");
								sender.getDelayQueue().add(message);
								sender.setDelayBlocked(true);
								return;
							}
						}
					}
				}
			}
		}
		
		if(message != null){
			sender.setDelayBlocked(false);
			sender.sendMessage(message);
			
			if (duplicate) {
				Message duplicateMessage = new Message(message);
				duplicateMessage.setDuplicateFlag(true);
				sender.sendMessage(duplicateMessage);
			}
			sender.clearDelayQueue();
		}
	}

	/* 
	 * Insert message to Receiver's queue
	 */
	public void insertReceiveQueue(Message message) {
		// be getting messages from the socket as they are delivered
		boolean receiveDuplicate = false;
		receiver.setDelayBlocked(false);
		// checking each received message against ReceiveRules
		for(Rule rule : receiveRules){
			if(rule.getSrc()==null || (rule.getSrc()!=null && rule.getSrc().equalsIgnoreCase(message.getSrc()))){
				if(rule.getKind()==null || (rule.getKind().equalsIgnoreCase(message.getMessageKind()))) { 
					if(	(rule.getDest()==null || rule.getDest().equalsIgnoreCase(message.getDest()))){
						if (rule.getSeqNum() == 0 || (rule.getSeqNum() == message.getSequenceNum())) {
							// Break when match the first rule 
							if(rule.getAction().equalsIgnoreCase("drop")){
								System.out.println("Rule: Message dropped!");
								return;
							}else if(rule.getAction().equalsIgnoreCase("duplicate")){
								System.out.println("Rule: Message duplicated!");
								receiveDuplicate = true;
								receiver.setDelayBlocked(false);
								break;
							}else if(rule.getAction().equalsIgnoreCase("delay")){
								System.out.println("Rule: Message delayed!");
								receiver.getDelayQueue().add(message);
								receiver.setDelayBlocked(true);
								return;
							}
						}
					}
				}
			}
		}
		
		// Insert message to receiver's queue
		if(message != null){
			receiver.getMessageQueue().add(message);
			
			if (receiveDuplicate) {
				Message duplicateMessage = new Message(message);
				duplicateMessage.setDuplicateFlag(true);
				receiver.getMessageQueue().add(duplicateMessage);
			}
			
			if (!receiver.isDelayBlocked()) {
				receiver.clearDelayQueue();
			}
		}
	}
	
	/*
	 *  Called by application, return a message
	 */
	public Message receive() {
		if (receiver.getMessageQueue().isEmpty()) {
			return null;
		}
		Message	message = receiver.getMessageQueue().poll();
		
		return message;
	}

	/*
	 * Set configuration, sendRules, receiveRules from yaml configuration file
	 */
	public void updateYaml(){
		Yaml yaml = new Yaml(new Constructor(Configuration.class));
		
		try {
			Configuration map = (Configuration) yaml.load(new FileInputStream(new File(configFile)));
			hostMap = new HashMap<String, Host>();
			ArrayList<Host> hosts = map.getConfiguration();
			for (Host host : hosts) {
				hostMap.put(host.getName(), host);
			}
			receiveRules = map.getReceiveRules();
			sendRules = map.getSendRules();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
