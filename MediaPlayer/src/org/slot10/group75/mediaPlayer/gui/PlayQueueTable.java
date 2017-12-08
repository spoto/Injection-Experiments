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

import javax.swing.JTable;

import org.slot10.group75.mediaPlayer.PlayQueue;

/**
 * Represents a variation of JTable displaying a playqueue.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class PlayQueueTable extends JTable {
	private static final long serialVersionUID = -4247419040000136580L;
	
	/**
	 * Initiates this table with the given playqueue data.
	 * 
	 * @param	playQueue		any playqueue
	 */
	public PlayQueueTable(PlayQueue playQueue) {
		super(new PlayQueueTableModel(playQueue));
		this.setFocusable(false);
		this.setAutoCreateRowSorter(true);
		this.setFillsViewportHeight(true);
		this.getColumnModel().setColumnMargin(5);
	}
	
	@Override
	public PlayQueueTableModel getModel() {
		return (PlayQueueTableModel) super.getModel();
	}
}
