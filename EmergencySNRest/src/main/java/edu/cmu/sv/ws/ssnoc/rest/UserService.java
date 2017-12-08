package edu.cmu.sv.ws.ssnoc.rest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.h2.util.StringUtils;

import edu.cmu.sv.ws.ssnoc.common.exceptions.ServiceException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnauthorizedUserException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ValidationException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.SSNCipher;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDetailDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessageDetailPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;

/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to users.
 * 
 */

@Path("/user")
public class UserService extends BaseService {
	public Boolean doDetails = false;
	/**
	 * This method checks the validity of the user name and if it is valid, adds
	 * it to the database
	 * 
	 * @param user
	 *            - An object of type User
	 * @return - An object of type Response with the status of the request
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/signup")
	public Response addUser(User user) {
		Log.enter(user);
		User resp = new User();

		try {
			IUserDAO dao = DAOFactory.getInstance().getUserDAO();
			UserPO existingUser = dao.findByName(user.getUserName());

			// Validation to check that user name should be unique
			// in the system. If a new users tries to register with
			// an existing userName, notify that to the user.
			if (existingUser != null) {
				Log.trace("User name provided already exists. Validating if it is same password ...");
				String[] detCheck = {user.getPassword(), existingUser.getUserName()};
				Boolean bVal = ((doDetails) ? validateUserPassword(detCheck) : validateUserPassword(user.getPassword(), existingUser));
				if (!bVal) {
					Log.warn("Password is different for the existing user name.");
					throw new ValidationException("User name already taken");
				} else {
					Log.debug("Yay!! Password is same for the existing user name.");

					resp.setUserName(existingUser.getUserName());
					return ok(resp);
				}
			}
			// if(PropertyUtils.INVALID_NAMES.contains(user.getUserName()){

			// }
			UserPO po = ConverterUtils.convert(user);
			po = SSNCipher.encryptPassword(po);

			dao.save(po);
			UserPO justAddedUser = dao.findByName(user.getUserName());
			IMessageDetailDAO mdao = DAOFactory.getInstance()
					.getMessageDetailDAO();
			java.util.Date date = new java.util.Date();
			Timestamp timeStmp = new Timestamp(date.getTime());

			MessageDetailPO mpo = new MessageDetailPO();
			mpo.setFrom_userId(justAddedUser.getUserId());
			mpo.setTo_userId(justAddedUser.getUserId());
			mpo.setMessage("Undefined");
			mpo.setMessage_timestamp(timeStmp);
			mdao.save(mpo);
			resp = ConverterUtils.convert(po);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		return created(resp);
	}

	/**
	 * This method is used to login a user.
	 * 
	 * @param user
	 *            - User information to login
	 * 
	 * @return - Status 200 when successful login. Else other status.
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}/authenticate")
	public Response loginUser(@PathParam("userName") String userName, User user) {
		Log.enter(userName, user);

		try {
			UserPO po = loadExistingUser(userName);
			if (!validateUserPassword(user.getPassword(), po)) {
				throw new UnauthorizedUserException(userName);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		return ok();
	}

	/**
	 * This method will validate the user's password based on what information
	 * is sent from the UI, versus the information retrieved for that user from
	 * the database.
	 * 
	 * @param password
	 *            - Encrypted Password
	 * @param po
	 *            - User info from DB
	 * 
	 * @return - Flag specifying YES or NO
	 */
	private boolean validateUserPassword(String password, UserPO po) {
		try {
			SecretKey key = SSNCipher.getKey(StringUtils.convertHexToBytes(po
					.getSalt()));
			if (password.equals(SSNCipher.decrypt(
					StringUtils.convertHexToBytes(po.getPassword()), key))) {
				return true;
			}
		} catch (Exception e) {
			Log.error("An Error occured when trying to decrypt the password", e);
			throw new ServiceException("Error when trying to decrypt password",
					e);
		}

		return false;
	}
	// Check userPsw - values are already crypted
	private boolean validateUserPassword(String[] details) {
		String[] checkUser = new String[2];
		checkUser[0] = details[1];
		checkUser[1] = details[0];
		return (details.equals(checkUser));
	}
	

	/**
	 * All all information related to a particular userName.
	 * 
	 * @param userName
	 *            - User Name
	 * 
	 * @return - Details of the User
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public User loadUser(@PathParam("userName") String userName) {
		Log.enter(userName);

		User user = null;
		try {
			UserPO po = loadExistingUser(userName);
			// get his status,location and status update time
			MessageDetailPO mpo = loadExistingUserStatus(po.getUserId());
			user = ConverterUtils.convert(po);
			user.setStatus(mpo.getMessage());
			user.setStatusUpdateTime(mpo.getMessage_timestamp());

		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(user);
		}

		return user;
	}

	// ****************************************************************
	// All functions related to Status
	// ****************************************************************

	/**
	 * This method is used to update the status of a user along with the
	 * timestamp and location.
	 * 
	 * @param user
	 *            - User information
	 * 
	 * @return - Status 200 when successful login. Else other status.
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}/updatestatus")
	public Response updateUserStatus(@PathParam("userName") String userName,
			User user) {
		Log.enter(userName, user);
		MessageDetailPO mpo = new MessageDetailPO();
		try {
			IUserDAO dao = DAOFactory.getInstance().getUserDAO();
			UserPO existingUser = dao.findByName(userName);

			if (existingUser != null) {

				IMessageDetailDAO mdao = DAOFactory.getInstance()
						.getMessageDetailDAO();
				mpo.setFrom_userId(existingUser.getUserId());
				mpo.setTo_userId(existingUser.getUserId());
				mpo.setMessage(user.getStatus());
				// Get Time stamp
				Date date = new Date();
				// getTime() returns current time in milliseconds
				long time = date.getTime();
				// Passed the milliseconds to constructor of Timestamp class
				Timestamp ts = new Timestamp(time);
				mpo.setMessage_timestamp(ts);
//				mpo.setLatitude(user.getLatitude());
//				mpo.setLongitude(user.getLongitude());
				mdao.save(mpo);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		Response res = Response.status(200).entity(mpo).build();
		System.out.println(res);
		return res;
	}

	// ****************************************************************
	// All functions related to Administer profile
	// ****************************************************************

	/**
	 * This method is used to update the profile of a user.
	 * 
	 * @param user
	 *            - User information
	 * 
	 * @return - Status 200 when successful update file except user name. Status
	 *         201 when successful update file including user name. Else other
	 *         status
	 */
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response updateUserProfile(@PathParam("userName") String userName,
			User user) {
		Log.enter(userName, user);
		int returnStatus = 0;
		IUserDAO dao = DAOFactory.getInstance().getUserDAO();
		UserPO existingUser = dao.findByName(userName);
		UserPO temp = new UserPO();
		temp.setUserName(user.getUserName());
		temp.setPassword(user.getPassword());
		temp.setRole(user.getRole());
		temp.setAccountStatus(user.getAccountStatus());
		temp.setLatitude(user.getLatitude());
		temp.setLongitude(user.getLongitude());
		if (existingUser != null) {
			SecretKey key = SSNCipher.getKey(StringUtils
					.convertHexToBytes(existingUser.getSalt()));
			try {
				existingUser.setPassword(SSNCipher.decrypt(StringUtils
						.convertHexToBytes(existingUser.getPassword()), key));
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| NoSuchPaddingException | IllegalBlockSizeException
					| BadPaddingException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int num;
			if ((num = existingUser.compareToAnotherOne(temp)) >= 0) {
				existingUser.setUserName(user.getUserName());
				if(user.getPassword().length() != 0){
					existingUser.setPassword(user.getPassword());
				}
				existingUser = SSNCipher.encryptPassword(existingUser);
				existingUser.setRole(user.getRole());
				existingUser.setAccountStatus(user.getAccountStatus());
				if (user.getLatitude() != 0.0 && user.getLongitude() != 0.0) {
				existingUser.setLatitude(user.getLatitude());
				existingUser.setLongitude(user.getLongitude());
				}
				dao.updateProfile(userName, existingUser);
			}
			if (num == 1) {
				returnStatus = 201;
			} else {
				returnStatus = 200;
			}
		}

		Response res = Response.status(returnStatus).entity(existingUser)
				.build();
		System.out.println(res);
		return res;
	}
}
