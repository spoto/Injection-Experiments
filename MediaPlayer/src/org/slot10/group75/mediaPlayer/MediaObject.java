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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slot10.group75.mediaPlayer.thread.*;

/**
 * Represents a media object
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public abstract class MediaObject extends MessagedBlockingThread implements Playable, ProcessingThread {
	private static final long serialVersionUID = 194754618760208993L;
	protected transient static int threadIDCounter = 0;
	
	protected transient File file;
	protected String stringRepresentation = "";
	protected transient HashMap<String, Serializable> propertiesCache = new HashMap<>();
	protected transient Map<String, ?> properties = null;
	protected transient Boolean playing = false;
	protected transient Boolean paused = false;
	
	/**
	 * Creates a media object object
	 * @param	path		file system path
	 * @param	threadName	name for the media object thread
	 */
	public MediaObject(String path, String threadName) {
		if (threadName == "") {
			this.setName("MediaObject" + ++threadIDCounter);
		}
		else {
			threadIDCounter++;
			this.setName(threadName);
		}
		
		try {
			this.file = (new File(path)).getAbsoluteFile();
			
			this.checkFile();
			this.updateProperties();
		}
		catch (IOException | UnsupportedMediaObjectTypeException  e) {
			Core.getInstance().handleError(e);
		}
	}
	
	/**
	 * Creates a media object object
	 * @param	path		file system path
	 */
	public MediaObject(String path) {
		this(path, "");
	}
	
	@Override
	public void loop() {
		this.sendMessage(MessagedThreadMessage.noOperationMessage);
	}
	
	@Override
	protected void handleMessage() throws MessagedThreadNoOperationException, MessagedThreadTerminateException, UnsupportedMessagedThreadMessageException{
		super.handleMessage();
		
		switch (this.currentMessage.getMessage()) {
			case MediaObjectMessage.PLAY:
				this.startPlaying();
				break;
			case MediaObjectMessage.PAUSE:
				this.pausePlaying();
				break;
			case MediaObjectMessage.RESTART:
				this.restartPlaying();
				break;
			case MediaObjectMessage.STOP:
				if (this.playing) {
					this.stopPlaying();
				}
				break;
			case MediaObjectMessage.PRELOAD:
				this.preload();
				break;
			default:
				throw new UnsupportedMessagedThreadMessageException();
		}
	}
	
	protected void pausePlaying() {
		this.paused = true;
	}
	
	protected void restartPlaying() {
		this.paused = false;
	}
	
	protected void stopPlaying() {
		this.lock.lock();
		this.playing = false;
		this.paused = false;
		MediaObjectPlayer.getInstance().sendMessage(MediaObjectPlayerMessage.mediaObjectStoppedMessage);
		this.lock.unlock();
	}
	
	protected void softStopPlaying() {
		this.lock.lock();
		this.playing = false;
		this.paused = false;
		MediaObjectPlayer.getInstance().sendMessage(MediaObjectPlayerMessage.mediaObjectSoftStoppedMessage);
		this.lock.unlock();
	}
	
	protected void stopPlayingImmediately() {
		this.lock.lock();
		this.playing = false;
		this.paused = false;
		this.lock.unlock();
	}
	
	@Override
	public Class<?> getMessageClass() {
		return MediaObjectMessage.class;
	}
	
	public String getPath() {
		return this.file.getPath();
	}
	
	public int getDuration() {
		return this.getDuration(false);
	}
	
	public abstract int getDuration(boolean forceUpdate);
	
	public String getDurationString() {
		return this.getDurationString(false);
	}
	
	public String getDurationString(boolean forceUpdate) {
		int duration = this.getDuration(forceUpdate);
		int minutes = 0;
		int seconds = 0;
		StringBuffer durationString = new StringBuffer();
		
		minutes = duration / 60;
		seconds = (int) Math.floor(duration % 60);
		
		durationString.append(minutes);
		durationString.append(":");
		durationString.append(((seconds < 10) ? "0" : "") + seconds);
		
		return durationString.toString();
	}
	
	public <T> T getProperty(String key) {
		return this.getProperty(key, false);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key, boolean forceUpdate) {
		if (!forceUpdate && (this.propertiesCache != null) && (this.propertiesCache.containsKey(key))) {
			return ((T) this.propertiesCache.get(key));
		}
		
		if ((this.properties == null) || forceUpdate) {
			try {
				this.updateProperties();
			}
			catch (IOException | UnsupportedMediaObjectTypeException e) {
				return null;
			}
		}
		
		if (this.properties.containsKey(key)) {
			return ((T) this.properties.get(key));
		}
		
		return null;
	}
	
	protected void checkFile() throws IOException {
		if (!this.file.canRead()) {
			if (!this.file.exists()) {
				throw new FileNotFoundException("Die Datei  \"" + this.file.getPath() + " existiert nicht");
			}
			
			throw new IOException("Datei \"" + this.file.getPath() + "\" kann nicht gelesen werden");
		}
	}
	
	public boolean isPlaying() {
		return this.playing;
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	/**
	 * Returns a string that textually represents this object.
	 * 
	 * @return	String representation of this object
	 * */
	@Override
	public String toString() {
		if (this.stringRepresentation.equals("")) {
			StringBuffer stringBuffer = new StringBuffer();
			String artist = this.getProperty("author");
			String title = this.getProperty("title");
			
			if (artist != null) {
				stringBuffer.append(artist);
			}
			else {
				stringBuffer.append("Unknown");
			}
			
			stringBuffer.append(" - ");
			
			if (title != null) {
				stringBuffer.append(title);
			}
			else {
				stringBuffer.append("Unknown");
			}
			
			this.stringRepresentation = stringBuffer.toString();
		}
		
		return this.stringRepresentation;
	}
	
	private synchronized void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		
		MediaObjectData data = (MediaObjectData) ois.readObject();
		
		this.file = new File(data.path);
		this.propertiesCache = data.propertiesCache;
		this.playing = false;
		this.paused = false;
	}
	
	private synchronized void writeObject(ObjectOutputStream oos) throws IOException {
		MediaObjectData data = new MediaObjectData(this.getPath(), this.propertiesCache);
		
		oos.defaultWriteObject();
		oos.writeObject(data);
	}
	
	public abstract void updateProperties() throws UnsupportedMediaObjectTypeException, IOException;
	protected abstract void startPlaying();
	protected abstract void preload();
}
