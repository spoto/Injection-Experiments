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

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultMutableTreeNode;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.MediaObject;
import org.slot10.group75.mediaPlayer.MediaObjectPlayer;
import org.slot10.group75.mediaPlayer.MediaObjectPlayerMessage;
import org.slot10.group75.mediaPlayer.Song;
import org.slot10.group75.mediaPlayer.UnsupportedMediaObjectTypeException;
import org.slot10.group75.mediaPlayer.user.User;

/**
 * Represents the menu bar in media player frame.
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class ISTEMediaPlayerMenuBar extends JMenuBar {
	private static final long serialVersionUID = -6775783622737272192L;
	private ISTEMediaPlayer mediaPlayer = null;
	
	public ISTEMediaPlayerMenuBar(ISTEMediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
		
		JMenu mnFile = new JMenu("Datei");
		JMenuItem mntmClose = new JMenuItem("Schließen");
		mntmClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Core.getInstance().shutDown();
			}
		});
		mnFile.add(mntmClose);
		this.add(mnFile);
		
		JMenu mnUser = new JMenu("Benutzer");
		JMenuItem mntmUpdateUser = new JMenuItem("Account bearbeiten");
		mntmUpdateUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					UserEditDialog editDialog = new UserEditDialog(ISTEMediaPlayerMenuBar.this.mediaPlayer, Core.getInstance().getUser().getUserData());
					editDialog.setVisible(true);
				}
				catch (PermissionDeniedException ex) {
					Core.getInstance().handleError(ex);
				}
			}
		});
		mnUser.add(mntmUpdateUser);
		
		if (Core.getInstance().getUser().getRole() == User.ROLE_ADMIN) {
			JMenuItem mntmManageUsers = new JMenuItem("Benutzer verwalten");
			mntmManageUsers.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new ManageUsersDialog(ISTEMediaPlayerMenuBar.this.mediaPlayer);
				}
			});
			mnUser.add(mntmManageUsers);
		}
		
		this.add(mnUser);
		
		JMenu mnPlaylist = new JMenu("Playlist");
		JMenuItem mntmAddFile = new JMenuItem("Datei hinzufügen");
		mntmAddFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog loadDialog = new FileDialog(ISTEMediaPlayerMenuBar.this.mediaPlayer, "Song auswählen", FileDialog.LOAD);
				loadDialog.setFilenameFilter(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						// XXX: edit this when other file types supported
						return name.endsWith("mp3");
					}
				});
				loadDialog.setVisible(true);
				
				String dir = loadDialog.getDirectory();
				String file = loadDialog.getFile();
				
				if ((dir != null) && (file != null)) {
					// XXX: update this when other media types supported
					if (file.endsWith(".mp3") || file.endsWith(".wav")) {
						PlayQueueTreeModel treeModel = ISTEMediaPlayerMenuBar.this.mediaPlayer.playlistTree.getModel();
						DefaultMutableTreeNode playlistNode = (DefaultMutableTreeNode) treeModel.getRoot().getChildAt(0);
						Song song = new Song(dir + file);
						PlayQueueTableModel tableModel = ISTEMediaPlayerMenuBar.this.mediaPlayer.currentPlaylistTable.getModel();
						Vector<Object> songVector = new Vector<>();
						
						songVector.add(ISTEMediaPlayerMenuBar.this.mediaPlayer.currentPlaylistTable.getRowCount() + 1);
						songVector.add(song.getProperty("author"));
						songVector.add(song.getProperty("title"));
						songVector.add(song.getDurationString());
						
						treeModel.insertNodeInto(new DefaultMutableTreeNode(song), playlistNode, playlistNode.getChildCount());
						tableModel.addRow(songVector);
						MediaObjectPlayer.getInstance().sendMessage(new MediaObjectPlayerMessage(MediaObjectPlayerMessage.QUEUE, song));
						Core.getInstance().getUser().getPlaylist().add(song);
						Core.getInstance().getUser().save();
						playlistNode.setUserObject(Core.getInstance().getUser().getPlaylist());
					}
					else {
						Core.getInstance().handleError(new UnsupportedMediaObjectTypeException("Unbekannter Dateityp"));
					}
				}
			}
		});
		mnPlaylist.add(mntmAddFile);
		
		JMenuItem mntmRemoveFile = new JMenuItem("Datei entfernen");
		mntmRemoveFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ISTEMediaPlayerMenuBar.this.mediaPlayer.playlistTree.getSelectedNode().getUserObject() instanceof MediaObject) {
					PlayQueueTreeModel treeModel = ISTEMediaPlayerMenuBar.this.mediaPlayer.playlistTree.getModel();
					PlayQueueTableModel tableModel = ISTEMediaPlayerMenuBar.this.mediaPlayer.currentPlaylistTable.getModel();
					MediaObject mediaObject = (MediaObject) ISTEMediaPlayerMenuBar.this.mediaPlayer.playlistTree.getSelectedNode().getUserObject();
					
					tableModel.removeRow(ISTEMediaPlayerMenuBar.this.mediaPlayer.playlistTree.getMinSelectionRow() - 1);
					treeModel.removeNodeFromParent(ISTEMediaPlayerMenuBar.this.mediaPlayer.playlistTree.getSelectedNode());
					MediaObjectPlayer.getInstance().sendMessage(new MediaObjectPlayerMessage(MediaObjectPlayerMessage.UNQUEUE, mediaObject));
					Core.getInstance().getUser().getPlaylist().remove(mediaObject);
					Core.getInstance().getUser().save();
				}
			}
		});
		mnPlaylist.add(mntmRemoveFile);
		this.add(mnPlaylist);
		
		JMenu mnHelp = new JMenu("Hilfe");
		JMenuItem mntmAbout = new JMenuItem("Über ISTE-Media Player");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutDialog(ISTEMediaPlayerMenuBar.this.mediaPlayer);
			}
		});
		mnHelp.add(mntmAbout);
		this.add(mnHelp);
	}
}
