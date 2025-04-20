package view.components.dashboard;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

public class TableActionCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	
	private TableActionEvent event;

	public TableActionCellEditor(TableActionEvent event) {
		super(new JCheckBox());
		this.event = event;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		ActionPanel action = new ActionPanel();
		action.initEvent(event, row);
		action.setBackground(table.getSelectionBackground());
		return action;
	}
}
