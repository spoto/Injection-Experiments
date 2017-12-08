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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.user.User;
import javax.swing.border.LineBorder;
import java.awt.Color;

/**
 * Shows the users manage dialog.
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class ManageUsersDialog extends JDialog {
	private static final long serialVersionUID = 6708424548041396552L;
	
	private final JPanel contentPanel = new JPanel();
	protected UsersManageList usersList = new UsersManageList();
	
	/**
	 * Creates a new users manage dialog
	 * @param	mediaPlayer		instance of current media player ui
	 */
	public ManageUsersDialog(ISTEMediaPlayer mediaPlayer) {
		super(mediaPlayer, "Benutzer verwalten", true);
		
		Point mediaPlayerRect = mediaPlayer.getLocationOnScreen();
		this.setBounds(mediaPlayerRect.x + 50, mediaPlayerRect.y + 50, 240, 300);
		this.setResizable(false);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setLayout(null);
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(contentPanel, BorderLayout.CENTER);
		usersList.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane(this.usersList);
		scrollPane.setBorder(new LineBorder(new Color(130, 135, 144)));
		scrollPane.setBounds(10, 10, 111, 250);
		contentPanel.add(scrollPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(buttonPane, BorderLayout.EAST);
		
		JButton btnAdd = new JButton("Hinzufügen");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserAddDialog addDialog = new UserAddDialog(ManageUsersDialog.this);
				addDialog.setVisible(true);
			}
		});
		buttonPane.setLayout(new GridLayout(8, 5, 0, 5));
		buttonPane.add(btnAdd);
		
		JButton btnEdit = new JButton("Bearbeiten");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = ManageUsersDialog.this.usersList.getSelectedValue();
				
				if (name != null) {
					try {
						UserEditDialog editDialog = new UserEditDialog(ManageUsersDialog.this, ManageUsersDialog.this.usersList.userData.get(name));
						editDialog.setVisible(true);
					}
					catch (PermissionDeniedException ex) {
						Core.getInstance().handleError(ex);
					}
				}
			}
		});
		buttonPane.add(btnEdit);
		
		JButton btnEntfernen = new JButton("Entfernen");
		btnEntfernen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = ManageUsersDialog.this.usersList.getSelectedValue();
				
				if (name != null) {
					if (name.equals(Core.getInstance().getUser().getName())) {
						JOptionPane.showMessageDialog(ManageUsersDialog.this, "Eigener Account kann nicht gelöscht werden", "Fehler", JOptionPane.ERROR_MESSAGE);
					}
					else {
						User.deleteUser(name);
						((DefaultListModel<String>) ManageUsersDialog.this.usersList.getModel()).removeElement(name);
					}
				}
			}
		});
		buttonPane.add(btnEntfernen);
		
		JButton btnClose = new JButton("Schließen");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageUsersDialog.this.setVisible(false);
				ManageUsersDialog.this.dispose();
			}
		});
		buttonPane.add(btnClose);
		
		//this.getRootPane().setDefaultButton(btnClose);
		this.setVisible(true);
	}
}
