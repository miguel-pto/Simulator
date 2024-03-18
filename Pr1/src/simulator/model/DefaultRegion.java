package simulator.model;

import java.util.List;

public class DefaultRegion extends Region {

	@Override
	public void update(double dt) {
	}

	@Override
	//SI LA DIETA DEL ANIMAL ES HERVÍBORA LE DEVUELVE UNA CANTIDAD DE COMIDA EN BASE A LA SUMA DE HERVÍBOROS QUE HAYA EN LA ZONA
	public double get_food(Animal a, double dt) {
		if (a.diet == Diet.HERVIBORE) {
			long n = this.getAnimals().stream().filter((e) -> e.get_diet() == Diet.HERVIBORE).count();
			return FOOD_MULTIPLIER_PARAMETER
					* Math.exp(-Math.max(0, n - FOOD_MAX_PARAMETER) * FOOD_MAX_MULTIPLIER_PARAMETER) * dt;
		} else
			return 0.0;
	}

	//TODO SE CREA AL CREAR LA FUNCION EN REGION(PAG 16) PERO NO CREO QUE SEA UTIL, SEGURAMENTE SE PUEDA QUITAR
	@Override
	public List<AnimalInfo> getAnimalsInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
