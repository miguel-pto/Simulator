package simulator.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegionManager implements AnimalMapView {

	public static final int MAX = 100;

	private int cols, rows, width, height, region_height, region_width;
	private Map<Animal, Region> animal_region;
	private Region[][] regions;

	// INICIALIZA EL MANAGER CON LA CANTIDAD DE FILAS Y COLUMNAS, CON LO QUE
	// CONSTRUYE LA ANCHURA Y ALTURA,
	// INICIALIZANDO CADA UNA DE LAS REGIONES INTERMEDIAS.
	public RegionManager(int cols, int rows, int width, int height) {
		this.cols = cols;
		this.rows = rows;
		this.width = width;
		this.height = height;
		region_width = width / cols + (width % cols != 0 ? 1 : 0);
		region_height = height / rows + (height % rows != 0 ? 1 : 0);
		regions = new Region[cols][rows];
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++)
				regions[i][j] = new DefaultRegion();
		}
		animal_region = new HashMap<Animal, Region>();
	}

	// ASIGNA UNA REGIÓN A UNA POSICIÓN ASIGNADA.
	public void set_region(int col, int row, Region r) {
		for (Animal a : regions[col][row].getAnimals()) {
			r.add_animal(a);
		}
		regions[col][row] = r;
	}

	// BUSCA LA REGIÓN QUE POSEE A UN ANIMAL PARTICULAR
	Region find_region(Animal a) {
		int col = (int) a.get_position().getX() / region_width;
		int row = (int) a.get_position().getY() / region_height;
		return regions[col][row];
	}

	// REGISTRA UN ANIMAL EN SU REGIÓN Y PROPORCIONA LA INFORMACIÓN AL MANAGER.
	void register_animal(Animal a) {
		a.init(this);
		Region r = find_region(a);
		r.add_animal(a);
		animal_region.put(a, r);
	}

	// ELIMINA EL ANIMAL DEL REGISTRO.
	void unregister_animal(Animal a) {
		animal_region.get(a).remove_animal(a);
		animal_region.remove(a);
	}

	// ACTUALIZA LA REGIÓN DE UN ANIMAL DADO.
	void update_animal_region(Animal a) {
		if (animal_region.get(a) != find_region(a)) {
			unregister_animal(a);
			register_animal(a);
			animal_region.put(a, find_region(a));
		}
	}

	@Override
	// DEVUELVE LA COMIDA QUE RECIBE UN ANIMAL
	public double get_food(Animal a, double dt) {
		return animal_region.get(a).get_food(a, dt);
	}

	// RECORRE TODAS LAS REGIONES Y LAS ACTUALIZA
	void update_all_regions(double dt) {
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++)
				regions[i][j].update(dt);
		}
	}

	@Override
	public int get_cols() {
		return cols;
	}

	@Override
	public int get_rows() {
		return rows;
	}

	@Override
	public int get_width() {
		return width;
	}

	@Override
	public int get_height() {
		return height;
	}

	@Override
	public int get_region_width() {
		return region_width;
	}

	@Override
	public int get_region_height() {
		return region_height;
	}

	@Override
	// CREA UN JSON Y LO DEVUELVE TRAS INTRODUCIRLE TODA LA INFORMACIÓN DE LAS
	// REGIONES
	public JSONObject as_JSON() {
		JSONObject o = new JSONObject();
		JSONArray regions = new JSONArray();

		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++) {
				JSONObject aux = new JSONObject();
				aux.put("row", j);
				aux.put("col", i);
				aux.put("data", this.regions[i][j].as_JSON());
				regions.put(aux);
			}

		o.put("regiones", regions);
		return o;
	}

	@Override
	// DEVUELVE UNA LISTA DE ANIMALES QUE HAN SIDO ENCONTRADOS DENTRO DEL RANGO DE
	// VISION DE UN ANIMAL
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		double r = e.get_sight_range();

		double rightBound = e.get_position().getX() + r;
		rightBound = rightBound >= width ? width - 1 : rightBound;

		double leftBound = e.get_position().getX() - r;
		leftBound = leftBound < 0 ? 0 : leftBound;

		double upperBound = e.get_position().getY() - r;
		upperBound = upperBound < 0 ? 0 : upperBound;

		double lowerBound = e.get_position().getY() + r;
		lowerBound = lowerBound >= height ? height - 1 : lowerBound;

		int x0 = (int) (leftBound / region_width);
		int x1 = (int) (rightBound / region_width);
		int y0 = (int) (upperBound / region_height);
		int y1 = (int) (lowerBound / region_height);

		List<Animal> selection = new ArrayList<Animal>();

		for (int i = x0; i <= x1; i++) {
			for (int j = y0; j <= y1; j++) {
				Region region = regions[i][j];
				for (Animal animal : region.getAnimals()) {
					if (animal != e && e.is_in_sight_range(animal) && filter.test(animal))
						selection.add(animal);
				}
			}
		}

		return selection;
	}

	//ITERADOR QUE RECORRE LA MATRIZ DE REIGONES (POR FILAS, DE IZQ A DER) Y PARA CADA REGION
	//	DEVUELVE LA INSTANCIA CORRESPONDIENTE DE RegionData
	public Iterator<RegionData> iterator() { // TODO REVISAR COLS Y ROWS. YA DE PASO EN EL MANAGER ENTERO
		return new Iterator<MapInfo.RegionData>() {
			int r = 0;
			int c = 0;
			
			@Override
			public RegionData next() {
				assert (r < rows);
				RegionData rd = new RegionData(r, c, regions[c][r]);
				c = (c + 1) % cols;
				if (c == 0)
					r = r + 1;
				return rd;
			}

			@Override
			public boolean hasNext() {
				return r < rows;
			}
		};
	}

}
