package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JButton;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.launcher.Main;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class ChangeRegionsDialog extends JDialog implements EcoSysObserver {
	private DefaultComboBoxModel<String> regionsModel;
	private DefaultComboBoxModel<String> fromRowModel;
	private DefaultComboBoxModel<String> toRowModel;
	private DefaultComboBoxModel<String> fromColModel;
	private DefaultComboBoxModel<String> toColModel;
	private DefaultTableModel dataTableModel;
	private Controller ctrl;
	private List<JSONObject> regionsInfo;

	private String[] headers = { "Key", "Value", "Description" };

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this);
	}

	@SuppressWarnings("serial")
	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		// TODO crea varios paneles para organizar los componentes visuales en el
		// dialogo, y añádelos al mainpanel. P.ej., uno para el texto de ayuda, uno para
		// la tabla, uno para los combobox, y uno para los botones.

		JPanel helpPanel = new JPanel(), tablePanel = new JPanel(), comboPanel = new JPanel(),
				buttonPanel = new JPanel();
		mainPanel.add(helpPanel);
		mainPanel.add(tablePanel);
		mainPanel.add(comboPanel);
		mainPanel.add(buttonPanel);

		// TODO crear el texto de ayuda que aparece en la parte superior del diálogo y
		// añádelo al panel correspondiente diálogo (Ver el apartado Figuras)
		JLabel helpText = new JLabel(
				"<html><body>Select a region type, the rows / cols interval, and provide values for the parameters in the Value column<br>(default values are used for parameters with no value).</body></html>");
		helpPanel.add(helpText);

		// RegionsInfo se usará para establecer la información en la tabla
		regionsInfo = Main.region_factory.get_info();

		// dataTableModel es un modelo de tabla que incluye todos los parámetros de la
		// region
		dataTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		dataTableModel.setColumnIdentifiers(headers);

		// TODO crear un JTable que use _dataTableModel, y añadirlo al diálogo
		JTable dataTable = new JTable(dataTableModel);
		tablePanel.add(new JScrollPane(dataTable));

		// regionsModel es un modelo de combobox que incluye los tipos de regiones
		regionsModel = new DefaultComboBoxModel<>();

		// TODO añadir la descripcionde todas las regiones a regionsModel, para eso usa
		// la clave “desc” o “type” de los JSONObject en _regionsInfo, ya que estos nos
		// dan información sobre lo que puede crear la factoría.
		for (JSONObject o : regionsInfo) {
			regionsModel.addElement(o.getString("type"));
		}

		// TODO crear un combobox que use _regionsModel y añadirlo al diálogo.
		JComboBox<String> regionsComboBox = new JComboBox<>(regionsModel);
		regionsComboBox.addActionListener((e) -> {
			for (int i = dataTableModel.getRowCount() - 1; i >= 0; i--) {
				dataTableModel.removeRow(i);
			}
			JSONObject info = regionsInfo.get(regionsComboBox.getSelectedIndex());
			JSONObject data = info.getJSONObject("data");
			int i = 0;
			for (String s : data.keySet()) {
				dataTableModel.addRow(new Vector<String>());
				dataTableModel.setValueAt(s, i, 0);
				dataTableModel.setValueAt(data.getString(s), i, 2);
				i++;
			}
		});
		comboPanel.add(new JLabel("Region type: "));
		comboPanel.add(regionsComboBox);

		// TODO crear 4 modelos de combobox para _fromRowModel, _toRowModel,
		// _fromColModel y _toColModel.
		fromRowModel = new DefaultComboBoxModel<>();

		toRowModel = new DefaultComboBoxModel<>();
		fromColModel = new DefaultComboBoxModel<>();
		toColModel = new DefaultComboBoxModel<>();

		// TODO crear 4 combobox que usen estos modelos y añadirlos al diálogo.
		JComboBox<String> fromRowComboBox = new JComboBox<>(fromRowModel);
		JComboBox<String> toRowComboBox = new JComboBox<>(toRowModel);
		JComboBox<String> fromColComboBox = new JComboBox<>(fromColModel);
		JComboBox<String> toColComboBox = new JComboBox<>(toColModel);
		comboPanel.add(new JLabel("Row from / to: "));
		comboPanel.add(fromRowComboBox);
		comboPanel.add(toRowComboBox);
		comboPanel.add(new JLabel("Column from / to: "));
		comboPanel.add(fromColComboBox);
		comboPanel.add(toColComboBox);

		// crear los botones OK y Cancel y añadirlos al diálogo.
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> setVisible(false));
		buttonPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener((e) -> {
			
		});
		buttonPanel.add(okButton);

		setPreferredSize(new Dimension(700, 400)); // puedes usar otro tamaño
		pack();
		setResizable(false);
		setVisible(false);

	}

	public void open(Frame parent) {
		setLocation(parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
				parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2);
		pack();
		setVisible(true);
	}

	private void updateModels(MapInfo map) {
		for (int i = 0; i < map.get_rows(); i++) {
			fromRowModel.addElement(String.valueOf(i));
			toRowModel.addElement(String.valueOf(i));
		}
		for (int i = 0; i < map.get_cols(); i++) {
			fromColModel.addElement(String.valueOf(i));
			toColModel.addElement(String.valueOf(i));
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		updateModels(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		updateModels(map);
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
