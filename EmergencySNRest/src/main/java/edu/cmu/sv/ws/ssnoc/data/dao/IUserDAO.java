package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.UserCluster;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of User information in the system.
 * 
 */
public interface IUserDAO {
	/**
	 * This method will save the information of the user into the database.
	 * 
	 * @param userPO
	 *            - User information to be saved.
	 */
	void save(UserPO userPO);
	
	void deleteAllUsers();

	/**
	 * This method will load all the users in the
	 * database.
	 * 
	 * @return - List of all users.
	 */
	List<UserPO> loadUsers();

	/**
	 * This method with search for a user by his userName in the database. 
	 * 
	 * @param userName
	 *            - User name to search for.
	 * 
	 * @return - UserPO with the user information if a match is found.
	 */
	UserPO findByName(String userName);
	
	/**
	 * This method with search for a user by his userName in the database. 
	 * 
	 * @param userId
	 *            - User ID to search for.
	 * 
	 * @return - UserPO with the user information if a match is found.
	 */
	UserPO findById(long userId);
	
	/**
	 * This method with search for a user by his userName or status in the database. 
	 * 
	 * @param name
	 *            - name substring to search for.
	 * @return - List of users with name as substring.
	 */
	ArrayList<String> searchUserByName(String name);
 
	/**
	 * This method with search for a user by his userName or status in the database. 
	 * 
	 * @param name
	 *            - status substring to search for.
	 * @return - List of users with status as substring.
	 */
	ArrayList<String> searchUserByStatus(String status);
	
	/**
	 * This method will update the profile of the user in the database.
	 * 
	 * @param userPO
	 *            - User profile to be saved.
	 */
	public void updateProfile(String userName, UserPO userPO);
	
	/**
	 * Get all the user IDs from the database
	 */
	List<Long> findAllUserIds();
	
	/**
	 * This method will analyze the social Network and return the near by Users.
	 * Near By Users are those who haven't chatted in the past 'N' hours
	 * @param duration
	 *
	 */
	ArrayList<UserCluster> getClusterNearByUsers(int duration);
}
