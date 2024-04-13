package simulator.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Animal;
import simulator.model.AnimalInfo;
import simulator.model.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {
	
	List<String> column_names;
	List<Info> data;
	
	
	RegionsTableModel(Controller ctrl) {
		column_names = new ArrayList<String>();
		data = new ArrayList<Info>();
		init_cols();
		ctrl.addObserver(this);
	}
	
	class Info {

		String row;
		String col;
		String desc;
		String diet_info[];

		Info(RegionData r) {
			row = String.valueOf(r.row());
			col = String.valueOf(r.col());
			desc = r.r().toString();		

			Diet[] diets = Diet.values();
			diet_info = new String[diets.length];
			List<AnimalInfo> animals = r.r().getAnimalsInfo();
			for (int i = 0; i < diets.length; i++) {
				Diet d = diets[i];
				diet_info[i] = String.valueOf(animals.stream().filter((a) -> a.get_diet() == d).count());
			}
		}
	}
	
	private void init_cols() {
		column_names.add("rows");
		column_names.add("cols");
		column_names.add("desc");
		for (Diet d : Diet.values()) {
			column_names.add(d.toString());
		}
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
		switch(columnIndex) {
		case 0:
			return data.get(rowIndex).row;
		case 1:
			return data.get(rowIndex).col;
		case 2:
			return data.get(rowIndex).desc;
		default:
			return data.get(rowIndex).diet_info[columnIndex - 3];
		}
	}
	
	private void get_data(MapInfo map) {
		for (RegionData r : map) {
			data.add(new Info(r));
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(map);
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(map);
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(map);
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		get_data(map);
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		get_data(map);
	}
	
	public String getColumnName(int index) {
		return column_names.get(index);
	}
}