package edu.cmu.sv.ws.ssnoc.data.dao.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.UserCluster;

public class GetUserClusterTest {

	static DAOFactory dao;

	@BeforeClass
	public static void init() throws SQLException {
		DBUtils.setUseTestDB(true);
		DBUtils.createStatement().execute("DROP ALL OBJECTS DELETE FILES");
		DBUtils.setDB_TABLES_EXIST(false);
		DBUtils.initializeDatabase();
		DBUtils.createStatement().execute("TRUNCATE TABLE SSN_USERS");
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
	}
	
	@Before
	public void setUp() throws Exception {
		
		DBUtils.createStatement().execute("TRUNCATE TABLE SSN_MESSAGE_DETAIL");
		
	}

	@Test
	public void canGetUserClusterWhenThereIsNoPrivateChat() {
		int duration = 1;
		ArrayList<UserCluster> userCluster = new ArrayList<UserCluster>(dao.getUserDAO().getClusterNearByUsers(duration));
		assertEquals(1,userCluster.size());
	}
	
	@Test
	public void canGetUserClusterWhenThereIsAPrivateChat() {
		int duration = 1;
		MessageDetailPO message1 = new MessageDetailPO();
		message1.setFrom_userId(3);
		message1.setTo_userId(4);
//		message1.setLocation(" ");
		message1.setMessage("Hello");
		dao.getMessageDetailDAO().save(message1);
		
		ArrayList<UserCluster> userCluster = new ArrayList<UserCluster>(dao.getUserDAO().getClusterNearByUsers(duration));
		
		assertEquals(2,userCluster.size());
		assertEquals("[aparna, ming]",userCluster.get(0).getUsersforCluster().toString());
		assertEquals("[chang, ming]",userCluster.get(1).getUsersforCluster().toString());
	}
	
	@Test
	public void canGetUserClusterWhenEveryOneChatWithEachother() {
		int duration = 1;
		MessageDetailPO message1 = new MessageDetailPO();
		message1.setFrom_userId(3);
		message1.setTo_userId(4);
//		message1.setLocation(" ");
		message1.setMessage("Hello");
		dao.getMessageDetailDAO().save(message1);
		MessageDetailPO message2 = new MessageDetailPO();
		message2.setFrom_userId(2);
		message2.setTo_userId(3);
//		message2.setLocation(" ");
		message2.setMessage("Hi");
		dao.getMessageDetailDAO().save(message2);
		MessageDetailPO message3 = new MessageDetailPO();
		message3.setFrom_userId(2);
		message3.setTo_userId(4);
//		message3.setLocation(" ");
		message3.setMessage("How are you");
		dao.getMessageDetailDAO().save(message3);
		
		ArrayList<UserCluster> userCluster = new ArrayList<UserCluster>(dao.getUserDAO().getClusterNearByUsers(duration));
		
		assertEquals(0,userCluster.size());
		
	}
	
	@Test
	public void canGetPrivateChatPastByDuration() {
		int duration = 1;
		MessageDetailPO message1 = new MessageDetailPO();
		message1.setFrom_userId(3);
		message1.setTo_userId(4);
//		message1.setLocation(" ");
		message1.setMessage("Hello");
		dao.getMessageDetailDAO().save(message1);
		MessageDetailPO message2 = new MessageDetailPO();
		message2.setFrom_userId(2);
		message2.setTo_userId(3);
//		message2.setLocation(" ");
		message2.setMessage("Hello");
		dao.getMessageDetailDAO().save(message2);
		
		List<MessageDetailPO> messagepos = new ArrayList<MessageDetailPO>(dao.getSystemAnalysisDAO().getPrivateChatsPast(duration));
		
		assertEquals(2,messagepos.size());
	}
	
	@Test
	public void canGetAllPrivateChatPast() {
		int duration = 0;
		MessageDetailPO message1 = new MessageDetailPO();
		message1.setFrom_userId(3);
		message1.setTo_userId(4);
//		message1.setLocation(" ");
		message1.setMessage("Hello");
		dao.getMessageDetailDAO().save(message1);
		MessageDetailPO message2 = new MessageDetailPO();
		message2.setFrom_userId(2);
		message2.setTo_userId(3);
//		message2.setLocation(" ");
		message2.setMessage("Hello");
		dao.getMessageDetailDAO().save(message2);
		
		List<MessageDetailPO> messagepos = new ArrayList<MessageDetailPO>(dao.getSystemAnalysisDAO().getPrivateChatsPast(duration));
		
		assertEquals(2,messagepos.size());
	}
	
}
