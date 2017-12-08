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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * Represents a playable song, usually mp3.
 * Data stream is played in an own thread via MP3 SPI.
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class Song extends MediaObject {
	private static final long serialVersionUID = 3594412053169969359L;
	
	private transient AudioInputStream inputStream = null;
	private transient AudioInputStream decodedInputStream = null;
	private transient AudioFormat format = null;
	private transient AudioFormat decodedFormat = null;
	private transient SourceDataLine dataLine = null;
	private transient byte[] data = new byte[4096];
	
	public Song(String path) {
		super(path, "Song-" + threadIDCounter);
	}
	
	@Override
	public void loop() {
		super.loop();
		
		if (this.playing && !this.paused) {
			try {
				int read = this.decodedInputStream.read(this.data, 0, this.data.length);
				
				if (read != -1) {
					this.dataLine.write(this.data, 0, read);
				}
				else {
					this.softStopPlaying();
				}
			}
			catch (IOException e) {
				try {
					this.internalStopPlaying();
				}
				catch (IOException ex) { }
				finally {
					Core.getInstance().handleError(e);
				}
			}
		}
	}
	
	@Override
	protected void startPlaying() {
		try {
			if (this.playing) {
				this.internalStopPlaying();
			}
			
			this.checkFile();
			
			this.inputStream = AudioSystem.getAudioInputStream(this.file);
			this.format = this.inputStream.getFormat();
			this.decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, this.format.getSampleRate(), 16, this.format.getChannels(), this.format.getChannels() * 2, this.format.getSampleRate(), false);
			this.decodedInputStream = AudioSystem.getAudioInputStream(this.decodedFormat, this.inputStream);
			this.dataLine = this.getDataLine();

			this.playing = true;
			this.paused = false;
			this.dataLine.start();
		}
		catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			try {
				this.internalStopPlaying();
				super.stopPlaying();
			}
			catch (IOException ex) { }
			finally {
				Core.getInstance().handleError(e);
			}
		}
	}
	
	protected void internalStopPlaying() throws IOException {
		if (this.playing) {
			this.playing = false;
			this.paused = false;
			this.dataLine.drain();
			this.dataLine.stop();
			this.dataLine.close();
			this.decodedInputStream.close();
			this.inputStream.close();
		}
	}
	
	@Override
	protected void stopPlaying() {
		try {
			this.lock.lock();
			this.internalStopPlaying();
		}
		catch (IOException e) { }
		finally {
			super.stopPlaying();
			this.lock.unlock();
		}
	}
	
	@Override
	protected void softStopPlaying() {
		try {
			this.lock.lock();
			this.internalStopPlaying();
		}
		catch (IOException e) { }
		finally {
			super.softStopPlaying();
			this.lock.unlock();
		}
	}
	
	@Override
	protected void stopPlayingImmediately() {
		try {
			this.internalStopPlaying();
		}
		catch (IOException e) { }
		finally {
			super.stopPlayingImmediately();
		}
	}
	
	@Override
	protected void preload() {
		// TODO: implement Song#preload()
	}
	
	protected SourceDataLine getDataLine() throws LineUnavailableException {
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, this.decodedFormat);
		SourceDataLine res = (SourceDataLine) AudioSystem.getLine(info);

		res.open(this.decodedFormat);

		return res;
	}
	
	@Override
	public int getDuration(boolean forceUpdate) {
		long duration = this.getProperty("duration", forceUpdate);
		
		return ((int) Math.floor(duration / 1000000));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateProperties() throws UnsupportedMediaObjectTypeException, IOException {
		this.checkFile();
		
		try {
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(this.file);
			AudioFormat format = fileFormat.getFormat();
			
			if (fileFormat instanceof TAudioFileFormat) {
				this.properties = ((TAudioFileFormat) fileFormat).properties();
			}
			
			if (format instanceof TAudioFormat) {
				if (this.properties != null) {
					// FIXME: putting these causes error
					// this.properties.putAll(((TAudioFormat) format).properties());
				}
			}
			
			Set<String> keys = this.properties.keySet();
			
			for (String key: keys) {
				if ((this.properties.get(key) instanceof Serializable) && !(this.properties.get(key) instanceof List)) {
					this.propertiesCache.put(key, (Serializable) this.properties.get(key));
				}
			}
		}
		catch (UnsupportedAudioFileException e) {
			throw new UnsupportedMediaObjectTypeException("Unbekannter Dateityp");
		}
	}

	private synchronized void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		
		this.data = new byte[4096];
	}
}
