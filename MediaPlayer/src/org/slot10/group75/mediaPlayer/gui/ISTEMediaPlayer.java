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

import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.MediaObjectPlayer;
import org.slot10.group75.mediaPlayer.MediaObjectPlayerMessage;
import org.slot10.group75.mediaPlayer.Playable;

/**
 * Shows the main window with all the components needed.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class ISTEMediaPlayer extends JFrame {
	private static final long serialVersionUID = 1555578239642981499L;
	private JPanel contentPane;
	
	protected JPanel controllsPanel;
	protected JLabel lblPrevious;
	public JLabel lblPlayPause;
	protected JLabel lblNext;
	protected JLabel lblStop;
	protected JLabel lblClose;
	protected ScrollPane playlistTreeScrollPane;
	protected PlayQueueTree playlistTree;
	protected JLabel lblMainImage;
	protected JScrollPane currentPlaylistScrollPane;
	protected PlayQueueTable currentPlaylistTable;
	
	public ISTEMediaPlayer() {
		this.setTitle("ISTE-Media Player");
		this.setResizable(false);
		this.setName("ISTEMediaPlayer");
		this.setLocationByPlatform(true);
		this.setFocusable(false);
		// FIXME: when message broadcaster works, replace close op with terminate broadcasting
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(782, 535);
		this.setBounds(GUIHandler.calcWindowCenterBounds(this));
		
		ISTEMediaPlayerMenuBar menuBar = new ISTEMediaPlayerMenuBar(this);
		this.setJMenuBar(menuBar);
		
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(null);
		
		this.controllsPanel = new JPanel();
		this.controllsPanel.setFocusable(false);
		this.controllsPanel.setBounds(-15, 0, 350, 70);
		this.contentPane.add(this.controllsPanel);
		
		this.lblPrevious = new MediaPlayerControlLabel("Zurück", new ImageIcon(Core.getInstance().getResourceLoader().getImage("previous")));
		this.lblPrevious.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getSource() instanceof MediaPlayerControlLabel)) {
					MediaObjectPlayer.getInstance().sendMessage(MediaObjectPlayerMessage.playPreviousMessage);
				}
			}
		});
		this.controllsPanel.add(this.lblPrevious);
		
		this.lblPlayPause = new MediaPlayerControlLabel("Start", new ImageIcon(Core.getInstance().getResourceLoader().getImage("play")));
		this.lblPlayPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getSource() instanceof MediaPlayerControlLabel)) {
					MediaPlayerControlLabel label = (MediaPlayerControlLabel) e.getSource();
					
					if (label.getText() == "Start") {
						if (MediaObjectPlayer.getInstance().isPaused()) {
							MediaObjectPlayer.getInstance().sendMessage(MediaObjectPlayerMessage.restartPlayingMessage);
						}
						else {
							if (ISTEMediaPlayer.this.playlistTree.getSelectedNode().getUserObject() instanceof Playable) {
								MediaObjectPlayer.getInstance().sendMessage(new MediaObjectPlayerMessage(MediaObjectPlayerMessage.PLAY, (Playable) ISTEMediaPlayer.this.playlistTree.getSelectedNode().getUserObject()));
							}
						}
					}
					else {
						MediaObjectPlayer.getInstance().sendMessage(MediaObjectPlayerMessage.pausePlayingMessage);
					}
				}
			}
		});
		this.controllsPanel.add(this.lblPlayPause);
		
		this.lblNext = new MediaPlayerControlLabel("Weiter", new ImageIcon(Core.getInstance().getResourceLoader().getImage("next")));
		this.lblNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getSource() instanceof MediaPlayerControlLabel)) {
					MediaObjectPlayer.getInstance().sendMessage(MediaObjectPlayerMessage.playNextMessage);
				}
			}
		});
		this.controllsPanel.add(this.lblNext);
		
		this.lblStop = new MediaPlayerControlLabel("Stop", new ImageIcon(Core.getInstance().getResourceLoader().getImage("stop")));
		this.lblStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getSource() instanceof MediaPlayerControlLabel)) {
					MediaObjectPlayer.getInstance().sendMessage(MediaObjectPlayerMessage.stopPlayingMessage);
				}
			}
		});
		this.controllsPanel.add(this.lblStop);
		
		this.lblClose = new MediaPlayerControlLabel("Beenden", new ImageIcon(Core.getInstance().getResourceLoader().getImage("close")));
		this.lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 1) && (e.getSource() instanceof MediaPlayerControlLabel)) {
					Core.getInstance().shutDown();
				}
			}
		});
		this.controllsPanel.add(this.lblClose);
		
		this.playlistTreeScrollPane = new ScrollPane();
		this.playlistTreeScrollPane.setBounds(10, 81, 200, 395);
		this.contentPane.add(this.playlistTreeScrollPane);
		
		this.playlistTree = new PlayQueueTree(Core.getInstance().getUser().getPlaylist());
		this.playlistTreeScrollPane.add(this.playlistTree);
		
		this.lblMainImage = new JLabel(new ImageIcon(Core.getInstance().getResourceLoader().getImage("main")));
		this.lblMainImage.setHorizontalAlignment(SwingConstants.CENTER);
		this.lblMainImage.setFocusable(false);
		this.lblMainImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.lblMainImage.setBounds(222, 82, 544, 248);
		this.contentPane.add(this.lblMainImage);
		
		this.currentPlaylistScrollPane = new JScrollPane();
		this.currentPlaylistScrollPane.setBounds(222, 341, 544, 135);
		this.contentPane.add(this.currentPlaylistScrollPane);
		
		this.currentPlaylistTable = new PlayQueueTable(Core.getInstance().getUser().getPlaylist());
		this.currentPlaylistScrollPane.setViewportView(this.currentPlaylistTable);
	}
}
