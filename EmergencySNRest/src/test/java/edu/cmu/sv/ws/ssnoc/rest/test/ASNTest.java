package edu.cmu.sv.ws.ssnoc.rest.test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.MessageDetail;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.rest.MessageService;
import edu.cmu.sv.ws.ssnoc.rest.SystemAnalysis;
import edu.cmu.sv.ws.ssnoc.rest.UserService;

public class ASNTest {
	
	MessageService messageService;
	UserService userService;
	SystemAnalysis asn;
	User user1;
	User user2;
	User user3;
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
	    asn = new  SystemAnalysis();

	}
	
	@Test
	public void canAnalyzeSocialNetwork() throws InterruptedException {
		user1 = new User();
		user2 = new User();
		user3 = new User();
		user1.setUserName("ming");
	    user1.setPassword("ming");
	    user2.setUserName("aparna");
	    user2.setPassword("aparna");
	    user3.setUserName("chang");
	    user3.setPassword("chang");
		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user3);
		MessageDetail message = new MessageDetail();
		message.setFrom_userName(user1.getUserName());
		message.setTo_userName(user2.getUserName());
		message.setMessage("Hello");
		message.setLocation(" ");
		
		response = messageService.sendChatMessage("ming", "aparna", message);
		int duration = 1;
		String json = asn.analyzeSocialNetwork(duration);

		assertEquals("[{\"userNames\":[\"ming\",\"chang\"]},{\"userNames\":[\"aparna\",\"chang\"]}]",json);
	}
	
	@Test
	public void canAnalyzeSocialNetworkUsersChatWithEachOther() throws InterruptedException {
		user1 = new User();
		user2 = new User();
		user3 = new User();
		user1.setUserName("ming");
	    user1.setPassword("ming");
	    user2.setUserName("aparna");
	    user2.setPassword("aparna");
	    user3.setUserName("chang");
	    user3.setPassword("chang");
		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user3);
		MessageDetail message1 = new MessageDetail();
		MessageDetail message2 = new MessageDetail();
		message1.setFrom_userName(user1.getUserName());
		message1.setTo_userName(user2.getUserName());
		message1.setMessage("Hello");
		message1.setLocation(" ");
		message2.setFrom_userName(user2.getUserName());
		message2.setTo_userName(user3.getUserName());
		message2.setMessage("Hi");
		message2.setLocation(" ");
		
		response = messageService.sendChatMessage("ming", "aparna", message1);
		response = messageService.sendChatMessage("aparna", "chang", message2);
		int duration = 1;
		String json = asn.analyzeSocialNetwork(duration);
		
		assertEquals("[{\"userNames\":[\"ming\",\"chang\"]}]",json);
	}
	
	@Test
	public void canAnalyzeSocialNetworkEveryoneChatWithEachOther() throws InterruptedException {
		user1 = new User();
		user2 = new User();
		user3 = new User();
		user1.setUserName("ming");
	    user1.setPassword("ming");
	    user2.setUserName("aparna");
	    user2.setPassword("aparna");
	    user3.setUserName("chang");
	    user3.setPassword("chang");
		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user3);
		MessageDetail message1 = new MessageDetail();
		MessageDetail message3 = new MessageDetail();
		message1.setFrom_userName(user1.getUserName());
		message1.setTo_userName(user2.getUserName());
		message1.setMessage("Hello");
		message1.setLocation(" ");
		message3.setFrom_userName(user1.getUserName());
		message3.setTo_userName(user3.getUserName());
		message3.setMessage("Hey");
		message3.setLocation(" ");
		
		response = messageService.sendChatMessage("ming", "aparna", message1);
		response = messageService.sendChatMessage("ming", "chang", message3);
		int duration = 1;
		String json = asn.analyzeSocialNetwork(duration);
		assertNull(json);
	}
	
	@Test
	public void canAnalyzeSocialNetworkWithOnlyOneUser() throws InterruptedException {
		user1 = new User();
		user1.setUserName("ming");
	    user1.setPassword("ming");
	    userService.addUser(user1);
	    
	    int duration = 1;
		String json = asn.analyzeSocialNetwork(duration);
		assertNull(json);
	}
	
	@Test
	public void canAnalyzeSocialNetworkWithNoPrivateChat() throws InterruptedException {
		user1 = new User();
		user2 = new User();
		user3 = new User();
		user1.setUserName("ming");
	    user1.setPassword("ming");
	    user2.setUserName("aparna");
	    user2.setPassword("aparna");
	    user3.setUserName("chang");
	    user3.setPassword("chang");
		userService.addUser(user1);
		userService.addUser(user2);
		userService.addUser(user3);
		MessageDetailPO message = new MessageDetailPO();
		message.setFrom_userId(2);
		message.setTo_userId(2);
//		message.setLocation(" ");
		message.setMessage("OK");

		dao.getMessageDetailDAO().save(message);
		
		int duration = 1;
		String json = asn.analyzeSocialNetwork(duration);		
		assertEquals("[{\"userNames\":[\"ming\",\"aparna\",\"chang\"]}]",json);
	}
}
