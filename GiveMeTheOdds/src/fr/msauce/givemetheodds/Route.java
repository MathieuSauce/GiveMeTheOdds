package fr.msauce.givemetheodds;

/*
 * Represent the route from two planets
 * @field Int		Duration of the hyper jump between the two planets
 * @field String	Origin of the hyper jump
 * @field String	Destination of the hyper jump
 */

public class Route {
	private int travel_time;
	private String origin;
	private String destination;

	public Route(String origin, String destination, int travel_time) {
		this.origin = origin;
		this.destination = destination;
		this.travel_time = travel_time;
	}

	public int getTravel_time() {
		return travel_time;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestination() {
		return destination;
	}

}
