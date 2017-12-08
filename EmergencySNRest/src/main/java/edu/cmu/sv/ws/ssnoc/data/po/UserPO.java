package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

/**
 * This is the persistence class to save all user information in the system.
 * This contains information like the user's name, his role, his account status
 * and the password information entered by the user when signing up. <br/>
 * Information is saved in SSN_USERS table.
 * 
 */
public class UserPO {
	private long userId;
	private String userName;
	private String password;
	private String salt;
	private String profession;
	private String role;
	private String accountStatus;
	private Double latitude;
	private Double longitude;

	public Double getLatitude() {
		return latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
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
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public int compareToAnotherOne(UserPO another){
//		if no password provided then cannot change username
		if(another.getPassword().length() == 0){
			if(this.userName.compareTo(another.getUserName()) != 0){
				return 1;
			}
			if(this.role.compareTo(another.getRole()) != 0){
				return 0;
			}
			if(this.accountStatus.compareTo(another.getAccountStatus()) != 0){
				return 0;
			}
			if(this.latitude.compareTo(another.getLatitude()) != 0 || this.longitude.compareTo(another.getLongitude()) != 0)
				return 0;
		}
		
		else{
			if(this.userName.compareTo(another.getUserName()) != 0){
				return 1;
			}
			if(this.password.compareTo(another.getPassword()) != 0){
				return 1;
			}
			if(this.role.compareTo(another.getRole()) != 0){
				return 0;
			}
			if(this.accountStatus.compareTo(another.getAccountStatus()) != 0){
				return 0;
			}
			if(this.latitude.compareTo(another.getLatitude()) != 0 || this.longitude.compareTo(another.getLongitude()) != 0)
				return 0;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
