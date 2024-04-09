package simulator.view;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.table.TableModel;

public class InfoTable extends JPanel {
	
	String title;
	TableModel tableModel;

	InfoTable(String title, TableModel tableModel) {
		this.title = title;
		this.tableModel = tableModel;
		initGUI();
	}

	private void initGUI() {
		// TODO cambiar el layout del panel a BorderLayout()
		this.setLayout(new BorderLayout());
		// TODO añadir un borde con título al JPanel, con el texto _title
		// TODO añadir un JTable (con barra de desplazamiento vertical) que use_tableModel
	}
}