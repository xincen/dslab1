package entity;

/**
 * Configuration class is used to containing all configurations,
 * such as host, send rules and receive rules.
 *
 * @author Xincen Hao
 * @author Bin Feng
 */
import java.util.ArrayList;


public class Configuration {
	private ArrayList<Host> configuration;
	private ArrayList<Rule> sendRules;
	private ArrayList<Rule> receiveRules;

	public ArrayList<Host> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ArrayList<Host> configuration) {
		this.configuration = configuration;
	}

	public ArrayList<Rule> getSendRules() {
		return sendRules;
	}

	public void setSendRules(ArrayList<Rule> sendRules) {
		this.sendRules = sendRules;
	}

	public ArrayList<Rule> getReceiveRules() {
		return receiveRules;
	}

	public void setReceiveRules(ArrayList<Rule> receiveRules) {
		this.receiveRules = receiveRules;
	}
	
	
	
}
