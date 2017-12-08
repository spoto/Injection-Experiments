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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slot10.group75.mediaPlayer.user.User;
import org.slot10.group75.mediaPlayer.user.UserAlreadyExistsException;

/**
 * Shows the user add dialog.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class UserAddDialog extends JDialog {
	private static final long serialVersionUID = 3179696349024650497L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField username;
	private JPasswordField password;
	private JComboBox<String> groupBox;
	private ManageUsersDialog parent;

	public UserAddDialog(ManageUsersDialog parent) {
		super(parent, "Benutzer hinzufügen", true);
		this.parent = parent;
		
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
		contentPanel.add(username);
		
		password = new JPasswordField();
		password.setBounds(138, 47, 86, 25);
		password.addFocusListener(new JTextFieldFocusGainedSelectAllListener());
		contentPanel.add(password);
		
		groupBox = new JComboBox<>();
		groupBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Benutzer", "Administrator"}));
		groupBox.setBounds(138, 83, 86, 25);
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
				String username = UserAddDialog.this.username.getText();
				String password = new String(UserAddDialog.this.password.getPassword());
				String group = ((String) UserAddDialog.this.groupBox.getSelectedItem()).toLowerCase();
				int role = 0;
				User user;
				
				if (!username.equals("") && !password.equals("")) {
					if (group.equals("benutzer")) {
						role = User.ROLE_USER;
					}
					else if (group.equals("administrator")) {
						role = User.ROLE_ADMIN;
					}
					else {
						role = User.ROLE_USER;
					}
					
					try {
						user = User.createUser(username, password, role);
						user.save();
						
						((DefaultListModel<String>) UserAddDialog.this.parent.usersList.getModel()).addElement(user.getName());
						
						UserAddDialog.this.setVisible(false);
						UserAddDialog.this.dispose();
					}
					catch (UserAlreadyExistsException ex) {
						JOptionPane.showMessageDialog(UserAddDialog.this, ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		buttonPane.add(btnSave);
		
		JButton btnAbort = new JButton("Abbrechen");
		btnAbort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserAddDialog.this.setVisible(false);
				UserAddDialog.this.dispose();
			}
		});
		buttonPane.add(btnAbort);
		
		this.getRootPane().setDefaultButton(btnSave);
	}
}
