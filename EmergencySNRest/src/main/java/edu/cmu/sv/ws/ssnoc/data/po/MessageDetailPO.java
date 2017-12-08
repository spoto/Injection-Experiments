package edu.cmu.sv.ws.ssnoc.data.po;

import java.sql.Timestamp;

/**
 * This is the persistence class to save all messages communicated within in the system.
 * This contains information like the from user, to user, message, time of message
 * and the location entered by the user during status update or during chats. <br/>
 * Information is saved in SSN_MESSAGE_DETAIL table.
 * 
 */

public class MessageDetailPO {
	
	private long messageId;
	private long from_userId;
	private long to_userId;
	private String message;
	private Timestamp message_timestamp;
//	private Double latitude;
//	private Double longitude;
	
	
	public long getMessageId() {
		return messageId;
	}
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	public long getFrom_userId() {
		return from_userId;
	}
	public void setFrom_userId(long from_userId) {
		this.from_userId = from_userId;
	}
	public long getTo_userId() {
		return to_userId;
	}
	public void setTo_userId(long to_userId) {
		this.to_userId = to_userId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getMessage_timestamp() {
		return message_timestamp;
	}
	public void setMessage_timestamp(Timestamp message_timestamp) {
		this.message_timestamp = message_timestamp;
	}
//	public void setLatitude(Double latitude) {
//		this.latitude = latitude;
//	}
//	
//	public void setLongitude(Double longitude) {
//		this.longitude = longitude;
//	}
//	
//	public Double getLatitude() {
//		return latitude;
//	}
//	
//	public Double getLongitude() {
//		return longitude;
//	}
}
