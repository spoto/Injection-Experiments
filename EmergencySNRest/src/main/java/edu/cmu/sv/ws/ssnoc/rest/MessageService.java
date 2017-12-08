package edu.cmu.sv.ws.ssnoc.rest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDetailDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.MessageDetail;


/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to messages.
 * 
 */

@Path("/message")
public class MessageService extends BaseService {
	
	/**
	 * This method adds a chatmessage to the database
	 * 
	 * @param sendinguserName
	 * @param receivinguserName
	 * @param chatmessage
	 * 
	 * @return - An object of type Response with the status of the request
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{sendingUserName}/{receivingUserName}")
	public Response sendChatMessage(@PathParam("sendingUserName") String sendinguserName,
			@PathParam("receivingUserName") String receivinguserName,
			MessageDetail chatmessage) {
		
		Log.enter(sendinguserName,receivinguserName,chatmessage);
		MessageDetail resp = new MessageDetail();
		
		try {
			UserPO sendpo = DAOFactory.getInstance().getUserDAO().findByName(sendinguserName);
			UserPO receivepo = DAOFactory.getInstance().getUserDAO().findByName(receivinguserName);
			if(sendpo == null || receivepo == null){
				return badRequest();
			}

			IMessageDetailDAO mdao = DAOFactory.getInstance().getMessageDetailDAO();
			java.util.Date date= new java.util.Date();
			Timestamp timeStmp = new Timestamp(date.getTime());
			
			MessageDetailPO mpo = new MessageDetailPO();
			mpo.setFrom_userId(sendpo.getUserId());
			mpo.setTo_userId(receivepo.getUserId());
			mpo.setMessage(chatmessage.getMessage());
			mpo.setMessage_timestamp(timeStmp);
//			mpo.setLocation("");
			mdao.save(mpo);
			
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		
		return created(resp);
	}
	
	/**
	 * The message with a particular messageId.
	 * 
	 * @param messageId
	 * 
	 * @return - Details of the Message
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{messageID}")
	public MessageDetail findMessage(@PathParam("messageID") long messageId) {
		Log.enter(messageId);
		
		if(messageId < 0){
			return null;
		}
		MessageDetail messageDetail = new MessageDetail();

		try {
			MessageDetailPO mpo =findMessageById(messageId);
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
			HashMap<Long,String> userMap = new HashMap<Long, String>();
			for (UserPO po : userPOs) {
				userMap.put(po.getUserId(), po.getUserName());
			}
			messageDetail.setFrom_userName(userMap.get(mpo.getFrom_userId()));
			messageDetail.setTo_userName(userMap.get(mpo.getTo_userId()));
			messageDetail.setMessage(mpo.getMessage());
//			messageDetail.setLocation(mpo.getLocation());
			messageDetail.setMessage_timestamp(mpo.getMessage_timestamp());
			
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(messageDetail);
		}

		return messageDetail;
	}
	
	/**
	 * This method saves the message posted at the wall by the user, in the database
	 * 
	 * @param user
	 *            - An object of type User
	 * @return - An object of type Response with the status of the request
	 */
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response addPublicMessage(MessageDetail msgDetail) {
		Log.enter(msgDetail);
		try {
			IUserDAO dao = DAOFactory.getInstance().getUserDAO();
			UserPO existingUser = dao.findByName(msgDetail.getFrom_userName());
			
			if (existingUser != null) {
				IMessageDetailDAO mdao = DAOFactory.getInstance().getMessageDetailDAO();
				MessageDetailPO mpo = new MessageDetailPO();
				mpo.setFrom_userId(existingUser.getUserId());
				mpo.setTo_userId(0);

				//				If no message found then return 
				if(msgDetail.getMessage().trim() == "")
						return badRequest();
				
				mpo.setMessage(msgDetail.getMessage());
				//Get Time stamp
				Date date= new Date();
		         //getTime() returns current time in milliseconds
				long time = date.getTime();
		         //Passed the milliseconds to constructor of Timestamp class 
				Timestamp ts = new Timestamp(time);
				mpo.setMessage_timestamp(ts);
//				mpo.setLocation(msgDetail.getLocation());
				
				mdao.save(mpo);		
			}
			else
				return badRequest();
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		return ok();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/announcement")
	public Response addPublicAnnouncements(MessageDetail msgDetail) {
		Log.enter(msgDetail);
	
		try {
			IUserDAO dao = DAOFactory.getInstance().getUserDAO();
			UserPO existingUser = dao.findByName(msgDetail.getFrom_userName());
			
			if (existingUser != null) {
				IMessageDetailDAO mdao = DAOFactory.getInstance().getMessageDetailDAO();
				MessageDetailPO mpo = new MessageDetailPO();
				mpo.setFrom_userId(existingUser.getUserId());
				mpo.setTo_userId(-1);
				mpo.setMessage(msgDetail.getMessage());
				//Get Time stamp
				Date date= new Date();
		         //getTime() returns current time in milliseconds
				long time = date.getTime();
		         //Passed the milliseconds to constructor of Timestamp class 
				Timestamp ts = new Timestamp(time);
				mpo.setMessage_timestamp(ts);
//				mpo.setLocation(msgDetail.getLocation());
				
				mdao.save(mpo);		
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		return ok();
	}

}
