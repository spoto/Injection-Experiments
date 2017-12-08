package ui.swing;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import exceptions.SystemDBException;

import api.Message;

import ui.Translator;

@SuppressWarnings("serial")
public class GameScreen extends javax.swing.JFrame implements ComponentListener, WindowListener {
	private static final int FASTUPDATE = 200;
	private static final int MEDIUMUPDATE = 1000;
	private static final int SLOWUPDATE = 2500;
	private GameBoard gameBoard;
	private JButton saveButton;
	private JLabel moneyLabel, timeSkip, timeSlowDown, timeSpeedUp, timeLabel;
	private JPanel toolBarPanel, timeController, contentPanel;
	private JToolBar toolBar;
	private SideBar sidebar;
	private final domain.Game game;
	private TilePanel selectedPanel = null;
	private final Timer timer = new Timer();
	private TimerTask timertask;
	private MarketWindow market;

	private class UpdateTask extends TimerTask{
		public void run() {
			game.update();
			moneyLabel.setText(String.format(
					ui.Translator.getString("moneystring"), game.getCash()));
			timeLabel.setText(Translator.timeFormat(game.getClock().getDate()));
			gameBoard.update();
			sidebar.update();
			while(domain.MsgQue.get().hasNext()){
				Message msg = domain.MsgQue.get().next();
				JOptionPane.showMessageDialog(GameScreen.this, Translator.timeFormat(msg.getTime()).toString()+"\n\n"+Translator.getString(msg.getMessage()), "",JOptionPane.PLAIN_MESSAGE);
			}
		}
	}
	
	public GameScreen(domain.Game thegame) {
		super();
		game = thegame;
		market = new MarketWindow(thegame);

		try {
			getContentPane().setLayout(new BoxLayout(getContentPane(),
					javax.swing.BoxLayout.Y_AXIS));
			{
				toolBar = new JToolBar();
				toolBar.setFloatable(false);
				{
					toolBarPanel = new JPanel();
					GridBagLayout toolBarPanelLayout = new GridBagLayout();
					toolBarPanelLayout.rowWeights = new double[] { 0.1, 0.1 };
					toolBarPanelLayout.rowHeights = new int[] { 7, 7 };
					toolBarPanelLayout.columnWeights = new double[] { 0.1, 0.1,
							0.1 };
					toolBarPanelLayout.columnWidths = new int[] { 7, 7, 7 };
					toolBarPanel.setLayout(toolBarPanelLayout);
					toolBarPanel.setOpaque(false);
					{
						saveButton = new JButton(Translator.getString("SAVE"));
						saveButton.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								try {
									game.save();
								} catch (SQLException e) {
									e.printStackTrace();
								} catch (SystemDBException e) {
									e.printStackTrace();
								}
							}
						});
						timeLabel = new JLabel();
						moneyLabel = new JLabel();
						timeController = new JPanel(new FlowLayout(FlowLayout.CENTER,10,5));
						timeController.setOpaque(false);
						{
							timeSlowDown = new JLabel(" << ");
							timeSkip	 = new JLabel(" >>| ");
							timeSpeedUp  = new JLabel(" >> ");
							timeController.add(timeSlowDown);
							timeController.add(timeSkip);
							timeController.add(timeSpeedUp);
							timeSlowDown.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									game.slowDown();
								}});
							timeSkip.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									game.skipDay();
								}});
							timeSpeedUp.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									game.speedUp();
								}});
						}
						toolBarPanel.add(saveButton, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
						toolBarPanel.add(timeLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
						toolBarPanel.add(moneyLabel, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
						toolBarPanel.add(timeController, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					}
				}
				toolBar.add(toolBarPanel);

				contentPanel = new JPanel();
				contentPanel.setLayout(new BoxLayout(contentPanel,
						javax.swing.BoxLayout.X_AXIS));
				gameBoard = new GameBoard(game, this);
				sidebar = new SideBar(game, this);
				contentPanel.add(gameBoard);
				contentPanel.add(sidebar);
			}
			getContentPane().add(toolBar);
			getContentPane().add(contentPanel);

			addWindowListener(this);
			addComponentListener(this);

			setSize(800, 600);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

	public void select(TilePanel tile) {
		if (this.selectedPanel != null) {
			this.selectedPanel.selected = false;
			this.selectedPanel.repaint();
		}
		this.selectedPanel = tile;
		this.selectedPanel.selected = true;
		this.selectedPanel.repaint();
	}

	TilePanel getSelected() {
		return selectedPanel;
	}
	
	private void setUpdate(int ms){
		if(timertask != null)
			timertask.cancel();
		if(ms == 0){
			timer.cancel();
			return;
		}
		timertask=new UpdateTask();
		try {
			timer.purge();
			timer.scheduleAtFixedRate(timertask, 0, ms);
		} catch ( java.lang.IllegalStateException e) {
			//we already canceled the timer, leave it like that
		}
	}
	
	public void updateMarket(){
		market.doUpdate();
	}
	public void showMarket(){
		market.setVisible(true);
	}

	public void componentResized(ComponentEvent evt) {
		int height = getContentPane().getHeight();
		int width = getContentPane().getWidth();
		toolBar.setSize(width, 40);
		contentPanel.setSize(width, height - 40);
		gameBoard.setSize(width - 300, height - 40);
		sidebar.setSize(300, height - 40);
	}

	public void componentHidden(ComponentEvent evt) {}
	public void componentMoved(ComponentEvent evt) {}
	public void componentShown(ComponentEvent evt) {}

	public void windowActivated(WindowEvent arg0) {
		setUpdate(FASTUPDATE);
	}
	public void windowClosed(WindowEvent arg0) {
		setUpdate(0);
	}
	public void windowClosing(WindowEvent arg0) {
		market.dispose();
		try {
			game.save();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SystemDBException e) {
			e.printStackTrace();
		}
		setUpdate(0);
		dispose();
	}
	public void windowDeactivated(WindowEvent arg0) {
		setUpdate(MEDIUMUPDATE);
	}
	public void windowDeiconified(WindowEvent arg0) {
		setUpdate(FASTUPDATE);
	}
	public void windowIconified(WindowEvent arg0) {
		setUpdate(SLOWUPDATE);
	}
	public void windowOpened(WindowEvent arg0) {
		setUpdate(FASTUPDATE);
	}
}