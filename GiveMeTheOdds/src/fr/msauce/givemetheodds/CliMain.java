package fr.msauce.givemetheodds;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

/*
 * Main class for the CLI
 */
public class CliMain {
	
	/*
	 * Runnable for the CLI
	 * @param String 	Path to the falcon info json file
	 * @param String	Path to the empire info json file
	 */

	private static void runCliProgram(String falconInfoPath, String empireInfoPath) {
		EmpireInfo empireInfo = JsonParser.parseEmpireFromFile(empireInfoPath);
		MilleniumFalconInfo falconInfo = JsonParser.parseFalconInfoFromFile(falconInfoPath);
		DbParser dbParser = new DbParser();
		dbParser.connect(dbParser.computePath(falconInfo.getDbPath(), falconInfoPath));
		ArrayList<Route> routes = dbParser.getRoutes();
		PathFinder pathFinder = new PathFinder(empireInfo, falconInfo, routes);
		System.out.println(pathFinder.runPathFinder().getProbability());
	}

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Usage : ");
			System.out.println(
					"file1 file2 with file1 being the path to Millenium-falcon.json and file2 the path to empire.json (absolute or relative)");
			return;
		}

		if (args.length == 2) {
			String falconInfoPath = args[0];
			String empireInfoPath = args[1];
			String cwd = System.getProperty("user.dir");
			if (!(Path.of(falconInfoPath)).isAbsolute()) {
				falconInfoPath = cwd + File.separator + falconInfoPath;
			}

			if (!(Path.of(empireInfoPath)).isAbsolute()) {
				empireInfoPath = cwd + File.separator + empireInfoPath;
			}

			if (!((new File(falconInfoPath)).exists()) || !((new File(empireInfoPath)).exists())) {
				System.out.println("Please specify correct filePath to json info files as argument");
				return;
			}
			runCliProgram(falconInfoPath, empireInfoPath);

		}

	}

}
