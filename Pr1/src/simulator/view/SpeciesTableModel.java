package simulator.view;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
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

	private List<String> column_names, codes;
	private Map<String, List<String>> data;

	// TODO PONER TODO BONITO
	SpeciesTableModel(Controller ctrl) {
		column_names = new ArrayList<String>();
		data = new HashMap<String, List<String>>();
		codes = new ArrayList<String>();
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
			if (!data.containsKey(a.get_genetic_code())) {
				String code = a.get_genetic_code();
				data.put(code, new ArrayList<String>());
				codes.add(code);
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
		get_data(animals);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		data = new HashMap<String, List<String>>();
		codes = new ArrayList<String>();
		get_data(animals);
		this.fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(animals);
		this.fireTableDataChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		data = new HashMap<String, List<String>>();
		get_data(animals);
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
		return data.get(codes.get(rowIndex)).get(columnIndex);
	}

	public String getColumnName(int index) {
		return column_names.get(index);
	}
}