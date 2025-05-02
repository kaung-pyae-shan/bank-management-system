package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Customer;

public class TransactionLogsPanel extends JPanel {
	
	private DefaultTableModel tableModel;
	private JTable table;

	private static final long serialVersionUID = 1L;

	public TransactionLogsPanel() {
		setLayout(new BorderLayout());
		
		JPanel formFieldPanel = new JPanel(new BorderLayout());
		formFieldPanel.setBorder(new EmptyBorder(20, 10, 0, 10));
		
		JLabel sortLbl = new JLabel("Sort by: ");
		JComboBox<String> sortCb = new JComboBox<String>();
		sortCb.setPreferredSize(new Dimension(100, 25));
		JPanel sortPanel = new JPanel(new FlowLayout());
		sortPanel.add(sortLbl);
		sortPanel.add(sortCb);
		
		JTextField searchField = new JTextField();
		searchField.setPreferredSize(new Dimension(200, 25));
		
		formFieldPanel.add(sortPanel, BorderLayout.WEST);
		formFieldPanel.add(searchField, BorderLayout.EAST);
		
		add(formFieldPanel, BorderLayout.NORTH);
		
		
	}
	
	private void initTable() {
		String[] cols = { "Reference No.", "Account No", "DOB", "Email", "Phone", "Address", "Since" };
		tableModel = new DefaultTableModel(cols, 0) {
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
		add(tableScrollPane, BorderLayout.SOUTH);
	}

}
