package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;


/**
 * DAO implementation for saving User information in the H2 database.
 * 
 */
public class MemoryDAOImpl extends BaseDAOImpl implements IMemoryDAO {

	@Override
	public void insertMemoryStats(MemoryPO memDetails) {
		if (memDetails == null) {
            Log.warn("NULL object sent");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_MEMORY_STATS)) {
            stmt.setLong(1, memDetails.getUsedVolatile());
            stmt.setLong(2, memDetails.getFreeVolatile());
            stmt.setLong(3, memDetails.getUsedNonVolatile());
            stmt.setLong(4, memDetails.getFreeNonVolatile());
            stmt.setString(5,memDetails.getCreatedAt());
//            stmt.setInt(6, memDetails.getMinutes());

            int rowCount = stmt.executeUpdate();
            Log.trace("No. of rows inserted: " + rowCount);
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
	}

	@Override
	public void deleteMemoryCrumbData() {
	       boolean status = false;

	       String truncateTable = SQL.DELETE_MEMORY_STATS;
	       try (Connection conn = getConnection();
	            Statement stmt = conn.createStatement();) {
	           Log.debug("Executing query: " + stmt);
	           status = stmt.execute(truncateTable);
	           Log.debug("Query execution completed with status: " + status);
	           Log.info("Data truncated successfully");
	       } catch (SQLException e) {
	           handleException(e);
	           Log.exit(status);
	       }
	}


	public List<MemoryPO> getMemoryStats(String toDate, String fromDate) {
		String query = SQL.GET_MEMORY_STATS;

        List<MemoryPO> memstatsPO = new ArrayList<MemoryPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, fromDate);
            stmt.setString(2, toDate);
            memstatsPO = processResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(memstatsPO);
        }

        return memstatsPO;
	}

	private List<MemoryPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processResults method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<MemoryPO> memStats = new ArrayList<MemoryPO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MemoryPO po = new MemoryPO();
                po.setUsedVolatile(rs.getLong(1));
                po.setFreeVolatile(rs.getLong(2));
                po.setUsedNonVolatile(rs.getLong(3));
                po.setFreeNonVolatile(rs.getLong(4));
                po.setCreatedAt(rs.getString(5));
//                po.setMinutes(rs.getInt(6));

                memStats.add(po);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(memStats);
        }

        return memStats;
	}

}
