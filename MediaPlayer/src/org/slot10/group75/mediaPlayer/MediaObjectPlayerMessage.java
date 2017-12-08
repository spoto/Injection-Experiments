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

import org.slot10.group75.mediaPlayer.thread.MessagedThreadMessage;

/**
 * MessagedThreadmessage implementation for media object player
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class MediaObjectPlayerMessage extends MessagedThreadMessage<Playable> {
	public static final int PLAY = 0;
	public static final int PAUSE = 1;
	public static final int RESTART = 2;
	public static final int STOP = 4;
	public static final int PREVIOUS = 8;
	public static final int NEXT = 16;
	public static final int QUEUE = 32;
	public static final int UNQUEUE = 64;
	public static final int MEDIA_OBJECT_STOPPED = 128;
	public static final int MEDIA_OBJECT_SOFT_STOPPED = 256;
	public static final int QUEUE_STOPPED = 512;
	public static final int QUEUE_SOFT_STOPPED = 1024;
	public static final int PLAYING_ERROR = 2048;
	
	public static final MediaObjectPlayerMessage startPlayingMessage = new MediaObjectPlayerMessage(PLAY);
	public static final MediaObjectPlayerMessage pausePlayingMessage = new MediaObjectPlayerMessage(PAUSE);
	public static final MediaObjectPlayerMessage restartPlayingMessage = new MediaObjectPlayerMessage(RESTART);
	public static final MediaObjectPlayerMessage stopPlayingMessage = new MediaObjectPlayerMessage(STOP);
	public static final MediaObjectPlayerMessage playPreviousMessage = new MediaObjectPlayerMessage(PREVIOUS);
	public static final MediaObjectPlayerMessage playNextMessage = new MediaObjectPlayerMessage(NEXT);
	public static final MediaObjectPlayerMessage mediaObjectStoppedMessage = new MediaObjectPlayerMessage(MEDIA_OBJECT_STOPPED);
	public static final MediaObjectPlayerMessage mediaObjectSoftStoppedMessage = new MediaObjectPlayerMessage(MEDIA_OBJECT_SOFT_STOPPED);
	public static final MediaObjectPlayerMessage queueStoppedMessage = new MediaObjectPlayerMessage(QUEUE_STOPPED);
	public static final MediaObjectPlayerMessage queueSoftStoppedMessage = new MediaObjectPlayerMessage(QUEUE_SOFT_STOPPED);
	public static final MediaObjectPlayerMessage playingErrorMessage = new MediaObjectPlayerMessage(PLAYING_ERROR);

	public MediaObjectPlayerMessage(int message) {
		super(message, null);
	}
	
	public MediaObjectPlayerMessage(int message, Playable playable) {
		super(message, playable);
	}
	
	@Override
	protected void validateMessage(int message) {
		switch (message) {
			case PLAY:
			case PAUSE:
			case RESTART:
			case STOP:
			case PREVIOUS:
			case NEXT:
			case QUEUE:
			case UNQUEUE:
			case MEDIA_OBJECT_STOPPED:
			case MEDIA_OBJECT_SOFT_STOPPED:
			case QUEUE_STOPPED:
			case QUEUE_SOFT_STOPPED:
			case PLAYING_ERROR:
				break;
			default:
				super.validateMessage(message);
		}
	}
}
