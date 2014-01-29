package entity;

/**
 * Rule is used to contain rule information for checking before sending
 * and after receiving message.
 * 
 * @author Xincen Hao
 * @author Bin Feng
 */
public class Rule {
	private String action;
	private String src;
	private String dest;
	private String kind;
	private int seqNum;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	
	
	
}
