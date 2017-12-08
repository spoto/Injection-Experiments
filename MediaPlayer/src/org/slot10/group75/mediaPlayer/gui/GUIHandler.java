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

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slot10.group75.mediaPlayer.Core;
import org.slot10.group75.mediaPlayer.thread.MessagedThread;
import org.slot10.group75.mediaPlayer.thread.MessagedThreadMessage;
import org.slot10.group75.mediaPlayer.thread.MessagedThreadNoOperationException;
import org.slot10.group75.mediaPlayer.thread.MessagedThreadTerminateException;
import org.slot10.group75.mediaPlayer.thread.UnsupportedMessagedThreadMessageException;

/**
 * Handles UI acions
 * @author	Jiacheng Qian (2783671) 
 * @author	Stefan Hahn (2837462) 
 * @author	Andreas Mannsdörfer (2801964)
 */
public final class GUIHandler extends MessagedThread {
	private static GUIHandler instance = null;
	private LoginForm loginForm = null;
	private ISTEMediaPlayer playerFrame = null;
	private Window mainWindow = null;
	
	private ConcurrentLinkedQueue<MessagedThreadMessage<?>> messages = new ConcurrentLinkedQueue<>();
	
	private GUIHandler() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | UnsupportedLookAndFeelException e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
			catch (Exception ex) {
				this.displayCriticalError(ex);
			}
		}
		catch (InstantiationException | IllegalAccessException e) {
			this.displayCriticalError(e);
		}
		
		this.setName("GUIHandler");
		this.start();
	}
	
	public static GUIHandler getInstance() {
		if (instance == null) {
			instance = new GUIHandler();
		}
		
		return instance;
	}
	
	public ISTEMediaPlayer getMediaPlayerFrame() {
		return this.playerFrame;
	}
	
	public static boolean isRunning() {
		return (instance != null);
	}
	
	@Override
	protected void handleMessage() throws MessagedThreadNoOperationException, MessagedThreadTerminateException, UnsupportedMessagedThreadMessageException {
		switch (this.currentMessage.getMessage()) {
			case GUIHandlerMessage.SHOW_LOGIN_FORM:
				this.showLoginForm();
				break;
			case GUIHandlerMessage.SHOW_PLAYER:
				this.showMediaPlayerFrame();
				break;
			case GUIHandlerMessage.ERROR:
				if (this.currentMessage.getParam() instanceof Throwable) {
					this.displayError((Throwable) this.currentMessage.getParam());
				}
				else {
					throw new UnsupportedMessagedThreadMessageException("Error message param must be and instance of type \"Exception\"");
				}
				
				break;
			case GUIHandlerMessage.CRITICAL_ERROR:
				if (this.currentMessage.getParam() instanceof Throwable) {
					this.displayCriticalError((Throwable) this.currentMessage.getParam());
				}
				else {
					throw new UnsupportedMessagedThreadMessageException("Error message param must be and instance of type \"Exception\"");
				}
				
				break;
			case MessagedThreadMessage.TERMINATE:
				this.terminate();
			default:
				super.handleMessage();
		}
	}
	
	@Override
	public void sendMessage(MessagedThreadMessage<?> message) {
		this.messages.add(message);
	}
	
	@Override
	protected MessagedThreadMessage<?> pollMessage() {
		this.noOperation();
		
		return this.messages.poll();
	}
	
	@Override
	protected Queue<MessagedThreadMessage<?>> getQueue() {
		return this.messages;
	}
	
	@Override
	public Class<?> getMessageClass() {
		return GUIHandlerMessage.class;
	}
	
	protected void terminate() {
		if (this.mainWindow != null) {
			this.mainWindow.setVisible(false);
			this.mainWindow.dispose();
		}
	}
	
	protected void showLoginForm() {
		try {
			if (this.loginForm == null) {
				new Thread(new Runnable() {
					public void run() {
						try {
							GUIHandler.this.mainWindow = GUIHandler.this.loginForm = new LoginForm();
							GUIHandler.this.mainWindow.setVisible(true);
						}
						catch (RuntimeException e) {
							GUIHandler.this.displayCriticalError(e);
						}
					}
				}, "LoginForm").start();
			}
		}
		catch (Exception e) {
			this.displayCriticalError(e);
		}
	}
	
	protected void showMediaPlayerFrame() {
		try {
			if (this.playerFrame == null) {
				new Thread(new Runnable() {
					public void run() {
						try {
							GUIHandler.this.mainWindow = GUIHandler.this.playerFrame = new ISTEMediaPlayer();
							GUIHandler.this.mainWindow.setVisible(true);
						}
						catch (RuntimeException e) {
							GUIHandler.this.displayCriticalError(e);
						}
					}
				}, "ISTEMediaPlayer").start();
			}
		}
		catch (Exception e) {
			this.displayCriticalError(e);
		}
	}
	
	public void displayError(final Throwable throwable) {
		JOptionPane.showMessageDialog(GUIHandler.this.mainWindow, throwable.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
	}
	
	public void displayCriticalError(final Throwable throwable) {
		if (this.mainWindow != null) {
			this.mainWindow.setVisible(false);
			this.mainWindow.dispose();
		}
		
		StringBuffer stacktraceString = new StringBuffer();
		StackTraceElement[] stacktrace = throwable.getStackTrace();
		
		stacktraceString.append(throwable.getMessage() + "\n");
		stacktraceString.append((new Throwable()).getStackTrace()[1].getClassName() + "." + (new Throwable()).getStackTrace()[1].getMethodName() + "\n");
		
		for (StackTraceElement element: stacktrace) {
			stacktraceString.append(element.toString());
			stacktraceString.append("\n");
		}
		
		JOptionPane.showMessageDialog(null, stacktraceString.toString(), "Kritischer Fehler", JOptionPane.ERROR_MESSAGE);
		Core.getInstance().shutDown(1);
	}
	
	public static Rectangle calcWindowCenterBounds(Window window) {
		Rectangle screenBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		Rectangle centerBounds = new Rectangle(window.getWidth(), window.getHeight());
		
		centerBounds.setLocation((int) Math.floor((screenBounds.width - window.getWidth()) / 2), (int) Math.floor((screenBounds.height - window.getHeight()) / 2));
		
		return centerBounds;
	}
}
