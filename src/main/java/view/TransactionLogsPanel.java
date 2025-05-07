package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import controller.TransactionController;
import model.Staff.Role;
import model.dto.TransactionDetail;
import utils.UpdateablePanel;

public class TransactionLogsPanel extends JPanel implements UpdateablePanel {

	private DefaultTableModel tableModel;
	private JTable table;
	private JTextField searchField;
	private TransactionController controller;
	private int loggedInStaffId;
	private Role role;
	private List<TransactionDetail> allTransactions;


	private static final long serialVersionUID = 1L;

	public TransactionLogsPanel(TransactionController controller, int loggedInStaffId, Role role) {
		this.controller = controller;
		this.loggedInStaffId = loggedInStaffId;
		this.role = role;
		setLayout(new BorderLayout(0, 20));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
		    var list = filterTransactions(searchField.getText());
		    refreshTable(sortTable(list, selected));
		});

		searchField = new JTextField("Search by Ref No, Acc No");
		searchField.setPreferredSize(new Dimension(200, 20));
		
		searchField.addFocusListener(new java.awt.event.FocusAdapter() {
		    @Override
		    public void focusGained(java.awt.event.FocusEvent e) {
		        if (searchField.getText().equals("Search by Ref No, Acc No")) {
		            searchField.setText("");
		        }
		    }

		    @Override
		    public void focusLost(java.awt.event.FocusEvent e) {
		        if (searchField.getText().isEmpty()) {
		            searchField.setText("Search by Ref No, Acc No");
		        }
		    }
		});


		formFieldPanel.add(sortPanel, BorderLayout.WEST);
		formFieldPanel.add(searchField, BorderLayout.EAST);
		
		searchField.getDocument().addDocumentListener(new DocumentListener() {
		    public void insertUpdate(DocumentEvent e) {
		        var list = filterTransactions(searchField.getText());
		        refreshTable(sortTable(list, sortCb.getSelectedItem().toString()));
		    }

		    public void removeUpdate(DocumentEvent e) {
		    	var list = filterTransactions(searchField.getText());
		        refreshTable(sortTable(list, sortCb.getSelectedItem().toString()));
		    }

		    public void changedUpdate(DocumentEvent e) {
		    	var list = filterTransactions(searchField.getText());
		        refreshTable(sortTable(list, sortCb.getSelectedItem().toString()));
		    }
		});


		initTable(loggedInStaffId, role);
		allTransactions = controller.fetchAllTransactionDetails(loggedInStaffId, role);
		refreshTable(allTransactions);
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
	
	private List<TransactionDetail> filterTransactions(String query) {
	    if (query == null || query.trim().isEmpty() || query.equals("")) {
	        return controller.fetchAllTransactionDetails(loggedInStaffId, role);
	    }

	    String lowerQuery = query.trim().toLowerCase();

	    List<TransactionDetail> filtered = new ArrayList<>();
	    for (TransactionDetail trx : allTransactions) {
	        String refNo = trx.referenceNo() != null ? trx.referenceNo().toLowerCase() : "";
	        String fromAcc = trx.fromAccount() != null ? trx.fromAccount().toLowerCase() : "";
	        String toAcc = trx.toAccount() != null ? trx.toAccount().toLowerCase() : "";

	        if (refNo.contains(lowerQuery) || fromAcc.contains(lowerQuery) || toAcc.contains(lowerQuery)) {
	            filtered.add(trx);
	        }
	    }

	    return filtered;
	}
	
	private List<TransactionDetail> sortTable(List<TransactionDetail> transactions,  String sortType) {
		// Sort based on selection
	    if ("Oldest first".equals(sortType)) {
	        transactions.sort((t1, t2) -> t1.timestamp().compareTo(t2.timestamp()));
	    } else {
	        transactions.sort((t1, t2) -> t2.timestamp().compareTo(t1.timestamp())); // Newest first
	    }
	    return transactions;
	}

	@Override
	public void updateData() {
		allTransactions = controller.fetchAllTransactionDetails(loggedInStaffId, role);
		refreshTable(allTransactions);
	}

}
