package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal selection = null;

		if (as.size() > 1) {
			selection = as.get(0) == a ? as.get(1) : as.get(0);
			
			for (Animal animal : as) {
				if (animal != a && animal.age < selection.age)
					selection = animal;
			}
		}

		return selection;
	}

}
