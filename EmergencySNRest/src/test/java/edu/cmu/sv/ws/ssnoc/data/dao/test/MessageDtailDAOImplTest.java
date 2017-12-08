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
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

public class MessageDtailDAOImplTest {
	
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
		DBUtils.createStatement().execute("TRUNCATE TABLE SSN_USERS");
		DBUtils.createStatement().execute("TRUNCATE TABLE SSN_MESSAGE_DETAIL");
		dao = DAOFactory.getInstance();
	}

	@Test
	public void canSaveMessage() {

		MessageDetailPO message = new MessageDetailPO();
		message.setFrom_userId(1);
		message.setTo_userId(1);
//		message.setLocation(" ");
		message.setMessage("OK");

		dao.getMessageDetailDAO().save(message);
		
		assertEquals(1,dao.getMessageDetailDAO().loadPublicWallMessages().size());
		assertEquals(message.getFrom_userId(),dao.getMessageDetailDAO().loadPublicWallMessages().get(0).getFrom_userId());	
		assertEquals(message.getTo_userId(),dao.getMessageDetailDAO().loadPublicWallMessages().get(0).getTo_userId());
		assertEquals(message.getMessage(),dao.getMessageDetailDAO().loadPublicWallMessages().get(0).getMessage());
	}

	@Test
	public void canDeleteAllMessage() {
		MessageDetailPO message = new MessageDetailPO();
		message.setFrom_userId(1);
		message.setTo_userId(1);
//		message.setLocation(" ");
		message.setMessage("OK");

		dao.getMessageDetailDAO().save(message);
		assertEquals(1,dao.getMessageDetailDAO().loadPublicWallMessages().size());
		
		dao.getMessageDetailDAO().deleteAllMessage();
		assertEquals(0,dao.getMessageDetailDAO().loadPublicWallMessages().size());		
	}

	@Test
	public void canloadStatus(){
		MessageDetailPO message1 = new MessageDetailPO();
		MessageDetailPO message2 = new MessageDetailPO();
		MessageDetailPO message3 = new MessageDetailPO();
		message1.setFrom_userId(1);
		message1.setTo_userId(1);
//		message1.setLocation(" ");
		message1.setMessage("OK");
		message2.setFrom_userId(2);
		message2.setTo_userId(2);
//		message2.setLocation(" ");
		message2.setMessage("HELP");
		message3.setFrom_userId(1);
		message3.setTo_userId(2);
//		message3.setLocation(" ");
		message3.setMessage("Hi");
		dao.getMessageDetailDAO().save(message1);
		dao.getMessageDetailDAO().save(message2);
		dao.getMessageDetailDAO().save(message3);
		List<MessageDetailPO> messagepos = dao.getMessageDetailDAO().loadStatuses();	
		//admin is always present in DB 
		assert(messagepos.size() > 0);	
	}

	@Test
	public void canFindStatusMessageByUserId() {	
		MessageDetailPO message = new MessageDetailPO();
		message.setFrom_userId(1);
		message.setTo_userId(1);
//		message.setLocation(" ");
		message.setMessage("HELP");
		dao.getMessageDetailDAO().save(message);
		
		MessageDetailPO statusmessagepo = dao.getMessageDetailDAO().findById(1);
		assertEquals(message.getFrom_userId(),statusmessagepo.getFrom_userId());
		assertEquals(message.getTo_userId(),statusmessagepo.getTo_userId());
		assertEquals(message.getMessage(),statusmessagepo.getMessage());
	}

	@Test
	public void canFindMessageByMessageId(){
		MessageDetailPO message = new MessageDetailPO();
		message.setFrom_userId(1);
		message.setTo_userId(1);
//		message.setLocation(" ");
		message.setMessage("OK");
		dao.getMessageDetailDAO().save(message);
		
		MessageDetailPO findmessage = dao.getMessageDetailDAO().loadStatuses().get(0);
		long id = findmessage.getMessageId();
		MessageDetailPO findmessagepoById = dao.getMessageDetailDAO().findByMessageId(id);
		
		assertEquals(message.getFrom_userId(),findmessagepoById.getFrom_userId());
		assertEquals(message.getTo_userId(),findmessagepoById.getTo_userId());
		assertEquals(message.getMessage(),findmessagepoById.getMessage());		
	}

	@Test
	public void canLoadPublicMessages(){
		MessageDetailPO message1 = new MessageDetailPO();
		MessageDetailPO message2 = new MessageDetailPO();
		MessageDetailPO message3 = new MessageDetailPO();
		message1.setFrom_userId(1);
		message1.setTo_userId(1);
//		message1.setLocation(" ");
		message1.setMessage("OK");
		message2.setFrom_userId(2);
		message2.setTo_userId(0);
//		message2.setLocation(" ");
		message2.setMessage("Hello");
		message3.setFrom_userId(1);
		message3.setTo_userId(2);
//		message3.setLocation(" ");
		message3.setMessage("Hi");

		dao.getMessageDetailDAO().save(message1);
		dao.getMessageDetailDAO().save(message2);
		dao.getMessageDetailDAO().save(message3);
		
		List<MessageDetailPO> messagepos = dao.getMessageDetailDAO().loadPublicWallMessages();
		assertEquals(2,messagepos.size());
	}

	@Test
	public void canLoadChatMessages(){
		MessageDetailPO message1 = new MessageDetailPO();
		MessageDetailPO message2 = new MessageDetailPO();
		MessageDetailPO message3 = new MessageDetailPO();
		message1.setFrom_userId(1);
		message1.setTo_userId(0);
//		message1.setLocation(" ");
		message1.setMessage("Hello Everyone");
		message2.setFrom_userId(2);
		message2.setTo_userId(1);
//		message2.setLocation(" ");
		message2.setMessage("Hello");
		message3.setFrom_userId(1);
		message3.setTo_userId(2);
//		message3.setLocation(" ");
		message3.setMessage("Hi");

		dao.getMessageDetailDAO().save(message1);
		dao.getMessageDetailDAO().save(message2);
		dao.getMessageDetailDAO().save(message3);
		
		List<MessageDetailPO> messagepos = dao.getMessageDetailDAO().loadChatMessages(1, 2);
		assertEquals(2,messagepos.size());
	}
	
	@Test
	 public void cannotSaveNullMessage(){
	 MessageDetailPO message = null;
	 dao.getMessageDetailDAO().save(message);
	 assertEquals(0,dao.getMessageDetailDAO().loadPublicWallMessages().size());
	 }
	
	@After
	public void cleanUp() throws Exception {
		
	}

}
