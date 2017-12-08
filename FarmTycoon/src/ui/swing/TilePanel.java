package ui.swing;

import java.awt.Graphics;
import java.awt.Image;

import api.Coordinate;

@SuppressWarnings("serial")
public class TilePanel extends javax.swing.JPanel {
	private domain.Game game;
	private Coordinate coords;
	private Image bgimage;
	private static Image cursor;
	public boolean selected = false;

	TilePanel(domain.Game game, Coordinate coord) {
		super();
		this.game = game;
		this.coords = coord;
		initGUI();
		update();
	}

	private void initGUI() {
	}

	public void update() {
		this.repaint();
	}

	public void paintComponent(Graphics g) {
		bgimage = Images.getImage(game.getTileInfo(coords).toString().toUpperCase(),
				this.getSize());
		cursor = Images.getImage("SELECTED", this.getSize());
		if (bgimage != null)
			g.drawImage(bgimage, 0, 0, null);
		if (selected && cursor != null)
			g.drawImage(cursor, 0, 0, null);
	}

	public Coordinate getCoords() {
		return coords;
	}
}
