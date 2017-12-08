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
package org.slot10.group75.mediaPlayer.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Represents a label with image for control bar.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class MediaPlayerControlLabel extends JLabel {
	private static final long serialVersionUID = -4103731280775253159L;
	
	/**
	 * Creates a control label - a variation of <code>JLabel</code> - with the specified icon and text.
	 * The text is displayed under the icon in the center of the label.
	 * 
	 * @param	text	The text to be displayed on the label
	 * @param	icon	The icon to be displayed on the label
	 */
	public MediaPlayerControlLabel(String text, ImageIcon icon) {
		super(text, icon, JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setFocusable(false);
		this.setMinimumSize(new Dimension(55, 65));
		this.setPreferredSize(new Dimension(55, 65));
	}
}
