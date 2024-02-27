package simulator.model;

import simulator.misc.Utils;

public class DynamicSupplyRegion extends Region {

	private double food, factor;
	public static final double INIT_FOOD = 1000.0, INIT_FACTOR = 2.0;

	public DynamicSupplyRegion(double init_food, double growth_factor) {
		food = init_food;
		factor = growth_factor;
	}

	@Override
	public void update(double dt) {
		if (Utils.rand.nextDouble() < 0.5)
			food += dt * factor;
	}

	@Override
	public double get_food(Animal a, double dt) {
		if (a.diet == Diet.HERVIBORE) {
			long n = this.getAnimals().stream().filter((e) -> e.get_diet() == Diet.HERVIBORE).count();
			double eaten = Math.min(food, FOOD_MULTIPLIER_PARAMETER
					* Math.exp(-Math.max(0, n - FOOD_MAX_PARAMETER) * FOOD_MAX_MULTIPLIER_PARAMETER) * dt);
			food -= eaten;
			return eaten;
		} else
			return 0.0;
	}

}
