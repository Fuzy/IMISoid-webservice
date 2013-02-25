package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static data.Credentials.*;

/**
 * DAO pro přístup do databáze.
 * 
 * @author KDA
 * 
 */
public class EventDao {
	private static final String SQL_DELETE = "delete from karta_vie where rowid like ?";
	private static final String SQL_GET = "select * from KARTA_VIE where icp like ? "
			+ "and datum >=  ? and datum <=  ? ";

	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO sysexit
			e.printStackTrace();
		}

	}

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public static String createEvent(Event event) {
		String rowid = "";

		return rowid;
	}

	public static boolean deleteEvent(String rowid) {
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConnection();
			stmt = conn.prepareStatement(SQL_DELETE);
			stmt.setString(1, rowid);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 1) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static List<Event> getEvents(String icp, String dateFrom,
			String dateTo) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		List<Event> events = new ArrayList<Event>();

		try {
			conn = getConnection();

			stmt = conn.prepareStatement(SQL_GET);
			stmt.setString(1, icp);
			stmt.setString(2, dateFrom);
			stmt.setString(3, dateTo);
			rset = stmt.executeQuery();
			
			/*
			 "select * from KARTA_VIE where icp like '"
					+ icp + "' " + " and datum >= '" + dateFrom
					+ "' and datum <= '" + dateTo + "'"
			  */
			 

			while (rset.next()) {
				Event event = Event.resultSetToEvent(rset);
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn, stmt, rset);
		}

		for (Event event : events) {
			System.out.println(event);
		}
		return events;
	}

	private static void closeConnection(Connection conn, Statement stmt,
			ResultSet rset) throws SQLException {
		if (rset != null) {
			rset.close();
		}
		if (stmt != null) {
			stmt.close();
		}
		if (conn != null) {
			conn.close();
		}
	}
}
