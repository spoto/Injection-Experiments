package edu.cmu.sv.ws.ssnoc.rest;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDetailDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.util.TestDBUtils;
import edu.cmu.sv.ws.ssnoc.dto.MessageDetail;



/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to measuring performance of the system.
 * 
 */

@Path("/performance")
public class MeasurePerformanceService extends BaseService{
	
	/**
	 * This method creates the test databases required to conduct the performance testing
	 */
	
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/setup")
	
	public Response createTestDB(){
		Log.enter("Entering method to create test databases");
		try {
			TestDBUtils.initializeTestDatabase();
		} catch (SQLException e) {
			Log.error("Oops :( We ran into an error when trying to intialize "
					+ "test database. Please check the trace for more details.", e);
		}
		return ok();
	}

	/**
	 * This method creates the test databases required to conduct the performance testing
	 */
	
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/resetTestDB")
	
	public Response resetTestDB(){
		Log.enter("Entering method to reset test databases");
		try {
			TestDBUtils.resetTestDatabase();
		} catch (SQLException e) {
			Log.error("Oops :( We ran into an error when trying to reset "
					+ "test database. Please check the trace for more details.", e);
		}
		return ok();
	}
	
	/**
	 * This method creates the test databases required to conduct the performance testing
	 */
	
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/teardown")
	
	public Response deleteTestDB(){
		Log.enter("Entering method to delete test databases");
		try {
			TestDBUtils.deleteTestDatabase();
		} catch (SQLException e) {
			Log.error("Oops :( We ran into an error when trying to delete "
					+ "test database. Please check the trace for more details.", e);
		}
		return ok();
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
	@Path("/testpost")
	public Response addTestMessage(MessageDetail msgDetail) {
		Log.enter(msgDetail);
	
		try {
			 // For testing purpose we will hard code the value of from and to user id to 0
			
			//IUserDAO dao = DAOFactory.getInstance().getUserDAO();
			//UserPO existingUser = dao.findByName(msgDetail.getFrom_userName());
			
				IMessageDetailDAO mdao = DAOFactory.getInstance().getMessageDetailDAO();
				MessageDetailPO mpo = new MessageDetailPO();
				mpo.setFrom_userId(0);
				mpo.setTo_userId(0);
				mpo.setMessage(msgDetail.getMessage());
				//Get Time stamp
				Date date= new Date();
		         //getTime() returns current time in milliseconds
				long time = date.getTime();
		         //Passed the milliseconds to constructor of Timestamp class 
				Timestamp ts = new Timestamp(time);
				mpo.setMessage_timestamp(ts);
				//mpo.setLocation(msgDetail.getLocation());
				
				mdao.testDBsave(mpo);		

		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		return ok();
	}
	

	/**
	 * This method loads all public wall messages in the system.
	 * 
	 * @return - List of all public wall messages.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/getpost")
	public List<MessageDetail> loadTestPublicWall() {
		Log.enter();

		List<MessageDetail> messageDetail = null;
		try {
			
			// LOAD THE STATUS MSG, TIMESTAMP, LOCATION 
			List<MessageDetailPO> msgPOs = DAOFactory.getInstance().getMessageDetailDAO().loadTestPublicWallMessages();
		
			messageDetail = new ArrayList<MessageDetail>();
				for(MessageDetailPO mPO : msgPOs)
				{       
					    MessageDetail mdto = new MessageDetail();
					    mdto.setFrom_userName("PUBLIC WALL");   
					    mdto.setTo_userName("PUBLIC WALL");
						mdto.setMessage(mPO.getMessage());
						mdto.setMessage_timestamp(mPO.getMessage_timestamp());
						//mdto.setLocation(mPO.getLocation());
										
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
