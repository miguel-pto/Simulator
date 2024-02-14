package src.simulator.model;

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
		super("Wolf", Diet.CARNIVORE, 50.0, 60.0, mate_strategy, pos);
		this.hunting_strategy = hunting_strategy;
	}

	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);
		hunting_strategy = p1.hunting_strategy;
	}

	protected void advance_normal(double dt) {
		if (pos.distanceTo(dest) < COLLISION_RANGE)
			dest = new Vector2D(Utils._rand.nextDouble(800), Utils._rand.nextDouble(600));
		move(speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_WOLF * dt, 0, MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_WOLF * dt, 0, MAX_DESIRE);
	}

	private void hunt(double dt) {
		dest = hunt_target.get_position();
		move(BOOST_FACTOR_WOLF * speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_WOLF * dt * FOOD_DROP_BOOST_FACTOR_WOLF, 0,
				MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_WOLF * dt, 0, MAX_DESIRE);
		if (pos.distanceTo(hunt_target.get_position()) < COLLISION_RANGE) {
			hunt_target.set_state(State.DEAD);
			hunt_target = null;
			energy += Utils.constrain_value_in_range(energy + FOOD_EAT_VALUE_WOLF, 0, MAX_ENERGY);
		}
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
		if (energy < FOOD_THRESHOLD_WOLF)
			set_state(State.HUNGER);
		else if (desire > DESIRE_THRESHOLD_WOLF)
			set_state(State.MATE);
	}

	@Override
	protected void update_hunger(double dt) {
		if (hunt_target == null || hunt_target.get_state() == State.DEAD)
			; // TODO BUSCAR PRESA
		if (hunt_target == null)
			advance_normal(dt);
		else {
			hunt(dt);
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

	@Override
	protected void update_mate(double dt) {
		if (mate_target != null && (mate_target.get_state() == State.DEAD))
			mate_target = null; // TODO O FUERA DEL CAMPO VISUAL
		if (mate_target == null) {
			// TODO BUSCAR PAREJA
			if (mate_target == null)
				advance_normal(dt);
			else {
				dest = mate_target.get_position();
				move(BOOST_FACTOR_WOLF * speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
				age += dt;
				energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_WOLF * dt * FOOD_DROP_BOOST_FACTOR_WOLF,
						0, MAX_ENERGY);
				desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_WOLF * dt, 0, MAX_DESIRE);
				if (pos.distanceTo(mate_target.get_position()) < 8) {
					desire = 0;
					mate_target.desire = 0;
					if (baby == null && Utils._rand.nextDouble() < PREGNANT_PROBABILITY_WOLF)
						baby = new Wolf(this, mate_target);
					energy = Utils.constrain_value_in_range(energy - 10, 0, MAX_ENERGY);
					mate_target = null;
				}
				if (energy < FOOD_THRESHOLD_WOLF)
					set_state(State.HUNGER);
				else if (desire < DESIRE_THRESHOLD_WOLF)
					set_state(State.MATE);
			}
		}
	}

	@Override
	protected void update_state(double dt) {
		// TODO CORREGIR POSICION FUERA DEL MAPA
		if (energy == 0.0 || age > MAX_AGE_WOLF)
			set_state(State.DEAD);
		if (state != State.DEAD)
			Utils.constrain_value_in_range(energy + region_mngr.get_food(this, dt), 0, MAX_ENERGY);
	}

}
