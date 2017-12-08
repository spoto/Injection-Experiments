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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.MediaObject;
import org.slot10.group75.mediaPlayer.MediaObjectPlayer;
import org.slot10.group75.mediaPlayer.MediaObjectPlayerMessage;
import org.slot10.group75.mediaPlayer.PlayQueue;
import org.slot10.group75.mediaPlayer.PlayQueueIterator;

/**
 * Variation of JTree displaying a playqueue.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class PlayQueueTree extends JTree {
	private static final long serialVersionUID = 6204260239225602910L;
	
	public DefaultMutableTreeNode selectedNode = null;
	
	public PlayQueueTree(PlayQueue playQueue) {
		this.setModel(new PlayQueueTreeModel(playQueue));
		this.setShowsRootHandles(true);
		this.setRootVisible(false);
		this.setToggleClickCount(0);
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.addMouseListener(new PlayQueueTreeMouseListener());
		this.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) PlayQueueTree.this.getLastSelectedPathComponent();
				
				if (node == null) {
					PlayQueueTree.this.selectedNode = null;
				}
				else {
					PlayQueueTree.this.selectedNode = node;
				}
			}
		});
		this.expandPath(new TreePath(new TreeNode[] { this.getModel().getRoot(), ((DefaultMutableTreeNode) this.getModel().getRoot().getChildAt(0)) }));
		
		// TODO: Use own renderer which can handle mediaobject childs and playing mediaobjects
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) this.getCellRenderer();
		renderer.setLeafIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("songTreeNode")));
		renderer.setClosedIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("playlistTreeNode")));
		renderer.setOpenIcon(new ImageIcon(Core.getInstance().getResourceLoader().getImage("playlistTreeNode")));
	}
	
	@Override
	public PlayQueueTreeModel getModel() {
		return ((PlayQueueTreeModel) super.getModel());
	}
	
	public DefaultMutableTreeNode getSelectedNode() {
		return this.selectedNode;
	}
	
	private class PlayQueueTreeMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if ((e.getClickCount() == 2) && (e.getSource() instanceof PlayQueueTree)) {
				DefaultMutableTreeNode treeNode = ((DefaultMutableTreeNode) ((PlayQueueTree) e.getSource()).getLastSelectedPathComponent());
				
				if (treeNode != null) {
					Object userObject = treeNode.getUserObject();
					PlayQueueIterator playable = null;
					
					/* only needed when dbclicked a leaf */
					DefaultMutableTreeNode childNode = null;
					Object childUserObject = null;
					
					if (userObject instanceof MediaObject) {
						childNode = treeNode;
						childUserObject = userObject;
						
						treeNode = (DefaultMutableTreeNode) treeNode.getParent();
						userObject = treeNode.getUserObject();
					}
					
					if (userObject instanceof PlayQueue) {
						if (childUserObject instanceof MediaObject) {
							playable = ((PlayQueue) userObject).listIterator(treeNode.getIndex(childNode));
						}
						else {
							playable = ((PlayQueue) userObject).listIterator();
						}
					}
					else {
						// TODO: what to do if we don't have a PlayQueue now?
					}
					
					MediaObjectPlayer.getInstance().sendMessage(new MediaObjectPlayerMessage(MediaObjectPlayerMessage.PLAY, playable));
				}
			}
		}
	}
}
