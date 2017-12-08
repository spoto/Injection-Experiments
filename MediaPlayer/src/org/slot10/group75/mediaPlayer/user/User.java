/*
 * Copyright (C) 2013 Stefan Hahn, Jiacheng Qian, Andreas Mannsdörfer
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.slot10.group75.mediaPlayer.user;

import iste.io.ISTEStream;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.Playlist;

/**
 * Represents a user of the media player
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
// TODO: you can get admin role by editing files, maybe checksum over whole data
public class User {
	public static final int ROLE_USER = 0;
	public static final int ROLE_ADMIN = 1;
	
	private String name = "";
	private String passwordHash = "";
	private int role = 0;
	private Playlist playlist = null;
	
	/**
	 * Creats a new user objec and validates the data against stored user data.
	 * @param	name		Name of the user
	 * @param	password	Password of the user
	 * @throws	InvalidUserLoginDataException	Thrown if no user with given name exists or if the password is wrong
	 */
	public User(String name, String password) throws InvalidUserLoginDataException {
		try {
			this.load(name);
			this.validatePassword(name, password);
		}
		catch (IOException e) {
			Core.getInstance().handleCriticalError(new RuntimeException("Benutzerdaten konnten nicht gelesen werden", e));
		}
	}
	
	/**
	 * Used to create user object which are not validated against stored user data.
	 */
	private User() { }
	
	/**
	 * Loads user data from file system.
	 * @param	name		username
	 * @throws	InvalidUserLoginDataException	Thrown if no user with given name exists
	 * @throws	IOException						Thrown if the data can't be read from file system
	 */
	private void load(String name) throws InvalidUserLoginDataException, IOException {
		if (!userExists(name)) {
			throw new InvalidUserLoginDataException("Kein Benutzer mit diesem Namen vorhanden");
		}
		
		UserData userData = UserData.class.cast(ISTEStream.readObject("userData/" + hash(name, 1) + ".usr"));
		this.name = userData.name;
		this.passwordHash = userData.passwordHash;
		this.role = userData.role;
		this.playlist = userData.playlist;
	}
	
	/**
	 * Saved the data of his user to file system.
	 */
	public void save() {
		saveData(this.getUserData());
	}
	
	/**
	 * Saved the given userData to file system
	 * @param	userData	user data
	 */
	public static void saveData(UserData userData) {
		File usersDir = new File("userData");
		
		try {
			if (!usersDir.exists()) {
				if (!usersDir.mkdir()) {
					throw new IOException("Konnte Benutzerordner nicht anlegen");
				}
			}
			
			ISTEStream.saveObject(userData, "userData/" + hash(userData.name, 1) + ".usr");
		}
		catch (IOException e) {
			Core.getInstance().handleError(new RuntimeException("Benutzerdaten konnten nicht geschrieben werden", e));
		}
	}
	
	/**
	 * Validates the password against sored password hash.
	 * @param	name		username
	 * @param	password	password
	 * @throws	InvalidUserLoginDataException	Thrown if the password is wrong
	 */
	private void validatePassword(String name, String password) throws InvalidUserLoginDataException {
		String passwordHash = hash(password);
		password = "";
		
		if (!this.passwordHash.equals(passwordHash)) {
			throw new InvalidUserLoginDataException("Ungültiges Passwort");
		}
	}
	
	/**
	 * Gets the name of this user
	 * @return				username
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Sets the name of this user
	 * @param	name		username
	 */
	private void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Sets the password (hash) for this user
	 * @param	password	password
	 */
	private void setPassword(String password) {
		this.passwordHash = hash(password);
	}
	
	/**
	 * Gets the personal playlist of this user
	 * @return				Playlist of this user
	 */
	public Playlist getPlaylist() {
		return this.playlist;
	}
	
	/**
	 * Sets the personal playlist for this user
	 * @param	playlist	playlist
	 */
	public void setPlaylist(Playlist playlist) {
		this.playlist = playlist;
	}
	
	/**
	 * Gets the group of his user
	 * @return				Either User#ROLE_ADMIN or User#ROLE_USER
	 */
	public int getRole() {
		return this.role;
	}
	
	/**
	 * Sets the group for this user
	 * @param	role		Either User#ROLE_ADMIN or User#ROLE_USER
	 */
	private void setRole(int role) {
		this.role = role;
	}
	
	/**
	 * Gets the UserData object for this user
	 * @return				UserData object for this user
	 */
	public UserData getUserData() {
		return new UserData(this.name, this.passwordHash, this.role, this.playlist);
	}
	
	/**
	 * Update the data of this user
	 * @param	name		new username, if null current name is used
	 * @param	password	new password, if null current password is used
	 * @param	playlist	new playlist, if null current playlist is used
	 * @throws	UserAlreadyExistsException		Thrown if a user with the given name already exists
	 */
	public static UserData updateUser(UserData oldUserData, String name, String password, int role, Playlist playlist) throws UserAlreadyExistsException {
		UserData newUserData = new UserData(oldUserData.name, oldUserData.passwordHash, oldUserData.role, oldUserData.playlist);
		
		if (name != null) {
			if (userExists(name)) {
				throw new UserAlreadyExistsException();
			}
			
			newUserData.name = name;
		}
		
		if (password != null) {
			newUserData.passwordHash = hash(password);
		}
		
		if (playlist != null) {
			newUserData.playlist = playlist;
		}
		
		newUserData.role = role;
		
		deleteUser(oldUserData.name);
		saveData(newUserData);
		
		if (Core.getInstance().getUser().getName().equals(oldUserData.name)) {
			Core.getInstance().getUser().setName(newUserData.name);
			Core.getInstance().getUser().passwordHash = newUserData.passwordHash;
			Core.getInstance().getUser().setPlaylist(newUserData.playlist);
			Core.getInstance().getUser().setRole(newUserData.role);
		}
		
		return newUserData;
	}
	
	/**
	 * Creates a user with the given data, has to be saved via call of User#save() manually.
	 * @param	name		username
	 * @param	password	password
	 * @param	role		indicates group of the user, User#ROLE_ADMIN oder User#ROLE_USER
	 * @return				User object for the created user
	 * @throws	UserAlreadyExistsException		Thrown if a user with the given name already exists
	 */
	public static User createUser(String name, String password, int role) throws UserAlreadyExistsException{
		if (userExists(name)) {
			throw new UserAlreadyExistsException();
		}
		
		User user = new User();
		
		user.setName(name);
		user.setPassword(password);
		user.setPlaylist(new Playlist("Playlist"));
		user.setRole(role);
		
		user.save();
		
		return user;
	}
	
	/**
	 * Deletes the user with the given name,
	 * ignores non-existing users.
	 * @param	name	username
	 */
	public static void deleteUser(String name) {
		if (userExists(name)) {
			File userData = new File("userData/" + hash(name, 1) + ".usr");
			
			userData.delete();
		}
	}
	
	/**
	 * Check whether a user with the given name exists.
	 * Returns true if a user exists, false if not.
	 * @param	name	username
	 * @return			True if users exists
	 */
	public static boolean userExists(String name) {
		File usersDir = new File("userData");
		
		try {
			if (!usersDir.exists()) {
				if (!usersDir.mkdir()) {
					throw new IOException("Konnte Benutzerordner nicht anlegen");
				}
				
				return false;
			}
			
			usersDir = null;
		}
		catch (IOException e) {
			Core.getInstance().handleCriticalError(e);
		}
		
		return (new File("userData/" + hash(name, 1) + ".usr")).exists();
	}
	
	/**
	 * Creates SHA hashs of UTF-8 representation of he given string
	 * @param	string		Any string
	 * @param	shaType		Indicats which SHA version to use, like 1 or 512
	 * @return				SHA hash of given string
	 */
	public static String hash(String string, int shaType) {
		byte[] hash = null;
		StringBuffer hashStringBuffer = new StringBuffer();
		
		try {
			hash = MessageDigest.getInstance("SHA-" + shaType).digest(string.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			Core.getInstance().handleCriticalError(e);
		}
		
		for (byte element: hash) {
			hashStringBuffer.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
		}
		
		return hashStringBuffer.toString();
	}
	
	/**
	 * Hashs the given string as UTF-8 with SHA-512
	 * @param	string		Any string
	 * @return				SHA hash of given string
	 */
	public static String hash(String string) {
		return hash(string, 512);
	}
}
