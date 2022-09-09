package fr.msauce.givemetheodds;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

import org.json.JSONObject;


/*
 * Handles the api requests
 */

public class Api {

	private EmpireInfo empireInfo;
	private PathFinder pathfinder;

	public Api(PathFinder pathfinder) {
		this.pathfinder = pathfinder;
	}
	
	/*
	 * Generates all necessary API request for the front-end to use
	 * 
	 */

	public void generateApiRequests() {

		after((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "GET, POST");
		});

		post("/upload", (request, response) -> {
			empireInfo = JsonParser.parseEmpireFromJson(request.body());
			JSONObject json = new JSONObject();
			json.put("respondeCode", 200);
			return json.toString();
		});

		get("/probability", (request, response) -> {
			if (empireInfo == null) {
				System.err.println("empireInfoIsNull");
				JSONObject json = new JSONObject();
				json.put("reponseCode", -1);
				return json;
			}

			
			Itinerary itinerary = pathfinder.runPathFinder(empireInfo);

			JSONObject json = JsonParser.generateJsonFromItinerary(itinerary);

			return json;

		});

	}

}
