package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
	private List<TransactionDetail> allTransactions;


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
		sortCb.addActionListener(e -> {
		    String selected = (String) sortCb.getSelectedItem();
		    List<TransactionDetail> transactions = controller.fetchAllTransactionDetails(loggedInStaffId, role);

		    // Sort based on selection
		    if ("Oldest first".equals(selected)) {
		        transactions.sort((t1, t2) -> t1.timestamp().compareTo(t2.timestamp()));
		    } else {
		        transactions.sort((t1, t2) -> t2.timestamp().compareTo(t1.timestamp())); // Newest first
		    }

		    refreshTable(transactions);
		});

		JTextField searchField = new JTextField("Search by Ref: No and Acc: No");
		searchField.setPreferredSize(new Dimension(200, 20));
		
		searchField.addFocusListener(new java.awt.event.FocusAdapter() {
		    @Override
		    public void focusGained(java.awt.event.FocusEvent e) {
		        if (searchField.getText().equals("Search by Ref: No and Acc: No")) {
		            searchField.setText("");
		        }
		    }

		    @Override
		    public void focusLost(java.awt.event.FocusEvent e) {
		        if (searchField.getText().isEmpty()) {
		            searchField.setText("Search by Ref: No and Acc: No");
		        }
		    }
		});


		formFieldPanel.add(sortPanel, BorderLayout.WEST);
		formFieldPanel.add(searchField, BorderLayout.EAST);
		
		searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
		    public void insertUpdate(javax.swing.event.DocumentEvent e) {
		        filterTransactions(searchField.getText());
		    }

		    public void removeUpdate(javax.swing.event.DocumentEvent e) {
		        filterTransactions(searchField.getText());
		    }

		    public void changedUpdate(javax.swing.event.DocumentEvent e) {
		        filterTransactions(searchField.getText());
		    }
		});


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
	
	private void filterTransactions(String query) {
	    if (query == null || query.trim().isEmpty()) {
	        refreshTable(allTransactions);
	        return;
	    }

	    String lowerQuery = query.trim().toLowerCase();

	    List<TransactionDetail> filtered = new java.util.ArrayList<>();
	    for (TransactionDetail trx : allTransactions) {
	        String refNo = trx.referenceNo() != null ? trx.referenceNo().toLowerCase() : "";
	        String fromAcc = trx.fromAccount() != null ? trx.fromAccount().toLowerCase() : "";
	        String toAcc = trx.toAccount() != null ? trx.toAccount().toLowerCase() : "";

	        if (refNo.contains(lowerQuery) || fromAcc.contains(lowerQuery) || toAcc.contains(lowerQuery)) {
	            filtered.add(trx);
	        }
	    }

	    refreshTable(filtered);
	}

}
