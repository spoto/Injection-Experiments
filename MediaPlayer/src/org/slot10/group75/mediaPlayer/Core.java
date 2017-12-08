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
package org.slot10.group75.mediaPlayer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slot10.group75.mediaPlayer.gui.GUIHandler;
import org.slot10.group75.mediaPlayer.user.InvalidUserLoginDataException;
import org.slot10.group75.mediaPlayer.user.User;

/**
 * Core class which manages access to GUI, user etc.
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public final class Core implements Thread.UncaughtExceptionHandler {
	private static Core instance = null;
	private User user = null;
	private GUIHandler guiHandler = null;
	private ResourceLoader resourceLoader = null;
	// FIXME: only needed whe message broadcaster works
	// private MessageBroadcaster messageBroadcaster = null;
	private static Lock lock = new ReentrantLock();
	private final static Object privLock = new Object();
	
	private Core() {
		this.guiHandler = GUIHandler.getInstance();
		this.resourceLoader = ResourceLoader.getInstance();
		// FIXME: get message broadcaster instance when it works
		// this.messageBroadcaster = MessageBroadcaster.getInstance();
	}
	
	public static Core getInstance() {
		if (instance == null) {
			instance = new Core();
		}
		
		return instance;
	}
	
	public static Lock getLock() {
		return lock;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public GUIHandler getGUIHandler() {
		return this.guiHandler;
	}
	
	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}
	
	public void loginUser(String username, String password) throws InvalidUserLoginDataException {
		if (this.user == null) {
			this.user = new User(username, password);
		}
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public int calcIdUser(int id){
		synchronized (privLock){
			id = ((id % 2 == 0) ? (id*172) + (id++) : id*255);
		}
		return id;
	}
	
	public void uncaughtException(Thread thread, Throwable throwable) {
		this.handleCriticalError(throwable);
	}
	
	public void handleError(Throwable throwable) {
		this.guiHandler.displayError(throwable);
	}
	
	public void handleCriticalError(Throwable throwable) {
		this.guiHandler.displayCriticalError(throwable);
	}
	
	public void shutDown(int exitCode) {
		// FIXME: use broadcaster to broadcast terminate message when broadcaster works
		// this.messageBroadcaster.sendMessage(MessageBroadcasterMessage.createBroadcastMessage(MessagedThreadMessage.terminateMessage));
		System.exit(exitCode);
	}
	
	public void shutDown() {
		this.shutDown(0);
	}
}
