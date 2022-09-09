package fr.msauce.givemetheodds;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Handles the parsing of Json
 * 
 */
public class JsonParser {
	
	
	/*
	 * Parses Empire info json file into EmpireInfo class
	 * @param String 		Path to the Empire json File
	 * @return EmpireInfo	Empire information from the file
	 * 
	 */
	
	public static EmpireInfo parseEmpireFromFile(String filePath) {

		String json = "";

		try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
			String line;
			while((line = reader.readLine()) != null) {
				json += line;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return parseEmpireFromJson(json);

	}
	
	/*
	 * Parses Empire info from a json string
	 * @param String 		json string to parse
	 * @return EmpireInfo	Empire information from the string
	 * 
	 */
	
	
	public static EmpireInfo parseEmpireFromJson(String json) {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray bountyHunterArray = jsonObject.getJSONArray("bounty_hunters");
		int countdown = jsonObject.getInt("countdown");
		
		
		HashMap<String, ArrayList<Integer>> bountyHuntersMap = new HashMap<String, ArrayList<Integer>>();
		
		for(int i = 0; i < bountyHunterArray.length(); i++) {
			String planetName = bountyHunterArray.getJSONObject(i).getString("planet");
			int day = bountyHunterArray.getJSONObject(i).getInt("day");
			
			if(!bountyHuntersMap.keySet().contains(planetName)) {
				bountyHuntersMap.put(planetName, new ArrayList<Integer>());
			} 
			bountyHuntersMap.get(planetName).add(day);
		}
		return new EmpireInfo(bountyHuntersMap, countdown);
	}
	
	/*
	 * Parses MilleniumFalconInfo in the file pointed by given filePath
	 * @param String				Path to the file
	 * @return MilleniumFalconInfo	Informations in the file
	 */
	
	public static MilleniumFalconInfo parseFalconInfoFromFile(String filePath) {
		String json = "";
		
		try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
			String line;
			while((line = reader.readLine()) != null) {
				json+= line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject jsonObject = new JSONObject(json);
		int autonomy = jsonObject.getInt("autonomy");
		String departure = jsonObject.getString("departure");
		String arrival = jsonObject.getString("arrival");
		String dbPath = jsonObject.getString("routes_db");
		
		return new MilleniumFalconInfo(autonomy, departure, arrival, dbPath);
		
	}
	
	/*
	 * Compiles given itinerary into a JsonObject
	 * @param Itinerary 	Itinerary to compile
	 * @return JSONObject	JSONObject associated
	 */
	
	
	public static JSONObject generateJsonFromItinerary(Itinerary itinerary) {
		JSONObject jsonObject = new JSONObject()
				.put("probability", itinerary.getProbability())
				.put("day", itinerary.getDay())
				.put("itinerary", itinerary.getItinerary())
				.put("destination", itinerary.getCurrentPlanet());
		return jsonObject;
	}
	
	


}
