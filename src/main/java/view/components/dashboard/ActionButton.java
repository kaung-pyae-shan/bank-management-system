package view.components.dashboard;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ActionButton extends JButton {
	
	private static final long serialVersionUID = 1L;

	public ActionButton(String filename) {
		setOpaque(true);
		setContentAreaFilled(true);
		setBackground(Color.WHITE);
//		setBorder(null);
		setIcon(new ImageIcon(getClass().getResource(filename)));
	}
}
