package simulator.model;

import java.util.List;

import simulator.misc.Utils;

public class DynamicSupplyRegion extends Region {

	private double food, factor;
	public static final double INIT_FOOD = 1000.0, INIT_FACTOR = 2.0;

	// INICIALIZA FOOD Y FACTOR A LOS VALORES PROPORCIONADOS
	public DynamicSupplyRegion(double init_food, double growth_factor) {
		food = init_food;
		factor = growth_factor;
	}

	@Override
	// ACTUALIZA ALEATORIAMENTE FOOD EN BASE A SU FACTOR DE CRECIMIENTO
	public void update(double dt) {
		if (Utils.rand.nextDouble() < 0.5)
			food += dt * factor;
	}

	@Override
	// SI LA DIETA DEL ANIMAL ES HERVÍBORA LE DEVUELVE UNA CANTIDAD DE COMIDA EN
	// BASE A LA SUMA DE HERVÍBOROS QUE HAYA EN LA ZONA
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

	public String toString() {
		return "Dynamic region";
	}

	// TODO SE CREA AL CREAR LA FUNCION EN REGION(PAG 16) PERO NO CREO QUE SEA UTIL,
	// SEGURAMENTE SE PUEDA QUITAR
	@Override
	public List<AnimalInfo> getAnimalsInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
