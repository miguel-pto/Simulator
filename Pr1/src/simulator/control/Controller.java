package simulator.control;

import java.io.OutputStream;

import org.json.JSONObject;

import simulator.model.Simulator;

public class Controller {

	private Simulator sim;

	public Controller(Simulator sim) {
		this.sim = sim;
	}

	public void load_data(JSONObject data) {
		// TODO SABER Q COÑO ES UN JSON
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {
		while (t <= sim.get_time()) {
			sim.advance(dt);
		}
		// TODO EL OUTPUT Y EL VISOR DE OBJETOS
	}
}
