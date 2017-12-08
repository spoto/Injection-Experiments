package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.UserCluster;

/**
 * DAO implementation for saving User information in the H2 database.
 * 
 */
public class UserDAOImpl extends BaseDAOImpl implements IUserDAO {
	/**
	 * This method will load users from the DB with specified account status. If
	 * no status information (null) is provided, it will load all users.
	 * 
	 * @return - List of users
	 */
	public List<UserPO> loadUsers() {
		Log.enter();

		String query = SQL.FIND_ALL_USERS;

		List<UserPO> users = new ArrayList<UserPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			users = processResults(stmt);
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(users);
		}

		return users;
	}

	private List<UserPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<UserPO> users = new ArrayList<UserPO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				UserPO po = new UserPO();
				po = new UserPO();
				po.setUserId(rs.getLong(1));
				po.setUserName(rs.getString(2));
				po.setPassword(rs.getString(3));
				po.setSalt(rs.getString(4));
				po.setProfession(rs.getString(5));
				po.setRole(rs.getString(6));
				po.setAccountStatus(rs.getString(7));
				po.setLatitude(rs.getDouble(8));
				po.setLongitude(rs.getDouble(9));
				users.add(po);
			}
			rs.close();
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}

	private List<Long> processResultsLong(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<Long> usersWithStatus = new ArrayList<Long>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				long l = rs.getLong(1);
				usersWithStatus.add(l);
			}
			rs.close();
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(usersWithStatus);
		}
		return usersWithStatus;
	}

	/**
	 * This method with search for a user by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 * 
	 * @param userName
	 *            - User name to search for.
	 * 
	 * @return - UserPO with the user information if a match is found.
	 */
	@Override
	public UserPO findByName(String userName) {
		Log.enter(userName);

		if (userName == null) {
			Log.warn("Inside findByName method with NULL userName.");
			return null;
		}

		UserPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_USER_BY_NAME)) {
			stmt.setString(1, userName.toUpperCase());

			List<UserPO> users = processResults(stmt);

			if (users.size() == 0) {
				Log.debug("No user account exists with userName = " + userName);
			} else {
				po = users.get(0);
			}
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(po);
		}

		return po;
	}

	/**
	 * This method with search for a user by his userName in the database.
	 * 
	 * @param userId
	 *            - User ID to search for.
	 * 
	 * @return - UserPO with the user information if a match is found.
	 */
	@Override
	public UserPO findById(long userId) {

		Log.enter(userId);

		UserPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_USER_BY_ID)) {
			stmt.setLong(1, userId);

			List<UserPO> users = processResults(stmt);

			if (users.size() == 0) {
				Log.debug("No user account exists with userId = " + userId);
			} else {
				po = users.get(0);
			}
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(po);
		}

		return po;

	}

	/**
	 * This method will save the information of the user into the database.
	 * 
	 * @param userPO
	 *            - User information to be saved.
	 */
	@Override
	public void save(UserPO userPO) {
		Log.enter(userPO);
		if (userPO == null) {
			Log.warn("Inside save method with userPO == NULL");
			return;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER)) {
			stmt.setString(1, userPO.getUserName());
			stmt.setString(2, userPO.getPassword());
			stmt.setString(3, userPO.getSalt());
			stmt.setString(4, userPO.getProfession());
			stmt.setString(5, "Citizen");
			stmt.setString(6, "Active");

			int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

	@Override
	public void deleteAllUsers() {
		Log.enter();

		String query = SQL.DELETE_ALL_USERS;

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			stmt.executeUpdate();
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
			Log.exit();
		}
	}

	/**
	 * This method will search for user names from the DB according to specified
	 * search user term.
	 * 
	 * @return - List of users
	 */
	public ArrayList<String> searchUserByName(String name) {
		Log.enter(name);

		ArrayList<String> userNames = new ArrayList<String>();
		if ((name == null || name.trim().isEmpty())) {
			Log.warn("NULL or Blank user name search.");
			return null;
		}
		String query = SQL.SEARCH_FOR_USERS_NAME;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			name = name.toUpperCase();
			name = "%" + name + "%";
			stmt.setString(1, name);

			Log.debug("Executing stmt = " + stmt);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					userNames.add(rs.getString(1));
				}
				rs.close();
			}

			if (userNames.size() == 0) {
				Log.info("No user names found with the given search term");
			}
			closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handleException(e);
		}

		return userNames;
	}

	/**
	 * This method will search for user names from the DB according to specified
	 * search status term.
	 * 
	 * @return - List of users
	 */
	public ArrayList<String> searchUserByStatus(String status) {
		ArrayList<String> userNames = new ArrayList<String>();
		UserDAOImpl user = new UserDAOImpl();
		if ((status == null || status.trim().isEmpty())) {
			Log.warn("NULL or Blank user status search.");
			return null;
		}
		String query = SQL.SEARCH_FOR_USERS_STATUS;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			stmt.setString(1, status);
			List<Long> userIds = new ArrayList<Long>();
			Log.debug("Executing stmt = " + stmt);
			userIds = processResultsLong(stmt);

			if (userIds.size() == 0) {
				Log.debug("No user ids exists with this status");
			} else {
				for (Long id : userIds) {
					UserPO userpo = new UserPO();
					userpo = user.findById(id);
					userNames.add(userpo.getUserName());
				}
			}
			closeConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			handleException(e);
		}

		return userNames;
	}

	/**
	 * This method will update the profile of the user in the database.
	 * 
	 * @param userPO
	 *            - User profile to be saved.
	 */
	public void updateProfile(String userName, UserPO userPO) {
		Log.enter(userPO);
		if (userPO == null) {
			Log.warn("Inside save method with userPO == NULL");
			return;
		}
		
		if ( userPO.getLatitude() != 0.0 && userPO.getLongitude() != 0.0) {
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_USER_PROFILE)) {
			stmt.setString(1, userPO.getUserName());
			stmt.setString(2, userPO.getPassword());
			stmt.setString(3, userPO.getSalt());
			stmt.setString(4, userPO.getRole());
			stmt.setDouble(5, userPO.getLatitude());
			stmt.setDouble(6, userPO.getLongitude());
			stmt.setString(7, userName);

			stmt.executeUpdate();
			Log.trace("Statement executed with new location");
			closeConnection(conn);
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		}
		else {
			try (Connection conn = getConnection();
					PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_USER_PROFILE_NO_LOCATION)) {
				stmt.setString(1, userPO.getUserName());
				stmt.setString(2, userPO.getPassword());
				stmt.setString(3, userPO.getSalt());
				stmt.setString(4, userPO.getRole());
				stmt.setString(5, userName);

				stmt.executeUpdate();
				Log.trace("Statement executed without new location");
				closeConnection(conn);
			} catch (SQLException e) {
				handleException(e);
			} finally {
				Log.exit();
			}
		}
	}


	/**
	 * This method will analyze the social Network and return the near by Users.
	 * Near By Users are those who haven't chatted in the past 'N' hours
	 * @param duration
	 *
	 */
	public ArrayList<UserCluster> getClusterNearByUsers(int duration){
		ArrayList<UserCluster> userCluster = null;
		SystemAnalysis sysAnalysis = new SystemAnalysis();
		List<MessageDetailPO> messages = null;

		List<Long> userIds = findAllUserIds();
		if(userIds == null)
			return null;
		else
			messages = sysAnalysis.getPrivateChatsPast(duration);

//		For every UserId create a set by removing users from a new initialized every time
		for (Long from_id : userIds) {
//			Get Cluster of each user
			List<Long> set = new ArrayList<Long>(userIds);
			List<String> clusterList = new ArrayList<String>();
			clusterList = sysAnalysis.setCloseUserClusters(set, messages, from_id, this);
			
//			Create Subsets and  do not add same sets
			boolean flag = false;
			if(!(clusterList.isEmpty())){
				if(userCluster == null){
					userCluster = new ArrayList<UserCluster>();
					flag = true;
				}
				ArrayList<UserCluster> temp = new ArrayList<UserCluster>(userCluster);
				for(UserCluster cluster: temp){
//					Clusters are same, don't do anything
					if(clusterList.containsAll(cluster.getUsersforCluster()) && 
							cluster.getUsersforCluster().containsAll(clusterList)){
						flag = false;
						break;
					}
//					New cluster is subset of existing cluster, replace the smaller one
					else if((!(clusterList.containsAll(cluster.getUsersforCluster())) &&
								cluster.getUsersforCluster().containsAll(clusterList)))
					{
						userCluster.remove(cluster);
						flag = true;
					}
//					Existing cluster is subset of new cluster so don't do anything
					else if	(clusterList.containsAll(cluster.getUsersforCluster()) &&
							(!(cluster.getUsersforCluster().containsAll(clusterList)))){
						flag = false;
						break;
					}
					else
						flag = true;
				}
				if(flag && clusterList.size() != 1)
				{
					UserCluster usercluster = new UserCluster();
					usercluster.setUserCluster(clusterList);
					userCluster.add(usercluster);
//					Only one cluster will exist if there is no exchange of messages
					if(messages.isEmpty())
						break;
				}
			}
		}
		return userCluster;
	}

	public List<Long> findAllUserIds(){
		List<Long> userIds = null;
		try {
			Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQL.FIND_ALL_USERS_ID);

			userIds = new ArrayList<Long>();
			userIds = processResultsLong(stmt);
			Log.trace("Got User Ids");

			if (userIds.size() == 1) {
				Log.debug("No user ids except Admin exists in the system");
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return userIds;
	}

}
