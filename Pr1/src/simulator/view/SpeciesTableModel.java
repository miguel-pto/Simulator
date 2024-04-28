package simulator.view;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.State;

@SuppressWarnings("serial")
class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

	// SpeciesTableModel ES LA RESPONSABLE DE MOSTRAR LA INFORMACION DE LOS ANIMALES
	// Y SUS ESPECIES
	// LA TABLA INCLUYE UNA FILA PARA CADA CÓDIGO GENÉTICO CON INFORMACIÓN SOBRE EL
	// NUMERO DE
	// ANIMALES EN CADA POSIBLE ESTADO

	// PARTE DE LA INTENCIÓN ES QUE SE PUEDA USAR SIN HACER MENCIÓN EXPLÍCITA DE LOS
	// ESTADOS
	// DE FORMA QUE SI SE AÑADEN NUEVOS O DIFERENTES NO HAGA FALTA CAMBIAR NADA

	private List<String> column_names;
	private Map<String, List<String>> data;

	// TODO PONER TODO BONITO
	SpeciesTableModel(Controller ctrl) {
		// SE INICIALIZAN LAS ESTRUCTURAS DE DATOS CORRESPONDIENTES
		column_names = new ArrayList<String>();
		data = new HashMap<String, List<String>>();
		init_cols();
		ctrl.addObserver(this); // ES REGISTRADO COMO OBSERVADOR
	}

	private void init_cols() {
		column_names.add("Species");
		for (State s : State.values()) {
			column_names.add(s.toString());
		} // POR CADA ESTADO LO AÑADE A LOS NOMBRES DE LAS COLUMNAS
	}

	private void get_data(List<AnimalInfo> animals) {
		// INICIALIZA EL ATRIBUTO data CON LA INFORMACION DE LOS ANIMALES
		data = new HashMap<String, List<String>>();
		for (AnimalInfo a : animals) {
			if (!data.containsKey(a.get_genetic_code())) {
				String code = a.get_genetic_code();
				data.put(code, new ArrayList<String>());
				data.get(code).add(code);
				for (State s : State.values()) {
					data.get(code).add(String.valueOf(animals.stream()
							.filter((e) -> code.equals(e.get_genetic_code()) && s.equals(e.get_state())).count()));
				}
			}
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// OBTIENE LOS DATOS DE LOS ANIMALES
		get_data(animals);
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// RESETEA LOS DATOS AL RECUPERARLOS DE LOS ANIMALES
		get_data(animals);
		this.fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		// AÑADE LOS DATOS DE LOS ANIMALES
		get_data(animals);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		get_data(animals);
		this.fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		// DEVUELVE EL NUMERO DE rows DEL MODELO
		return data.size();
	}

	@Override
	public int getColumnCount() {
		// DEVUELVE EL NUMERO DE column DEL MODELO
		return column_names.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// DEVUELVE EL DATO EN EL PUNTO rowIndex X columnIndex
		Object[] rows = data.keySet().toArray();
		return data.get(rows[rowIndex]).get(columnIndex);
	}

	public String getColumnName(int index) {
		// DEVUELVE EL NOMBRE DE LA COLUMNA INDICADA POR EL INDICE
		return column_names.get(index);
	}
}