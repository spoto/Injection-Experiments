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

import javax.swing.ImageIcon;

import org.slot10.group75.mediaPlayer.thread.*;

/**
 * Threaded media object player
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
// TODO: player is dependant on gui now because of icon switching; broadcast message?
public final class MediaObjectPlayer extends MessagedBlockingThread {
	private static MediaObjectPlayer instance = null;
	
	private MediaObject currentMediaObject = null;
	private PlayQueue playQueue = new PlayQueue();
	private PlayQueueIterator playQueueIterator = this.playQueue.listIterator();
	
	private MediaObjectPlayer() {
		this.setName("MediaObjectPlayer");
		this.start();
	}
	
	public static MediaObjectPlayer getInstance() {
		if (instance == null) {
			instance = new MediaObjectPlayer();
		}
		
		return instance;
	}
	
	public static boolean isRunning() {
		return (instance != null);
	}
	
	public boolean isPlaying() {
		return ((this.currentMediaObject != null) && (this.currentMediaObject.isPlaying()));
	}
	
	public boolean isPaused() {
		return ((this.currentMediaObject != null) && (this.currentMediaObject.isPaused()));
	}
	
	@Override
	protected void handleMessage() throws MessagedThreadNoOperationException, MessagedThreadTerminateException, UnsupportedMessagedThreadMessageException {
		super.handleMessage();
		
		switch (this.currentMessage.getMessage()) {
			case MediaObjectPlayerMessage.PLAY:
				if (this.currentMessage.getParam() instanceof MediaObject) {
					this.startMediaObjectPlaying((MediaObject) this.currentMessage.getParam());
				}
				else if (this.currentMessage.getParam() instanceof PlayQueue) {
					this.playQueue = ((PlayQueue) ((PlayQueue) this.currentMessage.getParam()).clone());
					this.playQueueIterator = this.playQueue.listIterator();
					this.playNextQueueElement();
				}
				else if (this.currentMessage.getParam() instanceof PlayQueueIterator) {
					this.playQueueIterator = ((PlayQueueIterator) this.currentMessage.getParam());
					this.playQueue = this.playQueueIterator.getQueue();
					this.playNextQueueElement();
				}
				else {
					throw new UnsupportedMessagedThreadMessageException("Message parameter of type \"" + this.currentMessage.getParam().getClass() + "\"");
				}
				
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setText("Pause");
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("pause")));
				
				break;
			case MediaObjectPlayerMessage.PAUSE:
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setText("Start");
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("play")));
				this.pauseMediaObjectPlaying();
				break;
			case MediaObjectPlayerMessage.RESTART:
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setText("Pause");
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("pause")));
				this.restartMediaObjectPlaying();
				break;
			case MediaObjectPlayerMessage.STOP:
				this.stopMediaObjectPlaying();
				break;
			case MediaObjectPlayerMessage.QUEUE:
				this.queueMediaObject((MediaObject) this.currentMessage.getParam());
				break;
			case MediaObjectPlayerMessage.UNQUEUE:
				this.unqueueMediaObject((MediaObject) this.currentMessage.getParam());
				break;
			case MediaObjectPlayerMessage.PREVIOUS:
				this.playPreviousQueueElement();
			case MediaObjectPlayerMessage.MEDIA_OBJECT_STOPPED:
				this.currentMediaObject = null;
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setText("Start");
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("play")));
				break;
			case MediaObjectPlayerMessage.NEXT:
			case MediaObjectPlayerMessage.MEDIA_OBJECT_SOFT_STOPPED:
				this.playNextQueueElement();
				break;
			case MediaObjectPlayerMessage.QUEUE_STOPPED:
				this.startMediaObjectPlaying(this.playQueueIterator.first());
				break;
			case MediaObjectPlayerMessage.QUEUE_SOFT_STOPPED:
				this.playQueueIterator.first();
				break;
			default:
				throw new UnsupportedMessagedThreadMessageException();
		}
	}
	
	private void startMediaObjectPlaying(MediaObject mediaObject) {
		this.currentMediaObject = mediaObject;
		this.currentMediaObject.start();
		this.currentMediaObject.sendMessage(MediaObjectMessage.playMessage);
	}
	
	private void pauseMediaObjectPlaying() {
		if (this.currentMediaObject != null) {
			this.currentMediaObject.sendMessage(MediaObjectMessage.pauseMessage);
		}
	}
	
	private void restartMediaObjectPlaying() {
		if (this.currentMediaObject != null) {
			this.currentMediaObject.sendMessage(MediaObjectMessage.restartMessage);
		}
	}
	
	private void stopMediaObjectPlaying() {
		if (this.currentMediaObject != null) {
			this.currentMediaObject.sendMessage(MediaObjectMessage.stopMessage);
			this.currentMediaObject = null;
		}
	}
	
	private void playPreviousQueueElement() {
		if (this.currentMediaObject != null) {
			this.currentMediaObject.stopPlayingImmediately();
		}
		
		if (this.playQueueIterator.getDirection() == 1) {
			this.playQueueIterator.previous();
		}
		
		if (this.playQueueIterator.hasPrevious()) {
			this.sendMessage(new MediaObjectPlayerMessage(MediaObjectPlayerMessage.PLAY, this.playQueueIterator.previous()));
		}
		else {
			this.sendMessage(new MediaObjectPlayerMessage(MediaObjectPlayerMessage.PLAY, this.playQueueIterator.last()));
		}
	}
	
	private void playNextQueueElement() {
		if (this.currentMediaObject != null) {
			this.currentMediaObject.stopPlayingImmediately();
		}
		
		if (this.playQueueIterator.getDirection() == -1) {
			this.playQueueIterator.next();
		}
		
		if (this.playQueueIterator.hasNext()) {
			this.startMediaObjectPlaying(this.playQueueIterator.next());
		}
		else {
			if (this.currentMessage.getMessage() == MediaObjectPlayerMessage.MEDIA_OBJECT_SOFT_STOPPED) {
				this.sendMessage(MediaObjectPlayerMessage.queueSoftStoppedMessage);
			}
			else {
				this.sendMessage(MediaObjectPlayerMessage.queueStoppedMessage);
			}
		}
	}
	
	private void queueMediaObject(MediaObject mediaObject) {
		this.playQueueIterator.push(mediaObject);
	}
	
	private void unqueueMediaObject(MediaObject mediaObject) {
		if (this.playQueue != null) {
			this.lock.lock();
			int cursor = this.playQueueIterator.getCursor();
			
			if (this.currentMediaObject == mediaObject) {
				this.currentMediaObject.stopPlayingImmediately();
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setText("Start");
				Core.getInstance().getGUIHandler().getMediaPlayerFrame().lblPlayPause.setIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("play")));
				
				if (this.playQueueIterator.hasNext()) {
					cursor++;
				}
				else {
					cursor = 0;
				}
			}
			
			this.playQueue.remove(mediaObject);
			this.playQueueIterator = this.playQueue.listIterator(cursor);
			this.lock.unlock();
		}
	}
	
	@Override
	public Class<?> getMessageClass() {
		return MediaObjectPlayerMessage.class;
	}
}
