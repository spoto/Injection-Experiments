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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.slot10.group75.mediaPlayer.MediaObject;
import org.slot10.group75.mediaPlayer.PlayQueue;

/**
 * Implementation of DefaultTreeModel for displaying data in a PlayQueueTree.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
// TODO: make sure you can only add nodes to root and PlayQueueTreeNodes
public class PlayQueueTreeModel extends DefaultTreeModel {
	private static final long serialVersionUID = -1480293560768035455L;
	
	public PlayQueueTreeModel(PlayQueue playQueue) {
		super(new DefaultMutableTreeNode(), true);
		
		this.resetData(playQueue);
	}
	
	public void resetData(PlayQueue playQueue) {
		if (playQueue == null) {
			throw new IllegalArgumentException("PlayQueue is null");
		}
		
		PlayQueue queue = (PlayQueue) playQueue.clone();
		
		// FIXME: have to clear current node before adding new data
		// this.clear()
		this.insertNodeInto(new PlayQueueTreeNode(queue), (DefaultMutableTreeNode) this.root, 0);
	}
	
	@Override
	public DefaultMutableTreeNode getRoot() {
		return ((DefaultMutableTreeNode) this.root);
	}
	
	@Override
	public boolean isLeaf(Object node) {
		DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
		
		if ((treeNode.getUserObject() != null) && (treeNode.getUserObject() instanceof MediaObject)) {
			return true;
		}
		
		return false;
	}
}
