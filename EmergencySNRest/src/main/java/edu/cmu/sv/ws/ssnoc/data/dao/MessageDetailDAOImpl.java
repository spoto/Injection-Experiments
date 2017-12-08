package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.PropertyUtils;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.TestSQL;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * DAO implementation for saving Message information in the H2 database.
 * 
 */

public class MessageDetailDAOImpl extends BaseDAOImpl implements IMessageDetailDAO {

	public static List<MessageDetailPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				MessageDetailPO mpo = new MessageDetailPO();
				mpo.setMessageId(rs.getLong(1));
				mpo.setFrom_userId(rs.getLong(2));
				mpo.setTo_userId(rs.getLong(3));
				mpo.setMessage(rs.getString(4));
				mpo.setMessage_timestamp(rs.getTimestamp(5));
//				mpo.setLocation(rs.getString(6));
//				mpo.setLatitude(rs.getDouble(6));
//				mpo.setLongitude(rs.getDouble(7));

				messages.add(mpo);
			}
			rs.close();
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(messages);
		}
		return messages;
	}

	private List<Long> processResultsLong(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<Long> messages = new ArrayList<Long>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				long l = rs.getLong(1);
				messages.add(l);
			}
			rs.close();
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(messages);
		}
		return messages;
	}

	// process all information in UserService wrt to the MessageDEtail PO
	/**
	 * This method will load messages from the DB 
	 * 
	 * @return - List of messages
	 */
	public List<MessageDetailPO> loadStatuses() {
		Log.enter();

		String query = SQL.FIND_ALL_USERS_STATUS;

		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			messages = processResults(stmt);
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}

		return messages;                               
	}

	/**
	 * This method clean up all messages in the DB 
	 * 
	 */
	public void deleteAllMessage() {
		Log.enter();

		String query = SQL.DELETE_ALL_MESSAGES;

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			stmt.execute();
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit();
		}                              
	}

	/**
	 * This method will save the information into the database.
	 * 
	 * @param userPO
	 *            - User information to be saved.
	 */                                                                          
	@Override
	public void save(MessageDetailPO messageDetailPO) {
		Log.enter(messageDetailPO);
		if (messageDetailPO == null) {
			Log.warn("Inside save method with messageDetailPO == NULL");
			return;
		}
		java.util.Date date= new java.util.Date();
		Timestamp timeStmp = new Timestamp(date.getTime());
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_MESSAGE_DETAIL)) {
			stmt.setLong(1, messageDetailPO.getFrom_userId());
			stmt.setLong(2, messageDetailPO.getTo_userId());
			stmt.setString(3, messageDetailPO.getMessage());
			stmt.setTimestamp(4, timeStmp );
//			stmt.setDouble(5, messageDetailPO.getLatitude());
//			stmt.setDouble(6, messageDetailPO.getLongitude());

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	/**
	 * This method with search for a user's status, location and time of update by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 * 
	 * @param userName
	 *            - User name to search for.
	 * 
	 * @return - UserPO with the user information if a match is found.
	 */
	@Override
	public MessageDetailPO findById(long userId) {
		Log.enter(userId);

		MessageDetailPO mpo = null;
		try
		{
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_USER_STATUS);
			stmt.setLong(1, userId);
			stmt.setLong(2, userId);

			List<MessageDetailPO> statusMessage = processResults(stmt);

			if (statusMessage.size() == 0) {
				Log.debug("No status exists with userId = " + userId);
			} else {
				mpo = statusMessage.get(0);
			}
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(mpo);
		}

		return mpo;
	}

	@Override
	public List<MessageDetailPO> loadPublicWallMessages() {
		Log.enter();

		String query = SQL.FIND_PUBLIC_WALL_MESSAGES;

		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			messages = processResults(stmt);
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}
		return messages;    
	}

	/**
	 * This method with search for a chat message by the messageId in the database. The
	 * search performed is a case insensitive search to allow case mismatch situations.
	 * 
	 * @param messageId
	 *            - messageId to search for.
	 * 
	 * @return - MessageDetailPO if a match is found.
	 */
	@Override
	public MessageDetailPO findByMessageId(long messageId) {
		Log.enter(messageId);

		MessageDetailPO mpo = new MessageDetailPO();
		try
		{
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_MESSAGE_BY_ID);
			stmt.setLong(1, messageId);

			List<MessageDetailPO> Message = processResults(stmt);

			if (Message.size() == 0) {
				Log.debug("No status exists with messageId = " + messageId);
			} else {
				mpo = Message.get(0);
			}
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(mpo);
		}

		return mpo;
	}

	/**
	 * This method with search for a chat messages between two users in the database. The
	 * search performed is a case insensitive search to allow case mismatch situations.
	 * 
	 * @param userId1
	 * @param userId2
	 * 
	 * @return - MessageDetailPO if a match is found.
	 */
	@Override
	public List<MessageDetailPO> loadChatMessages(long userId1,long userId2) {
		Log.enter(userId1,userId2);

		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		try
		{
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_CHAT_MESSAGES);
			stmt.setLong(1, userId1);
			stmt.setLong(2, userId2);
			stmt.setLong(3, userId2);
			stmt.setLong(4, userId1);

			messages = processResults(stmt);

			if (messages.size() == 0) {
				Log.debug("No chat massage exists between "+userId1+" and "+userId2);
			} 
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}
		return messages;
	}

	/**
	 * This method load all users with whom a user has chatted with
	 * in the database by userId.
	 * 
	 * @param userId
	 *            - User Id to search for.
	 * 
	 * @return - UserPO with the user information.
	 */
	@Override
	public List<UserPO> loadChatUsers(long userId){
		Log.enter(userId);
		List<UserPO> users = new ArrayList<UserPO>();
		UserDAOImpl user = new UserDAOImpl();

		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_CHATUSERS_BY_USERID);
			stmt.setLong(1, userId);
			stmt.setLong(2, userId);
			List<Long> userIds = new ArrayList<Long>();
			userIds  = processResultsLong(stmt);
			for(Long id : userIds ){
				UserPO userpo = new UserPO();
				userpo = user.findById(id);
				users.add(userpo);
			}
			if (users.size() == 0){
				Log.debug("No chat user exists with "+userId);
			} 
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(users);
		}

		return users;
	}


	/**
	 * This method will save the information into the database.
	 * 
	 * @param userPO
	 *            - User information to be saved.
	 */                                                                          
	@Override
	public void testDBsave(MessageDetailPO messageDetailPO) {
		Log.enter(messageDetailPO);
		if (messageDetailPO == null) {
			Log.warn("Inside save method with messageDetailPO == NULL");
			return;
		}
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(TestSQL.INSERT_MESSAGE_DETAIL)) {
			stmt.setLong(1, messageDetailPO.getFrom_userId());
			stmt.setLong(2, messageDetailPO.getTo_userId());
			stmt.setString(3, messageDetailPO.getMessage());
			stmt.setTimestamp(4, messageDetailPO.getMessage_timestamp());
			stmt.setString(5, " ");

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	/*
	 * This method will load the test messages on the wall
	 * 
	 */
	@Override
	public List<MessageDetailPO> loadTestPublicWallMessages() {
		Log.enter();

		String query = TestSQL.FIND_PUBLIC_WALL_MESSAGES;

		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			messages = processResults(stmt);
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}

		return messages;    
	}


	/**
	 * This method will search for public from the DB according to specified
	 * search term.
	 * 
	 * @return - List of messages
	 */
	public List<MessageDetailPO> searchForPublicMessage(String message) {
		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		if (message == null || message.trim().isEmpty()) {
			Log.warn("NULL or Blank message search.");
			return null;
		}
		Log.enter(message);
		// Creating the query here
		StringBuffer SQL_Query = new StringBuffer();
		boolean flag = false;
		SQL_Query.append("SELECT activity_id, from_user_id, to_user_id, message, message_timestamp FROM ");
		SQL_Query.append("SSN_MESSAGE_DETAIL ");
		SQL_Query.append("WHERE ((from_user_id = to_user_id) OR (to_user_id = 0)) AND (");
		message = message.toUpperCase();
		String[] searchWordsList = message.split(" ");
		Set<String> stopSearchWords = PropertyUtils.STOP_WORDS_FOR_SEARCH;
		for (String str : searchWordsList) {
			if (!(stopSearchWords.contains(str))) {
				for(String stop_word: stopSearchWords){
					if(!(str.contains(stop_word)))	{
						SQL_Query.append("UPPER(message) LIKE '%" +str+ "%'  OR " );
						flag = true;
						break;
					}
					else
						break;
				}
			}
		}
		String queryMinusOR = SQL_Query.substring(0, (SQL_Query.length()-5));	
		StringBuffer updatedSQLQuery = new StringBuffer(queryMinusOR);
		if(flag){
			updatedSQLQuery.append(") ORDER BY message_timestamp ASC");
			System.out.println(updatedSQLQuery.toString());
		}
		else{
			Log.info("Do not search !!! All Stop Words in Query Sent");
			return null;
		}

		try{ 
			Connection conn = getConnection();
			String str = updatedSQLQuery.toString();
			System.out.println(str);
			PreparedStatement stmt = conn.prepareStatement(str);
			messages = processResults(stmt);
			if (messages.size() == 0) {
				Log.debug("No messages retrieved with this query");
			}
			closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handleException(e);
		}

		return messages;
	}

	/**
	 * This method will search for private chat from the DB according to specified
	 * search term.
	 * 
	 * @return - List of users
	 */
	public List<MessageDetailPO> searchForPrivateChats(String message) {

		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		if (message == null || message.trim().isEmpty()) {
			Log.warn("NULL or Blank message search.");
			return null;
		}
		Log.enter(message);
		// Creating the query here
		StringBuffer SQL_Query = new StringBuffer();
		boolean flag = false;
		SQL_Query.append("SELECT activity_id, from_user_id, to_user_id, message, message_timestamp FROM ");
		SQL_Query.append("SSN_MESSAGE_DETAIL ");
		SQL_Query.append("WHERE ((from_user_id <> to_user_id) AND (to_user_id <> 0) AND (to_user_id <> -1)) AND (");
		message = message.toUpperCase();
		String[] searchWordsList = message.split(" ");
		Set<String> stopSearchWords = PropertyUtils.STOP_WORDS_FOR_SEARCH;
		for (String str : searchWordsList) {
			if (!(stopSearchWords.contains(str))) {
				for(String stop_word: stopSearchWords){
					if(!(str.contains(stop_word)))	{
						SQL_Query.append("UPPER(message) Like '%" +str+ "%'  OR " );
						flag = true;
						break;
					}
					else
						break;
				}

			}
		}
		String queryMinusOR = SQL_Query.substring(0, (SQL_Query.length()-5));	
		StringBuffer updatedSQLQuery = new StringBuffer(queryMinusOR);
		if(flag){
			updatedSQLQuery.append(") ORDER BY message_timestamp ASC");
			System.out.println(updatedSQLQuery.toString());
		}
		else{
			Log.info("Do not search !!! All Stop Words in Query Sent");
			return null;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(updatedSQLQuery
						.toString());) {
			messages = processResults(stmt);
			if (messages.size() == 0) {
				Log.debug("No messages retrieved with this query");
			}
			closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handleException(e);
		}

		return messages;
	}





	/**
	 * This method will search for private chat from the DB according to specified
	 * search term.
	 * 
	 * @return - List of users
	 */
	public List<MessageDetailPO> searchForPublicAnnouncements(String message) {

		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		if (message == null || message.trim().isEmpty()) {
			Log.warn("NULL or Blank message search.");
			return null;
		}
		Log.enter(message);
		// Creating the query here
		StringBuffer SQL_Query = new StringBuffer();
		boolean flag = false;
		SQL_Query.append("SELECT activity_id, from_user_id, to_user_id, message, message_timestamp FROM ");
		SQL_Query.append("SSN_MESSAGE_DETAIL ");
		SQL_Query.append("WHERE to_user_id = -1 AND ");
		message = message.toUpperCase();
		String[] searchWordsList = message.split(" ");
		Set<String> stopSearchWords = PropertyUtils.STOP_WORDS_FOR_SEARCH;
		for (String str : searchWordsList) {
			if (!(stopSearchWords.contains(str))) {
				for(String stop_word: stopSearchWords){
					if(!(str.contains(stop_word)))	{
						SQL_Query.append("UPPER(message) Like '%" +str+ "%'  OR " );
						flag = true;
						break;
					}
					else
						break;
				}

			}
		}
		String queryMinusOR = SQL_Query.substring(0, (SQL_Query.length()-5));	
		StringBuffer updatedSQLQuery = new StringBuffer(queryMinusOR);
		if(flag){
			updatedSQLQuery.append(" ORDER BY message_timestamp ASC");
			System.out.println(updatedSQLQuery.toString());
		}
		else{
			Log.info("Do not search !!! All Stop Words in Query Sent");
			return null;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(updatedSQLQuery
						.toString());) {
			messages = processResults(stmt);
			if (messages.size() == 0) {
				Log.debug("No messages retrieved with this query");
			}
			closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handleException(e);
		}

		return messages;
	}


	public List<MessageDetailPO> loadPublicAnnouncementMessages() {
		Log.enter("Loading Public Announcments");

		String query = SQL.FIND_PUBLIC_ANNOUNCEMENT_MESSAGES;

		List<MessageDetailPO> messages = new ArrayList<MessageDetailPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			messages = processResults(stmt);
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(messages);
		}

		return messages;
	}

}
