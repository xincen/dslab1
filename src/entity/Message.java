package entity;

/**
 * Message class is used to containing message information,
 * such as source, destination, sequence number, duplicate flag, etc.
 *
 * @author Xincen Hao
 * @author Bin Feng
 */

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -288330371015983606L;
	// header
	private String src;
	private String dest;	// destination node name
	private int sequenceNum;
	private static int globalSequenceNum = 0;	// sequence number
	private boolean duplicateFlag;	// used only by the MessagePasser, not your application
	private String messageKind;		// need not be unique
	
	// payload
	private Object object;
	
	
	public Message(String dest, String messageKind, Object object) {
		this.dest = dest;
		this.messageKind = messageKind;
		this.object = object;
	}

	public Message(Message message) {
		this.src = message.getSrc();
		this.dest = message.getDest();
		this.messageKind = message.getMessageKind();
		this.object = message.getObject();
		this.sequenceNum = message.getSequenceNum();
		this.duplicateFlag = message.isDuplicateFlag();
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


	public int getSequenceNum() {
		return sequenceNum;
	}


	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	public void setNextSequenceNum() {
		Message.globalSequenceNum++;
		this.sequenceNum = Message.globalSequenceNum;
	}

	public boolean isDuplicateFlag() {
		return duplicateFlag;
	}


	public void setDuplicateFlag(boolean duplicateFlag) {
		this.duplicateFlag = duplicateFlag;
	}


	public String getMessageKind() {
		return messageKind;
	}


	public void setMessageKind(String messageKind) {
		this.messageKind = messageKind;
	}


	public Object getObject() {
		return object;
	}


	public void setObject(Object object) {
		this.object = object;
	}

}
