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
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.user.User;
import org.slot10.group75.mediaPlayer.user.UserAlreadyExistsException;
import org.slot10.group75.mediaPlayer.user.UserData;

/**
 * Shows the user edit dialog. Used to edit own accounts ofr every user
 * and for editing other accounts for admins.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class UserEditDialog extends JDialog {
	private static final long serialVersionUID = 8403868914485092191L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField username;
	private JPasswordField password;
	private JComboBox<String> groupBox;
	private Window parent;
	private UserData userData;

	public UserEditDialog(JFrame parent, UserData userData) throws PermissionDeniedException {
		super(parent, "Benutzer bearbeiten", true);
		this.parent = parent;
		this.userData = userData;
		this.init();
	}
	
	/**
	 * @wbp.parser.constructor
	 */
	public UserEditDialog(JDialog parent, UserData userData) throws PermissionDeniedException {
		super(parent, "Benutzer bearbeiten", true);
		this.parent = parent;
		this.userData = userData;
		this.init();
	}
	
	private void init() throws PermissionDeniedException {
		if (Core.getInstance().getUser().getRole() != User.ROLE_ADMIN) {
			throw new PermissionDeniedException();
		}
		
		Point parentRect = this.parent.getLocationOnScreen();
		this.setResizable(false);
		this.setBounds(parentRect.x + 50, parentRect.y + 50, 240, 170);
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setLayout(null);
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		username = new JTextField();
		username.setBounds(138, 11, 86, 25);
		username.addFocusListener(new JTextFieldFocusGainedSelectAllListener());
		username.setText(this.userData.name);
		contentPanel.add(username);
		
		password = new JPasswordField();
		password.setBounds(138, 47, 86, 25);
		password.addFocusListener(new JTextFieldFocusGainedSelectAllListener());
		password.setText("     ");
		contentPanel.add(password);
		
		groupBox = new JComboBox<>();
		groupBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Benutzer", "Administrator"}));
		groupBox.setBounds(138, 83, 86, 25);
		groupBox.setSelectedIndex(this.userData.role);
		contentPanel.add(groupBox);
		
		JLabel lblUsername = new JLabel("Benutzername");
		lblUsername.setFocusable(false);
		lblUsername.setLabelFor(username);
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setBounds(10, 11, 118, 25);
		contentPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Passwort");
		lblPassword.setLabelFor(password);
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setBounds(10, 46, 118, 26);
		contentPanel.add(lblPassword);
		
		JLabel lblGroup = new JLabel("Gruppe");
		lblGroup.setLabelFor(groupBox);
		lblGroup.setHorizontalAlignment(SwingConstants.TRAILING);
		lblGroup.setBounds(10, 82, 118, 26);
		contentPanel.add(lblGroup);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnSave = new JButton("Speichern");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = UserEditDialog.this.username.getText();
				String password = new String(UserEditDialog.this.password.getPassword());
				String group = "benutzer";
				int role = UserEditDialog.this.userData.role;
				
				if (!username.equals("") && !password.equals("")) {
					if (username.equals(UserEditDialog.this.userData.name)) {
						username = null;
					}
					
					if (password.equals("     ")) {
						password = null;
					}
					
					if (UserEditDialog.this.groupBox.isEnabled()) {
						group = ((String) UserEditDialog.this.groupBox.getSelectedItem()).toLowerCase();
						
						if (group.equals("benutzer")) {
							role = User.ROLE_USER;
						}
						else if (group.equals("administrator")) {
							role = User.ROLE_ADMIN;
						}
						else {
							role = User.ROLE_USER;
						}
					}
					
					try {
						UserData newUserData = User.updateUser(UserEditDialog.this.userData, username, password, role, null);
						
						if (UserEditDialog.this.parent instanceof ManageUsersDialog) {
							ManageUsersDialog dialog = (ManageUsersDialog) UserEditDialog.this.parent;
							
							// FIXME: users vanish when editing
							dialog.usersList.userData.remove(UserEditDialog.this.userData.name);
							((DefaultListModel<String>) dialog.usersList.getModel()).removeElement(UserEditDialog.this.userData.name);
							dialog.usersList.userData.put(username, newUserData);
							((DefaultListModel<String>) dialog.usersList.getModel()).addElement(username);
						}
						
						UserEditDialog.this.setVisible(false);
						UserEditDialog.this.dispose();
					}
					catch (UserAlreadyExistsException ex) {
						JOptionPane.showMessageDialog(UserEditDialog.this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		buttonPane.add(btnSave);
		
		JButton btnAbort = new JButton("Abbrechen");
		btnAbort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserEditDialog.this.setVisible(false);
				UserEditDialog.this.dispose();
			}
		});
		buttonPane.add(btnAbort);
		
		this.getRootPane().setDefaultButton(btnSave);
	}
}
