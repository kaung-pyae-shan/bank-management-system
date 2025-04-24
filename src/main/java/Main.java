import javax.swing.SwingUtilities;

import config.DependenciesConfig;
import view.common.MainView;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MainView view = new MainView(new DependenciesConfig());
			view.setVisible(true);
		});
	}
}
