package fr.msauce.givemetheodds;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


/*
 * Class used to compute the best path to the destination
 * 
 * @field HashMap<String, Node> 		Map representing the route graph
 * @field ArrayList<Itinerary>			Itineraries that get to the destination
 * 
 * @field EmpireInfo					Informations on the empire (from the empire json file)
 * @field MilleniumFalconInfo			Informations on the millenium falcon (from the millenium falcon file)
 * @field ArrayList<Route>				List of the routes the ship can take (from the universe database)
 * 
 */

public class PathFinder {

	private HashMap<String, Node> nodeMap = new HashMap<String, Node>();
	private ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();

	private EmpireInfo empireInfo;
	private MilleniumFalconInfo falconInfo;
	private ArrayList<Route> routes;

	public PathFinder(EmpireInfo empireInfo, MilleniumFalconInfo falconInfo, ArrayList<Route> routes) {
		this.empireInfo = empireInfo;
		this.falconInfo = falconInfo;
		this.routes = routes;
	}
	
	public PathFinder(MilleniumFalconInfo falconInfo, ArrayList<Route> routes) {
		this.falconInfo = falconInfo;
		this.routes = routes;
	}

	public void setEmpireInfo(EmpireInfo empireInfo) {
		this.empireInfo = empireInfo;
	}
	
	/*
	 * Main pathfinder method, handles the whole path calculation
	 * @param EmpireInfo Informations of the empire (from the empire file)
	 */
	
	public Itinerary runPathFinder(EmpireInfo empireInfo) {
		this.empireInfo = empireInfo;
		return runPathFinder();
	}
	
	/*
	 * Main pathfinder method, handles the whole path calculation
	 * 
	 */
	
	public Itinerary runPathFinder() {
		emptyPathFinder();
		generateRoutesGraph();
		calculatePaths();
		return getBestItinerary();
	}
	
	/*
	 * Empties the graph, as well as the itineraries, in order to make a fresh call to the pathfinder's methods
	 */
	
	private void emptyPathFinder() {
		nodeMap.clear();
		itineraries.clear();
	}

	/*
	 * Generates the node graph based on the routes
	 * 
	 */
	
	private void generateRoutesGraph() {
		if (routes.isEmpty()) {
			throw new IllegalArgumentException("Routes can't be empty");
		}
		routes.forEach(route -> {
			if (!nodeMap.containsKey(route.getOrigin())) {
				addNode(new Node(route.getOrigin()));
			}
			if (!nodeMap.containsKey(route.getDestination())) {
				addNode(new Node(route.getDestination()));
			}
			nodeMap.get(route.getOrigin()).addDestination(nodeMap.get(route.getDestination()), route.getTravel_time());
			nodeMap.get(route.getDestination()).addDestination(nodeMap.get(route.getOrigin()), route.getTravel_time());

		});
		nodeMap.keySet().forEach((planet) -> {
			nodeMap.get(planet).addDestination(nodeMap.get(planet), 1);
		});

	}

	private void addNode(Node node) {
		nodeMap.put(node.getName(), node);
	}
	
	/*
	 * Handles the argument for the recursive calculation of each path in the graph from departure node (planet) to arrival node (planet)
	 * 
	 */

	private void calculatePaths() {
		Itinerary itinerary = new Itinerary(falconInfo.getDeparture(), falconInfo.getAutonomy(), empireInfo, falconInfo);
		for (Node node : nodeMap.get(falconInfo.getDeparture()).getAdjacentNodes().keySet()) {
			calculatePathsRecursive(itinerary.duplicate(), node,
					nodeMap.get(falconInfo.getDeparture()).getAdjacentNodes().get(node));

		}
		int itinerariesSize = itineraries.size();
		for(int i = 0; i < itinerariesSize; i++) {
			tryToDodgeBountyHunters(itineraries.get(i));
		}
		computeProbabilities();

	}
	
	/*
	 * Recursive part of the function used to calculate each possible path to destination
	 * @param Itinerary 	Current itinerary (or if called by the calculatePaths function Base itinerary)
	 * @param Node			Destination of the current travel
	 * @param int			Distance of the destination from the current node
	 * 
	 */

	private void calculatePathsRecursive(Itinerary itinerary, Node destinationNode, int distance) {

		if (itinerary.getVisitedPlanet().contains(destinationNode.getName())) {
			return;
		}

		int routeResult = itinerary.takeRoute(destinationNode.getName(), distance);

		if (itinerary.getCurrentPlanet().equals(falconInfo.getArrival())) {
			itineraries.add(itinerary);
			return;
		}
		if (routeResult == -1) {
			return;
		}
		for (Node node : destinationNode.getAdjacentNodes().keySet()) {

			calculatePathsRecursive(itinerary.duplicate(), node,  destinationNode.getAdjacentNodes().get(node));
		}

	}
	
	/*
	 * Runs the probability calculation on each itinerary in the itineraries list
	 * 
	 */

	private void computeProbabilities() {
		this.itineraries.forEach((itinerary) -> {
			checkBountyHuntersOnItinerary(itinerary);
			itinerary.calculateProbability();
		});
	}
	
	/*
	 * Counts the number of bounty hunter encounter on a given itinerary
	 * @param Itinerary 	Itinerary to test
	 */
	
	private void checkBountyHuntersOnItinerary(Itinerary itinerary) {
		int day = 0;
		
		for(int i = 0; i < itinerary.getVisitedPlanet().size() - 1; i++) {
			Node currentPlanet = nodeMap.get(itinerary.getVisitedPlanet().get(i));
			Node nextPlanet = nodeMap.get(itinerary.getVisitedPlanet().get(i+1));
			day = day + currentPlanet.getAdjacentNodes().get(nextPlanet);
			if(checkBountyHuntersOnThisLocation(empireInfo, nextPlanet.getName(), day)) {
				itinerary.increaseNumberOfEvents();
			}
		}
	}
	
	/*
	 * Checks if BountyHunters are present on a given planet at a given day
	 * @param EmpireInfo 	Informations on the Empire (given by the empire file)
	 * @param String 		Planet to check
	 * @param int			Day to check
	 * 
	 */
	
	private boolean checkBountyHuntersOnThisLocation(EmpireInfo empireInfo, String planet, int day) {
		if (empireInfo.getBountyHuntersMap().keySet().contains(planet)) {
			if (empireInfo.getBountyHuntersMap().get(planet).contains(day)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		nodeMap.values().forEach(node -> {
			sb.append(node.toString() + "\n");
		});

		return sb.toString();
	}

	public ArrayList<Itinerary> getItineraries() {
		return this.itineraries;
	}
	
	
	/*
	 * Used to optimize given itinerary.
	 * Handles the arguments for the recursive function used to dodge bounty hunters. 
	 * @param Itinerary Itinerary to optimize
	 * 
	 */
	
	private void tryToDodgeBountyHunters(Itinerary itinerary) {
		Queue<String> planetQueue = new LinkedList<String>();
		
		int remainingDaysOnItinerary = empireInfo.getCountdown() - itinerary.getDay();
		System.out.println("Itinerary : " + itinerary.getItinerary());
		for(String planet : itinerary.getVisitedPlanet()) {
			planetQueue.add(planet);
		}
		Itinerary newItinerary = new Itinerary(falconInfo.getDeparture(), falconInfo.getAutonomy(), empireInfo, falconInfo);
		dodgeBountyHuntersRecursive(newItinerary, remainingDaysOnItinerary, planetQueue );
	}
	
	/*
	 * Recursive function that is used to compute all possible itineraries from a given one by adding as many waiting day on a planet as possible
	 * @param Itinerary 	Base itinerary
	 * @param int			Days we can spare on the base itinerary (IE if the empire takes 10 day to destroy endor, and the itinerary takes 9 to reach it, then you can spare one day)
	 * @param Queue<String>	FIFO Queue of the planets in the itinerary
	 * 
	 */
	
	private void dodgeBountyHuntersRecursive(Itinerary itinerary, int remainingDaysOnItinerary, Queue<String> planetQueue) {
		if(planetQueue.isEmpty()){
			return;
		}
		String currentPlanet;
		currentPlanet = planetQueue.poll();
		
		if(planetQueue.isEmpty()){
			return;
		}

		int distanceToNextPlanet = nodeMap.get(currentPlanet).getAdjacentNodes().get(nodeMap.get(planetQueue.peek()));
		for(int i = 0; i < remainingDaysOnItinerary; i++) {
			itinerary.waitForOneDay();
			calculatePathsRecursive(itinerary.duplicate(), nodeMap.get(planetQueue.peek()), distanceToNextPlanet);
			
		}
		itinerary.takeRoute(planetQueue.peek(),distanceToNextPlanet);
		dodgeBountyHuntersRecursive(itinerary.duplicate(), remainingDaysOnItinerary, duplicateQueue(planetQueue));
		
	}
	
	/*
	 * Creates a copy of a given queue
	 * @param Queue<String> Queue to copy
	 * @return Queue<String> Copy of the queue
	 */
	
	private Queue<String> duplicateQueue(Queue<String> queue){
		Queue<String> newQueue = new LinkedList<String>();
		Iterator<String> iterator = queue.iterator();
		while(iterator.hasNext()) {
			newQueue.add(iterator.next());
		}
		return newQueue;
	}

	
	/*
	 * Goes through the itinerary list and finds the best itinerary
	 * The conditions to be the best itinerary is to have the best probability, and then the least travel time
	 * @return Itinerary Best itinerary
	 *  
	 */

	private Itinerary getBestItinerary() {
		for(Itinerary it : itineraries) {
			System.out.println(it);
		}
		if (itineraries.isEmpty()) {
			return null;
		}
		Itinerary bestItinerary = itineraries.get(0);
		for (Itinerary itinerary : itineraries) {
			if (itinerary.getProbability() > bestItinerary.getProbability()) {
				
				bestItinerary = itinerary;
				break;
			}
			if (itinerary.getProbability() == bestItinerary.getProbability()
					&& itinerary.getDay() < bestItinerary.getDay()) {

				bestItinerary = itinerary;
			}

		}
		return bestItinerary;
	}

	
	/*
	 * Represents a node on the graph
	 * @field String					Name of the planet
	 * @field HashMap<Node,Integer>		Adjacent nodes
	 */
	private class Node {
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Node named ").append(this.name).append(" with adjacent nodes : ");
			this.adjacentNodes.keySet().forEach((node) -> sb.append(node.getName()).append(" "));
			return sb.toString();
		}

		private String name;
		HashMap<Node, Integer> adjacentNodes = new HashMap<>();

		public void addDestination(Node destination, int distance) {
			adjacentNodes.put(destination, distance);
		}

		public String getName() {
			return this.name;
		}

		public Node(String name) {
			this.name = name;
		}

		public HashMap<Node, Integer> getAdjacentNodes() {
			return this.adjacentNodes;
		}

	}

}
