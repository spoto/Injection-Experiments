package ui.swing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ui.Translator;

@SuppressWarnings("serial")
public class TitleScreen extends javax.swing.JFrame {
	private static Image backgroundImage;
	private JLabel newGameButton, loadGameButton;

	public TitleScreen() {
		super(Translator.getString("WELCOMEMSG"));
		try {
			setSize(480, 300);
			backgroundImage = ImageIO.read(TitleScreen.class.getClassLoader()
					.getResource("ui/swing/images/titleBackground.png"));
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setResizable(false);

			getContentPane().add(new LangButton("en", 0));
			getContentPane().add(new LangButton("nl", 1));
			getContentPane().add(new LangButton("fr", 2));
			getContentPane().add(new LangButton("no", 3));
			
			newGameButton = new JLabel(){{
				setBounds(304, 12, 162, 38);
				setFont(new java.awt.Font("Abyssinica SIL", 1, 16));
				setForeground(new java.awt.Color(0, 0, 0));
				addMouseListener(new ButtonListener("newGame"));
			}};
			loadGameButton = new JLabel(){{
				setBounds(304, 50, 162, 38);
				setFont(new java.awt.Font("Abyssinica SIL", 1, 16));
				if (domain.Controller.getInstance().saveExists())
					setForeground(new java.awt.Color(0, 0, 0));
				else
					setForeground(new java.awt.Color(128, 128, 128));
				addMouseListener(new ButtonListener("loadGame"));
			}};
			
			updateLang();
			
			getContentPane().add(newGameButton);
			getContentPane().add(loadGameButton);
			getContentPane().add(new JPanel() {
				{setBounds(0, 0, 480, 300);}
				public void paint(Graphics g) {
					super.paint(g);
					g.drawImage(TitleScreen.backgroundImage, 0, 0, null);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateLang(){
		newGameButton.setText(ui.Translator.getString("newGame"));
		loadGameButton.setText(ui.Translator.getString("loadGame"));
	}

	private class ButtonListener extends MouseAdapter{
		String action;
		ButtonListener(String action){
			this.action=action;
		}
		public void mouseExited(MouseEvent evt) {
			evt.getComponent().setFont(
					new java.awt.Font("Abyssinica SIL", 1, 16));
		}

		public void mouseEntered(MouseEvent evt) {
			evt.getComponent().setFont(
					new java.awt.Font("Abyssinica SIL", 3, 16));
		}

		public void mouseClicked(MouseEvent evt) {
			if(action == "newGame")
				StartUp.newGame();
			if(action == "loadGame")
				StartUp.loadGame();
		}
	}
	
	private class LangButton extends JLabel{
		private String locale;
		private Image image;
		LangButton(String lang, int id) throws IOException{
			super();
			setBounds(12 + (id * 76), 190, 64, 64);
			setSize(64, 64);
			locale=lang;
			image = ImageIO.read(getClass().getClassLoader()
					.getResource("ui/swing/images/lang_" + lang + ".png")).getScaledInstance(64, 64, Image.SCALE_DEFAULT);
			addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					Translator.setLocale(locale);
					updateLang();
				}
			});
		}
		public void paint(Graphics g) {
			g.drawImage(this.image, 0, 0, null);
		}
	}
}
