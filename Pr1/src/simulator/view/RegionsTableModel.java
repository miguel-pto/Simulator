package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.Diet;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.MapInfo.RegionData;
import simulator.model.RegionInfo;

@SuppressWarnings("serial")
class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {

	// RegionsTableModel ES LA RESPONSABLE DE MOSTRAR LA INFORMACION DE LOS ANIMALES
	// Y SUS DIETAS
	// LA TABLA INCLUYE UNA FILA PARA CADA REGIÓN CON INFORMACIÓN SOBRE SU FILA Y SU
	// COLUMNA
	// EN LA MATRIZ DE REGIONES, SU DESCRIPCIÓN (QUE ES LO DEVUELTO EN EL toString
	// DE LA REGIÓN
	// Y EL NÚMERO DE ANIMALES EN LA REGIÓN PARA CADA TIPO DE DIETA

	// PARTE DE LA INTENCIÓN ES QUE SE PUEDA USAR SIN HACER MENCIÓN EXPLÍCITA DE LOS
	// TIPOS DE DIETAS
	// DE FORMA QUE SI SE AÑADEN NUEVOS O DIFERENTES NO HAGA FALTA CAMBIAR NADA

	List<String> column_names;
	List<Info> data;

	RegionsTableModel(Controller ctrl) {
		// INICIALIZA LAS ESTRUCTURAS DE DATOS CORRESPONDIENTES
		column_names = new ArrayList<String>();
		data = new ArrayList<Info>();
		init_cols();
		ctrl.addObserver(this); // LO REGISTRA COMO OBSERVADOR
	}

	class Info {

		String row;
		String col;
		String desc;
		String diet_info[];

		Info(RegionData r) {
			// ASIGNA LOS ATRIBUTOS CON LOS VALORES DE LA FILA Y LA COLUMNA
			row = String.valueOf(r.row());
			col = String.valueOf(r.col());
			// ASIGNA LA DESCRIPCIÓN DE LOS DATOS DE LA REGION
			desc = r.r().toString();

			// DECLARA LA LISTA DE LAS DIFERENTES DIETAS
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
		// INICIALIZA LAS COLUMAS, AÑADIENDO LAS FILAS, COLUMNAS Y DESCRIPCIONES, ASÍ
		// COMO LOS DIFERENTES
		// VALORES DE DIETAS POSIBLES ENTRE LOS ANIMALES
		column_names.add("rows");
		column_names.add("cols");
		column_names.add("desc");
		for (Diet d : Diet.values()) {
			column_names.add(d.toString());
		}
	}

	@Override
	public int getRowCount() {
		// DEVUELVE EL TAMAÑO DEL ATRIBUTO data
		return data.size();
	}

	@Override
	public int getColumnCount() {
		// DEVUELVE EL TAMAÑO DE LOS NOMBRES DE LAS COLUMNAS
		return column_names.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// SEGUN EL INDICE DE LA COLUMNA, DEVUELVE EL DATO DEL INDICE DE LA FILA
		switch (columnIndex) {
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
		// REGISTRA EN EL ATRIBUTO data LA INFORMACION DE LAS REGIONES DEL MapInfo
		data = new ArrayList<Info>();
		for (RegionData r : map) {
			data.add(new Info(r));
		}
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(map);
		this.fireTableDataChanged();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(map);
		this.fireTableDataChanged();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		get_data(map);
		this.fireTableDataChanged();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		get_data(map);
		this.fireTableDataChanged();
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		get_data(map);
		this.fireTableDataChanged();
	}

	public String getColumnName(int index) {
		// DEVUELVE EL NOMBRE DE LA COLUMNA APUNTADA POR EL INDICE
		return column_names.get(index);
	}
}