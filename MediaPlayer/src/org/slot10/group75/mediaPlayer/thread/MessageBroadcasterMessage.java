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
 * MessagedThreadMessage implementation for MessageBroadcaster.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class MessageBroadcasterMessage extends MessagedThreadMessage<Object> {
	public static final int BROADCAST = 0;
	public static final int REGISTER = 1;
	public static final int UNREGISTER = 2;
	public static final int BROADCASTED_MESSAGE = 4;
	
	public MessageBroadcasterMessage(int message, Object param) {
		super(message, param);
	}
	
	@Override
	protected void validateMessage(int messageType) {
		switch (messageType) {
			case BROADCAST:
			case REGISTER:
			case UNREGISTER:
				break;
			default:
				super.validateMessage(message);
		}
	}
	
	public static MessageBroadcasterMessage createBroadcastMessage(MessagedThreadMessage<?> messageObject) {
		return new MessageBroadcasterMessage(BROADCAST, messageObject);
	}
	
	public static MessageBroadcasterMessage createBroadcastedMessageMessage(MessagedThreadMessage<?> messageObject) {
		return new MessageBroadcasterMessage(BROADCASTED_MESSAGE, messageObject);
	}
	
	public static MessageBroadcasterMessage createRegisterMessage(MessagedThread thread) {
		return new MessageBroadcasterMessage(REGISTER, thread);
	}
	
	public static MessageBroadcasterMessage createUnregisterMessage(MessagedThread thread) {
		return new MessageBroadcasterMessage(UNREGISTER, thread);
	}
}
