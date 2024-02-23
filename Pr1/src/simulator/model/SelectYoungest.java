package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal selection = as.get(0);

		for (Animal animal : as) {
			if (animal.age < selection.age)
				selection = animal;
		}

		return selection;
	}

}
