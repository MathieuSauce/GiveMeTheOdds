package fr.msauce.givemetheodds;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoutesUtil {

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
