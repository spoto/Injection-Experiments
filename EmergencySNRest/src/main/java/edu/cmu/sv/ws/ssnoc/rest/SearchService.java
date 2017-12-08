/**
 * This class contains the implementation of the RESTful API calls made to 
 * Search specific information in the database
 * 
 */


package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.HashMap;
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
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.MessageDetail;


@Path("/search")
public class SearchService extends BaseService{
	
	/**
	 * This method searches for a list of user names available in the system
	 * according to a user name search term and return the list
	 * 
	 * @param user
	 *            - A user name search string
	 * @return - A list of user names
	 */
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/user/{name}")
	public  String searchUsersByName(@PathParam("name") String name){
		ArrayList<String> users = new ArrayList<String>();
		try {
				users = DAOFactory.getInstance().getUserDAO().searchUserByName(name);
			}
		catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}
		String json = new Gson().toJson(users);
		return json;
	}
	
	/**
	 * This method searches for a list of user names available in the system
	 * according to a status search term and return the list
	 * 
	 * @param user
	 *            - A user name search string
	 * @return - A list of user names
	 */
	
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/status/{value}")
	public String searchUsersByStatus(@PathParam("value") String status){
		Log.enter(status);
		ArrayList<String> users = new ArrayList<String>();
		try {
				users = DAOFactory.getInstance().getUserDAO().searchUserByStatus(status);
			}
		catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}
		String json = new Gson().toJson(users);
		return json;
	}
	
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/publicMessages/{message}")
	public List<MessageDetail> searchForPublicMessages(@PathParam("message") String message){
		Log.enter(message);
		List<MessageDetail> publicMessages = new ArrayList<MessageDetail>();
		try {
		List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().searchForPublicMessage(message);
		
		List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
		HashMap<Long,String> userMap = new HashMap<Long, String>();
		for (UserPO po : userPOs) {
			userMap.put(po.getUserId(), po.getUserName());
		}
		publicMessages = new ArrayList<MessageDetail>();
			for(MessageDetailPO mPO : msgPOs)
			{       
				    MessageDetail mdto = new MessageDetail();
				    mdto.setFrom_userName(userMap.get(mPO.getFrom_userId())); 
				    if(mPO.getTo_userId() == 0){
				    	mdto.setTo_userName("PUBLIC WALL");
				    }
				    else
				    	mdto.setTo_userName(userMap.get(mPO.getTo_userId()));
					mdto.setMessage(mPO.getMessage());
					mdto.setMessage_timestamp(mPO.getMessage_timestamp());
//					mdto.setLocation(mPO.getLocation());
									
					publicMessages.add(mdto);
		
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(publicMessages);
		}

		return publicMessages;
	}

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/privateMessages/{message}")
	public List<MessageDetail> searchForPrivateMessages(@PathParam("message") String message){
		Log.enter(message);
		List<MessageDetail> privateChats = new ArrayList<MessageDetail>();
		try {
		List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().searchForPrivateChats(message);
		
		List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
		HashMap<Long,String> userMap = new HashMap<Long, String>();
		for (UserPO po : userPOs) {
			userMap.put(po.getUserId(), po.getUserName());
		}
		privateChats = new ArrayList<MessageDetail>();
			for(MessageDetailPO mPO : msgPOs)
			{       
				    MessageDetail mdto = new MessageDetail();
				    mdto.setFrom_userName(userMap.get(mPO.getFrom_userId()));
				   	mdto.setTo_userName(userMap.get(mPO.getTo_userId()));
					mdto.setMessage(mPO.getMessage());
					mdto.setMessage_timestamp(mPO.getMessage_timestamp());
//					mdto.setLocation(mPO.getLocation());
									
					privateChats.add(mdto);
		
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(privateChats);
		}

		return privateChats;
	}

	

	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/publicAnnouncement/{message}")
	public List<MessageDetail> searchForPublicAnnouncements(@PathParam("message") String message){
		Log.enter(message);
		List<MessageDetail> publicAnnouncements = new ArrayList<MessageDetail>();
		try {
		List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().searchForPublicAnnouncements(message);
		
		List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
		HashMap<Long,String> userMap = new HashMap<Long, String>();
		for (UserPO po : userPOs) {
			userMap.put(po.getUserId(), po.getUserName());
		}
		publicAnnouncements = new ArrayList<MessageDetail>();
			for(MessageDetailPO mPO : msgPOs)
			{       
				    MessageDetail mdto = new MessageDetail();
				    mdto.setFrom_userName(userMap.get(mPO.getFrom_userId()));
				   	mdto.setTo_userName("PUBLIC ANNOUNCEMENTS");
					mdto.setMessage(mPO.getMessage());
					mdto.setMessage_timestamp(mPO.getMessage_timestamp());
//					mdto.setLocation(mPO.getLocation());
									
					publicAnnouncements.add(mdto);
		
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(publicAnnouncements);
		}

		return publicAnnouncements;
	}
}
