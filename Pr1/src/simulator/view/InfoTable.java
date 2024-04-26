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
	
	//DEFINIMOS UNA CLASE QUE REPRESENTE UNA TABLA QUE RECIBE EL MODELO DE TABLA COMO PARAMETROS 
	//	Y LA USAMOS PARA LAS TABLAS RESPONSABLES DE MOSTRAR LA INFORMACIÓN DE LOS ANIMALES Y DE 
	//	LAS REGIONES
	
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
		// SE CAMBIA EL LAYOUT DEL PANEL A BorderLayout
		this.setLayout(new BorderLayout());
		// SE AÑADE UN BORDE CON TÍTULO MEDIANTE EL TEXTO title
		this.setBorder(BorderFactory.createTitledBorder(getBorder(), title));
		// SE AÑADE UNA BARRA DE DESPLAZAMIENTO VERTICAL QUE USE tableModel (MEDIANTE UN JTable)
		table = new JTable(tableModel);
		scroller = new JScrollPane(table);
		table.getTableHeader().setReorderingAllowed(false);
		this.add(scroller);
	}
}