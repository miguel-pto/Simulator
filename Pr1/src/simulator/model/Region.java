package simulator.model;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {

	public final static double FOOD_MULTIPLIER_PARAMETER = 60.0, FOOD_MAX_PARAMETER = 5.0,
			FOOD_MAX_MULTIPLIER_PARAMETER = 2.0;

	protected List<Animal> animal_list;

	protected Region() {
		animal_list = new ArrayList<Animal>();
	}

	@Override
	public JSONObject as_JSON() {
		JSONObject o = new JSONObject();
		JSONArray animals = new JSONArray();
		
		for (Animal a : animal_list) {
			animals.put(a.as_JSON());
		}
		
		o.put("animals", animals);
		return o;
	}

	final void add_animal(Animal a) {
		if (!animal_list.contains(a))
			animal_list.add(a);
	}

	final void remove_animal(Animal a) {
		animal_list.remove(a);
	}

	final List<Animal> getAnimals() {
		return Collections.unmodifiableList(animal_list);
	}

}
