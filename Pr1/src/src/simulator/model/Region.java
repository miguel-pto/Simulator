package src.simulator.model;

import java.util.List;

import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {

	public final static double FOOD_MULTIPLIER_PARAMETER = 60.0, FOOD_MAX_PARAMETER = 5.0,
			FOOD_MAX_MULTIPLIER_PARAMETER = 2.0;

	protected List<AnimalInfo> al;

	protected Region() {
		// TODO INICIALIZAR LISTA DE ANIMALES
	}

	@Override
	public JSONObject as_JSON() {
		// TODO Auto-generated method stub
		return null;
	}

	final void add_animal(AnimalInfo a) { // TODO MIRAR SI TIENE QUE SER ANIMAL O ANIMALINFO
		if (!al.contains(a))
			al.add(a);
	}

	final void remove_animal(Animal a) {
		al.remove(a);
	}

	final List<Animal> getAnimals() {
		return null; // TODO
	}

}
