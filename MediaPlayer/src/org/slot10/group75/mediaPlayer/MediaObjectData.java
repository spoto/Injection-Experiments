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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Serializable data of a media object
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class MediaObjectData implements Serializable {
	private static final long serialVersionUID = 3463884542114739330L;
	
	public String path;
	public HashMap<String, Serializable> propertiesCache = new HashMap<>();
	
	/**
	 * Creates a new media object data object
	 * @param	path			file system path
	 * @param	propertiesCache	cached serializable properties
	 */
	public MediaObjectData(String path, HashMap<String, ?> propertiesCache) {
		this.path = path;
		
		Object tmp = null;
		Set<String> keySet = propertiesCache.keySet();
		
		for (String key: keySet) {
			tmp = propertiesCache.get(key);
			
			if ((tmp instanceof Serializable) && !(tmp instanceof List)) {
				this.propertiesCache.put(key, (Serializable) tmp);
			}
		}
	}
}
