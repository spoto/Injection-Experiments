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
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.user.InvalidUserLoginDataException;

/**
 * Displays the user login form.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class LoginForm extends JFrame {
	private static final long serialVersionUID = 3381754244884830628L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField username;
	private JPasswordField password;
	
	public LoginForm() {
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setTitle("Login");
		this.setSize(270, 136);
		this.setBounds(GUIHandler.calcWindowCenterBounds(this));
		this.getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.getContentPane().add(contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(null);
		
		username = new JTextField();
		username.setBounds(145, 11, 109, 25);
		username.setColumns(10);
		username.addFocusListener(new JTextFieldFocusGainedSelectAllListener());
		contentPanel.add(username);
		
		password = new JPasswordField();
		password.setBounds(145, 47, 109, 25);
		password.setColumns(10);
		password.addFocusListener(new JTextFieldFocusGainedSelectAllListener());
		contentPanel.add(password);
		
		JLabel lblUsername = new JLabel("Benutzername");
		lblUsername.setLabelFor(username);
		lblUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUsername.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblUsername.setBounds(10, 13, 125, 20);
		contentPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Passwort");
		lblPassword.setLabelFor(password);
		lblPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPassword.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lblPassword.setBounds(10, 49, 125, 20);
		contentPanel.add(lblPassword);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnConfirm = new JButton("OK");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Core.getInstance().loginUser(LoginForm.this.username.getText(), new String(LoginForm.this.password.getPassword()));
					Core.getInstance().getGUIHandler().sendMessage(GUIHandlerMessage.showPlayerMessage);
					LoginForm.this.dispose();
				}
				catch (InvalidUserLoginDataException ex) {
					Core.getInstance().handleError(ex);
					LoginForm.this.username.requestFocus();
				}
			}
		});
		btnConfirm.setActionCommand("OK");
		buttonPane.add(btnConfirm);
		getRootPane().setDefaultButton(btnConfirm);
	
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Core.getInstance().shutDown();
			}
		});
		btnCancel.setActionCommand("Cancel");
		buttonPane.add(btnCancel);
	}
}
