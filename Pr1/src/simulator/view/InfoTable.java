package simulator.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import simulator.model.State;

public class InfoTable extends JPanel {
	
	private String title;
	private TableModel tableModel;
	private JTable table;
	private JScrollPane scroller;

	InfoTable(String title, TableModel tableModel) {
		this.title = title;
		this.tableModel = tableModel;
		initGUI();
	}

	private void initGUI() {
		// TODO cambiar el layout del panel a BorderLayout()
		this.setLayout(new BorderLayout());
		// TODO añadir un borde con título al JPanel, con el texto title
		this.setBorder(BorderFactory.createTitledBorder(getBorder(), title));
		// TODO añadir un JTable (con barra de desplazamiento vertical) que use tableModel
		table = new JTable(tableModel);
		scroller = new JScrollPane(table);
		table.getTableHeader().setReorderingAllowed(false);
		this.add(scroller);
	}
}