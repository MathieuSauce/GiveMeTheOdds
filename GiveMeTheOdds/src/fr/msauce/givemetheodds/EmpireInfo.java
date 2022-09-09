package fr.msauce.givemetheodds;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Represents the information on the empire
 * @field HashMap<String, ArrayList<Integer>> 	Map of the bounty hunters position
 * @field int 									Countdown till the death star destroys endor
 */


public class EmpireInfo {
	private HashMap<String, ArrayList<Integer>> bountyHuntersMap;
	private int countdown;

	public EmpireInfo(HashMap<String, ArrayList<Integer>> bountyHuntersMap, int countdown) {
		this.bountyHuntersMap = bountyHuntersMap;
		this.countdown = countdown;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Endor is going to be destroyed in ").append(countdown).append(" days \n");
		for (String key : bountyHuntersMap.keySet()) {
			sb.append("Bounty Hunters are present on planet ").append(key).append(" on day : ");
			for (int day : bountyHuntersMap.get(key)) {
				sb.append(day).append(" ");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	public int getCountdown() {
		return this.countdown;
	}

	public HashMap<String, ArrayList<Integer>> getBountyHuntersMap() {
		return bountyHuntersMap;
	}
}
