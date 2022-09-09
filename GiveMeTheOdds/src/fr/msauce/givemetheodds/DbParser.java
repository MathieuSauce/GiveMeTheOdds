package fr.msauce.givemetheodds;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DbParser {

	private Connection connection = null;

	private ArrayList<Route> routes;

	public void connect(String pathToDb) {

		try {
			String url = "jdbc:sqlite:" + pathToDb;
			connection = DriverManager.getConnection(url);
			SelectAll();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void SelectAll() throws SQLException {
		String sqlRequest = "SELECT * FROM routes";
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(sqlRequest);

		routes = RoutesUtil.createRoutesFromResultSet(results);
	}

	public ArrayList<Route> getRoutes() {
		return routes;
	}

	public ArrayList<Route> parseRouteFromDb(String dbPath) {
		this.connect(dbPath);
		return routes;
	}

	public String computePath(String dbString, String falconString) {
		Path filePath = Path.of(dbString);
		if (filePath.isAbsolute()) {
			return dbString;
		}
		String falconPath = new File(falconString).getParent();
		return falconPath + File.separator + dbString;
	}

}
