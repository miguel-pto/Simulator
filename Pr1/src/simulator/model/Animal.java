package simulator.model;

import org.json.JSONObject;
import org.json.JSONArray;
import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo {

	public final static double INIT_ENERGY = 100.0, MUTATION_TOLERANCE = 0.2, NEARBY_FACTOR = 60.0, COLLISION_RANGE = 8,
			HUNGER_DECAY_EXP_FACTOR = 0.007, MAX_ENERGY = 100, MAX_DESIRE = 100, INIT_SPEED_PARAMETER = 0.1;

	protected String genetic_code;
	protected Diet diet;
	protected State state;
	protected Vector2D pos, dest;
	protected double energy, speed, age, desire, sight_range;
	protected Animal mate_target, baby;
	protected AnimalMapView region_mngr;
	protected SelectionStrategy mate_strategy;

	// CONSTRUCTOR PARA ANIMALES INICIALIZADOS DESDE LA ENTRADA
	protected Animal(String genetic_code, Diet diet, double sight_range, double init_speed,
			SelectionStrategy mate_strategy, Vector2D pos) {
		this.genetic_code = genetic_code;
		this.diet = diet;
		this.sight_range = sight_range;
		speed = Utils.get_randomized_parameter(init_speed, INIT_SPEED_PARAMETER);
		this.mate_strategy = mate_strategy;
		this.pos = pos;
		state = State.NORMAL;
		energy = 100.0;
		desire = 0.0;
	}

	// CONSTRUCTOR PARA ANIMALES NACIDOS DURANTE LA SIMULACIÓN
	protected Animal(Animal p1, Animal p2) {
		state = State.NORMAL;
		desire = 0.0;
		genetic_code = p1.get_genetic_code();
		diet = p1.get_diet();
		energy = (p1.get_energy() + p2.get_energy()) / 2;
		pos = p1.get_position()
				.plus(Vector2D.get_random_vector(-1, 1).scale(NEARBY_FACTOR * (Utils.rand.nextGaussian() + 1)));
		sight_range = Utils.get_randomized_parameter((p1.get_sight_range() + p2.get_sight_range()) / 2, 0.2);
		speed = Utils.get_randomized_parameter((p1.get_speed() + p2.get_speed()) / 2, 0.2);
		this.mate_strategy = p2.mate_strategy;
	}

	// INICIALIZAR LA REGIÓN Y LA POSICIÓN
	void init(AnimalMapView reg_mngr) {
		region_mngr = reg_mngr;
		if (pos == null) { // NO HABÍA POSICIÓN EN LA ENTRADA
			double x = Utils.rand.nextDouble(reg_mngr.get_width() - 1);
			double y = Utils.rand.nextDouble(reg_mngr.get_height() - 1);
			pos = new Vector2D(x, y);
		} else // HABÍA POSICIÓN EN LA ENTRADA
			pos.adjust(reg_mngr.get_width() - 1, reg_mngr.get_height() - 1);
		// INICIALIZA EL DESTINO A UNA POSICIÓN ALEATORIA
		double x = Utils.rand.nextDouble(reg_mngr.get_width() - 1);
		double y = Utils.rand.nextDouble(reg_mngr.get_height() - 1);
		dest = new Vector2D(x, y);
	}

	// DAR A LUZ, SE DEVUELVE BABY Y SE PONE A NULL
	Animal deliver_baby() {
		Animal aux = baby;

		baby = null;

		return aux;

		/*
		 * try { return baby; } finally { baby = null; }
		 */
	}

	// ACTUALIZAR POS SEGÚN LA VELOCIDAD
	protected void move(double speed) {
		pos = pos.plus(dest.minus(pos).direction().scale(speed));
	}

	@Override
	public JSONObject as_JSON() {
		JSONObject o = new JSONObject();

		JSONArray pos = new JSONArray();
		// POSICIÓN
		pos.put(this.pos.getX());
		pos.put(this.pos.getY());
		o.put("pos", pos);
		// CÓDIGO GENÉTICO
		o.put("gcode", genetic_code);
		// DIETA
		o.put("diet", diet.toString());
		// ESTADO
		o.put("state", state.toString());

		return o;
	}

	@Override
	public State get_state() {
		return state;
	}

	@Override
	public Vector2D get_position() {
		return pos;
	}

	@Override
	public String get_genetic_code() {
		return genetic_code;
	}

	@Override
	public Diet get_diet() {
		return diet;
	}

	@Override
	public double get_speed() {
		return speed;
	}

	@Override
	public double get_sight_range() {
		return sight_range;
	}

	@Override
	public double get_energy() {
		return energy;
	}

	@Override
	public double get_age() {
		return age;
	}

	@Override
	public Vector2D get_destination() {
		return dest;
	}

	@Override
	public boolean is_pregnant() {
		return baby != null;
	}

	protected abstract void update_normal(double dt);

	protected abstract void update_hunger(double dt);

	protected abstract void update_danger(double dt);

	protected abstract void update_mate(double dt);

	// FUNCIÓN QUE SE EJECUTA SIEMPRE AL FINAL DE UPDATE
	protected abstract void update_state(double dt);

	protected abstract void mate();

	// UPDATE QUE COMPARTEN TODOS LOS ANIMALES. CADA UNO LUEGO IMPLEMENTA LA FUNCIÓN
	// ABSTRACTA QUE ACTUALIZA DEPENDIENDO DEL ESTADO
	public void update(double dt) {
		switch (state) {
		case NORMAL:
			update_normal(dt);
			break;
		case HUNGER:
			update_hunger(dt);
			break;
		case DANGER:
			update_danger(dt);
			break;
		case MATE:
			update_mate(dt);
			break;
		default:
			break;
		}
		update_state(dt);
	}

	protected abstract void advance_normal(double dt);

	protected abstract void advance_boost(double dt);

	protected abstract void set_normal();

	protected abstract void set_danger();

	protected abstract void set_mate();

	protected abstract void set_hunger();

	protected boolean is_in_sight_range(Animal a) {
		return pos.distanceTo(a.pos) <= sight_range;
	}

	protected boolean is_in_range(Vector2D pos) {
		return this.pos.distanceTo(pos) < COLLISION_RANGE;
	}

	protected Animal find_mate() {
		return mate_strategy.select(this,
				region_mngr.get_animals_in_range(this, (e) -> e.genetic_code == this.genetic_code));
	}

	// LO COMPARTEN TODOS LOS ANIMALES Y LUEGO IMPLEMENTAN CADA SET
	protected void set_state(State state) {
		this.state = state;
		switch (state) {
		case NORMAL:
			set_normal();
			break;
		case DANGER:
			set_danger();
			break;
		case MATE:
			set_mate();
			break;
		case HUNGER:
			set_hunger();
			break;
		default:
			break;
		}
	}

	public boolean is_alive() {
		return state != State.DEAD;
	}

	// DEVUELVE TRUE SI ESTÁ FUERA DEL MAPA
	protected boolean is_out_of_bounds() {
		return 0 > pos.getX() || pos.getX() >= region_mngr.get_width() || 0 > pos.getY()
				|| pos.getY() >= region_mngr.get_height();
	}
}
