package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;

@Path("/users")
public class UsersService extends BaseService {
	/**
	 * This method loads all active users in the system.
	 * 
	 * @return - List of all active users.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "users")
	public List<User> loadUsers() {
		Log.enter();

		List<User> users = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
			// WE ALSO LOAD THE STATUS MSG, TIMESTAMP, LOCATION  OF EACH USER
			List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().loadStatuses();
			users = new ArrayList<User>();
			for (UserPO po : userPOs) {
				User dto = ConverterUtils.convert(po);
				for(MessageDetailPO mPO : msgPOs)
				{
					if (mPO.getFrom_userId() == po.getUserId()){
						dto.setStatus(mPO.getMessage());
						dto.setStatusUpdateTime(mPO.getMessage_timestamp());
//						dto.setLocation(mPO.getLocation());
//						dto.setLatitude(mPO.getLatitude());
//						dto.setLongitude(mPO.getLongitude());
					}
				}
				users.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}
	
	/**
	 * This method retrieves all users with whom a user has chatted with.
	 * 
	 *  @param userName
	 *            - User Name
	 * 
	 * @return - List of users.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "chatusers")
	@Path("/{userName}/chatbuddies")
	public List<User> findChatUsers(@PathParam("userName") String userName) {
		Log.enter(userName);
		List<User> users = null;

		try {
			UserPO userpo = DAOFactory.getInstance().getUserDAO().findByName(userName);
			List<UserPO> chatuserPOs = DAOFactory.getInstance().getMessageDetailDAO().loadChatUsers(userpo.getUserId());
			users = new ArrayList<User>();
			for (UserPO chatuser : chatuserPOs){
				User dto = ConverterUtils.convert(chatuser);
				MessageDetailPO chatUserStatus = DAOFactory.getInstance().getMessageDetailDAO().findById(chatuser.getUserId());
				dto.setUserName(chatuser.getUserName());
				dto.setStatus(chatUserStatus.getMessage());
				dto.setStatusUpdateTime(chatUserStatus.getMessage_timestamp());
//				dto.setLocation(chatUserStatus.getLocation());
				
				users.add(dto);	
			}			
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}
		return users;
	}
}
