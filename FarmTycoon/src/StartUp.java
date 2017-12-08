/**
 * Startup Class which launches the UI
 * 
 * @author Rigès De Witte
 * @author Simon Peeters
 * @author Barny Pieters
 * @author Laurens Van Damme
 * 
 */
public class StartUp {

	public static void main(String[] args) {
		new ui.swing.StartUp();
	}

	public StartUp() {
		new ui.cli.ConsoleApp();
	}

}