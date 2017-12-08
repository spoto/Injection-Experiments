package edu.cmu.sv.ws.ssnoc.data.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.PropertyUtils;

/**
 * This class is the Connection Pool implementation for the H2 database.
 * 
 */
public class H2ConnectionPoolImpl implements IConnectionPool {
	private static H2ConnectionPoolImpl instance = null;
	private HikariDataSource ds = null;
	private HikariDataSource tds = null;
	boolean useTestDB;

	static {
		try {
			Log.info("Initializing the connection pool ... ");
			instance = new H2ConnectionPoolImpl();
			Log.info("Connection pool initialized successfully.");
		} catch (Exception e) {
			Log.error(
					"Exception when trying to initialize the connection pool",
					e);
		}
	}

	/**
	 * Constructor to initialize H2 connection pool.
	 */
	private H2ConnectionPoolImpl() {
		HikariConfig config = new HikariConfig();
		config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
		config.addDataSourceProperty("URL", PropertyUtils.DB_CONN_URL);
		config.addDataSourceProperty("user", PropertyUtils.DB_USERNAME);
		config.addDataSourceProperty("password", PropertyUtils.DB_PASSWORD);

		ds = new HikariDataSource(config);
		ds.setMaximumPoolSize(PropertyUtils.DB_CONNECTION_POOL_SIZE);
		
	}

	public static H2ConnectionPoolImpl getInstance() {
		return instance;
	}

	/**
	 * This method will return the connection object to the database.
	 */
	public Connection getConnection() throws SQLException {
		Log.enter("useTestDBFlag is " + useTestDB);
	  if(useTestDB){
		  if (tds == null) {
				Log.error("HikariDataSource is NULL. Nooooooooooo :)");
				return null;
			}

			return tds.getConnection();
		  }
	  else {
		if (ds == null) {
			Log.error("HikariDataSource is NULL. Nooooooooooo :)");
			return null;
		}

		return ds.getConnection();
	  }
	}


	
	/*
	 * This method tells the value of the useTestDB	
	 */
	public boolean isUseTestDB() {
		return useTestDB;
	}

	/**
	 * This method sets the useTestDB value 
	 */

	@Override
	public void setUpTestDB(boolean useTestDB) {
		this.useTestDB = useTestDB;
		if(useTestDB){
			if (tds != null) {
				tds.close();
			}
			
			HikariConfig config = new HikariConfig();
			config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
			config.addDataSourceProperty("URL", PropertyUtils.DB_CONN_URL + "Test"+ System.currentTimeMillis());
			config.addDataSourceProperty("user", PropertyUtils.DB_USERNAME);
			config.addDataSourceProperty("password", PropertyUtils.DB_PASSWORD);

			tds = new HikariDataSource(config);
			tds.setMaximumPoolSize(PropertyUtils.DB_CONNECTION_POOL_SIZE);
		}	
		
	}
	
}
