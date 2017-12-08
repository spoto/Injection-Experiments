package edu.cmu.sv.ws.ssnoc.data.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.TestSQL;

/**
 * This is a utility class to provide common functions to access and handle
 * Database operations.
 * 
 */
public class TestDBUtils {
	private static boolean TEST_DB_TABLES_EXIST = false;
	private static List<String> CREATE_TEST_TABLE_LST;
	private static List<String> RESET_TEST_TABLE_LST;
	private static List<String> DELETE_TEST_TABLE_LST;

	static {
		CREATE_TEST_TABLE_LST = new ArrayList<String>();
		CREATE_TEST_TABLE_LST.add(TestSQL.CREATE_USERS);
		CREATE_TEST_TABLE_LST.add(TestSQL.CREATE_MESSAGE_DETAIL);
		RESET_TEST_TABLE_LST = new ArrayList<String>();
		RESET_TEST_TABLE_LST.add(TestSQL.RESET_TEST_USERS);
		RESET_TEST_TABLE_LST.add(TestSQL.RESET_TEST_MESSAGE_DETAIL);
		DELETE_TEST_TABLE_LST = new ArrayList<String>();
		DELETE_TEST_TABLE_LST.add(TestSQL.DELETE_TEST_USERS);
		DELETE_TEST_TABLE_LST.add(TestSQL.DELETE_TEST_MESSAGE_DETAIL);
	}

	/**
	 * This method will initialize the test database.
	 * 
	 * @throws SQLException
	 */
	public static void initializeTestDatabase() throws SQLException {
		createTestTablesInDB();
	}
	

	/**
	 * This method will reset the test database.
	 * 
	 * @throws SQLException
	 */
	public static void resetTestDatabase() throws SQLException {
		resetTestTablesInDB();
	}
	
	/**
	 * This method will delete the test database.
	 * 
	 * @throws SQLException
	 */
	public static void deleteTestDatabase() throws SQLException {
		deleteTestTablesInDB();
	}

	/**
	 * This method will create necessary test tables in the database.
	 * 
	 * @throws SQLException
	 */
		protected static void createTestTablesInDB() throws SQLException {
		  Log.enter();
		if (TEST_DB_TABLES_EXIST) {
			return;
		}
	
		final String CORE_TABLE_NAME = TestSQL.SSN_TEST_USERS;
	
		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();) {
			if (!doesTestTableExistInDB(conn, CORE_TABLE_NAME)) {
				Log.info("Creating tables in database ...");
	
				for (String query : CREATE_TEST_TABLE_LST) {
					Log.debug("Executing query: " + query);
					boolean status = stmt.execute(query);
					Log.debug("Query execution completed with status: "
							+ status);
				}
	
				Log.info("Tables created successfully");
			} else {
				Log.info("Tables already exist in database. Not performing any action.");
			}
	
			TEST_DB_TABLES_EXIST = true;
		}
		Log.exit();
	}

	/**
	 * This method will check if the test table exists in the database.
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
	public static boolean doesTestTableExistInDB(Connection conn, String tableName)
			throws SQLException {
		Log.enter(tableName);

		if (conn == null || tableName == null || "".equals(tableName.trim())) {
			Log.error("Invalid input parameters. Returning doesTableExistInDB() method with FALSE.");
			return false;
		}

		boolean tableExists = false;

		final String SELECT_QUERY = TestSQL.CHECK_TEST_TABLE_EXISTS_IN_DB;

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
	
	/**
	 * This method resets the test database 
	 * @throws SQLException
	 */
	protected static void resetTestTablesInDB() throws SQLException {
		Log.enter();

		if (TEST_DB_TABLES_EXIST) {
			return;
		}
	
			final String CORE_TABLE_NAME = TestSQL.SSN_TEST_USERS;
			try (Connection conn = getConnection();
					Statement stmt = conn.createStatement();) {
			if (doesTestTableExistInDB(conn, CORE_TABLE_NAME)) {
			Log.info("Reseting test tables in database ...");

			for (String query : RESET_TEST_TABLE_LST) {
				Log.debug("Executing query: " + query);
				boolean status = stmt.execute(query);
				Log.debug("Query execution completed with status: "
						+ status);
			}

			Log.info("Reset Test Tables created successfully");
		} else {
			Log.info("No tables... Not performing any action.");
		}
	}		
	Log.exit();
  }
	
	/**
	 * This method deletes the test database 
	 * @throws SQLException
	 */
	protected static void deleteTestTablesInDB() throws SQLException {
		Log.enter();
		if (TEST_DB_TABLES_EXIST) {
			return;
		}
	
			final String CORE_TABLE_NAME = TestSQL.SSN_TEST_USERS;
			try (Connection conn = getConnection();
					Statement stmt = conn.createStatement();) {
			if (doesTestTableExistInDB(conn, CORE_TABLE_NAME)) {
			Log.info("Deleting test tables in database ...");

			for (String query : DELETE_TEST_TABLE_LST) {
				Log.debug("Executing query: " + query);
				boolean status = stmt.execute(query);
				Log.debug("Query execution completed with status: "
						+ status);
			}

			Log.info("Reset Test Tables created successfully");
		} else {
			Log.info("No tables... Not performing any action.");
		}
	}		
		Log.exit();
  }
	
}