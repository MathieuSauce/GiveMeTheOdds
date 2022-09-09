package fr.msauce.givemetheodds;

import java.util.ArrayList;


/*
 * Represents one itinerary and its associated methods
 * @field Int					Number of days it takes to get to the destination
 * @field ArrayList<String> 	List of visited planets during this itinerary
 * @field ArrayList<String>		Itinerary taken to get to the destination (contains refuel information)
 * @field double				Probability to get to the destination unharmed
 * @field int					Number of encounter with bounty hunters
 * @field String				Current planet the ship is on
 * @field Boolean				Boolean to make sure we don't compute the probability multiple times
 * 
 * @field EmpireInfo			Informations on the Empire (from the empire file)
 * @field FalconInfo			Informations on the Millenium Falcon (from the falcon file)
 * 
 */

public class Itinerary {
	private int day;
	private ArrayList<String> visitedPlanet;
	private ArrayList<String> itinerary;
	private double probability = 1;
	private int autonomy;
	private int numberOfEvents = 0;
	private String currentPlanet;
	private boolean computedProbability = false;
	
	private EmpireInfo empireInfo;
	private MilleniumFalconInfo falconInfo;

	public int getDay() {
		return day;
	}

	public ArrayList<String> getVisitedPlanet() {
		return visitedPlanet;
	}

	public double getProbability() {
		return this.probability;
	}
	
	public ArrayList<String> getItinerary(){
		return itinerary;
	}
	
	public void increaseNumberOfEvents() {
		this.numberOfEvents++;
	}
	
	/*
	 * Calculates the probability that the crew gets to the destination based on how many time they encountered bounty hunters.
	 * 
	 */

	public void calculateProbability() {
		if (computedProbability)
			return;
		for (int i = 1; i <= numberOfEvents; i++) {
			probability = probability - (Math.pow(9, i - 1) / Math.pow(10, i));
		}
		numberOfEvents = 0;
		probability = probability * 100;
		computedProbability = true;
	}
	
	/*
	 * Simulates the crew taking making an hyper jump from the current planet to a passed destination planet
	 * Handles refuel
	 * @param String 		Destination of the hyper jump
	 * @param int			Duration (in days) of the hyper jump
	 * 
	 */

	public int takeRoute(String destination, int travel_time) {
		
		
		day += travel_time;

		if (autonomy < travel_time) {
			day++;
			visitedPlanet.add(currentPlanet);
			autonomy = falconInfo.getAutonomy();

		}
		if (day > empireInfo.getCountdown()) {
			return -1;
		}

		autonomy -= travel_time;
		visitedPlanet.add(destination);
		
		currentPlanet = destination;
		if (currentPlanet.equals(falconInfo.getArrival())) {
			computeItinerary();
			return 1;
		}
		return 0;
	}


	/*
	 * Creates the itinerary based on the visited planet list (adds refuel and wait day)
	 */
	private void computeItinerary() {
		String lastPlanet = "";
		for(String planet : visitedPlanet ) {
			if(lastPlanet.equals(planet)) {
				itinerary.add("Refuel on " + planet);
			} else {
				itinerary.add(planet);
			}
			lastPlanet = planet;
		}
	}
	
	/*
	 * Wait for one day on the itinerary
	 */
	public void waitForOneDay() {
		day++;
		visitedPlanet.add(currentPlanet);
	}

	/*
	 * Creates a copy of this itinerary
	 * @return Itinerary Copy this itinerary
	 * 
	 */
	public Itinerary duplicate() {
		ArrayList<String> newVisitedPlanet = new ArrayList<String>();
		this.visitedPlanet.forEach(planet -> newVisitedPlanet.add(planet));
		return new Itinerary(day, newVisitedPlanet, probability, autonomy, numberOfEvents, currentPlanet, empireInfo, falconInfo);
	}

	public String getCurrentPlanet() {
		return this.currentPlanet;
	}

	public Itinerary(String origin, int autonomy, EmpireInfo empireInfo, MilleniumFalconInfo falconInfo) {
		this.day = 0;
		this.visitedPlanet = new ArrayList<String>();
		this.itinerary = new ArrayList<String>();
		this.autonomy = autonomy;
		this.currentPlanet = origin;
		this.visitedPlanet.add(origin);
		this.empireInfo = empireInfo;
		this.falconInfo = falconInfo;
	}

	private Itinerary(int day, ArrayList<String> visitedPlanet, double probability, int autonomy, int numberOfEvents,
			String currentPlanet, EmpireInfo empireInfo, MilleniumFalconInfo falconInfo) {
		this.day = day;
		this.visitedPlanet = visitedPlanet;
		this.itinerary = new ArrayList<String>();
		this.probability = probability;
		this.autonomy = autonomy;
		this.numberOfEvents = numberOfEvents;
		this.currentPlanet = currentPlanet;
		this.empireInfo = empireInfo;
		this.falconInfo = falconInfo;
	}

	public String toString() {
		return "Itinerary to " + currentPlanet + " arrives on day " + day + " taking this route " + visitedPlanet
				+ "with a probability of " + probability + "%\n";
	}

}
