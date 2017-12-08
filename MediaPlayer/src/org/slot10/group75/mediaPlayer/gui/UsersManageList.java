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

import iste.io.ISTEStream;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.slot10.group75.mediaPlayer.user.UserData;

/**
 * Represents the list of users in UsersManageDialog.
 * 
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public class UsersManageList extends JList<String> {
	private static final long serialVersionUID = 1078637989510967379L;
	
	protected HashMap<String, UserData> userData = new HashMap<>();
	
	public UsersManageList() {
		super();
		
		this.reload();
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void reload() {
		this.loadData();
		String[] names = this.getNamesArray();
		DefaultListModel<String> listModel = new DefaultListModel<>();
		
		for (String name: names) {
			listModel.addElement(name);
		}
		
		this.setModel(listModel);
	}
	
	private void loadData() {
		File[] userFiles = (new File("userData/")).listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".usr");
			}
		});
		UserData currentUserData = null;
		
		for (File userFile: userFiles) {
			try {
				currentUserData = (UserData) ISTEStream.readObject(userFile.getPath());
				this.userData.put(currentUserData.name, currentUserData);
				currentUserData = null;
			}
			catch (IOException e) {
				continue;
			}
		}
	}
	
	private String[] getNamesArray() {
		String[] names = new String[this.userData.size()];
		Set<String> keys = this.userData.keySet();
		int i = 0;
		
		for (String username: keys) {
			names[i] = username;
			i++;
		}
		
		return names;
	}
}
