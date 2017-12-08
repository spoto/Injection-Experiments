/**
 * UI test
 * 
 */

package edu.cmu.sv.ws.ssnoc.rest;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;


@Path("/test")
public class UITest extends BaseService{
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	@Path("/start")
	public String startUITest() {
		DBUtils.setUseTestDB(true);
		try {
			DBUtils.createStatement().execute("DROP ALL OBJECTS DELETE FILES");
			DBUtils.setDB_TABLES_EXIST(false);
			DBUtils.initializeDatabase();
			DBUtils.createStatement().execute("TRUNCATE TABLE SSN_USERS");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "OK";
	}
	
	@GET
	@Produces({ MediaType.TEXT_PLAIN })
	@Path("/end")
	public String endUITest(){
		Log.enter("stop testing UI");
		try {
			DBUtils.closeTestConnection();
			DBUtils.setUseTestDB(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "OK";
	}
}
