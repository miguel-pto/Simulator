package src.simulator.model;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Simulator {
	RegionManager region_manager;
	List<Animal> animals_list;
	double t;
	public Simulator(int cols, int rows, int widht, int height, Factory<Animal> animals_factory, Factory<Region> regions_factory) {
		region_manager = new RegionManager(cols, rows, widht, height);
		//Crear lista de animales 
		//animals_list = 
		t = 0.0;
	}
	
	
	
	private void set_region(int row, int col, Region r) {
		region_manager.set_region(row, col, r);
	}
	
	void set_region(int row, int col, JSONObject r_json) {
		//TODO Que lea el json y cree la region/animal
		
		Region R = new Region();
		set_region(row, col, R);
	}
	
	private void add_animal(Animal a) {
		region_manager.register_animal(a);
	}
	
	void add_animal(JSONObject a_json) {
		String type = a_json.getString(type);
		//TODO
		
		Animal A = new Animal(type, );
		add_animal(A);
	}
	
	public MapInfo get_map_info() {
		return region_manager;
	}
	
	public List<? extends AnimalInfo> get_animals() {
		return Collections.unmodifiableList(animals_list);
	}
	
	public double get_time() {
		return t;
	}
	
	public void advance(double dt) {
		t += dt;
		Animal a;
		for (int i= animals_list.size()-1; i>=0; i--) {
			a = animals_list.get(i);
			if (a.get_state() == State.DEAD) {
				//TODO Hay que quitar de la lista el animal, creo que ya esta hecho en la lista, pero por si acaso dejo el TO DO
				region_manager.unregister_animal(a);
				animals_list.remove(i);
			}
			else {
				a.update(dt);
				region_manager.update_animal_region(a);
				if (a.is_pregnant()) {
					Animal baby = a.deliver_baby();
					add_animal(baby);
				}
			}
		}
		region_manager.update_all_regions(dt);
	}
	
	public JSONObject as_JSON() {
		//TODO
		/* devuelve una estructura JSON con t  tiempo actual y s es lo que devuelve as_JSON() del gestor de regiones:
			{
				"time": t,
				"state": s,
			}
	*/
		
	}
}
