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
package org.slot10.group75.mediaPlayer.gui;

import org.slot10.group75.mediaPlayer.thread.MessagedThreadMessage;

/**
 * MessagedThreadMessage implementation for GUIHandler.
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class GUIHandlerMessage<T> extends MessagedThreadMessage<T> {
	public static final int ERROR = 0;
	public static final int CRITICAL_ERROR = 1;
	public static final int SHOW_LOGIN_FORM = 2;
	public static final int SHOW_PLAYER = 4;
	
	public static final GUIHandlerMessage<Object> showLoginFormMessage = new GUIHandlerMessage<>(SHOW_LOGIN_FORM);
	public static final GUIHandlerMessage<Object> showPlayerMessage = new GUIHandlerMessage<>(SHOW_PLAYER);
	
	public GUIHandlerMessage(int message) {
		super(message, null);
	}
	
	public GUIHandlerMessage(int message, T object) {
		super(message, object);
	}
	
	@Override
	protected void validateMessage(int messageType) {
		switch (messageType) {
			case ERROR:
			case CRITICAL_ERROR:
			case SHOW_LOGIN_FORM:
			case SHOW_PLAYER:
				break;
			default:
				super.validateMessage(messageType);
		}
	}
	
	public static GUIHandlerMessage<Throwable> createCriticalErrorMessage(Throwable throwable) {
		return createErrorMessage(throwable, CRITICAL_ERROR);
	}
	
	public static GUIHandlerMessage<Throwable> createErrorMessage(Throwable throwable) {
		return createErrorMessage(throwable, ERROR);
	}
	
	protected static GUIHandlerMessage<Throwable> createErrorMessage(Throwable throwable, int type) {
		return new GUIHandlerMessage<Throwable>(type, throwable);
	}
}
