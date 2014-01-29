package entity;

/**
 * Host class is used to contain host computer information,
 * such as name, IP, port.
 * 
 * @author Xincen Hao
 * @author Bin Feng
 */
public class Host {
	private String name;
	private String ip;
	private int port;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
