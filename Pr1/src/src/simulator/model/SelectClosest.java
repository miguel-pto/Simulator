package src.simulator.model;

import java.util.List;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal selection = as.get(0);
		
		double min = a.get_position().distanceTo(selection.get_position());

		for (Animal animal : as) {
			if (a.get_position().distanceTo(animal.get_position()) > min)
				selection = animal;
		}

		return selection;
	}

}
