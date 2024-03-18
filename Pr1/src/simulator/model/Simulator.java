package simulator.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONObject;
import simulator.factories.Factory;
import simulator.view.EcoSysObserver;

public class Simulator implements JSONable {

	private RegionManager region_manager;
	private List<Animal> animal_list;
	private List<EcoSysObserver> observer_list; 
	private double t;
	private Factory<Animal> animals_factory;
	private Factory<Region> regions_factory;
	

	//INICIALIZA EL SIMULADOR CON LOS ATRIBUTOS ASIGNADOS
	public Simulator(int cols, int rows, int width, int height, Factory<Animal> animals_factory,
			Factory<Region> regions_factory) {
		region_manager = new RegionManager(cols, rows, width, height);
		animal_list = new ArrayList<Animal>();
		this.animals_factory = animals_factory;
		this.regions_factory = regions_factory;
		t = 0.0;
	}

	//LLAMA AL MANAGER DE LA REGION PARA FIJAR UNA REGION EN UNA POSICIÓN DADA
	private void set_region(int row, int col, Region r) {
		region_manager.set_region(row, col, r);
	}

	//CREA UNA REGION DESDE UN JSON Y LLAMA A LA FUNCIÓN ANTERIOR PARA QUE SEA FIJADA
	public void set_region(int row, int col, JSONObject r_json) {
		Region R = regions_factory.create_instance(r_json);
		set_region(row, col, R);
	}

	//LLAMA A LA LISTA Y AL MANAGER PARA QUE AÑADE Y REGISTRE EL ANIMAL PROPORCIONADO
	private void add_animal(Animal a) {
		animal_list.add(a);
		region_manager.register_animal(a);
		notify_onAnimalAdded();
		
	}

	//CREA UN ANIMAL DESDE UN JSON Y LLAMA A LA FUNCIÓN ANTERIOR PARA QUE SEA AÑADIDO
	public void add_animal(JSONObject a_json) {
		Animal A = animals_factory.create_instance(a_json);
		add_animal(A);
	}

	//DEVUELVE EL MANAGER DE REGIONES
	public MapInfo get_map_info() {
		return region_manager;
	}

	//DEVUELVE LA LISTA NO MODIFICABLE DE ANIMALES
	public List<? extends AnimalInfo> get_animals() {
		return Collections.unmodifiableList(animal_list);
	}

	//DEVUELVE EL TIEMPO
	public double get_time() {
		return t;
	}

	//ELIMINA LOS ANIMALES CUYO ESTADO HAYA SIDO ASIGNADO COMO DEAD
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

	//RECORRE TODOS LOS ANIMALES DE LA LISTA Y EN CADA UNO DE ELLOS ACTUALIZA SU RESPECTIVA REGIÓN
	private void update_animal_regions(double dt) {
		for (Animal a : animal_list) {
			a.update(dt);
			region_manager.update_animal_region(a);
		}
	}

	//RECORRE TODOS LOS ANIMALES DE LA LISTA Y SI SE ENCUENTRAN PREÑADOS LLAMAN A DAR A LUZ Y A AÑADIR A SU CRIA
	private void deliver_babies() {
		for (int i = 0; i < animal_list.size(); i++) {
			Animal a = animal_list.get(i);
			if (a.is_pregnant()) {
				Animal baby = a.deliver_baby();
				add_animal(baby);
			}
		}
	}

	//FUNCIÓN ENCARGADA DE AVANZAR EL TIEMPO, ELIMINAR LOS MUERTOS, ACTUALIZAR LAS REGIONES Y QUE LOS ANIMALES DEN A LUZ
	public void advance(double dt) {
		t += dt;
		remove_dead();
		update_animal_regions(dt);
		region_manager.update_all_regions(dt);
		deliver_babies();
	}

	@Override
	//CREA UN JSON Y LO DEVUELVE TRAS INTRODUCIRLE INFORMACIÓN DEL TIEMPO Y EL ESTADO DEL REGION MANAGER
	public JSONObject as_JSON() {
		JSONObject o = new JSONObject();
		o.put("time", t);
		o.put("state", region_manager.as_JSON());
		return o;
	}
	
	//VACÍA LA LISTA DE ANIMALES CREANDO UNA NUEVA, CREA UN REGIONMANAGER DEL TAMAÑO ADECUADO Y PONE TIEMPO A 0
	public void reset(int cols, int rows, int width, int height) {
		animal_list = new ArrayList<Animal>();
		region_manager = new RegionManager(cols, rows, width, height);
		t = 0.0;
		for (EcoSysObserver o:observer_list) {
			notify_onRegister(o);
		}
	}
	
	//AÑADE EL OBSERVADOR O A LA LISTA DE OBSERVADORES TRAS CONFIRMAR QUE NO ESTE YA EN ELLA
	public void addObserver(EcoSysObserver o) {
		if (!observer_list.contains(o)) {
			observer_list.add(o);
			notify_onRegister(o);
		}
	}
	
	//ENVIA UNA NOTIFICACION ONREGISTER AL OBSERVADOR MANDADO
	private void notify_onRegister (EcoSysObserver o) {
		List<AnimalInfo> animal_info= new ArrayList<>(animal_list);
		o.onRegister(t, get_map_info(), animal_info);
	}
	
	//ENVIA UNA NOTIFICACION ONANIMALADDED A TODOS LOS OBSERVADORES
	private void notify_onAnimalAdded () {
		List<AnimalInfo> animal_info= new ArrayList<>(animal_list);
		for (EcoSysObserver o: observer_list)
			o.onAnimalAdded(t, get_map_info(), animal_info);
	}
	
	//TODO CAMBIAR EL NULL POR UN REGIONINFO R
	//ENVIA UNA NOTIFICACION ONREGIONSET A TODOS LOS OBSERVADORES
	private void notify_onRegionSet (int row, int col) {
		List<RegionInfo> region_info= new ArrayList<>(region_manager.);
		for (EcoSysObserver o: observer_list)
			o.onRegionSet(row, col, get_map_info(), null);
	}
	
	public void removeObserver(EcoSysObserver o) {
		observer_list.remove(o);
	}
}
