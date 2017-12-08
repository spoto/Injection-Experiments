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
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * Shows the About dialog
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class AboutDialog extends JDialog {
	private static final long serialVersionUID = -1146261266670016531L;

	public AboutDialog(ISTEMediaPlayer mediaPlayer) {
		super(mediaPlayer, "Über ISTE-Media Player", true);
		Point mediaPlayerRect = mediaPlayer.getLocationOnScreen();
		this.setBounds(mediaPlayerRect.x + 50, mediaPlayerRect.y + 50, 450, 230);
		this.setTitle("Über ISTE-Media Player");
		this.setResizable(false);
		this.getContentPane().setLayout(new BorderLayout());
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(null);
		contentPanel.setBorder(null);
		this.getContentPane().add(contentPanel);
		
		JTextArea taLicense = new JTextArea("ISTE-Media Player 1.0.0 dev\n\n" + 
			"Copyright (C) 2013 Stefan Hahn, Jiacheng Qian, Andreas Mannsdörfer\n" + 
			"This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.\n" + 
			"This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.\n" + 
			"You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.\n\n" +
			"Playlist and song icons by Yusuke Kamiyamane.\n" +
			"Close icon by Icojam.\n" +
			"Other control icons by Rizwan Ashraf."
		);
		taLicense.setBackground(SystemColor.control);
		taLicense.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		taLicense.setEditable(false);
		taLicense.setFont(new Font("Tahoma", Font.PLAIN, 11));
		taLicense.setLineWrap(true);
		taLicense.setWrapStyleWord(true);
		
		JScrollPane licenseScrollPane = new JScrollPane(taLicense);
		licenseScrollPane.setBounds(0, 0, 444, 170);
		licenseScrollPane.setBorder(null);
		licenseScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPanel.add(licenseScrollPane);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnClose = new JButton("Schließen");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AboutDialog.this.setVisible(false);
				AboutDialog.this.dispose();
			}
		});
		buttonPane.add(btnClose);
		getRootPane().setDefaultButton(btnClose);

		this.setVisible(true);
	}
}
