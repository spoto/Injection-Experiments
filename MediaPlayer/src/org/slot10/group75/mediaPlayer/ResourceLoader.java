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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Loads resources from file system
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public final class ResourceLoader {
	private static ResourceLoader instance = null;
	private HashMap<String, BufferedImage> images = new HashMap<>();
	
	private ResourceLoader() { }
	
	public static ResourceLoader getInstance() {
		if (instance == null) {
			instance = new ResourceLoader();
		}
		
		return instance;
	}
	
	/**
	 * Gets the named image from resources folder.
	 * Images must be in png format with file ending in lower case.
	 * Once loaded the image is cached for performance reasons.
	 * @param	name		name of the image without file ending
	 * @return				BufferedImage instance for the image
	 */
	public BufferedImage getImage(String name) {
		if (this.images.containsKey(name)) {
			return this.images.get(name);
		}
		
		return this.loadImage(name);
	}
	
	/**
	 * Loads the image from file system
	 * @param	name		name of the image
	 * @return				BufferedImage insance for the image
	 */
	private BufferedImage loadImage(String name) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("resources/" + name + ".png"));
			this.images.put(name, image);
			
			return image;
		}
		catch (IOException e) {
			Core.getInstance().handleCriticalError(e);
			return null;
		}
	}
}
