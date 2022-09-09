package fr.msauce.givemetheodds;

import java.util.ArrayList;
import java.util.HashMap;

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

	public void generateRoutesGraph() {
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
	}

	public void addNode(Node node) {
		nodeMap.put(node.getName(), node);
	}

	public void calculatePaths() {
		Itinerary itinerary = new Itinerary(falconInfo.getDeparture(), falconInfo.getAutonomy());
		for (Node node : nodeMap.get(falconInfo.getDeparture()).getAdjacentNodes().keySet()) {
			calculatePath(itinerary.duplicate(), node,
					nodeMap.get(falconInfo.getDeparture()).getAdjacentNodes().get(node));

		}

		computeProbabilities();
	}

	private void calculatePath(Itinerary itinerary, Node currentNode, int distance) {

		if (itinerary.getVisitedPlanet().contains(currentNode.getName())) {
			return;
		}

		int routeResult = itinerary.takeRoute(currentNode.getName(), distance, empireInfo, falconInfo);

		if (itinerary.getCurrentPlanet().equals(falconInfo.getArrival())) {
			itineraries.add(itinerary);
			return;
		}
		if (routeResult == -1) {
			return;
		}
		for (Node node : currentNode.getAdjacentNodes().keySet()) {

			calculatePath(itinerary.duplicate(), node, currentNode.getAdjacentNodes().get(node));
		}

	}

	private void computeProbabilities() {
		this.itineraries.forEach((itinerary) -> itinerary.calculateProbability());
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


	public Itinerary getBestItinerary() {
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
