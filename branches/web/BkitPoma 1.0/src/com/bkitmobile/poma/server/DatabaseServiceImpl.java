package com.bkitmobile.poma.server;

import java.io.File;
import java.sql.*;
import com.twmacinta.util.*;

import java.util.*;
import org.owasp.validator.html.*;

import com.anotherbigidea.flash.interfaces.SWFActionBlock.TryCatchFinally;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.Tracked;
import com.bkitmobile.poma.client.database.Tracker;
import com.bkitmobile.poma.client.database.WayPoint;
import com.google.appengine.repackaged.com.google.common.base.Tracer.Stat;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements
		DatabaseService {

	public static Connection connection = null;
	private Policy policy = null;

	@Override
	public String executeQuery(String sqlQuery) {
		// TODO Auto-generated method stub
		sqlQuery = preventSQLInj(sqlQuery);
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
		trackerUsername = preventSQLInj(trackerUsername);
		String query = "SELECT TRACKEDUN FROM MANAGE WHERE TRACKERUN = \'"
				+ trackerUsername + "\'";
		System.out.println(query);

		// ArrayList trackedList = new ArrayList();
		String[] result = null;

		try {
			int numRow;
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			numRow = rs.getRow();
			result = new String[numRow];
			rs.first();
			// extract data from the ResultSet
			for (int i = 0; i < numRow; i++) {
				result[i] = rs.getString("TRACKEDUN");
				System.out.println("Server=" + rs.getString("TRACKEDUN"));
			}
			// while (rs.next()) {
			// // trackedList.add(rs.getString(1));
			//				
			// }
			rs.close();
			stm.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// e.printStackTrace();
		}
		return result;
	}

	@Override
	public Tracked getTrackedDetail(String trackedUN) {
		trackedUN = preventSQLInj(trackedUN);
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + trackedUN
				+ "\';";
		Tracked tracked = null;
		try {

			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			ResultSet rs2;
			if (rs.getRow() >= 1) {
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
				tracked.setSchedule(String.valueOf(rs.getObject("SCHEDULE")));
				tracked.setIntervalGPS(String.valueOf(rs
						.getObject("INTERVALGPS")));

				query = "SELECT * FROM STAFF WHERE TRACKEDUN = \'"
						+ rs.getString("USERNAME") + "\'";
				rs2 = stm.executeQuery(query);
				rs2.next();
				tracked.setOwnerUN(String.valueOf(rs2.getObject("TRACKERUN")));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tracked;
	}

	@Override
	public Tracker getTrackerDetail(String trackerUN) {
		// TODO Auto-generated method stub
		trackerUN = preventSQLInj(trackerUN);
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
	public Integer insertTracker(Tracker tracker) {
		tracker.setAddr(preventSQLInj(tracker.getAddr()));
		tracker.setBirthday(preventSQLInj(tracker.getBirthday()));
		tracker.setCountry(preventSQLInj(tracker.getCountry()));
		tracker.setEmail(preventSQLInj(tracker.getEmail()));
		tracker.setGMT(preventSQLInj(tracker.getGMT()));
		tracker.setLang(preventSQLInj(tracker.getLang()));
		tracker.setName(preventSQLInj(tracker.getName()));
		tracker.setPassword(preventSQLInj(tracker.getPassword()));
		tracker.setState(preventSQLInj(tracker.getState()));
		tracker.setTel(preventSQLInj(tracker.getTel()));
		tracker.setTypeCus(preventSQLInj(tracker.getTypeCus()));
		tracker.setUsername(preventSQLInj(tracker.getUsername()));

		// TODO Auto-generated method stub
		System.out.println("Insert Tracker server");
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

			String hashPass = MD5.asHex(tracker.getPassword().getBytes());
			System.out.println("md5 = " + hashPass);

			// en-MD5
			// MD5 md5 = new MD5();
			// md5.Update(myString, null);
			// String hash = md5.asHex();

			System.out.println("tracker.getTypeCus() " + tracker.getTypeCus());
			;
			query = "INSERT INTO TRACKER  ( USERNAME , PASSWORD , NAME , BIRTHDAY , TEL , ADDR , EMAIL , TYPECUS , STATETRACKER , GMT , LANG , COUNTRY ) VALUES  "
					+ "( '"
					+ tracker.getUsername()
					+ "' , '"
					+ hashPass
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
					+ Integer.parseInt(tracker.getTypeCus())
					+ " , "
					+ Integer.parseInt(tracker.getState())
					+ " , "
					+ tracker.getGMT()
					+ " , '"
					+ tracker.getLang()
					+ "' , '"
					+ tracker.getCountry() + "' )";
			System.out.println(query);
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
	public String insertTracked(String trackerUN, Tracked tracked) {
		// TODO Auto-generated method stub
		trackerUN = preventSQLInj(trackerUN);

		tracked.setBirthday(preventSQLInj(tracked.getBirthday()));
		tracked.setCountry(preventSQLInj(tracked.getCountry()));
		tracked.setEmail(preventSQLInj(tracked.getEmail()));
		tracked.setGMT(preventSQLInj(tracked.getGMT()));
		tracked.setLang(preventSQLInj(tracked.getLang()));
		tracked.setName(preventSQLInj(tracked.getName()));
		tracked.setState(preventSQLInj(tracked.getState()));
		tracked.setTel(preventSQLInj(tracked.getTel()));
		tracked.setUsername(preventSQLInj(tracked.getUsername()));
		tracked.setIconPath(preventSQLInj(tracked.getIconPath()));
		tracked.setOwnerUN(preventSQLInj(tracked.getOwnerUN()));

		// String query = "SELECT * FROM TRACKED WHERE USERNAME = \'"
		// + tracked.getUsername() + "\';";
		String query = "";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			int length = getLength(stm, "TRACKED");
			// ResultSet rs = stm.executeQuery(query);
			// rs.last();
			// if (rs.getRow() >= 1)
			// return "";
			// insert

			// INSERT INTO TRACKED ( USERNAME , APIKEY , NAME , BIRTHDAY , TEL ,
			// EMAIL , STATETRACKED , GPSSTATE , GMT , LANG , COUNTRY , ICONPATH
			// , SHOWINMAP , EMBEDDED ) VALUES ( 'TAM2' , 'TAM' , 'TAM' ,
			// '20080202' , '0' , '0' , 0 , 0 , 7 , 'VI' , 'VIETNAM' , '0' ,
			// TRUE , TRUE )

			query = "INSERT INTO TRACKED  ( USERNAME , APIKEY , NAME , BIRTHDAY , TEL , EMAIL , STATETRACKED , GPSSTATE , GMT , LANG , COUNTRY , ICONPATH , SHOWINMAP , EMBEDDED , SCHEDULE , INTERVALGPS , OWNERUN ) VALUES  "
					+ "( '"
					+ (length + 1)
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
					+ " , "
					+ tracked.getEmbedded()
					+ " , "
					+ tracked.getSchedule()
					+ " , "
					+ tracked.getIntervalGPS()
					+ ", \'"
					+ tracked.getOwnerUN()
					+ "\'" + " )";

			System.out.println(query);
			stm.execute(query);

			// Insert into table Manage
			query = "INSERT INTO MANAGE ( TRACKERUN , TRACKEDUN ) VALUES ( \'"
					+ trackerUN + "\' , \'" + tracked.getUsername() + "\' ) ";
			stm.execute(query);

			// Insert into staff

			return String.valueOf(length + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public boolean verifyTracker(String us) {
		// TODO Auto-generated method stub
		us = preventSQLInj(us);
		String query = "SELECT * FROM TRACKER WHERE USERNAME = \'" + us + "\';";
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
	public Integer setEmbedded(String trackedUN, boolean b) {
		// TODO Auto-generated method stub
		trackedUN = preventSQLInj(trackedUN);
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
	public Integer setShowInMap(String trackedUN, boolean b) {
		// TODO Auto-generated method stub
		trackedUN = preventSQLInj(trackedUN);
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

	@Override
	public Integer removeTracked(String trackerUN, String trackedUN) {
		// TODO Auto-generated method stub
		trackerUN = preventSQLInj(trackerUN);
		trackedUN = preventSQLInj(trackedUN);
		try {
			String sql = "DELETE FROM MANAGE WHERE TRACKEDUN = \'" + trackedUN
					+ "\'";
			Statement statement = DatabaseServiceImpl.getConnection()
					.createStatement();
			statement.execute(sql);

			sql = "SELECT * FROM TRACK WHERE USERNAME = \'" + trackedUN + "\'";
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String trackID = rs.getString("TRACKUN");
				sql = "DELETE FROM WAYPOINT WHERE TRACKID = \'" + trackID
						+ "\'";
				statement.execute(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}

	@Override
	public Integer updateInfoTracked(Tracked tracked) {
		tracked.setBirthday(preventSQLInj(tracked.getBirthday()));
		tracked.setCountry(preventSQLInj(tracked.getCountry()));
		tracked.setEmail(preventSQLInj(tracked.getEmail()));
		tracked.setGMT(preventSQLInj(tracked.getGMT()));
		tracked.setLang(preventSQLInj(tracked.getLang()));
		tracked.setName(preventSQLInj(tracked.getName()));
		tracked.setState(preventSQLInj(tracked.getState()));
		tracked.setTel(preventSQLInj(tracked.getTel()));
		tracked.setUsername(preventSQLInj(tracked.getUsername()));
		tracked.setIconPath(preventSQLInj(tracked.getIconPath()));

		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'"
				+ tracked.getUsername() + "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return -1;

			query = "UPDATE TRACKER SET NAME = \' " + tracked.getName()
					+ "\' , BIRTHDAY = \'" + tracked.getBirthday()
					+ "\' , TEL = \'" + tracked.getTel() + "\' , "
					+ "EMAIL = \'" + tracked.getEmail() + "\' , GMT = \'"
					+ tracked.getGMT() + "\' , LANG = \'" + tracked.getLang()
					+ "\' , " + "COUNTRY = \'" + tracked.getCountry()
					+ "\' , ICONPATH = \'" + tracked.getIconPath() + "\' "
					+ " , SCHEDULE = " + tracked.getSchedule() + " "
					+ " , INTERVALGPS = " + tracked.getIntervalGPS() + "\' "
					+ "WHERE USERNAME = \'" + tracked.getUsername() + "\'";
			System.out.println(query);
			stm.execute(query);

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@Override
	public Integer updateInfoTracker(Tracker tracker) {
		tracker.setAddr(preventSQLInj(tracker.getAddr()));
		tracker.setBirthday(preventSQLInj(tracker.getBirthday()));
		tracker.setCountry(preventSQLInj(tracker.getCountry()));
		tracker.setEmail(preventSQLInj(tracker.getEmail()));
		tracker.setGMT(preventSQLInj(tracker.getGMT()));
		tracker.setLang(preventSQLInj(tracker.getLang()));
		tracker.setName(preventSQLInj(tracker.getName()));
		tracker.setPassword(preventSQLInj(tracker.getPassword()));
		tracker.setState(preventSQLInj(tracker.getState()));
		tracker.setTel(preventSQLInj(tracker.getTel()));
		tracker.setTypeCus(preventSQLInj(tracker.getTypeCus()));
		tracker.setUsername(preventSQLInj(tracker.getUsername()));

		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'"
				+ tracker.getUsername() + "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return -1;

			query = "UPDATE TRACKER SET PASSWORD = \'" + tracker.getPassword()
					+ "\' , NAME = \' " + tracker.getName()
					+ "\' , BIRTHDAY = \'" + tracker.getBirthday()
					+ "\' , TEL = \'" + tracker.getTel() + "\' , ADDRESS = \'"
					+ tracker.getAddr() + "\' , " + "EMAIL = \'"
					+ tracker.getEmail() + "\' , GMT = \'" + tracker.getGMT()
					+ "\' , LANG = \'" + tracker.getLang() + "\' , "
					+ "COUNTRY = \'" + tracker.getCountry() + "\'"
					+ "WHERE USERNAME = \'" + tracker.getUsername() + "\'";
			System.out.println(query);
			stm.execute(query);

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@Override
	public String[] getTracks(String trackedUN) {
		trackedUN = preventSQLInj(trackedUN);

		ArrayList<String> arrTrackID = new ArrayList<String>();
		try {
			String sql = "SELECT * FROM TRACK WHERE USERNAME = \'" + trackedUN
					+ "\'";
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				arrTrackID.add(rs.getString("USERNAME"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (String[]) arrTrackID.toArray();
	}

	@Override
	public ArrayList getWayPoint(String trackID) {
		// TODO Auto-generated method stub
		ArrayList arrWayPoints = new ArrayList();
		try {
			String sql = "SELECT * FROM WAYPOINT WHERE USERNAME = \'" + trackID
					+ "\'";
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				WayPoint wayPoint = new WayPoint(String.valueOf(rs
						.getObject("TIME")), rs.getString("TRACKID"), String
						.valueOf(rs.getObject("LONGTITUDE")), String.valueOf(rs
						.getObject("LATTITUDE")), String.valueOf(rs
						.getObject("SPEED")));
				arrWayPoints.add(wayPoint);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrWayPoints;
	}

	/*
	 * @Override public Integer updateManage(String trackerUN, String trackedUN,
	 * String schedule, int interval) { String query; ResultSet rs = null; try {
	 * Statement stm = DatabaseServiceImpl.getConnection() .createStatement();
	 * 
	 * query = "SELECT * FROM MANAGE WHERE USERNAME = \'" + trackerUN + "\';";
	 * rs = stm.executeQuery(query); rs.last(); if (rs.getRow() >= 1) return -1;
	 * 
	 * query = "SELECT * FROM MANAGE WHERE USERNAME = \'" + trackerUN + "\';";
	 * rs = stm.executeQuery(query); rs.last(); if (rs.getRow() >= 1) return -1;
	 * 
	 * query = "UPDATE MANAGE SET PASSWORD = \'" + tracker.getPassword() +
	 * "\' , NAME = \' " + tracker.getName() + "\' , BIRTHDAY = \'" +
	 * tracker.getBirthday() + "\' , TEL = \'" + tracker.getTel() +
	 * "\' , ADDRESS = \'" + tracker.getAddr() + "\' , " + "EMAIL = \'" +
	 * tracker.getEmail() + "\' , GMT = \'" + tracker.getGMT() +
	 * "\' , LANG = \'" + tracker.getLang() + "\' , " + "COUNTRY = \'" +
	 * tracker.getCountry() + "\'" + "WHERE USERNAME = \'" +
	 * tracker.getUsername() + "\'"; System.out.println(query);
	 * stm.execute(query);
	 * 
	 * return 0; } catch (Exception e) { e.printStackTrace(); return 2; } return
	 * 0; }
	 */

	@Override
	public Integer updateTrackedGPSState(String trackedUN, boolean type) {
		trackedUN = preventSQLInj(trackedUN);
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + trackedUN
				+ "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return -1;

			query = "UPDATE TRACKED SET GPSSTATE = " + type
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
	public Integer updateTrackerTypeCus(String trackerUN, int type) {
		trackerUN = preventSQLInj(trackerUN);
		// TODO Auto-generated method stub
		String query = "SELECT * FROM TRACKER WHERE USERNAME = \'" + trackerUN
				+ "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return -1;

			query = "UPDATE TRACKER SET TYPECUS = " + type
					+ "WHERE USERNAME = \'" + trackerUN + "\'";
			System.out.println(query);
			stm.execute(query);

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 2;
		}
	}

	@Override
	public Integer updateTrackedShowInMap(String trackedUN, int type) {
		trackedUN = preventSQLInj(trackedUN);
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + trackedUN
				+ "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return -1;

			query = "UPDATE TRACKED SET SHOWINMAP = " + type
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
	public boolean verifyTracked(String us) {
		us = preventSQLInj(us);
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

	private static Policy getAntiSamyPolicy() {
		Policy policy = null;
		try {
			File file = new File(
					"./com/poma/bkitpoma/server/antisamy-anythinggoes-1.3.xml");
			System.out.println(file.getAbsolutePath());
			policy = Policy.getInstance(file);
		} catch (PolicyException pe) {
			pe.printStackTrace();
		}
		return policy;
	}

	public String preventSQLInj(String dirtyInput) {
		CleanResults cr = null;
		try {
			if (policy == null)
				policy = getAntiSamyPolicy();
			AntiSamy as = new AntiSamy();
			cr = as.scan(dirtyInput, policy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cr.getCleanHTML(); // some custom function
	}

	@Override
	public boolean loginTracked(String username, String password) {
		username = preventSQLInj(username);
		password = preventSQLInj(password);
		String query = "SELECT * FROM TRACKED WHERE USERNAME = \'" + username
				+ "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean loginTracker(String username, String password) {
		username = preventSQLInj(username);
		password = preventSQLInj(password);
		String query = "SELECT * FROM TRACKER WHERE USERNAME = \'" + username
				+ "\';";
		try {
			Statement stm = DatabaseServiceImpl.getConnection()
					.createStatement();
			ResultSet rs = stm.executeQuery(query);
			rs.last();
			if (rs.getRow() >= 1)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Integer insertManage(String trackerUN, String trackedUN) {
		// TODO Auto-generated method stub
		trackerUN = preventSQLInj(trackerUN);
		trackedUN = preventSQLInj(trackedUN);
		String sql = "INSERT INTO MANAGE ( TRACKERUN , TRACKEDUN ) VALUES ( \'"
				+ trackerUN + "\' , '" + trackedUN + "\' ) ";
		Statement stm = null;
		try {
			stm = this.getConnection().createStatement();
			stm.execute(sql);
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}

	@Override
	public Integer insertWayPoint(WayPoint wayPoint) {
		// TODO Auto-generated method stub
		wayPoint.setLattitude(preventSQLInj(wayPoint.getLattitude()));
		wayPoint.setLongtitude(preventSQLInj(wayPoint.getLongtitude()));
		wayPoint.setSpeed(preventSQLInj(wayPoint.getSpeed()));
		wayPoint.setTime(preventSQLInj(wayPoint.getTime()));
		wayPoint.setTrackID(preventSQLInj(wayPoint.getTrackID()));
		String sql = "INSERT INTO WAYPOINT ( TIMESERVER , TRACKID , LAT , LNG , SPEED ) VALUES ( "
				+ wayPoint.getTime()
				+ " , "
				+ wayPoint.getTrackID()
				+ " , "
				+ wayPoint.getLattitude()
				+ " , "
				+ wayPoint.getLongtitude()
				+ " , " + wayPoint.getSpeed() + " ) ";
		System.out.println(sql);
		Statement stm = null;
		try {
			stm = this.getConnection().createStatement();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 2;
	}

	@Override
	public String insertTrack(String trackedUN) {
		int length = 0;
		try {
			
			Statement stm = this.getConnection().createStatement();
			length = getLength(stm, "TRACK");
			String sql = "INSERT INTO TRACK (TRACKID , TRACKEDUN ) VALUES ( \'" + (length+1) + "\' , \'" + trackedUN + "\' ) ";
			stm.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(length+1);
	}

	private int getLength(Statement stm, String table) throws SQLException {
		String sql = "SELECT * FROM " + table;
		ResultSet rs = stm.executeQuery(sql);
		rs.last();
		return rs.getRow();
	}

	@Override
	public String getNewTrackedUN() {
		// TODO Auto-generated method stub
		try{
			Statement stm = this.getConnection().createStatement();
			return String.valueOf(getLength(stm, "TRACKED"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
