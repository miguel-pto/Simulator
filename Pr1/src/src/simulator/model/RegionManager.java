package src.simulator.model;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONObject;
//asdlkfjahgrñ
public class RegionManager implements AnimalMapView {

	private int cols, rows, width, height, region_height, region_width;
	private Map<Animal, Region> animal_region;
	private List<List<Region>> regions;

	public RegionManager(int cols, int rows, int width, int height) {
		this.cols = cols;
		this.rows = rows;
		this.width = width;
		this.height = height;
		region_width = width / cols;
		region_height = height / rows;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) regions.get(i).add(new DefaultRegion());
		}
		// TODO INICIALIZAR ANIMAL_REGION
	}
	
	public void set_region(int row, int col, Region r) {
		Region prev = regions.get(row).get(col);
		regions.get(row).set(col, r);
		for (AnimalInfo a : prev.al) {
			regions.get(row).get(col).add_animal(a);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double get_food(Animal a, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
		// TODO Auto-generated method stub
		return null;
	}

}
