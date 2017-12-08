package edu.cmu.sv.ws.ssnoc.data;

/**
 * This class contains all the SQL related code that is used by the project.
 * Note that queries are grouped by their purpose and table associations for
 * easy maintenance.
 * 
 */
public class SQL {
	/*
	 * List the USERS table name, and list all queries related to this table
	 * here.
	 */
	public static final String SSN_USERS = "SSN_USERS";
	public static final String SSN_MESSAGE_DETAIL = "SSN_MESSAGE_DETAIL";
	public static final String SSN_MEMORY_TABLE = "SSN_MEMORY_TABLE";

	/**
	 * Query to check if a given table exists in the H2 database.
	 */
	public static final String CHECK_TABLE_EXISTS_IN_DB = "SELECT count(1) as rowCount "
			+ " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA() "
			+ " AND UPPER(TABLE_NAME) = UPPER(?)";

	// ****************************************************************
	// All queries related to USERS
	// ****************************************************************
	/**
	 * Query to create the USERS table.
	 */
	public static final String CREATE_USERS = "create table IF NOT EXISTS "
			+ SSN_USERS + " ( user_id IDENTITY NOT NULL AUTO_INCREMENT PRIMARY KEY,"
			+ " user_name VARCHAR(100)," + " password VARCHAR(512),"
			+ " salt VARCHAR(512)," + " profession VARCHAR(100)," + " role VARCHAR(100)," +" accountStatus VARCHAR(100)," +" latitude DOUBLE(12),"
			+ " longitude DOUBLE(12))";

	/**
	 * Query to load all users in the system.
	 */
	public static final String FIND_ALL_USERS = "select user_id, user_name, password,"
			+ " salt, profession, role, accountStatus, latitude, longitude" + " from " + SSN_USERS + " order by user_name";
	
	public static final String FIND_ALL_USERS_ID = "SELECT user_id FROM " + SSN_USERS + " ORDER BY user_id ASC";
	
	
	/**
	 * Query to find a user details depending on his name. Note that this query
	 * does a case insensitive search with the user name.
	 */
	public static final String FIND_USER_BY_NAME = "select user_id, user_name, password,"
			+ " salt, profession, role, accountStatus, latitude, longitude"
			+ " from "
			+ SSN_USERS
			+ " where UPPER(user_name) = UPPER(?)";
	
	/**
	 * Query to find a user details depending on his Id. 
	 */
	public static final String FIND_USER_BY_ID = "select user_id, user_name, password,"
			+ " salt, profession, role, accountStatus, latitude, longitude"
			+ " from "
			+ SSN_USERS
			+ " where user_id = (?)";

	/**
	 * Query to insert a row into the users table.
	 */
	public static final String INSERT_USER = "insert into " + SSN_USERS
			+ " (user_name, password, salt, profession, role, accountStatus) values (?, ?, ?, ?, ?, ?)";
	
	/**
	 * Query to delete all users in the H2 database.
	 */
	public static final String DELETE_ALL_USERS = "DELETE FROM "
			+ SSN_USERS;
	
	/**
	 * Query to update a user's profile in the H2 database.
	 */
	public static final String UPDATE_USER_PROFILE = "UPDATE " + SSN_USERS + " SET "
			+ "user_name = (?), password = (?), salt = (?), role = (?), latitude = (?), longitude = (?) "
			+ "WHERE user_name = (?)";
	
	/**
	 * Query to update a user's profile without changing location in the H2 database.
	 */
	public static final String UPDATE_USER_PROFILE_NO_LOCATION = "UPDATE " + SSN_USERS + " SET "
			+ "user_name = (?), password = (?), salt = (?), role = (?) "
			+ "WHERE user_name = (?)";
	
	// ****************************************************************
	// All queries related to SEARCH
	// ****************************************************************	
	/**
	 * Query to Search all users in the system with specific userName.
	 */
	public static final String SEARCH_FOR_USERS_NAME = "SELECT user_name FROM " + SSN_USERS
			+	" WHERE UPPER(user_name) LIKE (?)";
	
	/**
	 * Query to Search all users in the system with specific Status.
	 */
	public static final String SEARCH_FOR_USERS_STATUS = "SELECT from_user_id"
			+ " from "
			+ SSN_MESSAGE_DETAIL
			+ " WHERE message = (?)"
			+ " and"
			+ " message_timestamp in"
			+ " (SELECT max(message_timestamp)"
			+ " from "
			+ SSN_MESSAGE_DETAIL 
			+ " where from_user_id = to_user_id"
			+ " GROUP BY from_user_id)";
	
	
	// ****************************************************************
	// All queries related to MESSAGES
	// ****************************************************************
	
	/**
	 * Query to check if a given table exists in the H2 database.
	 */
	public static final String CREATE_MESSAGE_DETAIL = "create table IF NOT EXISTS "
			+ SSN_MESSAGE_DETAIL + " ( activity_id IDENTITY NOT NULL AUTO_INCREMENT PRIMARY KEY,"
			+ " from_user_id VARCHAR(100)," + " to_user_id VARCHAR(512),"
			+ " message VARCHAR(512)," + " message_timestamp TIMESTAMP(8))";
	

	/**
	 * Query to insert a row into the user message activity table.
	 */
	public static final String INSERT_MESSAGE_DETAIL = "insert into " + SSN_MESSAGE_DETAIL           
			+ " (from_user_id, to_user_id, message, message_timestamp) values (?,?,?,?)";
	
	/**
	 * Query to find a user details depending on his name. Note that this query
	 * does a case insensitive search with the user name.
	 */
	public static final String FIND_USER_STATUS = "select TOP 1 activity_id, from_user_id, to_user_id, message, message_timestamp"
			+ " from "
			+ SSN_MESSAGE_DETAIL
			+ " where from_user_id = (?) AND to_user_id = (?)"
			+ " order by message_timestamp DESC";
	
	
	public static final String FIND_ALL_USERS_STATUS = "select activity_id, from_user_id, to_user_id, message, message_timestamp"
			+ " from "
			+ SSN_MESSAGE_DETAIL
			+ " WHERE message_timestamp in"
			+ " (SELECT max(message_timestamp)"
			+ " from "
			+ SSN_MESSAGE_DETAIL 
			+ " where from_user_id = to_user_id"
			+ " GROUP BY from_user_id)";
	
	/**
	 * Query to load all message activity in the system.
	 */
	public static final String FIND_PUBLIC_WALL_MESSAGES = "select activity_id, from_user_id, to_user_id, message, message_timestamp"
			 + " from " + SSN_MESSAGE_DETAIL
			 + " where (from_user_id = to_user_id) OR (to_user_id = 0)"
			 + " order by message_timestamp DESC";
	
	/**
	 * Query to find a chat message depending on messageId. 
	 */
	public static final String FIND_MESSAGE_BY_ID = "select TOP 1 activity_id, from_user_id, to_user_id, message, message_timestamp"
			+ " from "
			+ SSN_MESSAGE_DETAIL
			+ " where activity_id = (?)"
			+ " order by message_timestamp DESC";
	
	/**
	 * Query to load all chat messages between two users.
	 */
	public static final String FIND_CHAT_MESSAGES = "select activity_id, from_user_id, to_user_id, message, message_timestamp"
			 + " from " + SSN_MESSAGE_DETAIL
			 + " where (from_user_id = (?) AND to_user_id = (?)) OR (from_user_id = (?) AND to_user_id = (?))"
			 + " order by message_timestamp DESC";
	
	/**
	 * Query to find all users  with whom a user has chatted with by userId. 
	 */
	public static final String FIND_CHATUSERS_BY_USERID = "select distinct to_user_id"
			+ " from "
			+ SSN_MESSAGE_DETAIL
			//+ " where from_user_id = (?)";
			+ " where from_user_id = (?) AND to_user_id <> (?) AND to_user_id <> 0 AND to_user_id <> -1";
	
	/**
	 * Query to find all users  with whom a user has chatted in the past 'N' hours. 
	 */
	public static final String FIND_CHAT_MESSAGES_BY_DUR  = "SELECT DISTINCT from_user_id, to_user_id" +
			" FROM " + SSN_MESSAGE_DETAIL +
			" WHERE (from_user_id <> to_user_id AND to_user_id <> 0 AND to_user_id <> -1 AND message_timestamp > (?))";
	
	/**
	 * Query to find all private chats. 
	 */
	public static final String FIND_ALL_CHAT_MESSAGES  = "SELECT DISTINCT from_user_id, to_user_id" +
			" FROM " + SSN_MESSAGE_DETAIL +
			" WHERE (from_user_id <> to_user_id AND to_user_id <> 0 AND to_user_id <> -1)";
	
	
	/**
	 * Query to delete all massages in the H2 database.
	 */
	public static final String DELETE_ALL_MESSAGES = "DELETE FROM "
			+ SSN_MESSAGE_DETAIL;
	
	    //****************************************************************
		// All queries related to PUBLIC ANNOUNCEMENTS
		// ****************************************************************

		/**
		 * Query to load all recent Public Announcements in the system.
		 */

		//public static final String FIND_PUBLIC_ANNOUNCEMENT_MESSAGES = "select DISTINCT activity_id, from_user_id, to_user_id, message, message_timestamp"
			//	+ " from " + SSN_MESSAGE_DETAIL
			//	+ " where (to_user_id = -1)"
			//	+ " group by from_user_id, activity_id"
			//	+ " order by message_timestamp DESC";
		
		public static final String FIND_PUBLIC_ANNOUNCEMENT_MESSAGES = "select TOP 5 activity_id,from_user_id, to_user_id, message, message_timestamp"
				+ " from " + SSN_MESSAGE_DETAIL
				+ " where (to_user_id = -1)"
				+ " order by message_timestamp DESC";
		
		 //****************************************************************
		// All queries related to MEASURE MEMORY
		// ****************************************************************
		
		/**
		 * Query to create memory table.
		 */

		public static final String CREATE_MEMORY_TABLE = "create table IF NOT EXISTS "
	            + SSN_MEMORY_TABLE +
	            " ( memory_id IDENTITY NOT NULL AUTO_INCREMENT PRIMARY KEY, usedVolatileMemory long, freeVolatileMemory long, usedNonVolatileMemory long, " +
	            "freeNonVolatileMemory long, createdAt varchar(20))";
		
		
		/**
		 * Query to create memory crumb.
		 */
		public static final String GET_MEMORY_STATS = "select usedVolatileMemory, freeVolatileMemory, usedNonVolatileMemory, freeNonVolatileMemory, " +
	            "createdAt from "+SSN_MEMORY_TABLE+" where createdAt between (?) and (?)"+" order by createdAt DESC";
		
		
		/**
		 * Query to insert a row in memory table.
		 */
		public static final String INSERT_MEMORY_STATS = "insert into " + SSN_MEMORY_TABLE           
				+ " (usedVolatileMemory, freeVolatileMemory, usedNonVolatileMemory, " +
	            "freeNonVolatileMemory, createdAt) values (?,?,?,?,?)";
		
		/**
		 * Query to delete memory table.
		 */
		public static final String DELETE_MEMORY_STATS = "delete from" + SSN_MEMORY_TABLE           
				+ " (usedVolatileMemory, freeVolatileMemory, usedNonVolatileMemory, " +
	            "freeNonVolatileMemory, createdAt) values (?,?,?,?,?)";
		
		
		
	
		/**
		 * Query to empty out test message detail table. 
		 */
		public static final String DELETE_TEST_USERS = "DROP TABLE " + SSN_USERS;
		
		/**
		 * Query to empty out test message detail table. 
		 */
		public static final String DELETE_TEST_MESSAGE_DETAIL = "DROP TABLE " + SSN_MESSAGE_DETAIL;
		
		/**
		 * Query to empty out test message detail table. 
		 */
		public static final String DELETE_TEST_MEMORY = "DROP TABLE " + SSN_MEMORY_TABLE;
		
}

	

   