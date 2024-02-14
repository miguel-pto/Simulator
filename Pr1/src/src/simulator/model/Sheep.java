package src.simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Sheep extends Animal {

	public final static String SHEEP_GENETIC_CODE = "Sheep";
	public final static double INIT_SIGHT_SHEEP = 40, INIT_SPEED_SHEEP = 35, BOOST_FACTOR_SHEEP = 2.0,
			MAX_AGE_SHEEP = 8, FOOD_DROP_BOOST_FACTOR_SHEEP = 1.2, FOOD_DROP_RATE_SHEEP = 20.0,
			DESIRE_THRESHOLD_SHEEP = 65.0, DESIRE_INCREASE_RATE_SHEEP = 40.0, PREGNANT_PROBABILITY_SHEEP = 0.9;

	Animal danger_source;
	SelectionStrategy danger_strategy;

	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
		super("Sheep", Diet.HERVIBORE, INIT_SIGHT_SHEEP, INIT_SPEED_SHEEP, mate_strategy, pos);
		this.danger_strategy = danger_strategy;
	}

	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		danger_strategy = p1.danger_strategy;
	}

	protected void advance_normal(double dt) {
		if (pos.distanceTo(dest) < COLLISION_RANGE)
			dest = new Vector2D(Utils._rand.nextDouble(800), Utils._rand.nextDouble(600));
		move(speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_SHEEP * dt, 0, MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_SHEEP * dt, 0, MAX_DESIRE);
	}

	private void flee(double dt) {
		pos.plus(pos.minus(danger_source.get_position()).direction());
		move(2.0 * speed * dt * Math.exp((energy - MAX_ENERGY) * HUNGER_DECAY_EXP_FACTOR));
		age += dt;
		energy = Utils.constrain_value_in_range(energy - FOOD_DROP_RATE_SHEEP * dt * FOOD_DROP_BOOST_FACTOR_SHEEP, 0,
				MAX_ENERGY);
		desire = Utils.constrain_value_in_range(desire + DESIRE_INCREASE_RATE_SHEEP * dt, 0, MAX_DESIRE);
	}

	private void mate() {
		desire = 0.0;
		mate_target.desire = 0.0;
		if (baby == null && Utils._rand.nextDouble() < PREGNANT_PROBABILITY_SHEEP)
			;
		baby = new Sheep(this, mate_target);
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
			;// TODO BUSCAR PELIGRO
		if (danger_source == null) {
			if (desire > DESIRE_THRESHOLD_SHEEP) {
				set_state(State.MATE);
			}
		} else {
			set_state(State.DANGER);
		}
	}

	@Override
	protected void update_hunger(double dt) {
	}

	@Override
	protected void update_danger(double dt) {
		if (danger_source != null && danger_source.get_state() == State.DEAD)
			danger_source = null;
		if (danger_source == null) {
			advance_normal(dt);
		} else {
			flee(dt);
		}

		if (danger_source == null && pos.distanceTo(danger_source.get_position()) > sight_range) { // TODO COMPROBAR
																									// NO GITANADA
			// TODO BUSCAR PELIGRO
			if (danger_source == null)
				if (desire > DESIRE_THRESHOLD_SHEEP) {
					set_state(State.MATE);
				} else {
					set_state(State.NORMAL);
				}
		}
	}

	@Override
	protected void update_mate(double dt) {
		if (mate_target != null && mate_target.get_state() == State.DEAD)
			mate_target = null;
		if (mate_target == null) {
			// TODO BUSCAR PAREJA
			if (mate_target == null) {
				advance_normal(dt);
				if (pos.distanceTo(mate_target.get_position()) < COLLISION_RANGE) {
					mate();
				}
				if (danger_source == null)
					; // TODO BUSCAR PELIGRO
				if (danger_source != null) {
					set_state(State.DANGER);
				} else if (desire < DESIRE_THRESHOLD_SHEEP) {
					set_state(State.NORMAL);
				}
			}
		}
	}

	@Override
	protected void update_state(double dt) {
		// TODO CORREGIR SI SE SALE DEL MAPA
		if (energy == 0.0 || age > MAX_AGE_SHEEP)
			set_state(State.DEAD);
		if (state != State.DEAD) // TODO MIRAR
			energy = Utils.constrain_value_in_range(energy + region_mngr.get_food(this, dt), 0, MAX_ENERGY);
	}

}
