package simulator.model;

public class DefaultRegion extends Region {

	@Override
	public void update(double dt) {
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a.diet == Diet.HERVIBORE) {
			int n = 0; // TODO NUMERO DE HERBIVOROS EN LA REGION
			return FOOD_MULTIPLIER_PARAMETER
					* Math.exp(-Math.max(0, n - FOOD_MAX_PARAMETER) * FOOD_MAX_MULTIPLIER_PARAMETER) * dt;
		} else
			return 0.0;
	}

}
