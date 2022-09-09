package fr.msauce.givemetheodds;

/*
 * Represents the information of the Millenium Falcon
 * @field int 		Autonomy of the ship
 * @field String	Departure planet 
 * @field String	Arrival planet
 * @field String	Path to the database (absolute or relative to the falcon info path)
 * 
 */

public class MilleniumFalconInfo {
	private int autonomy;
	private String departure;
	private String arrival;
	private String dbPath;

	public MilleniumFalconInfo(int autonomy, String departure, String arrival, String dbPath) {

		this.autonomy = autonomy;
		this.departure = departure;
		this.arrival = arrival;
		this.dbPath = dbPath;
	}

	@Override
	public String toString() {
		return "MilleniumFalconInfo [autonomy=" + autonomy + ", departure=" + departure + ", arrival=" + arrival
				+ ", dbPath=" + dbPath + "]";
	}

	public String getDeparture() {
		return this.departure;
	}

	public int getAutonomy() {
		return this.autonomy;
	}

	public String getArrival() {
		return this.arrival;
	}

	public String getDbPath() {
		return dbPath;
	}

}
