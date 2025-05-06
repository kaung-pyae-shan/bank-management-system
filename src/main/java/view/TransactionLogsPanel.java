package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.TransactionController;
import model.Staff.Role;
import model.dto.TransactionDetail;

public class TransactionLogsPanel extends JPanel {

	private TransactionController controller;
	private DefaultTableModel tableModel;
	private JTable table;

	private static final long serialVersionUID = 1L;

	public TransactionLogsPanel(TransactionController controller, int loggedInStaffId, Role role) {
		this.controller = controller;
		setLayout(new BorderLayout(0, 20));

		JPanel formFieldPanel = new JPanel(new BorderLayout());
		formFieldPanel.setBorder(new EmptyBorder(20, 10, 0, 10));

		JLabel sortLbl = new JLabel("Sort by: ");
		JComboBox<String> sortCb = new JComboBox<>(new String[] { "Newest first", "Oldest first" });
		JPanel sortPanel = new JPanel(new FlowLayout());
		sortPanel.add(sortLbl);
		sortPanel.add(sortCb);

		JTextField searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(200, 20));

		formFieldPanel.add(sortPanel, BorderLayout.WEST);
		formFieldPanel.add(searchField, BorderLayout.EAST);

		initTable(loggedInStaffId, role);
		refreshTable(controller.fetchAllTransactionDetails(loggedInStaffId, role));
		add(formFieldPanel, BorderLayout.NORTH);

	}

	private void initTable(int staffId, Role role) {
		String[] adminCols = { "Reference No.", "Timestamp", "Type", "Amount", "FromAccount", "ToAccount",
				"PerformedBy"};
		String[] tellerCols = { "Reference No.", "Timestamp", "Type", "Amount", "FromAccount", "ToAccount" };

		String[] columns = role == Role.ADMIN ? adminCols : tellerCols;
		tableModel = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		table = new JTable(tableModel);
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowHeight(40);

		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.setPreferredSize(new Dimension(700, 280)); // width, height
		add(tableScrollPane, BorderLayout.CENTER);
	}

	private void refreshTable(List<TransactionDetail> trxDetails) {
		tableModel.setRowCount(0);
		
		for (TransactionDetail trx : trxDetails) {
			Object[] rowData = {
					trx.referenceNo(),
					trx.timestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
					trx.type(),
					trx.amount(),
					trx.fromAccount() == null? "N/A" : trx.fromAccount(),
					trx.toAccount() == null? "N/A" : trx.toAccount(),
					trx.performedBy()
			};
			tableModel.addRow(rowData);
		}
		tableModel.fireTableDataChanged();
	}
}
