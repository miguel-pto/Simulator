package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	//SELECTION STRATEGY QUE SELECCIONA AL ANIMAL CUYA EDAD TIENE EL ATRIBUTO MÁS PEQUEÑO
	public Animal select(Animal a, List<Animal> as) {
		Animal selection = null;

		if (!as.isEmpty()) {
			selection = as.get(0);

			for (Animal animal : as) {
				if (animal != a && animal.age < selection.age)
					selection = animal;
			}
		}

		return selection;
	}

}
