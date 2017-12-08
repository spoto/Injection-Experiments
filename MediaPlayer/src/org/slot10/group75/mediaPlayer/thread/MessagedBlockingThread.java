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

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * MessagedThread which blocks via LinkedBlockingQueue when there
 * are no messages in queue. Reactivated when receiving messages.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public abstract class MessagedBlockingThread extends MessagedThread {
	protected LinkedBlockingQueue<MessagedThreadMessage<?>> messages = new LinkedBlockingQueue<>();
	
	@Override
	public void sendMessage(MessagedThreadMessage<?> message) {
		try {
			this.messages.put(message);
		}
		catch (InterruptedException e) { }
	}
	
	@Override			
	protected MessagedThreadMessage<?> pollMessage() {
		try {
			return this.messages.take();
		}
		catch (InterruptedException e) {
			return null;
		}
	}
	
	@Override
	public Queue<MessagedThreadMessage<?>> getQueue() {
		return this.messages;
	}
}
