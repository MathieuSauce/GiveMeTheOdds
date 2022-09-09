package fr.msauce.givemetheodds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoutesUtil {
	
	/*
	 * Creates the list of route from the results of the database calls
	 * @param ResultSet 	Results of the database call
	 */

	public static ArrayList<Route> createRoutesFromResultSet(ResultSet resultSet) throws SQLException {
		ArrayList<Route> routes = new ArrayList<Route>();
		while (resultSet.next()) {

			routes.add(new Route(resultSet.getString("ORIGIN"), resultSet.getString("DESTINATION"),
					resultSet.getInt("TRAVEL_TIME")));
		}
		if (routes.isEmpty()) {
			System.err.println("Route is empty in routeUtil");
		}
		return routes;
	}

}
