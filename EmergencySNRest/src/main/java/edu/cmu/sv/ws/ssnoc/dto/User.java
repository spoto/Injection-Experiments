package edu.cmu.sv.ws.ssnoc.dto;

import java.sql.Timestamp;

import com.google.gson.Gson;

/**
 * This object contains user information that is responded as part of the REST
 * API request.
 * 
 */
public class User {
	private String userName;
	private String password;
	private String profession;
	private String status;
	private Timestamp statusUpdateTime;
	private String role;
	private String accountStatus;
	private Double latitude;
	private Double longitude;

	public String getUserName() {
		return userName;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Timestamp getStatusUpdateTime() {
		return statusUpdateTime;
	}

	public void setStatusUpdateTime(Timestamp statusUpdateTime) {
		this.statusUpdateTime = statusUpdateTime;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getRole(){
		return role;
	}
	
	public void setRole(String role){
		this.role = role;
	}

	public String getAccountStatus(){
		return accountStatus;
	}
	
	public void setAccountStatus(String accountStatus){
		this.accountStatus = accountStatus;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
