package simulator.launcher;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import simulator.factories.*;
import simulator.model.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.misc.Utils;
import simulator.model.SelectionStrategy;

public class Main {

	private enum ExecMode {
		BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

		private String tag;
		private String desc;

		private ExecMode(String modeTag, String modeDesc) {
			tag = modeTag;
			desc = modeDesc;
		}

		public String get_tag() {
			return tag;
		}

		public String get_desc() {
			return desc;
		}
	}

	// default values for some parameters
	//
	private final static Double default_time = 10.0; // in seconds
	private final static Double default_delta_time = 0.03; // in seconds

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double time = null;
	private static Double delta_time = null; //Para almacenar los valores de los atributos para usarlos desde otros metodos
	private static String in_file = null;
	private static String out_file = null; // Tanto delta_time como out_file son añadidos porque hipotetizo que se extrapola al resto de valores, no solo a time y in_time
	private static Boolean sv = null;
	private static ExecMode mode = ExecMode.BATCH;
	private static Factory<SelectionStrategy> selection_strategy_factory;
	private static Factory<Animal> animal_factory;
	private static Factory<Region> regions_factory;

	private static void parse_args(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = build_options();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parse_help_option(line, cmdLineOptions);
			parse_in_file_option(line);
			parse_time_option(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options build_options() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// delta time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: " + default_delta_time + ".")
				.build());
		
		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("A configuration file.").build());
		
		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where output is written.").build());
		
		// simple-viewer
		cmdLineOptions.addOption(Option.builder("sv").longOpt("simple-viewer").desc("Show the viewer window in console mode.").build());
		
		// steps
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg()
				.desc("A real number representing the total simulation time in seconds. Default value: "
						+ default_time + ".")
				.build());

		return cmdLineOptions;
	}

	private static void parse_help_option(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parse_in_file_option(CommandLine line) throws ParseException {
		in_file = line.getOptionValue("i");
		if (mode == ExecMode.BATCH && in_file == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}

	private static void parse_time_option(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", default_time.toString());
		try {
			time = Double.parseDouble(t);
			assert (time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}

	private static void init_factories() {
		//TODO 
		// Completar el método init_factories para inicializar las factorías y almacenarlas en los atributos correspondientes.
		// Añadir regions factory
		
		List<Builder<SelectionStrategy>> selection_strategy_builders = new ArrayList<>();
		selection_strategy_builders.add(new SelectFirstBuilder());
		selection_strategy_builders.add(new SelectClosestBuilder());
		selection_strategy_builders.add(new SelectYoungestBuilder());
		selection_strategy_factory = new	BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);
		List<Builder<Animal>> animal_builders = new ArrayList<>();
		animal_builders.add(new SheepBuilder(selection_strategy_factory));
		animal_builders.add(new WolfBuilder(selection_strategy_factory));
		animal_factory = new BuilderBasedFactory<Animal>(animal_builders);
		
		

	}

	private static JSONObject load_JSON_file(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private static void start_batch_mode() throws Exception {
		//TODO
		//Completar segun campus
		
		//Carga el archivo de entrada en un JSONObject
		InputStream is = new FileInputStream(new File(in_file));
		JSONObject entrada = new JSONObject();
		int width, height, rows, cols;
		JSONArray animals, regions;
		//TODO 
		//lee los valores
		
		//SON PLACEHOLDERS, HAY QUE QUITARLOS
		width = height = rows = cols = 1; 
		
		entrada.put("width", width);
		entrada.put("height", height);
		entrada.put("rows", rows);
		entrada.put("cols", cols);
		entrada.put("animals", animals);
		entrada.put("regions", regions);
		
		
		//Crea el archivo de salida
		FileOutputStream os = new FileOutputStream(new File(out_file));
		
		//Crea una instancia del simulador
		Simulator sim = new Simulator(rows, cols, width, height, animal_factory, regions_factory);
	}

	private static void start_GUI_mode() throws Exception {
		throw new UnsupportedOperationException("GUI mode is not ready yet ...");
	}

	private static void start(String[] args) throws Exception {
		init_factories();
		parse_args(args);
		switch (mode) {
		case BATCH:
			start_batch_mode();
			break;
		case GUI:
			start_GUI_mode();
			break;
		}
	}

	public static void main(String[] args) {
		Utils._rand.setSeed(2147483647l);
		try {
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
