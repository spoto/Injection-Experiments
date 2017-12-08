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
 * MessagedThreadMessage implementation for media object threads
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class MediaObjectMessage extends MessagedThreadMessage<Object> {
	public static final int PLAY = 0;
	public static final int PAUSE = 1;
	public static final int RESTART = 2;
	public static final int STOP = 4;
	public static final int PRELOAD = 8;
	
	public static final MediaObjectMessage playMessage = new MediaObjectMessage(PLAY);
	public static final MediaObjectMessage pauseMessage = new MediaObjectMessage(PAUSE);
	public static final MediaObjectMessage restartMessage = new MediaObjectMessage(RESTART);
	public static final MediaObjectMessage stopMessage = new MediaObjectMessage(STOP);
	public static final MediaObjectMessage preloadMessage = new MediaObjectMessage(PRELOAD);
	
	public MediaObjectMessage(int message) {
		super(message);
	}
	
	@Override
	protected void validateMessage(int message) {
		switch (message) {
			case PLAY:
			case PAUSE:
			case RESTART:
			case STOP:
			case PRELOAD:
				break;
			default:
				super.validateMessage(message);
		}
	}
}
