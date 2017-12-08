package edu.cmu.sv.ws.ssnoc.rest.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.MessageDetail;
import edu.cmu.sv.ws.ssnoc.rest.MessagesService;
import edu.cmu.sv.ws.ssnoc.rest.UserService;

public class MessagesServiceTest {
	
	MessagesService messagesService;
	UserService userService;
	UserPO userpo1;
	UserPO userpo2;
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
		DBUtils.getConnection().createStatement().execute("TRUNCATE TABLE SSN_USERS");
		DBUtils.getConnection().createStatement().execute("TRUNCATE TABLE SSN_MESSAGE_DETAIL");
		dao = DAOFactory.getInstance();
		messagesService = new MessagesService();
	    userService = new UserService();
	    
	    userpo1 = new UserPO();
		userpo2 = new UserPO();
		userpo1.setUserName("ming");
	    userpo1.setPassword("ming");
	    userpo2.setUserName("aparna");
	    userpo2.setPassword("aparna");
	    dao.getUserDAO().save(userpo1);
	    DAOFactory.getInstance().getUserDAO().save(userpo2);
	    userpo1 = dao.getUserDAO().findByName(userpo1.getUserName());
	    userpo2 = dao.getUserDAO().findByName(userpo2.getUserName());
	    
	}

	@Test
	public void canLoadMessagesBetweenUsers() {
		
		MessageDetailPO messagepo = new MessageDetailPO();
		messagepo.setFrom_userId(userpo1.getUserId());
		messagepo.setTo_userId(userpo2.getUserId());
		messagepo.setMessage("Hello");
		dao.getMessageDetailDAO().save(messagepo);
		
		
		List<MessageDetail> messages = messagesService.loadMessages(userpo1.getUserName(), userpo2.getUserName());
		assertEquals(1,messages.size());
	}
	
	@Test
	public void canLoadPublicMessagesInWall() {
		
		MessageDetailPO messagepo = new MessageDetailPO();
		messagepo.setFrom_userId(userpo1.getUserId());
		messagepo.setTo_userId(0);
		messagepo.setMessage("Hello Everyone");
		
		dao.getMessageDetailDAO().save(messagepo);
		
		List<MessageDetail> messages = messagesService.loadPublicWall();
		
		assertEquals(1,messages.size());
		assertEquals(userpo1.getUserName(),messages.get(0).getFrom_userName());
		assertEquals("PUBLIC WALL",messages.get(0).getTo_userName());
		assertEquals(messagepo.getMessage(),messages.get(0).getMessage());
		
	}
	
	@Test
	public void canLoadStatusMessagesInWall() {
		
		MessageDetailPO messagepo = new MessageDetailPO();
		messagepo.setFrom_userId(userpo1.getUserId());
		messagepo.setTo_userId(userpo1.getUserId());
		messagepo.setMessage("OK");
//		messagepo.setLocation(" ");
		
		dao.getMessageDetailDAO().save(messagepo);
		
		List<MessageDetail> messages = messagesService.loadPublicWall();
		
		assertEquals(1,messages.size());
		assertEquals(userpo1.getUserName(),messages.get(0).getFrom_userName());
		assertEquals(userpo1.getUserName(),messages.get(0).getTo_userName());
		assertEquals(messagepo.getMessage(),messages.get(0).getMessage());	
	}
	
	@After
	public void cleanUp() throws Exception {
		
	}

}
