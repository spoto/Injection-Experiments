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
package org.slot10.group75.mediaPlayer.thread;

/**
 * Basic class for messaged thread messages.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class MessagedThreadMessage<T> {
	public static final int NO_OPERATION = -2;
	public static final int TERMINATE = -4;
	
	public static final MessagedThreadMessage<Object> noOperationMessage = new MessagedThreadMessage<>(NO_OPERATION);
	public static final MessagedThreadMessage<Object> terminateMessage = new MessagedThreadMessage<>(TERMINATE);

	protected int message = 0;
	protected T param = null;
	
	public MessagedThreadMessage(int message) {
		this(message, null);
	}
	
	public MessagedThreadMessage(int message, T param) {
		this.validateMessage(message);
		
		this.message = message;
		this.param = param;
	}
	
	protected void validateMessage(int messageType) {
		if ((messageType != NO_OPERATION) && (messageType != TERMINATE)) {
			throw new IllegalArgumentException("Invalid message \"" + message + "\"");
		}
	}
	
	public int getMessage() {
		return this.message;
	}
	
	public T getParam() {
		return this.param;
	}
}
