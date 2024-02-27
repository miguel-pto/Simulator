package simulator.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegionManager implements AnimalMapView {

	public static final int MAX = 100;

	private int cols, rows, width, height, region_height, region_width;
	private Map<Animal, Region> animal_region;
	private Region[][] regions;

	public RegionManager(int cols, int rows, int width, int height) {
		this.cols = cols;
		this.rows = rows;
		this.width = width;
		this.height = height;
		region_width = width / rows;
		region_height = height / cols;
		regions = new Region[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++)
				regions[i][j] = new DefaultRegion();
		}
		animal_region = new HashMap<Animal, Region>();
	}

	public void set_region(int row, int col, Region r) {
		for (Animal a : regions[row][col].getAnimals()) {
			r.add_animal(a);
		}
		regions[row][col] = r;
	}

	Region find_region(Animal a) {
		int row = (int) a.get_position().getX() / region_width;
		int col = (int) a.get_position().getY() / region_height;
		return regions[row][col];
	}

	void register_animal(Animal a) {
		a.init(this);
		Region r = find_region(a);
		r.add_animal(a);
		animal_region.put(a, r);
	}

	void unregister_animal(Animal a) {
		animal_region.get(a).remove_animal(a);
		animal_region.remove(a);
	}

	void update_animal_region(Animal a) {
		if (animal_region.get(a) != find_region(a)) {
			unregister_animal(a);
			register_animal(a);
			animal_region.put(a, find_region(a));
		}
	}

	@Override
	public double get_food(Animal a, double dt) {
		return animal_region.get(a).get_food(a, dt);
	}

	void update_all_regions(double dt) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; i < cols; j++)
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
	public JSONObject as_JSON() {
		JSONObject o = new JSONObject();
		JSONArray regions = new JSONArray();
		
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				JSONObject aux = new JSONObject();
				aux.put("row", i);
				aux.put("col", j);
				aux.put("data", this.regions[i][j].as_JSON());
				regions.put(aux);
			}
		
		o.put("regiones", regions);
		return o;
	}

	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		double r = e.get_sight_range();

		double rightBound = e.get_position().getX() + r;
		rightBound = rightBound >= width ? width - 1 : rightBound;

		double leftBound = e.get_position().getX() - r;
		leftBound = leftBound >= width ? width - 1 : leftBound;

		double upperBound = e.get_position().getY() - r;
		upperBound = upperBound >= width ? width - 1 : upperBound;

		double lowerBound = e.get_position().getY() + r;
		lowerBound = lowerBound >= width ? width - 1 : lowerBound;

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

}
