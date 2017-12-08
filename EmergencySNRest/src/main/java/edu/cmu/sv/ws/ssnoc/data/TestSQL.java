package edu.cmu.sv.ws.ssnoc.data;

/**
 * This class contains all the SQL related code that is used by the project.
 * Note that queries are grouped by their purpose and table associations for
 * easy maintenance.
 * 
 */
public class TestSQL {
	/*
	 * List the USERS table name, and list all queries related to this table
	 * here.
	 */
	public static final String SSN_TEST_USERS = "SSN_TEST_USERS";
	public static final String SSN_TEST_MESSAGE_DETAIL = "SSN_TEST_MESSAGE_DETAIL";

	/**
	 * Query to check if a given table exists in the H2 database.
	 */
	public static final String CHECK_TEST_TABLE_EXISTS_IN_DB = "SELECT count(1) as rowCount "
			+ " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA() "
			+ " AND UPPER(TABLE_NAME) = UPPER(?)";


	// ****************************************************************
	// All queries related to USERS
	// ****************************************************************
	/**
	 * Query to create the USERS table.
	 */
	public static final String CREATE_USERS = "create table IF NOT EXISTS "
			+ SSN_TEST_USERS + " ( user_id IDENTITY PRIMARY KEY,"
			+ " user_name VARCHAR(100)," + " password VARCHAR(512),"
			+ " salt VARCHAR(512) )";

	/**
	 * Query to load all users in the system.
	 */
	public static final String FIND_ALL_USERS = "select user_id, user_name, password,"
			+ " salt " + " from " + SSN_TEST_USERS + " order by user_name";

	/**
	 * Query to find a user details depending on his name. Note that this query
	 * does a case insensitive search with the user name.
	 */
	public static final String FIND_USER_BY_NAME = "select user_id, user_name, password,"
			+ " salt "
			+ " from "
			+ SSN_TEST_USERS
			+ " where UPPER(user_name) = UPPER(?)";
	
	/**
	 * Query to find a user details depending on his Id. 
	 */
	public static final String FIND_USER_BY_ID = "select user_id, user_name, password,"
			+ " salt "
			+ " from "
			+ SSN_TEST_USERS
			+ " where user_id = (?)";

	/**
	 * Query to insert a row into the users table.
	 */
	public static final String INSERT_USER = "insert into " + SSN_TEST_USERS
			+ " (user_name, password, salt) values (?, ?, ?)";
	
	// ****************************************************************
	// All queries related to MESSAGES
	// ****************************************************************
	
	/**
	 * Query to check if a given table exists in the H2 database.
	 */
	public static final String CREATE_MESSAGE_DETAIL = "create table IF NOT EXISTS "
			+ SSN_TEST_MESSAGE_DETAIL + " ( activity_id IDENTITY PRIMARY KEY,"
			+ " from_user_id VARCHAR(100)," + " to_user_id VARCHAR(512),"
			+ " message VARCHAR(512)," + " message_timestamp TIMESTAMP(8)," + " location VARCHAR(512))";
	

	/**
	 * Query to insert a row into the user message activity table.
	 */
	public static final String INSERT_MESSAGE_DETAIL = "insert into " + SSN_TEST_MESSAGE_DETAIL           
			+ " (from_user_id, to_user_id, message,message_timestamp,location) values (?,?,?,?,?)";
	
	/**
	 * Query to find a user details depending on his name. Note that this query
	 * does a case insensitive search with the user name.
	 */
	public static final String FIND_USER_STATUS = "select TOP 1 activity_id, from_user_id, to_user_id, message, message_timestamp, location"
			+ " from "
			+ SSN_TEST_MESSAGE_DETAIL
			+ " where from_user_id = (?) AND to_user_id = (?)"
			+ " order by message_timestamp DESC";
	
	
	public static final String FIND_ALL_USERS_STATUS = "select DISTINCT activity_id, from_user_id, to_user_id, message, message_timestamp, location"
			+ " from "
			+ SSN_TEST_MESSAGE_DETAIL
			+ " where from_user_id = to_user_id"
			+ " order by message_timestamp ASC";
	
	/**
	 * Query to load all message activity in the system.
	 */
	public static final String FIND_PUBLIC_WALL_MESSAGES = "select activity_id, from_user_id, to_user_id, message, message_timestamp, location"
			 + " from " + SSN_TEST_MESSAGE_DETAIL
			 + " where (from_user_id = to_user_id) OR (to_user_id = 0)"
			 + " order by message_timestamp";
	
	/**
	 * Query to find a chat message depending on messageId. 
	 */
	public static final String FIND_MESSAGE_BY_ID = "select TOP 1 activity_id, from_user_id, to_user_id, message, message_timestamp, location"
			+ " from "
			+ SSN_TEST_MESSAGE_DETAIL
			+ " where activity_id = (?)"
			+ " order by message_timestamp DESC";
	
	/**
	 * Query to load all chat messages between two users.
	 */
	public static final String FIND_CHAT_MESSAGES = "select activity_id, from_user_id, to_user_id, message, message_timestamp, location"
			 + " from " + SSN_TEST_MESSAGE_DETAIL
			 + " where (from_user_id = (?) AND to_user_id = (?)) OR (from_user_id = (?) AND to_user_id = (?))"
			 + " order by message_timestamp DESC";
	
	/**
	 * Query to find all users  with whom a user has chatted with by userId. 
	 */
	public static final String FIND_CHATUSERS_BY_USERID = "select distinct to_user_id"
			+ " from "
			+ SSN_TEST_MESSAGE_DETAIL
			//+ " where from_user_id = (?)";
			+ " where from_user_id = (?) AND to_user_id <> (?)";
	
	/**
	 * Query to empty out test message detail table. 
	 */
	public static final String RESET_TEST_USERS = "TRUNCATE TABLE " + SSN_TEST_USERS;
		
	/**
	 * Query to empty out test message detail table. 
	 */
	public static final String RESET_TEST_MESSAGE_DETAIL = "TRUNCATE TABLE " + SSN_TEST_MESSAGE_DETAIL;
	
	/**
	 * Query to empty out test message detail table. 
	 */
	public static final String DELETE_TEST_USERS = "DROP TABLE " + SSN_TEST_USERS;
	
	/**
	 * Query to empty out test message detail table. 
	 */
	public static final String DELETE_TEST_MESSAGE_DETAIL = "DROP TABLE " + SSN_TEST_MESSAGE_DETAIL;
}
