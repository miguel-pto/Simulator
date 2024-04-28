package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.BorderFactory;
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

	// ESTA CLASE ES LA RESPONSABLE DE IMPLEMENTAR LA VENTANA DE DIALOGO QUE PERMITE
	// MODIFICAR LAS REGIONES

	// SI MÁS TIPOS DE REGIONES SON AÑADIDOS A LA FACTORIA DE REGIONES, ES
	// IMPORTANTE QUE EL DIÁLOGO SIGA
	// FUNCIONANDO IGUAL SIN NECESIDAD DE MODIFICAR CODIGO, POR LO QUE EVITAMOS
	// HACER REFERENCIA DIRECTA
	// A LOS TIPOS DE REGIONES, SACANDO SIEMPRE LA INFORMACIÓN MEDIANTE EL MÉTODO
	// get_info() DE SU FACTORIA

	private DefaultComboBoxModel<String> regionsModel;
	private DefaultComboBoxModel<String> fromRowModel;
	private DefaultComboBoxModel<String> toRowModel;
	private DefaultComboBoxModel<String> fromColModel;
	private DefaultComboBoxModel<String> toColModel;
	private DefaultTableModel dataTableModel;
	private Controller ctrl;
	private List<JSONObject> regionsInfo;

	private String[] headers = { "Key", "Value", "Description" };

	// EL DIALOGO SE CREA/ABRE AL PULSAR SOBRE SU BOTÓN. SE CREA UNA ÚNICA INSTANCIA
	// DE LA VENTADA DE DIÁLOGO EN LA CONSTRUCTORA Y SE LLAMA AL METODO open

	ChangeRegionsDialog(Controller ctrl) {
		super((Frame) null, true);
		this.ctrl = ctrl;
		initGUI();
		ctrl.addObserver(this); // SE REGISTRA COMO OBSERVADOR
	}

	@SuppressWarnings("serial")
	private void initGUI() {
		setTitle("Change Regions");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		// DIFERENTES PANELES SON CREADOS PARA ORGANIZAR LOS COMPONENTES VISUALES
		// EN EL DIALOGO Y SON AÑADIDOS AL mainPanel

		// SE CREA UN PANEL PARA EL TEXTO DE AYUDA, PARA LA TABLA, PARA LOS COMBOBOX Y
		// PARA LOS BOTONES
		JPanel helpPanel = new JPanel(), tablePanel = new JPanel(), comboPanel = new JPanel(),
				buttonPanel = new JPanel();
		mainPanel.add(helpPanel);
		mainPanel.add(tablePanel);
		mainPanel.add(comboPanel);
		mainPanel.add(buttonPanel);

		// SE CREA EL TEXTO DE AYUDA QUE APARECERA EN LA PARTE SUPERIOR DEL DIALOGO,
		// SIENDO AÑADIDO AL PANEL CORRESPONDIENTE DIALOGO
		JLabel helpText = new JLabel(
				"<html><body>Select a region type, the rows / cols interval, and provide values for the parameters in the Value column<br>(default values are used for parameters with no value).</body></html>");
		helpPanel.add(helpText);

		// SE USARARÁ RegionsInfo PARA QUE LA INFORMACIÓN EN LA TABLA SEA ESTABLECIDA
		regionsInfo = Main.region_factory.get_info();

		// dataTableModel ES UN MODELO DE TABLA EL CUAL INCLUYE TODOS LOS PARÁMETROS DE
		// LA REGION
		dataTableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int column) {
				return column == 1;
			}
		};
		dataTableModel.setColumnIdentifiers(headers);

		// UN JTable QUE HAGA USO DE dataTableModel ES CREADO Y AÑADIDO AL DIALOGO
		JTable dataTable = new JTable(dataTableModel);
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
		tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		tablePanel.add(new JScrollPane(dataTable));

		// regionsModel ES UN MODELO DE COMBOBOX EL CUAL INCLUYE LOS DIFERENTES TIPOS DE
		// REGIONES
		regionsModel = new DefaultComboBoxModel<>();

		// TODAS LAS REGIONES TIENEN SU DESCRIPCIÓN AÑADIDA AL regionsModel
		// PARA ELLO SE USAN LA CLAVE desc O type DE LOS JSONObject EN regionsInfo
		// PUESTO
		// QUE OTORGAN INFORMACIÓN SOBRE LO QUE LA FACTORIA PUEDE CREAR

		for (JSONObject o : regionsInfo) {
			regionsModel.addElement(o.getString("type"));
		}

		// EL COMBOBOX QUE USE regionsModel ES CREADO Y AÑADIDO AL DIÁLOGO
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

		// 4 MODELOS DE COMBOBOX SON CREADOS PARA
		// fromRowModel, toRowModel, fromColModel y toColModel
		fromRowModel = new DefaultComboBoxModel<>();
		toRowModel = new DefaultComboBoxModel<>();
		fromColModel = new DefaultComboBoxModel<>();
		toColModel = new DefaultComboBoxModel<>();

		// 4 COMBOBOX QUE USEN ESTOS MODELOS SON CREADOS Y AÑADIDOS AL DIALOGO
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

		// LOS BOTONES OK Y Cancel SON CREADOS Y AÑADIDOS AL DIÁLOGO
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener((e) -> setVisible(false));
		buttonPanel.add(cancelButton);

		JButton okButton = new JButton("OK");
		okButton.addActionListener((e) -> {
			try {
				int fromRow = fromRowComboBox.getSelectedIndex();
				int toRow = toRowComboBox.getSelectedIndex();
				int fromCol = fromColComboBox.getSelectedIndex();
				int toCol = toColComboBox.getSelectedIndex();
				int regionIndex = regionsComboBox.getSelectedIndex();
				String type = regionsInfo.get(regionIndex).getString("type");
				JSONObject data = new JSONObject();
				if (dataTable.getValueAt(0, 1) != null)
					data.put("factor", dataTable.getValueAt(0, 1));
				if (dataTable.getValueAt(1, 1) != null)
					data.put("food", dataTable.getValueAt(1, 1));
				String json = "{ \"regions\": [ {" + "\"row\": [" + fromRow + "," + toRow + "], " + "\"col\": ["
						+ fromCol + "," + toCol + "], " + "\"spec\": {" + "\"type\": \"" + type + "\", " + "\"data\": "
						+ data + "}" + "} ] }";
				// CONVIERTE LA INFORMACION EN UN JSON QUE INCLUYE UNA CLAVE Y EL VALOR PARA
				// CADA FILA EN LA TABLA,
				// PARA LA FILA QUE INCLUYEN VALOR NO VACIO, REFIRIENDONOS AL JSON COMO
				// region_data
				JSONObject region_data = new JSONObject(json);
				ctrl.set_regions(region_data);
				// EL JSON ES PASADO A crtl PARA CAMBIAR LAS REGIONES
			} catch (Exception x) {
				ViewUtils.showErrorMsg(x.getMessage());
			} finally {
				setVisible(false);
			}
		});
		buttonPanel.add(okButton);

		setPreferredSize(new Dimension(700, 400)); // SE TOMA ESTE TAMAÑO, PERO OTRO PUEDE SER UTILIZADO
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
		// POR CADA FILA Y COLUMNA DE map SE ACTUALIZAN LOS MODELOS
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
