package edu.cmu.sv.ws.ssnoc.rest.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.MessageDetail;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.rest.MessageService;
import edu.cmu.sv.ws.ssnoc.rest.UserService;

public class MessageServiceTest {

	MessageService messageService;
	UserService userService;
	User user1;
	User user2;
	Response response;
	DAOFactory dao;
	
	@BeforeClass
	public static void init() throws SQLException{
		DBUtils.setUseTestDB(true);
		DBUtils.createStatement().execute("DROP ALL OBJECTS DELETE FILES");
		DBUtils.setDB_TABLES_EXIST(false);
		DBUtils.initializeDatabase();
	}
	
	@Before
	public void setUp() throws Exception {
		messageService = new MessageService();
	    userService = new UserService();
	    DBUtils.getConnection().createStatement().execute("TRUNCATE TABLE SSN_USERS");
	    DBUtils.getConnection().createStatement().execute("TRUNCATE TABLE SSN_MESSAGE_DETAIL");
	    dao = DAOFactory.getInstance();
	    dao = DAOFactory.getInstance();
//	    DAOFactory.getInstance().getUserDAO().deleteAllUsers();
//		DAOFactory.getInstance().getMessageDetailDAO().deleteAllMessage();
		user1 = new User();
		user2 = new User();
		user1.setUserName("ming");
	    user1.setPassword("ming");
	    user2.setUserName("aparna");
	    user2.setPassword("aparna");
		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user1);
		userService.addUser(user2);
	}

	@Test
	public void canSendChatMessage() {
		MessageDetail message = new MessageDetail();
		message.setFrom_userName(user1.getUserName());
		message.setTo_userName(user2.getUserName());
		message.setMessage("Hello");
		message.setLocation(" ");
		
		response = messageService.sendChatMessage("ming", "aparna", message);
		assertEquals(201,response.getStatus());
//		
//		UserPO userpo1 = DAOFactory.getInstance().getUserDAO().findByName("ming");
//		UserPO userpo2 = DAOFactory.getInstance().getUserDAO().findByName("aparna");
//		MessageDetailPO messagepo = DAOFactory.getInstance().getMessageDetailDAO().loadChatMessages(userpo1.getUserId(),userpo2.getUserId()).get(0);
//		
//		assertEquals(userpo1.getUserId(),messagepo.getFrom_userId());
//		assertEquals(userpo2.getUserId(),messagepo.getTo_userId());
//		assertEquals(message.getMessage(),messagepo.getMessage());
//		
	}

	@Test
	public void canFindMessageByMessageId(){
		MessageDetail message = new MessageDetail();
		message.setFrom_userName(user1.getUserName());
		message.setTo_userName(user2.getUserName());
		message.setMessage("Hello");
		message.setLocation(" ");
		
		response = messageService.sendChatMessage("ming", "aparna", message);
		UserPO userpo1 = DAOFactory.getInstance().getUserDAO().findByName("ming");
		UserPO userpo2 = DAOFactory.getInstance().getUserDAO().findByName("aparna");
		MessageDetailPO messagepo = DAOFactory.getInstance().getMessageDetailDAO().loadChatMessages(userpo1.getUserId(),userpo2.getUserId()).get(0);
		
		MessageDetail findmessage = messageService.findMessage(messagepo.getMessageId());
		
		assertEquals(message.getFrom_userName(),findmessage.getFrom_userName());
		assertEquals(message.getTo_userName(),findmessage.getTo_userName());
		assertEquals(message.getMessage(),findmessage.getMessage());		
	}
	
//	@Test
//	public void cannotFindMessageByMessageId(){
//		MessageDetail message = new MessageDetail();
//		message.setFrom_userName(user1.getUserName());
//		message.setTo_userName(user2.getUserName());
//		message.setMessage("Hello");
//		message.setLocation(" ");
//		
//		response = messageService.sendChatMessage("ming", "aparna", message);
//		UserPO userpo1 = DAOFactory.getInstance().getUserDAO().findByName("ming");
//		UserPO userpo2 = DAOFactory.getInstance().getUserDAO().findByName("aparna");
//		MessageDetailPO messagepo = DAOFactory.getInstance().getMessageDetailDAO().loadChatMessages(userpo1.getUserId(),userpo2.getUserId()).get(0);
//		
//		MessageDetail findmessage = messageService.findMessage(-3);
//		
//		assertNull(findmessage);		
//	}
	
	@Test
	public void canAddPublicMessage(){
		MessageDetail message = new MessageDetail();
		message.setFrom_userName(user1.getUserName());
		message.setMessage("Hello Everyone");
		message.setLocation(" ");
		
		response = messageService.addPublicMessage(message);
		assertEquals(200,response.getStatus());
	}
//	/**
//	 * Do not allow to send empty messages
//	 */
//	@Test
//	public void canAddEmptyPublicMessage(){
//		MessageDetail message = new MessageDetail();
//		message.setFrom_userName(user1.getUserName());
//		message.setMessage("");
//		message.setLocation(" ");
//		
//		response = messageService.addPublicMessage(message);
//		assertNotEquals(Status.BAD_REQUEST, response.getStatus());
//	}
	
	@After
	public void cleanUp() throws Exception {

	}
}
