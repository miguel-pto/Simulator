package simulator.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import simulator.model.State;

class SpeciesTableModel extends AbstractTableModel implements EcoSysObserver {

	private List<String> column_names;
	private List<List<Object>> data;
	Set<String> codes; // AUXILIAR PARA LA TABLA

	// TODO PONER TODO BONITO
	SpeciesTableModel(Controller ctrl) {
		column_names = new ArrayList<String>();
		data = new ArrayList<List<Object>>();
		codes = new HashSet<String>();
		init_cols();
		ctrl.addObserver(this);
	}

	private void init_cols() {
		column_names.add("Species");
		for (State s : State.values()) {
			column_names.add(s.toString());
		}
	}

	private void get_data(List<AnimalInfo> animals) {
		for (AnimalInfo a : animals) {
			if (!codes.contains(a.get_genetic_code())) {
				new_row(a.get_genetic_code(), animals);
			}
		}
	}

	private void new_row(String code, List<AnimalInfo> animals) {
		int i = codes.size();
		codes.add(code);
		data.add(new ArrayList<Object>());
		data.get(i).add(code);
		for (State s : State.values()) {
			data.get(i).add(animals.stream().filter((e) -> e.get_state().equals(s) && e.get_genetic_code().equals(code)).count());
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		data = new ArrayList<List<Object>>();
		codes = new HashSet<String>();
		get_data(animals);
		this.fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		// update_data(animals);
		// OTRA MANERA MAS EFICIENTE
		// TODO REVISALO A VER SI TE PARECE BIEN. DE ESTA MANERA NO HAY Q ACTUALIZAR
		// TODOS LOS DATOS PERO NO SE SI ES UN POCO SUCIA
		AnimalInfo a = animals.get(animals.size() - 1);
		add_animal(a, animals);
		//this.fireTableDataChanged(); TODO VER CADA CUANTO ACTUALIZAMOS LA TABLA
	}
	
	private void add_animal(AnimalInfo a, List<AnimalInfo> animals) {
		if (!codes.contains(a.get_genetic_code())) {
			new_row(a.get_genetic_code(), animals);
		} else {
			int i = 0;
			while (data.get(i).get(0) != a.get_genetic_code())
				i++;
			int j = column_names.indexOf(a.get_state().toString());
			data.get(i).set(j, animals.stream().filter(
					(e) -> e.get_state().equals(a.get_state()) && e.get_genetic_code().equals(a.get_genetic_code()))
					.count());
		}
	}

	private void update_data(List<AnimalInfo> animals) {
		for (int i = 0; i < data.size(); i++) {
			int j = 1;
			String code = (String) data.get(i).get(0);
			for (State s : State.values()) {
				data.get(i).set(j,
						animals.stream().filter((e) -> e.get_state().equals(s) && e.get_genetic_code().equals(code)).count());
				j++;
			}
		}
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		update_data(animals);
		this.fireTableDataChanged();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public int getColumnCount() {
		return column_names.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}

	public String getColumnName(int index) {
		return column_names.get(index);
	}
}