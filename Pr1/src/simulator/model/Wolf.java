package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Wolf extends Animal {

	public final static String WOLF_GENETIC_CODE = "Wolf";
	public final static double INIT_SIGHT_WOLF = 50, INIT_SPEED_WOLF = 60, BOOST_FACTOR_WOLF = 3.0, MAX_AGE_WOLF = 14.0,
			FOOD_THRESHOLD_WOLF = 50.0, FOOD_DROP_BOOST_FACTOR_WOLF = 1.2, FOOD_DROP_RATE_WOLF = 18.0,
			FOOD_DROP_DESIRE_WOLF = 10.0, FOOD_EAT_VALUE_WOLF = 50.0, DESIRE_THRESHOLD_WOLF = 65.0,
			DESIRE_INCREASE_RATE_WOLF = 30.0, PREGNANT_PROBABILITY_WOLF = 0.75;

	Animal hunt_target;
	SelectionStrategy hunting_strategy;

	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos) {
		super("Wolf", Diet.CARNIVORE, INIT_SIGHT_WOLF, INIT_SPEED_WOLF, mate_strategy, pos);
		this.hunting_strategy = hunting_strategy;
	}

	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);
		hunting_strategy = p1.hunting_strategy;
	}

	// AVANZAR CON VELOCIDAD NORMAL
	protected void advance_normal(double dt) {
		if (pos.distanceTo(dest) < COLLISION_RANGE)
			dest = new Vector2D(Utils.rand.nextDouble(800), Utils.rand.nextDouble(600));
		move(speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_WOLF * dt, 0, MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_WOLF * dt, 0, MAX_DESIRE);
	}

	// FUNCIÓN AUXILIAR USADA EN UPDATE_HUNGER PARA LEGIBILIDAD
	protected void hunt() {
		hunt_target.set_state(State.DEAD);
		hunt_target = null;
		energy += Utils.constrain_value_in_range(energy + FOOD_EAT_VALUE_WOLF, 0, MAX_ENERGY);
	}

	// AVANZAR CON VELOCIDAD BOOSTEADA
	protected void advance_boost(double dt) {
		move(BOOST_FACTOR_WOLF * speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_WOLF * dt * FOOD_DROP_BOOST_FACTOR_WOLF, 0,
				MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_WOLF * dt, 0, MAX_DESIRE);
	}

	@Override
	protected void set_normal() {
		hunt_target = null;
		mate_target = null;
	}

	@Override
	protected void set_danger() {
	}

	@Override
	protected void set_mate() {
		hunt_target = null;
	}

	@Override
	protected void set_hunger() {
		mate_target = null;
	}

	@Override
	protected void update_normal(double dt) {
		advance_normal(dt);
		if (energy < FOOD_THRESHOLD_WOLF) {
			set_state(State.HUNGER);
		} else if (desire >= DESIRE_THRESHOLD_WOLF)
			set_state(State.MATE);
	}

	@Override
	protected void update_hunger(double dt) {
		if (hunt_target == null || !hunt_target.is_alive() || !is_in_sight_range(hunt_target))
			hunt_target = hunting_strategy.select(this,
					region_mngr.get_animals_in_range(this, (e) -> e.diet == Diet.HERVIBORE));
		if (hunt_target == null)
			advance_normal(dt);
		else {
			dest = hunt_target.get_position();
			advance_boost(dt);
			if (is_in_range(hunt_target.pos)) {
				hunt();
			}
		}
		if (energy > FOOD_THRESHOLD_WOLF) {
			if (desire < DESIRE_THRESHOLD_WOLF)
				set_state(State.NORMAL);
			else
				set_state(State.MATE);
		}
	}

	@Override
	protected void update_danger(double dt) {
	}

	// FUNCIÓN AUXILIAR USADA EN UPDATE_MATE PARA LEGIBILIDAD
	protected void mate() {
		desire = 0;
		mate_target.desire = 0;
		if (baby == null && Utils.rand.nextDouble() < PREGNANT_PROBABILITY_WOLF)
			baby = new Wolf(this, mate_target);
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_DESIRE_WOLF, 0, MAX_ENERGY);
		mate_target = null;
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
			if (is_in_range(mate_target.pos)) {
				mate();
			}
		}
		if (energy < FOOD_THRESHOLD_WOLF)
			set_state(State.HUNGER);
		else if (desire < DESIRE_THRESHOLD_WOLF)
			set_state(State.NORMAL);
	}

	@Override
	protected void update_state(double dt) {
		if (is_out_of_bounds()) {
			pos = pos.adjust(region_mngr.get_width() - 1, region_mngr.get_height() - 1);
			set_state(State.NORMAL);
		}
		if (energy == 0.0 || age > MAX_AGE_WOLF)
			set_state(State.DEAD);
		if (state != State.DEAD)
			energy = Utils.constrain_value_in_range(energy + region_mngr.get_food(this, dt), 0, MAX_ENERGY);
	}

}
