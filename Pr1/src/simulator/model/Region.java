package simulator.model;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {

	// TODO Hay que añadir a todas las clases que extiendan Region el metodo
	// toString (lo dice en pag 13-14)

	public final static double FOOD_MULTIPLIER_PARAMETER = 60.0, FOOD_MAX_PARAMETER = 5.0,
			FOOD_MAX_MULTIPLIER_PARAMETER = 2.0;

	protected List<Animal> animal_list;

	protected Region() {
		animal_list = new ArrayList<Animal>();
	}

	@Override
	// CONVIERTE LA LISTA DE ANIAMLES EN UN JSON
	public JSONObject as_JSON() {
		JSONObject o = new JSONObject();
		JSONArray animals = new JSONArray();

		for (Animal a : animal_list) {
			animals.put(a.as_JSON());
		}

		o.put("animals", animals);
		return o;
	}

	// SI EL ANIMAL NO SE ENCUENTRA EN LA LISTA LO AÑADE EN ESTA
	final void add_animal(Animal a) {
		if (!animal_list.contains(a))
			animal_list.add(a);
	}

	// ELIMINA ANIMAL DE LA LISTA
	final void remove_animal(Animal a) {
		animal_list.remove(a);
	}

	// DEVUELVE LA LISTA DE ANIMALES INMODIFICABLE
	final List<Animal> getAnimals() {
		return Collections.unmodifiableList(animal_list);
	}

	public List<AnimalInfo> getAnimalsInfo() {
		return new ArrayList<AnimalInfo>(animal_list); // EN LAS DIAPOSITIVAS DICE ANIMALS, PERO ASUMO QUE NO TIENE SENTIDO, PONGO
												// ANIMAL_LIST
		// TODO LAS DIAPOSITIVAS DICEN QUE TMB SE PUEDE UTILIZAR
		// Collections.unmodifiableList(animal_list); QUE ERA LO QUE YA TENIAMOS
		// LA OPCION QUE ESTA PUESTA ES MEJOR PARA LA PROGRAMACION CONCURRENTE, i.e, ES
		// MEJOR PARA LA TERCERA ENTREGA
	}

}
