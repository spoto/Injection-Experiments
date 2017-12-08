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

import java.io.Serializable;
import java.util.List;

/**
 * PlayQueue implementation representing a playlist
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class Playlist extends PlayQueue implements Serializable {
	private static final long serialVersionUID = 3308227346004999434L;
	
	public Playlist(String name, List<MediaObject> mediaObjectList) {
		super(name, mediaObjectList);
	}
	
	public Playlist(String name) {
		super(name);
	}
	
	@Override
	public void checkName(String name) {
		// not allowed are / : < > " * ? | \0
		// needed if you save playlist in file system by its name
		if (name.matches(".*?([/:<>\"\\*\\?\\|\\x00]+).*?")) {
			throw new IllegalArgumentException("Playlist name \"" + name + "\" contains invalid characters");
		}
	}
}
