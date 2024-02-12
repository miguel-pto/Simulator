package src.simulator.model;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {

	public final static double FOOD_MULTIPLIER_PARAMETER = 60.0, FOOD_MAX_PARAMETER = 5.0,
			FOOD_MAX_MULTIPLIER_PARAMETER = 2.0;

	protected List<Animal> al;

	protected Region() {
		al = new ArrayList<Animal>();
	}

	@Override
	public JSONObject as_JSON() {
		// TODO Auto-generated method stub
		return null;
	}

	final void add_animal(Animal a) {
		if (!al.contains(a))
			al.add(a);
	}

	final void remove_animal(Animal a) {
		al.remove(a);
	}

	final List<Animal> getAnimals() {
		return Collections.unmodifiableList(al);
	}

}
