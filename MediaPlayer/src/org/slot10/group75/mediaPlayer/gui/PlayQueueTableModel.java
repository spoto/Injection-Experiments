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

import java.util.Arrays;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.slot10.group75.mediaPlayer.PlayQueue;

/**
 * Implementation of DefaultTableModel for PlayQueueTable.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class PlayQueueTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 5144891818916303683L;
	
	private static Class<?>[] columnClasses = {Integer.class, String.class, String.class, String.class};
	
	public PlayQueueTableModel(PlayQueue playQueue) {
		super(playQueue.toDataVector(), new Vector<String>(Arrays.asList(new String[] {
			"Nummer", "Künstler", "Titel", "Dauer"
		})));
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return false;
	}
	
	@Override
	public void setValueAt(Object value, int row, int col) {
		throw new IllegalStateException("Play queue table is not editable");
	}
}
