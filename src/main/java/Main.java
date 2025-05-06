import javax.swing.SwingUtilities;

import config.DependenciesConfig;
import view.Login;
import view.common.MainView;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
//			MainView mainView = new MainView(new DependenciesConfig(), 1);
//			mainView.setVisible(true);
			Login view = new Login(new DependenciesConfig().getLoginController());
			view.setVisible(true);
		});
	}
}
