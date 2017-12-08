package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

//import java.sql.Timestamp;

public class MemoryPO {
	private String crumbID;
    private long usedVolatile;
    private long freeVolatile;
    private long usedNonVolatile;
    private long freeNonVolatile;
    private String createdAt;
//    private int minutes;

    public void setCrumbID(String crumbID){
    	this.crumbID = crumbID;
    }

    public String getCrumbID(){
    	return crumbID;
    }

    public void setUsedVolatile(long usedVolatile){
    	this.usedVolatile = usedVolatile;
    }

    public long getUsedVolatile(){
    	return usedVolatile;
    }

    public void setFreeVolatile(long freeVolatile){
    	this.freeVolatile = freeVolatile;
    }

    public long getFreeVolatile(){
    	return freeVolatile;
    }

    public void setUsedNonVolatile(long usedNonVolatile){
    	this.usedNonVolatile = usedNonVolatile;
    }

    public long getUsedNonVolatile(){
    	return usedNonVolatile;
    }

    public void setFreeNonVolatile(long freeNonVolatile){
    	this.freeNonVolatile = freeNonVolatile;
    }

    public long getFreeNonVolatile(){
    	return freeNonVolatile;
    }
    
    public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
    public String toString() {
        return new Gson().toJson(this);
    }

//	public int getMinutes() {
//		return minutes;
//	}
//
//	public void setMinutes(int minutes) {
//		this.minutes = minutes;
//	}
}
