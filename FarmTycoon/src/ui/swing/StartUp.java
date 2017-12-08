package ui.swing;

import javax.swing.JOptionPane;

public class StartUp {
	static TitleScreen title;
	static GameScreen game;
	
	public StartUp(){
		title = new TitleScreen();
		title.setLocationRelativeTo(null);
		title.setVisible(true);
	}
	
	public static void newGame(){
		title.dispose();
		domain.Controller.getInstance().newGame();
		game = new GameScreen(domain.Controller.getInstance().getGame());
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
	public static void loadGame(){
		if(!domain.Controller.getInstance().saveExists()){
			JOptionPane.showMessageDialog(null, ui.Translator.getString("LoadFailNoSave"));
			return;
		}
		title.dispose();
		domain.Controller.getInstance().loadGame();
		game = new GameScreen(domain.Controller.getInstance().getGame());
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}
