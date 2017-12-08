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

import java.util.ArrayList;

/**
 * Thread forwarding given messages to every registered thread.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
// FIXME: registration doesn't work
public class MessageBroadcaster extends MessagedBlockingThread {
	private static MessageBroadcaster instance = null;
	private ArrayList<MessagedThread> threads = new ArrayList<>(25);
	
	private MessageBroadcaster() {
		this.setName("MessageBroadcaster");
		this.start();
	}
	
	public static MessageBroadcaster getInstance() {
		if (instance == null) {
			instance = new MessageBroadcaster();
		}
		
		return instance;
	}
	
	@Override
	protected void handleMessage() throws MessagedThreadNoOperationException, MessagedThreadTerminateException, UnsupportedMessagedThreadMessageException {
		switch (this.currentMessage.getMessage()) {
			case MessageBroadcasterMessage.BROADCAST:
				this.broadcastCurrentMessage();
				break;
			case MessageBroadcasterMessage.REGISTER:
				if (this.currentMessage.getParam() instanceof MessagedThread) {
					this.lock.lock();
					if ((this.threads.size() + 5) > this.threads.size()) {
						ArrayList<MessagedThread> tmpThreads = this.threads;
						this.threads = new ArrayList<>(this.threads.size() + 5);
						
						for (MessagedThread thread: tmpThreads) {
							this.threads.add(thread);
						}
						
						this.threads.add((MessagedThread) this.currentMessage.getParam());
					}
					this.lock.unlock();
				}
				else {
					throw new UnsupportedMessagedThreadMessageException("Message param has to be of type \"MessagedThread\"");
				}
				
				break;
			case MessageBroadcasterMessage.UNREGISTER:
				if (this.currentMessage.getParam() instanceof MessagedThread) {
					this.threads.remove(this.currentMessage.getParam());
				}
				else {
					throw new UnsupportedMessagedThreadMessageException("Message param has to be of type \"MessagedThread\"");
				}
				
				break;
			default:
				super.handleMessage();
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void broadcastCurrentMessage() {
		this.lock.lock();
		
		for (MessagedThread thread: this.threads) {
			thread.sendMessage(MessageBroadcasterMessage.createBroadcastedMessageMessage((MessagedThreadMessage<Object>) this.currentMessage.getParam()));
		}
		
		this.lock.unlock();
	}
	
	@Override
	public Class<?> getMessageClass() {
		return MessageBroadcasterMessage.class;
	}
}
