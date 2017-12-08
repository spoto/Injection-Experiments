package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of Messages information in the system.
 * 
 */

public interface IMessageDetailDAO  {
	
	/**
	 * This method will save the message information into the database.
	 * 
	 * @param messageDetailPO
	 *            - Message information to be saved.
	 */
	void save(MessageDetailPO messageDetailPO);
	
	void deleteAllMessage();
	
	/**
	 * This method will load all the messages in the
	 * database.
	 * 
	 * @return - List of all messages.
	 */
	List<MessageDetailPO> loadStatuses();
	
	/**
	 * This method will load status for a userId in the
	 * database.
	 * 
	 * @return - message.
	 */
	
	MessageDetailPO findById(long userId);
	
	/**
	 * This method will load a message for a messageId in the
	 * database.
	 * 
	 * @return - message.
	 */
	
	MessageDetailPO findByMessageId(long messageId);
	
	/**
	 * This method will load all the public wall messages from the
	 * database.
	 * 
	 * @return - List of all messages.
	 */
	List<MessageDetailPO> loadPublicWallMessages();
	
	/**
	 * This method will load all the chat messages between two users 
	 * in the database.
	 * 
	 * @return - List of chat messages.
	 */
	List<MessageDetailPO> loadChatMessages(long userId1,long userId2);
	
	/**
	 * This method will load all users with whom a user has chatted with
	 * in the database by userId.
	 * 
	 * @return - List of users.
	 */
	List<UserPO> loadChatUsers(long userId);
	
	/**
	 * This method will save the message information into the test database.
	 * 
	 * @param messageDetailPO
	 *            - Message information to be saved.
	 */

	void testDBsave(MessageDetailPO messageDetailPO);
	
	/**
	 * This method will load all the test public wall messages from the
	 * database.
	 * 
	 * @return - List of all messages.
	 */

	List<MessageDetailPO> loadTestPublicWallMessages();
	
	/**
	 * This method will search all the public wall messages from the
	 * database.
	 * 
	 * @return - List of all messages.
	 */

	List<MessageDetailPO> searchForPublicMessage(String message);
	
	/**
	 * This method will search all the private chats messages from the
	 * database.
	 * 
	 * @return - List of all messages.
	 */

	List<MessageDetailPO> searchForPrivateChats(String message);
	
	/**
	 * This method will search all the public announcements messages from the
	 * database.
	 * 
	 * @return - List of all messages.
	 */

	List<MessageDetailPO> searchForPublicAnnouncements(String message);
	
	/**
	 * This method will load all the public announcement messages from the
	 * database.
	 * 
	 * @return - List of all messages.
	 */
	List<MessageDetailPO> loadPublicAnnouncementMessages();
	
	
}
