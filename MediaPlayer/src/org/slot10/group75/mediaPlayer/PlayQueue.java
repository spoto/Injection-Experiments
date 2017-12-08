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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class PlayQueue extends ArrayList<MediaObject> implements Playable {
	private static final long serialVersionUID = 7104921236034166503L;
	
	protected String name = "";
	
	public PlayQueue(String name) {
		this.setName(name);
	}
	
	public PlayQueue(String name, List<MediaObject> mediaObjectList) {
		this(name);
		this.addAll(mediaObjectList);
	}
	
	public PlayQueue() {
		this("");
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.checkName(name);
		this.name = name;
	}
	
	/**
	 * Overwrite if you have to validate name.
	 * 
	 * @param	name						PlayQueue name
	 * @throws	IllegalArgumentException	Thrown if name contains invalid characters
	 */
	public void checkName(String name) { }
	
	@Override
	public PlayQueueIterator listIterator() {
		return new PlyQueueIterator(0);
	}
	
	@Override
	public PlayQueueIterator listIterator(int index) {
		if ((index < 0) || (index > this.size())) {
			throw new IndexOutOfBoundsException("Index: " + index);
		}
		
		return new PlyQueueIterator(index);
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public Vector<Vector<?>> toDataVector() {
		Vector<Vector<?>> dataVector = new Vector<>();
		
		for (MediaObject mediaObject: this) {
			Vector<Object> songVector = new Vector<>();
			
			songVector.add(this.indexOf(mediaObject) + 1);
			songVector.add(mediaObject.getProperty("author"));
			songVector.add(mediaObject.getProperty("title"));
			songVector.add(mediaObject.getDurationString());
			
			dataVector.add(songVector);
		}
		
		return dataVector;
	}
	
	/**
	 * Own iterator implementation for play queue with additional functions
	 * @author	Jiacheng Qian (2783671) 
	 * @author	Stefan Hahn (2837462) 
	 * @author	Andreas Mannsdörfer (2801964)
	 */
	private class PlyQueueIterator implements PlayQueueIterator {
		private static final long serialVersionUID = 3399023677734956405L;
		
		private int cursor;
		private int lastRet = -1;
		private int direction = 1;
		private int expectedModCount = PlayQueue.this.modCount;
		
		public PlyQueueIterator(int index) {
			super();
			
			this.cursor = index;
		}
		
		public boolean hasPrevious() {
			return (this.cursor != 0);
		}
		
		public boolean hasNext() {
			return (this.cursor != PlayQueue.this.size());
		}
		
		public int previousIndex() {
			return (this.cursor - 1);
		}
		
		public int nextIndex() {
			return this.cursor;
		}
		
		public MediaObject previous() {
			this.checkForComodification();
			
			int i = this.cursor - 1;
			
			if (i < 0) {
				throw new NoSuchElementException();
			}
			
			Object[] elementData = PlayQueue.this.toArray();
			
			if (i >= elementData.length) {
				throw new ConcurrentModificationException();
			}
			
			this.cursor = i;
			this.direction = -1;
			
			return ((MediaObject) elementData[this.lastRet = i]);
		}
		
		public MediaObject next() {
			this.checkForComodification();
			
			int i = this.cursor;
			
			if (i >= PlayQueue.this.size()) {
				throw new NoSuchElementException();
			}
			
			Object[] elementData = PlayQueue.this.toArray();
			
			if (i >= elementData.length) {
				throw new ConcurrentModificationException();
			}
			
			this.cursor = i + 1;
			this.direction = 1;
			
			return ((MediaObject) elementData[this.lastRet = i]);
		}
		
		public MediaObject first() {
			this.checkForComodification();
			
			int i = 0;
			Object[] elementData = PlayQueue.this.toArray();
			
			if (i >= elementData.length) {
				throw new ConcurrentModificationException();
			}
			
			this.cursor = i;
			this.direction = -1;
			
			return ((MediaObject) elementData[this.lastRet = i]);
		}
		
		public MediaObject last() {
			this.checkForComodification();
			
			int i = PlayQueue.this.size() - 1;
			Object[] elementData = PlayQueue.this.toArray();
			
			if (i >= elementData.length) {
				throw new ConcurrentModificationException();
			}
			
			this.cursor = i + 1;
			this.direction = 1;
			
			return ((MediaObject) elementData[this.lastRet = i]);
		}
		
		public void add(MediaObject element) {
			this.checkForComodification();
			
			try {
				int i = this.cursor;
				
				PlayQueue.this.add(i, element);
				this.cursor = i + 1;
				this.lastRet = -1;
				this.direction = 1;
				this.expectedModCount = PlayQueue.this.modCount;
			}
			catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
		
		public void push(MediaObject element) {
			this.checkForComodification();
			
			try {
				int i = PlayQueue.this.size();
				
				PlayQueue.this.add(i, element);
				this.expectedModCount = PlayQueue.this.modCount;
			}
			catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
		
		public void set(MediaObject element) {
			if (this.lastRet < 0) {
				throw new IllegalStateException();
			}
			
			this.checkForComodification();

			try {
				PlayQueue.this.set(this.lastRet, element);
			}
			catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
		
		public void remove() {
			if (this.lastRet < 0) {
				throw new IllegalStateException();
			}
			
			this.checkForComodification();
			
			try {
				PlayQueue.this.remove(this.lastRet);
				this.cursor = this.lastRet;
				this.lastRet = -1;
				this.direction = -1;
				this.expectedModCount = PlayQueue.this.modCount;
			}
			catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}
		
		public int getDirection() {
			return this.direction;
		}
		
		public int getCursor() {
			return this.cursor;
		}
		
		public PlayQueue getQueue() {
			return PlayQueue.this;
		}
		
		private final void checkForComodification() {
			if (PlayQueue.this.modCount != this.expectedModCount) {
				throw new ConcurrentModificationException();
			}
		}
	}
}
