package fr.msauce.givemetheodds;

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
