package fr.msauce.givemetheodds;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {

		String falconPath = Main.class.getResource("/millennium-falcon.json.txt").getPath();


		MilleniumFalconInfo falconInfo = JsonParser.parseFalconInfoFromFile(falconPath);


		DbParser dbParser = new DbParser();
		dbParser.connect(dbParser.computePath(falconInfo.getDbPath(), falconPath));
		ArrayList<Route> routes = dbParser.getRoutes();

		PathFinder pathFinder = new PathFinder(falconInfo, routes);
		Api api = new Api(pathFinder);
		api.generateApiRequests();
	}
}
