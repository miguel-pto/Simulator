package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Rabbit extends Animal {

	public final static String RABBIT_GENETIC_CODE = "Rabbit";
	public final static double INIT_SIGHT_RABBIT = 40, INIT_SPEED_RABBIT = 35, BOOST_FACTOR_RABBIT = 2.0,
			MAX_AGE_RABBIT = 8, FOOD_DROP_BOOST_FACTOR_RABBIT = 1.2, FOOD_DROP_RATE_RABBIT = 20.0,
			DESIRE_THRESHOLD_RABBIT = 65.0, DESIRE_INCREASE_RATE_RABBIT = 40.0, PREGNANT_PROBABILITY_RABBIT = 0.9;

	Animal danger_source;
	SelectionStrategy danger_strategy;

	public Rabbit(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super("Rabbit", Diet.HERVIBORE, INIT_SIGHT_RABBIT, INIT_SPEED_RABBIT, mate_strategy, pos);
		this.danger_strategy = danger_strategy;
	}

	protected Rabbit(Rabbit p1, Animal p2) {
		super(p1, p2);
		danger_strategy = p1.danger_strategy;
	}

	// AVANZAR CON VELOCIDAD NORMAL
	protected void advance_normal(double dt) {
		if (is_in_range(dest))
			dest = new Vector2D(Utils.rand.nextDouble(800), Utils.rand.nextDouble(600));
		move(speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_RABBIT * dt, 0, MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_RABBIT * dt, 0, MAX_DESIRE);
	}

	// AVANZAR CON VELOCIDAD BOOSTEADA
	protected void advance_boost(double dt) {
		move(BOOST_FACTOR_RABBIT * speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_RABBIT * dt * FOOD_DROP_BOOST_FACTOR_RABBIT, 0,
				MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_RABBIT * dt, 0, MAX_DESIRE);
	}

	// FUNCIÓN AUXILIAR QUE SE USA EN UPDATE_MATE
	protected void mate() {
		desire = 0.0;
		mate_target.desire = 0.0;
		if (baby == null && Utils.rand.nextDouble() < PREGNANT_PROBABILITY_RABBIT)
			baby = new Rabbit(this, mate_target);
		mate_target = null;
	}

	@Override
	protected void set_normal() {
		mate_target = null;
		danger_source = null;
	}

	@Override
	protected void set_danger() {
		mate_target = null;
	}

	@Override
	protected void set_mate() {
		danger_source = null;
	}

	@Override
	protected void set_hunger() {
	}

	@Override
	protected void update_normal(double dt) {
		advance_normal(dt);
		if (danger_source == null)
			danger_source = danger_strategy.select(this,
					region_mngr.get_animals_in_range(this, (e) -> e.diet == Diet.CARNIVORE));
		if (danger_source == null) {
			if (desire > DESIRE_THRESHOLD_RABBIT) {
				set_state(State.MATE);
			}
		} else {
			set_state(State.DANGER);
		}
	}

	@Override
	protected void update_hunger(double dt) {
	}

	// FUNCIÓN AUXILIAR USADA EN UPDATE_DANGER PARA LEGIBILIDAD
	private void flee(double dt) {
		dest = pos.plus(pos.minus(danger_source.get_position()).direction());
		advance_boost(dt);
	}

	@Override
	protected void update_danger(double dt) {
		if (danger_source != null && !danger_source.is_alive())
			danger_source = null;
		if (danger_source == null) {
			advance_normal(dt);
		} else {
			flee(dt);
		}

		if (danger_source == null || !is_in_sight_range(danger_source))
			danger_source = danger_strategy.select(this,
					region_mngr.get_animals_in_range(this, (e) -> e.diet == Diet.CARNIVORE));
		if (danger_source == null)
			if (desire > DESIRE_THRESHOLD_RABBIT) {
				set_state(State.MATE);
			} else {
				set_state(State.NORMAL);
			}
	}

	@Override
	protected void update_mate(double dt) {
		if (mate_target != null && (!mate_target.is_alive() || !is_in_sight_range(mate_target)))
			mate_target = null;
		if (mate_target == null)
			mate_target = find_mate();
		if (mate_target == null)
			advance_normal(dt);
		else {
			dest = mate_target.get_position();
			advance_boost(dt);
			if (is_in_range(mate_target.get_position())) {
				mate();
			}
			if (danger_source == null)
				danger_source = danger_strategy.select(this,
						region_mngr.get_animals_in_range(this, (e) -> e.diet == Diet.CARNIVORE));
			if (danger_source != null)
				set_state(State.DANGER);
			else if (desire < DESIRE_THRESHOLD_RABBIT)
				set_state(State.NORMAL);
		}
	}

	@Override
	protected void update_state(double dt) {
		if (is_out_of_bounds()) {
			pos = pos.adjust(region_mngr.get_width() - 1, region_mngr.get_height() - 1);
			set_state(State.MATE);
		}
		if (energy == 0.0 || age > MAX_AGE_RABBIT)
			set_state(State.DEAD);
		if (is_alive())
			energy = Utils.constrain_value_in_range(energy + region_mngr.get_food(this, dt), 0, MAX_ENERGY);
	}

}
