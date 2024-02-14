package src.simulator.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;

public class Simulator implements JSONable{
	
	RegionManager region_manager;
	List<Animal> animal_list;
	double t;
	
	public Simulator(int cols, int rows, int widht, int height, Factory<Animal> animals_factory, Factory<Region> regions_factory) {
		region_manager = new RegionManager(cols, rows, widht, height);
		animal_list = new ArrayList<Animal>();
		t = 0.0;
	}
	
	
	
	private void set_region(int row, int col, Region r) {
		region_manager.set_region(row, col, r);
	}
	
	void set_region(int row, int col, JSONObject r_json) {
		//TODO Que lea el json y cree la region/animal
		
		Region R = new DefaultRegion(); //POR AHORA
		set_region(row, col, R);
	}
	
	private void add_animal(Animal a) {
		animal_list.add(a);
		region_manager.register_animal(a);
	}
	
	void add_animal(JSONObject a_json) {
		String type = a_json.getString("type");
		//TODO
	}
	
	public MapInfo get_map_info() {
		return region_manager;
	}
	
	public List<? extends AnimalInfo> get_animals() {
		return Collections.unmodifiableList(animal_list);
	}
	
	public double get_time() {
		return t;
	}
	
	private void remove_dead() {
		for (int i = animal_list.size() - 1; i >= 0; i--) {
			Animal a;
			a = animal_list.get(i);
			if (a.get_state() == State.DEAD) {
				animal_list.remove(a);
				region_manager.unregister_animal(a);
			}
		}
	}
	
	private void update_animal_regions(double dt) {
		for (Animal a : animal_list) {
			a.update(dt);
			region_manager.update_animal_region(a);
		}
	}
	
	private void deliver_babies() {
		for (Animal a : animal_list) {
			if (a.is_pregnant()) {
				Animal baby = a.deliver_baby();
				add_animal(baby);
			}
		}
	}
	
	public void advance(double dt) {
		t += dt;
		remove_dead();
		update_animal_regions(dt);
		region_manager.update_all_regions(dt);
		deliver_babies();
	}

	@Override
	public JSONObject as_JSON() {
		// TODO Auto-generated method stub
		return null;
	}
}
