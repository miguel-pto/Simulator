package simulator.model;

import java.util.List;

public class SelectFirst implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal selection = null;

		if (!as.isEmpty())
			selection = as.get(0);

		return selection;
	}
}
