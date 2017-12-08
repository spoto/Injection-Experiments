/**
 * 
 */
package edu.cmu.sv.ws.ssnoc.dto;

import java.sql.Timestamp;

/**
 * @author Aparna
 *  This object contains activity information that is responded as part of the REST
 * API request.
 *
 */
public class MessageDetail {
	private String to_userName;
	private String from_userName;
	private String message;
	private Timestamp message_timestamp;
	private String location;
	
	
	public String getTo_userName() {
		return to_userName;
	}
	public void setTo_userName(String to_userName) {
		this.to_userName = to_userName;
	}
	public String getFrom_userName() {
		return from_userName;
	}
	public void setFrom_userName(String from_userName) {
		this.from_userName = from_userName;
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	
	
	

}
