package simulator.model;

import java.util.List;

public class SelectFirst implements SelectionStrategy {

	@Override
	// SELECTION STRATEGY QUE SELECCIONA AL ANIMAL NUMERADO COMO EL PRIMERO
	public Animal select(Animal a, List<Animal> as) {
		Animal selection = null;

		if (!as.isEmpty())
			selection = as.get(0);

		return selection;
	}
}
