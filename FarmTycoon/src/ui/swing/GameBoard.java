package ui.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.HashMap;
import java.util.Map;

import api.Coordinate;

import domain.Game;

public class GameBoard extends javax.swing.JPanel {
	private Game game;
	private Map<Coordinate,TilePanel> tiles = new HashMap<Coordinate,TilePanel>();
	private GameScreen gameScreen;

	public GameBoard(domain.Game game, GameScreen gameScreen) {
		super();
		this.game = game;
		this.gameScreen = gameScreen;
		initGUI();
	}

	private void initGUI() {
		try {
			GridBagLayout layout = new GridBagLayout();
			layout.rowWeights = new double[domain.Farm.size.getY()];
			layout.rowHeights = new int[domain.Farm.size.getY()];
			layout.columnWeights = new double[domain.Farm.size.getX()];
			layout.columnWidths = new int[domain.Farm.size.getX()];
			for (int i=0; i<domain.Farm.size.getY(); i++) {
				layout.rowHeights[i] = 7;
				layout.rowWeights[i] = 0.1;
			}
			for (int i=0; i<domain.Farm.size.getX(); i++) {
				layout.columnWidths[i] = 7;
				layout.columnWeights[i] = 0.1;
			}
			this.setLayout(layout);
			for (Coordinate i : Coordinate.getCoordSet(new Coordinate(0,0),
						domain.Farm.size)) {
				TilePanel tile = new TilePanel(game,i);
				this.add(tile, new GridBagConstraints(i.getX(), i.getY(), 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				tile.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						gameScreen.select((TilePanel) evt.getComponent());
					}
				});
				tiles.put(i,tile);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void update() {
		for (TilePanel tile : tiles.values())
			tile.update();
	}

}
