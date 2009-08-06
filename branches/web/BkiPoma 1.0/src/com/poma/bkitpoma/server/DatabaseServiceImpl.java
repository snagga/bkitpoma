package com.poma.bkitpoma.server;

import java.sql.*;

import java.util.*;

import com.poma.bkitpoma.client.DatabaseService;
import com.poma.bkitpoma.client.Tracked;
import com.poma.bkitpoma.client.Tracker;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements
		DatabaseService {

	public static Connection connection = null;

	@Override
	public String executeQuery(String sqlQuery) {
		// TODO Auto-generated method stub
		String result = "";
		System.out.println("exeQuery");
		try {
			System.out.println("Server result = " + result);
			Statement Stmt = DatabaseServiceImpl.getConnection()
					.createStatement();
			Stmt.execute(sqlQuery);
			result = "done";
			Stmt.close();
			System.out.println("Server result = " + result);
			// connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			result = e.getMessage();
			// e.printStackTrace();
		}
		return result;
	}

	public static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				String url = "jdbc:jiql://local";
				String user = "admin";
				String pwd = "123456";
				// or
				// String url =
				// "jdbc:jiql:https://bkitpoma.appspot.com/bkitpoma/jiqlservlet?user=admin&password=123456"
				// ;

				Properties props = new Properties();
				props.put("user", user);
				props.put("password", pwd);

				Class clazz = Class.forName("org.jiql.jdbc.Driver");
				Driver driver = (Driver) clazz.newInstance();
				connection = driver.connect(url, props);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public ArrayList getTrackedInfoList(String trackerUsername) {
		return null;
		/*
		 * String query = "SELECT TRACKEDUN FROM MANAGE WHERE TRACKERUN = \'" +
		 * trackerUsername + "\'"; System.out.println(query);
		 * 
		 * ArrayList trackedList = new ArrayList();
		 * 
		 * // Generate the contact list for (int i = 0; i <
		 * CONTACT_NAMES.length; i++) { Contact contact = new Contact();
		 * contact.setName(CONTACT_NAMES[i]);
		 * contact.setEmail(CONTACT_EMAILS[i]);
		 * contact.setAddress(CONTACT_ADDRESSES[i]);
		 * contact.setPhoneNumber(CONTACT_PHONES[i]); contactList.add(contact);
		 * }
		 * 
		 * try { Statement stm = DatabaseServiceImpl.getConnection()
		 * .createStatement(); ResultSet rs = stm.executeQuery(query);
		 * 
		 * // extract data from the ResultSet while (rs.next()) { Tracked
		 * tracked = new Tracked(); int id = rs.getInt(1);
		 * System.out.println("id="+id); String name = rs.getString(2);
		 * System.out.println("name="+name); if (rs.wasNull()) {
		 * System.out.println("name is null"); } else {
		 * System.out.println("name is not null"); }
		 * System.out.println("---------------"); } rs.close(); st.close();
		 * 
		 * 
		 * result = "done"; stm.close(); // connection.close(); } catch
		 * (Exception e) { // TODO Auto-generated catch block result =
		 * e.getMessage(); // e.printStackTrace(); } return null;
		 */

	}

	@Override
	public String[] getTrackedList(String trackerUsername) {
		// TODO Auto-generated method stub
		String query = "SELECT TRACKEDUN FROM MANAGE WHERE TRACKERUN = \'"
				+ trackerUsername + "\'";
		System.out.println(query);

		ArrayList trackedList = new ArrayList();

		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);

			// extract data from the ResultSet
			while (rs.next()) {
				trackedList.add(rs.getString(1));
				System.out.println("id=" + rs.getString(1));
				if (rs.wasNull()) {
					System.out.println("name is null");
				} else {
					System.out.println("name is not null");
				}
				System.out.println("---------------");
			}
			rs.close();
			stm.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// e.printStackTrace();
		}
		return (String[]) trackedList.toArray();
	}

	@Override
	public Tracked getTrackedDetail(String trackedUN) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + trackedUN
				+ "\';";
		Tracked tracked = null;
		try {

			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			if (rs.getRow() > 1) {
				tracked = new Tracked();
				tracked.setUsername(rs.getString("USERNAME"));
				tracked.setAPIKey(rs.getString("APIKEY"));
				tracked.setName(rs.getString("NAME"));
				java.sql.Date tmpDate = rs.getDate("BIRTHDAY");
				String tmp = String.valueOf("" + tmpDate.getYear()
						+ tmpDate.getMonth() + tmpDate.getDay());
				tracked.setBirthday(tmp);
				tracked.setTel(rs.getString("TEL"));
				tracked.setEmail(rs.getString("EMAIL"));
				tracked.setState(rs.getString("STATE"));
				tracked.setGpsState(rs.getString("GPSSTATE"));
				tracked.setGMT(rs.getString("GMT"));
				tracked.setLang(rs.getString("LANG"));
				tracked.setCountry(rs.getString("COUNTTRY"));
				tracked.setIconPath(rs.getString("ICONPATH"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tracked;
	}

	@Override
	public Tracker getTrackerDetail(String trackerUN) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKER WHERE USERNAME = \'" + trackerUN
				+ "\';";
		System.out.println(query);
		Tracker tracker = null;
		try {
			// System.out.println(
			// "INSERT INTO TRACKER  ( USERNAME , PASSWORD , NAME , BIRTHDAY , TEL , ADDR , EMAIL , TYPECUS , STATETRACKER , GMT , LANG , COUNTRY ) VALUES  ( \'TAM2\' , \'TAM\' , \'TAM\' , CURRENT_DATE , \'0\' , \'0\' , \'0\' , DEFAULT , DEFAULT , DEFAULT , DEFAULT , DEFAULT )  "
			// );
			// String tmp = executeQuery(
			// "INSERT INTO TRACKER  ( USERNAME , PASSWORD , NAME , BIRTHDAY , TEL , ADDR , EMAIL , TYPECUS , STATETRACKER , GMT , LANG , COUNTRY ) VALUES  ( \'TAM2\' , \'TAM\' , \'TAM\' , CURRENT_DATE , \'0\' , \'0\' , \'0\' , DEFAULT , DEFAULT , DEFAULT , DEFAULT , DEFAULT )  "
			// );
			// System.out.println(tmp);
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			System.out.println(rs.getRow());
			if (rs.getRow() >= 1) {
				rs.first();
				tracker = new Tracker();
				tracker.setUsername(rs.getString("USERNAME"));
				tracker.setPassword(rs.getString("PASSWORD"));
				tracker.setName(rs.getString("NAME"));
				java.sql.Date tmpDate = rs.getDate("BIRTHDAY");
				String tmp = String.valueOf("" + tmpDate.getYear()
						+ tmpDate.getMonth() + tmpDate.getDay());
				tracker.setBirthday(tmp);
				tracker.setTel(rs.getString("TEL"));
				tracker.setAddr(rs.getString("ADDR"));
				tracker.setEmail(rs.getString("EMAIL"));
				tracker.setTypeCus(rs.getString("TYPECUS"));
				tracker.setState(rs.getString("STATETRACKER"));
				tracker.setGMT(rs.getString("GMT"));
				tracker.setLang(rs.getString("LANG"));
				tracker.setCountry(rs.getString("COUNTRY"));
				System.out.println("Server: " + tracker.toString());
			} else {
				System.out.println("No Row Selected");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tracker;
	}

	@Override
	/*
	 * @return: 0 - Success 1 - Duplicate key 2 - Other error
	 */
	public int insertTracker(Tracker tracker) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKER WHERE USERNAME = \'"
				+ tracker.getUsername() + "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return 1;
			// insert

			query = "INSERT INTO TRACKER  ( USERNAME , PASSWORD , NAME , BIRTHDAY , TEL , ADDR , EMAIL , TYPECUS , STATETRACKER , GMT , LANG , COUNTRY ) VALUES  "
					+ "( '"
					+ tracker.getUsername()
					+ "' , '"
					+ tracker.getPassword()
					+ "' , '"
					+ tracker.getName()
					+ "' , '"
					+ tracker.getBirthday()
					+ "' , '"
					+ tracker.getTel()
					+ "' , '"
					+ tracker.getAddr()
					+ "' , '"
					+ tracker.getEmail()
					+ "' , "
					+ tracker.getTypeCus()
					+ " , "
					+ tracker.getState()
					+ " , "
					+ tracker.getGMT()
					+ " , '"
					+ tracker.getLang()
					+ "' , '"
					+ tracker.getCountry() + "' )";
			stm.execute(query);

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}

	}

	@Override
	/*
	 * @return: 0 - Success 1 - Duplicate key 2 - Other error
	 */
	public int insertTracked(Tracked tracked) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'"
				+ tracked.getUsername() + "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return 1;
			// insert

			// INSERT INTO TRACKED ( USERNAME , APIKEY , NAME , BIRTHDAY , TEL ,
			// EMAIL , STATETRACKED , GPSSTATE , GMT , LANG , COUNTRY , ICONPATH
			// , SHOWINMAP , EMBEDDED ) VALUES ( 'TAM2' , 'TAM' , 'TAM' ,
			// '20080202' , '0' , '0' , 0 , 0 , 7 , 'VI' , 'VIETNAM' , '0' ,
			// TRUE , TRUE )

			query = "INSERT INTO TRACKED  ( USERNAME , APIKEY , NAME , BIRTHDAY , TEL , EMAIL , STATETRACKED , GPSSTATE , GMT , LANG , COUNTRY , ICONPATH , SHOWINMAP , EMBEDDED ) VALUES  "
					+ "( '"
					+ tracked.getUsername()
					+ "' , '"
					+ tracked.getAPIKey()
					+ "' , '"
					+ tracked.getName()
					+ "' , '"
					+ tracked.getBirthday()
					+ "' , '"
					+ tracked.getTel()
					+ "' , '"
					+ tracked.getEmail()
					+ "' , "
					+ tracked.getState()
					+ " , "
					+ tracked.getGpsState()
					+ " , "
					+ tracked.getGMT()
					+ " , '"
					+ tracked.getLang()
					+ "' , '"
					+ tracked.getCountry()
					+ "' , '"
					+ tracked.getIconPath()
					+ "' , "
					+ tracked.getShowInMap()
					+ " , " + tracked.getEmbedded() + " )";

			System.out.println(query);
			stm.execute(query);

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}

	}

	@Override
	public boolean verifyTracker(String us) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + us + "\';";
		boolean result = false;
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1) {
				result = false;
			} else
				result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	/*
	 * 0 - Success -1 - Key not exist 2 - Other errors
	 */
	public int setEmbedded(String trackedUN, boolean b) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + trackedUN
				+ "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return -1;

			query = "UPDATE TRACKED SET EMBEDDED = " + b
					+ "WHERE USERNAME = \'" + trackedUN + "\'";
			System.out.println(query);
			stm.execute(query);

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@Override
	public int setShowInMap(String trackedUN, boolean b) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + trackedUN
				+ "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return -1;

			query = "UPDATE TRACKED SET SHOWINMAP = " + b
					+ "WHERE USERNAME = \'" + trackedUN + "\'";
			System.out.println(query);
			stm.execute(query);

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}
}
