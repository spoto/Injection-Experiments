package edu.cmu.sv.ws.ssnoc.dto;

public class Memory {
	private long usedVolatile;
    private long freeVolatile;
    private long usedNonVolatile;
    private long freeNonVolatile;
    private String createdAt;
//    private int minutes;
    
    public long getUsedVolatile() {
		return usedVolatile;
	}
	public void setUsedVolatile(long usedVolatile) {
		this.usedVolatile = usedVolatile;
	}
	public long getFreeVolatile() {
		return freeVolatile;
	}
	public void setFreeVolatile(long freeVolatile) {
		this.freeVolatile = freeVolatile;
	}
	public long getUsedNonVolatile() {
		return usedNonVolatile;
	}
	public void setUsedNonVolatile(long usedNonVolatile) {
		this.usedNonVolatile = usedNonVolatile;
	}
	public long getFreeNonVolatile() {
		return freeNonVolatile;
	}
	public void setFreeNonVolatile(long freeNonVolatile) {
		this.freeNonVolatile = freeNonVolatile;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
//	public int getMinutes() {
//		return minutes;
//	}
//	public void setMinutes(int minutes) {
//		this.minutes = minutes;
//	}

    
    
}
