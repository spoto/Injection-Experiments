package edu.cmu.sv.ws.ssnoc.data.dao.test;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;



public class LoadChatUsersTest {

	DAOFactory dao;
	
	@BeforeClass
	public static void init() throws SQLException{
		DBUtils.setUseTestDB(true);
		DBUtils.getConnection().createStatement().execute("DROP ALL OBJECTS DELETE FILES");
		DBUtils.setDB_TABLES_EXIST(false);
		DBUtils.initializeDatabase();
	}

	@Before
	public void setUp() throws Exception {
		DBUtils.createStatement().execute("TRUNCATE TABLE SSN_USERS");
		DBUtils.createStatement().execute("TRUNCATE TABLE SSN_MESSAGE_DETAIL");
		dao = DAOFactory.getInstance();
		UserPO user1 = new UserPO();
		UserPO user2 = new UserPO();
		UserPO user3 = new UserPO();
		user1.setUserName("ming");
		user1.setPassword("ming");
		user2.setUserName("aparna");
		user2.setPassword("aparna");
		user3.setUserName("chang");
		user3.setPassword("chang");
		dao.getUserDAO().save(user1);
		dao.getUserDAO().save(user2);
		dao.getUserDAO().save(user3);
		
		MessageDetailPO message1 = new MessageDetailPO();
		MessageDetailPO message2 = new MessageDetailPO();
		MessageDetailPO message3 = new MessageDetailPO();
		message1.setFrom_userId(1);
		message1.setTo_userId(0);
//		message1.setLocation(" ");
		message1.setMessage("Hello Everyone");
		message2.setFrom_userId(1);
		message2.setTo_userId(2);
//		message2.setLocation(" ");
		message2.setMessage("Hello");
		message3.setFrom_userId(1);
		message3.setTo_userId(3);
//		message3.setLocation(" ");
		message3.setMessage("Hi");
		dao.getMessageDetailDAO().save(message1);
		dao.getMessageDetailDAO().save(message2);
		dao.getMessageDetailDAO().save(message3);
		
	}

	@Test
	public void canLoadChatUsers() {
		
		List<UserPO> users = dao.getMessageDetailDAO().loadChatUsers(1);
		assertEquals(2, users.size());
	}
	
	@After
	public void cleanUp() throws Exception {
		
	}

}
