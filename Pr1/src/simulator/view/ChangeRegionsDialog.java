package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;

	
import javax.swing.JFrame;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;	

public class ChangeRegionsDialog extends JDialog implements EcoSysObserver{
	private JComboBox<String> regionsComboBox;
	private JComboBox<Integer> fromRow;
	private DefaultComboBoxModel<String> regionsModel;
	private DefaultComboBoxModel<String> fromRowModel;
	private DefaultComboBoxModel<String> toRowModel;
	private JComboBox<Integer> fromCol;
	private DefaultComboBoxModel<String> fromColModel;
	private DefaultComboBoxModel<String> toColModel;
	private JComboBox<Integer> toRow;
	private JComboBox<Integer> toCol;
	private JTable dataTable;
	private DefaultTableModel dataTableModel;
	private Controller ctrl;
	private List<JSONObject> regionsInfo;
	
	private String[] headers = {"Key", "Value", "Description"};
	
	
	
	
	ChangeRegionsDialog(Controller ctrl){
		super((Frame)null, true);
		this.ctrl = ctrl;
		initGUI();
		this.ctrl.addObserver(this);
	}
	
	private void initGUI() {
		setTitle("Change Regions"); 
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		//TODO crea varios paneles para organizar los componentes visuales en el dialogo, y añádelos al mainpanel. P.ej., uno para
		 //el texto de ayuda, uno para la tabla, uno para los combobox, y uno para los botones.
		
		JPanel helpPanel = new JPanel();
		
		
		// TODO crear el texto de ayuda que aparece en la parte superior del diálogo y añádelo al panel correspondiente diálogo (Ver el
		 //apartado Figuras)
		
		//RegionsInfo se usará para establecer la información en la tabla
		regionsInfo = Main.region_factory.get_info();
		
		//dataTableModel es un modelo de tabla que incluye todos los parámetros de la region
		dataTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				//TODO hacer editable solo la columna 1
			}
		};
		dataTableModel.setColumnIdentifiers(headers);
		
		//TODO crear un JTable que use _dataTableModel, y añadirlo al diálogo
		
		//regionsModel es un modelo de combobox que incluye los tipos de regiones
		regionsModel = new DefaultComboBoxModel();
		
		//TODO añadir la descripcionde todas las regiones a regionsModel, para eso usa la clave “desc” o “type” de los
		 //JSONObject en _regionsInfo, ya que estos nos dan información sobre lo que puede crear la factoría.
		
		// TODO crear un combobox que use _regionsModel y añadirlo al diálogo.
		
		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel, _fromColModel y _toColModel.
		
		// TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.
		
		// TODO crear los botones OK y Cancel y añadirlos al diálogo.
		
		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);

	}
	
	

	public void open(Frame parent) {
		setLocation(parent.getLocation().x + parent.getWidth()/2 - getWidth()/2,
						parent.getLocation().y + parent.getHeight()/2 - getHeight()/2);
		pack();
		setVisible(true);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// TODO Auto-generated method stub
		
	}
	
}
