package simulator.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import simulator.model.*;

import simulator.factories.Factory;

public class Simulator implements JSONable {

	private RegionManager region_manager;
	private List<Animal> animal_list;
	private double t;
	private Factory<Animal> animals_factory;
	private Factory<Region> regions_factory;

	public Simulator(int cols, int rows, int widht, int height, Factory<Animal> animals_factory,
			Factory<Region> regions_factory) {
		region_manager = new RegionManager(cols, rows, widht, height);
		animal_list = new ArrayList<Animal>();
		this.animals_factory = animals_factory;
		this.regions_factory = regions_factory;
		t = 0.0;
	}

	private void set_region(int row, int col, Region r) {
		region_manager.set_region(row, col, r);
	}

	public void set_region(int row, int col, JSONObject r_json) {
		Region R = regions_factory.create_instance(r_json);
		set_region(row, col, R);
	}

	private void add_animal(Animal a) {
		animal_list.add(a);
		region_manager.register_animal(a);
	}

	public void add_animal(JSONObject a_json) {
		Animal A = animals_factory.create_instance(a_json);
		add_animal(A);
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
		JSONObject o = new JSONObject();
		o.put("time", t);
		o.put("state", region_manager.as_JSON());
		return o;
	}
}
