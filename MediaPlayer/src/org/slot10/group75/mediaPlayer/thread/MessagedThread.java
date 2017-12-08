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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A thread implementation which can recieve sent messages for
 * inter-thread communication.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public abstract class MessagedThread extends Thread implements BroadcastMessageReceiver {
	protected Lock lock = new ReentrantLock();
	
	protected Boolean started = false;
	protected MessagedThreadMessage<?> currentMessage = null;
	
	@Override
	public void start() {
		if (!this.started) {
			super.start();
			
			// FIXME: registration doesn't work
			// MessageBroadcaster.getInstance().sendMessage(MessageBroadcasterMessage.createRegisterMessage(this));
		}
	}
	
	@Override
	public final void run() {
		this.started = true;
		
		loop:
		while (true) {
			try {
				this.currentMessage = this.pollMessage();
				
				if (this.currentMessage != null) {
					this.handleMessage();
				}
			}
			catch (MessagedThreadNoOperationException e) {
				this.noOperation();
			}
			catch (MessagedThreadTerminateException e) {
				// FIXME: registration doesn't work
				// MessageBroadcaster.getInstance().sendMessage(MessageBroadcasterMessage.createUnregisterMessage(this));
				break loop;
			}
			
			if (this instanceof ProcessingThread) {
				((ProcessingThread) this).loop();
			}
		}
	}
	
	protected void handleMessage() throws MessagedThreadNoOperationException, MessagedThreadTerminateException {
		if (this.currentMessage != null) {
			if (this.currentMessage instanceof MessageBroadcasterMessage) {
				this.handleBroadcastMessage((MessageBroadcasterMessage) this.currentMessage);
			}
			else if (this.currentMessage.getClass() == MessagedThreadMessage.class){
				switch (this.currentMessage.getMessage()) {
					case MessagedThreadMessage.NO_OPERATION:
						throw new MessagedThreadNoOperationException();
					case MessagedThreadMessage.TERMINATE:
						throw new MessagedThreadTerminateException();
					default:
						throw new UnsupportedMessagedThreadMessageException();
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void handleBroadcastMessage(MessageBroadcasterMessage message) throws MessagedThreadNoOperationException, MessagedThreadTerminateException {
		if ((message.getParam().getClass() == this.getMessageClass()) || (message.getParam().getClass() == MessagedThreadMessage.class)) {
			this.currentMessage = (MessagedThreadMessage<Object>) message.getParam();
			this.handleMessage();
		}
	}
	
	protected void noOperation() {
		try {
			sleep(10L);
		}
		catch (InterruptedException e) { }
	}
	
	public abstract void sendMessage(MessagedThreadMessage<?> message);
	protected abstract MessagedThreadMessage<?> pollMessage();
	protected abstract Queue<MessagedThreadMessage<?>> getQueue();
	protected abstract Class<?> getMessageClass();
}
