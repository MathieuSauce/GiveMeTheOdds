package fr.msauce.givemetheodds;

import java.util.ArrayList;

public class Itinerary {
	private int day;
	private ArrayList<String> visitedPlanet;
	private double probability = 1;
	private int autonomy;
	private int numberOfEvents = 0;
	private String currentPlanet;
	private boolean computedProbability = false;

	public int getDay() {
		return day;
	}

	public ArrayList<String> getVisitedPlanet() {
		return visitedPlanet;
	}

	public double getProbability() {
		return this.probability;
	}

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

	public int takeRoute(String destination, int travel_time, EmpireInfo empireInfo, MilleniumFalconInfo falconInfo) {

		day += travel_time;
		if (checkBountyHuntersOnThisLocation(empireInfo, destination)) {
			numberOfEvents++;
		}

		if (autonomy < travel_time) {
			day++;
			visitedPlanet.add("Refuel on " + currentPlanet);
			autonomy = falconInfo.getAutonomy();
			if (checkBountyHuntersOnThisLocation(empireInfo, currentPlanet)) {
				numberOfEvents++;
			}
		}
		if (day > empireInfo.getCountdown()) {
			return -1;
		}

		autonomy -= travel_time;
		visitedPlanet.add(destination);
		currentPlanet = destination;
		if (currentPlanet.equals(falconInfo.getArrival())) {
			return 1;
		}
		return 0;
	}

	private boolean checkBountyHuntersOnThisLocation(EmpireInfo empireInfo, String location) {
		if (empireInfo.getBountyHuntersMap().keySet().contains(location)) {
			if (empireInfo.getBountyHuntersMap().get(location).contains(day)) {
				return true;
			}
		}
		return false;
	}


	public Itinerary duplicate() {
		ArrayList<String> newVisitedPlanet = new ArrayList<String>();
		this.visitedPlanet.forEach(planet -> newVisitedPlanet.add(planet));
		return new Itinerary(day, newVisitedPlanet, probability, autonomy, numberOfEvents, currentPlanet);
	}

	public String getCurrentPlanet() {
		return this.currentPlanet;
	}

	public Itinerary(String origin, int autonomy) {
		this.day = 0;
		this.visitedPlanet = new ArrayList<String>();
		this.autonomy = autonomy;
		this.currentPlanet = origin;
		this.visitedPlanet.add(origin);
	}

	private Itinerary(int day, ArrayList<String> visitedPlanet, double probability, int autonomy, int numberOfEvents,
			String currentPlanet) {
		this.day = day;
		this.visitedPlanet = visitedPlanet;
		this.probability = probability;
		this.autonomy = autonomy;
		this.numberOfEvents = numberOfEvents;
		this.currentPlanet = currentPlanet;
	}

	public String toString() {
		return "Itinerary to " + currentPlanet + " arrives on day " + day + " taking this route " + visitedPlanet
				+ "with a probability of " + probability + "%\n";
	}

}
