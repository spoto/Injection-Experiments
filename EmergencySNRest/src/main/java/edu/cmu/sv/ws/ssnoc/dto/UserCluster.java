/**
 * @author Gautam Madaan
 *  This object contains Clusters of users for the REST
 * API request.
 *
 */

package edu.cmu.sv.ws.ssnoc.dto;

import java.util.List;

public class UserCluster {

	private List<String> userNames;	
	
	public List<String> getUsersforCluster() {
		return userNames;
	}
	
	public void setUserCluster(List<String> userNames){
		this.userNames = userNames;
	}
}
