package edu.cmu.sv.ws.ssnoc.data.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.SSNCipher;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * This is a utility class to provide common functions to access and handle
 * Database operations.
 * 
 */
public class DBUtils {
	private static boolean DB_TABLES_EXIST = false;


	private static List<String> CREATE_TABLE_LST;

	static {
		CREATE_TABLE_LST = new ArrayList<String>();
		CREATE_TABLE_LST.add(SQL.CREATE_USERS);
		CREATE_TABLE_LST.add(SQL.CREATE_MESSAGE_DETAIL);
		CREATE_TABLE_LST.add(SQL.CREATE_MEMORY_TABLE);
	}

	/**
	 * This method will initialize the database.
	 * 
	 * @throws SQLException
	 */
	public static void initializeDatabase() throws SQLException {
		createTablesInDB();
	}
	
	public static void setDB_TABLES_EXIST(boolean dB_TABLES_EXIST) {
		DB_TABLES_EXIST = dB_TABLES_EXIST;
	}

	/**
	 * This method will create necessary tables in the database.
	 * 
	 * @throws SQLException
	 */
	protected static void createTablesInDB() throws SQLException {
		Log.enter();
		if (DB_TABLES_EXIST) {
			Log.info("I entered in the DB Tables existed method line 49");
			return;
		}

		final String CORE_TABLE1_NAME = SQL.SSN_USERS;

		final String CORE_TABLE2_NAME = SQL.SSN_MEMORY_TABLE;
		Connection conn = getConnection();
		try (Statement stmt = conn.createStatement();) {
			if (!doesTableExistInDB(conn, CORE_TABLE1_NAME) || !(doesTableExistInDB(conn, CORE_TABLE2_NAME))) {
				Log.info("Creating tables in database ...");

				for (String query : CREATE_TABLE_LST) {
					Log.debug("Executing query: " + query);
					boolean status = stmt.execute(query);
					Log.debug("Query execution completed with status: "
							+ status);
				}

				Log.info("Tables created successfully");
				insertAdmin(conn);
			} else {
				Log.info("Tables already exist in database. Not performing any action.");
			}
		}
		DB_TABLES_EXIST = true;
		Log.exit();
	}

	/**
	 * This method will check if the table exists in the database.
	 * 
	 * @param conn
	 *            - Connection to the database
	 * @param tableName
	 *            - Table name to check.
	 * 
	 * @return - Flag whether the table exists or not.
	 * 
	 * @throws SQLException
	 */
	public static boolean doesTableExistInDB(Connection conn, String tableName)
			throws SQLException {
		Log.enter(tableName);

		if (conn == null || tableName == null || "".equals(tableName.trim())) {
			Log.error("Invalid input parameters. Returning doesTableExistInDB() method with FALSE.");
			return false;
		}

		boolean tableExists = false;

		final String SELECT_QUERY = SQL.CHECK_TABLE_EXISTS_IN_DB;

		ResultSet rs = null;
		try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_QUERY)) {
			selectStmt.setString(1, tableName.toUpperCase());
			rs = selectStmt.executeQuery();
			int tableCount = 0;
			if (rs.next()) {
				tableCount = rs.getInt(1);
			}

			if (tableCount > 0) {
				tableExists = true;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		Log.exit(tableExists);

		return tableExists;
	}

	/**
	 * This method insert a admin into database
	 * 
	 * @throws SQLException
	 */
	public static void insertAdmin(Connection conn) throws SQLException {
		// create a admin when database created
		try (Statement adminStmt = conn
				.createStatement();) {
			Log.debug("Executing query: INSERT_ADMIN");
			UserPO temp = new UserPO();
			temp.setPassword("admin");
			SSNCipher.encryptPassword(temp);
			String sqlInsertAdmin = "insert into " + "SSN_USERS"
					+ " (user_name, password, salt, profession, role, accountStatus) values"
					+ " ('SSNAdmin', '" + temp.getPassword() + "', '" + temp.getSalt()
					+ "', 'Administrator', 'Administrator', 'Active')";
			boolean status = adminStmt.execute(sqlInsertAdmin);
			Log.debug("Query execution completed with status: " + status);
			Log.debug("Executing query: INSERT_ADMIN_STATUS");
			String sqlInsertAdminStatus = "insert into " + "SSN_MESSAGE_DETAIL" 
			+ " (from_user_id, to_user_id, message, message_timestamp) values (1,1,'OK', null)";
			status = adminStmt.execute(sqlInsertAdminStatus);
			Log.debug("Query execution completed with status: " + status);
			Log.info("Admin created successfully");
		}
	}

	/**
	 * This method returns a database connection from the Hikari CP Connection
	 * Pool
	 * 
	 * @return - Connection to the H2 database
	 * 
	 * @throws SQLException
	 */
	public static final Connection getConnection() throws SQLException {
		IConnectionPool cp = ConnectionPoolFactory.getInstance()
				.getH2ConnectionPool();
		
		return cp.getConnection();
	}
	
	public static final void closeConnection(Connection conn) throws SQLException{
		conn.close();
	}

	public static final void setUseTestDB(boolean useTestDB) {
		IConnectionPool cp = ConnectionPoolFactory.getInstance()
				.getH2ConnectionPool();
		cp.setUpTestDB(useTestDB);
	}
	
	public static final void closeTestConnection() throws SQLException{
		IConnectionPool cp = ConnectionPoolFactory.getInstance()
				.getH2ConnectionPool();
		cp.getConnection().close();
		
	}
	
	public static final Statement createStatement() {
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      return stmt;
	}
	
	
}
