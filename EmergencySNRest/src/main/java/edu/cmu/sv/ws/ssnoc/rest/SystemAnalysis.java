/**
 * This class contains the implementation of the RESTful API calls made to 
 * analyze the application
 * 
 */

package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.dto.UserCluster;

@Path("/analysis")
public class SystemAnalysis extends BaseService{
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/network/{duration}")
	public String analyzeSocialNetwork(@PathParam("duration") int duration){
		Log.enter(duration);
		
		List<UserCluster> userCluster = new ArrayList<UserCluster>();
		
		try {
			userCluster = DAOFactory.getInstance().getUserDAO().getClusterNearByUsers(duration);
		}
	catch (Exception e) {	
		handleException(e);
	} finally {
		Log.exit(userCluster);
	}
		if(userCluster == null)
			return null;
		else if(!(userCluster.isEmpty())){
			String json = new Gson().toJson(userCluster);
			return json;
		}
		else
			return null;
	}
	
}
