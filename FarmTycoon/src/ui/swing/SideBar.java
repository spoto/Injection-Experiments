package ui.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import api.TileInfo;

import ui.Translator;

import domain.Game;
import exceptions.InvalidStateException;

public class SideBar extends javax.swing.JPanel implements ComponentListener {

	private JPanel selectedImage;
	private JLabel selectedName;
	private Game game;
	private GameScreen gameScreen;
	private JPanel actionsPanel;
	private TileInfo info;
	private JScrollPane infopanel;

	private class ActionButton extends JPanel {
		private api.TileAction action;

		ActionButton(api.TileAction action) {
			super();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(new JLabel(Translator.getString(action.name())));
			add(Box.createHorizontalGlue());
			if (action.getTime()!= 0)
				add(new JLabel(String.format(Translator.getString("daystring"), action.getTime())));
			add(Box.createRigidArea(new Dimension(10,2)));
			if (action.getCost()!= 0)
				add(new JLabel(String.format(Translator.getString("moneystring"), action.getCost())));

			this.action = action;
			this.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					execute();
				}
			});
		}

		private void execute() {
			if(action.name().equals("ENTER")) {
				if(info.getField().equals("Market")) {
					gameScreen.showMarket();
				}
			}
			game.executeAction(gameScreen.getSelected().getCoords(), action);
			gameScreen.updateMarket();
		}
	}

	public SideBar(domain.Game game, GameScreen gameScreen) {
		super();
		this.game = game;
		this.gameScreen = gameScreen;
		initGUI();
	}

	private void initGUI() {
		try {
			BoxLayout layout = new BoxLayout(this, javax.swing.BoxLayout.Y_AXIS);
			this.setLayout(layout);
			this.setPreferredSize(new java.awt.Dimension(300, 570));
			addComponentListener(this);
			{
				selectedImage = new JPanel() {
					public Image img;

					public void paintComponent(Graphics g) {
						try {
							img = Images.getImage(
									game.getTileInfo(
											gameScreen.getSelected()
													.getCoords()).toString()
											.toUpperCase()).getScaledInstance(
									300, 300, Image.SCALE_DEFAULT);
						} catch (Exception e) {
							img = null;
						}
						if (img != null)
							g.drawImage(img, 0, 0, null);
					}

				};
				this.add(selectedImage);
				selectedImage.setSize(300, 300);
			}
			selectedName = new JLabel();
			actionsPanel = new JPanel();
			infopanel = new JScrollPane(actionsPanel);
			add(selectedName);
			add(Box.createVerticalStrut(5));
			add(infopanel);
			actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
		this.selectedImage.repaint();
		if (gameScreen.getSelected() != null) {
			if (info==null || !game.getTileInfo(gameScreen.getSelected().getCoords())
					.toString().equals(info.toString())) {
				this.info = game.getTileInfo(gameScreen.getSelected()
						.getCoords());
				this.selectedName
						.setText(Translator.getString(info.toString()));
				actionsPanel.removeAll();
				try {
					for (api.TileAction action : game.getTileActions(gameScreen
							.getSelected().getCoords())){
						actionsPanel.add(new ActionButton(action));
					}
				} catch (NullPointerException e) {
				} catch (InvalidStateException e) {
				}
				actionsPanel.repaint();
			}
		}
	}
	public void componentResized(ComponentEvent evt) {
		int height = getHeight();
		infopanel.setSize(300, height - 300);
		infopanel.setPreferredSize(new Dimension(300,height-300));
		infopanel.setMaximumSize(new Dimension(300,height-300));
		infopanel.setMinimumSize(new Dimension(300,height-300));
	}

	public void componentHidden(ComponentEvent evt) {}
	public void componentMoved(ComponentEvent evt) {}
	public void componentShown(ComponentEvent evt) {}

}
