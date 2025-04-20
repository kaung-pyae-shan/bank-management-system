package view.components.dashboard;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class ActionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ActionButton btnApprove;
	private ActionButton btnReject;

	/**
	 * Create the panel.
	 */
	public ActionPanel() {
		setLayout(new GridLayout(1, 2, 0, 0));
		
		btnApprove = new ActionButton("/approve.png");
		add(btnApprove);
		
		btnReject = new ActionButton("/reject.png");
		add(btnReject);
	}
	
	public void initEvent(TableActionEvent event, int row) {
		btnApprove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event.onApprove(row);
			}
		});
		btnReject.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				event.onReject(row);
			}
		});
	}
}
