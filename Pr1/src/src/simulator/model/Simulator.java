package src.simulator.model;

import java.util.List;

import org.json.JSONObject;

public class Simulator {
	RegionManager RegionManager;
	List<Animal> animals_list;
	double dt;
	public Simulator(int cols, int rows, int widht, int height, Factory<Animal> animals_factory, Factory<Region> regions_factory) {
		//Crea un nuevo manager
		//Crea lista de animales
		dt = 0.0;
	}
	
	
	
	private void set_region(int row, int col, Region r) {
		//Con el manager lo mete en las pos row, col
	}
	
	void set_region(int row, int col, JSONObject r_json) {
		//Region R = new Region();
		set_region(row, col, R);
	}
	
	private void add_animal(Animal a) {
		//Necesito gestor de regiones
	}
	
	void add_animal(JSONObject a_json) {
		String type = a_json.getString(type);
		//TODO 
		Animal A = new Animal(type, );
		add_animal(A);
	}
	
	MapInfo get_map_info() {
		return RegionManager;
	}
	
	public List<? extends AnimalInfo> get_animals(){
		//Devuelve una versión inmodificable de la lista de animales
	}
	
	public double get_time() {
		return dt;
	}
}
