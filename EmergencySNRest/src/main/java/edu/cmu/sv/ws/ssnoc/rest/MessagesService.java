package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.MessageDetail;

/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to messages.
 * 
 */

@Path("/messages")
public class MessagesService extends BaseService {
	
	/**
	 * List all messages related to two users.
	 * 
	 * @param userName1
	 *            - User Name1
	 * @param userName2
	 *            - User Name2
	 * 
	 * @return - Details of the Messages
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName1}/{userName2}")
	@XmlElementWrapper(name = "chat")
	public List<MessageDetail> loadMessages(@PathParam("userName1") String userName1,@PathParam("userName2") String userName2) {
		Log.enter(userName1,userName2);

		List<MessageDetail> chatMessages = null;
		try {
			UserPO userPO1 = DAOFactory.getInstance().getUserDAO().findByName(userName1);
			UserPO userPO2 = DAOFactory.getInstance().getUserDAO().findByName(userName2);
			if(userPO1 == null || userPO2 == null)
				return null;
			long userId1 = userPO1.getUserId();
			long userId2 = userPO2.getUserId();
			HashMap<Long,String> userMap = new HashMap<Long, String>();
			userMap.put(userId1, userName1);
			userMap.put(userId2, userName2);
			
			List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().loadChatMessages(userId1, userId2);
			chatMessages = new ArrayList<MessageDetail>();
			for (MessageDetailPO mPO : msgPOs) {
				MessageDetail chatmsg = new MessageDetail();
				chatmsg.setFrom_userName(userMap.get(mPO.getFrom_userId())); 
				chatmsg.setTo_userName(userMap.get(mPO.getTo_userId()));
				chatmsg.setMessage(mPO.getMessage());
				chatmsg.setMessage_timestamp(mPO.getMessage_timestamp());
//				chatmsg.setLocation(mPO.getLocation());
				chatMessages.add(chatmsg);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(chatMessages);
		}

		return chatMessages;
	}

	/**
	 * This method loads all public wall messages in the system.
	 * 
	 * @return - List of all public wall messages.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "wall")
	@Path("/wall")
	public List<MessageDetail> loadPublicWall() {
		Log.enter();

		List<MessageDetail> messageDetail = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
			// WE ALSO LOAD THE STATUS MSG, TIMESTAMP, LOCATION  OF EACH USER
			List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().loadPublicWallMessages();
			if(msgPOs.isEmpty() || msgPOs == null){
				return null;
			}
			HashMap<Long,String> userMap = new HashMap<Long, String>();
			for (UserPO po : userPOs) {
				userMap.put(po.getUserId(), po.getUserName());
			}
			messageDetail = new ArrayList<MessageDetail>();
				for(MessageDetailPO mPO : msgPOs)
				{       
					    MessageDetail mdto = new MessageDetail();
					    mdto.setFrom_userName(userMap.get(mPO.getFrom_userId()));  /// check if this works LONG in long
					    if(mPO.getTo_userId() == 0){
					    	mdto.setTo_userName("PUBLIC WALL");
					    }
					    else
					    	mdto.setTo_userName(userMap.get(mPO.getTo_userId()));
						mdto.setMessage(mPO.getMessage());
						mdto.setMessage_timestamp(mPO.getMessage_timestamp());
//						mdto.setLocation(mPO.getLocation());
						messageDetail.add(mdto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(messageDetail);
		}

		return messageDetail;
	}

	/**
	 * This method loads all public wall messages in the system.
	 * 
	 * @return - List of all public wall messages.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/announcement")
	public List<MessageDetail> loadPublicAnnouncements() {
		Log.enter();

		List<MessageDetail> messageDetail = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
			// WE ALSO LOAD THE MSG, TIMESTAMP, LOCATION  OF EACH USER
			List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().loadPublicAnnouncementMessages();
			HashMap<Long,String> userMap = new HashMap<Long, String>();
			for (UserPO po : userPOs) {
				userMap.put(po.getUserId(), po.getUserName());
			}
			messageDetail = new ArrayList<MessageDetail>();
				for(MessageDetailPO mPO : msgPOs)
				{       
					    MessageDetail mdto = new MessageDetail();
					    mdto.setFrom_userName(userMap.get(mPO.getFrom_userId()));  /// check if this works LONG in long
					    if(mPO.getTo_userId() == -1){
					    	mdto.setTo_userName("PUBLIC ANNOUNCEMENT");
					    }
					    else
					    	mdto.setTo_userName(userMap.get(mPO.getTo_userId()));
						mdto.setMessage(mPO.getMessage());
						mdto.setMessage_timestamp(mPO.getMessage_timestamp());
//						mdto.setLocation(mPO.getLocation());
										
						messageDetail.add(mdto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(messageDetail);
		}

		return messageDetail;
	}

}
