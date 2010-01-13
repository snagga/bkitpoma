package com.bkitmobile.poma.test.client;

import java.util.ArrayList;
import java.util.Date;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.HashMap;

public class Test {
	public static HashMap<String, CTracker> ctrackerHash = new HashMap<String, CTracker>();
	public static HashMap<Long, CTracked> ctrackedHash = new HashMap<Long, CTracked>();
	public static HashMap<Long, CTrack> ctrackHash = new HashMap<Long, CTrack>();
	public static HashMap<String, CWaypoint> cwaypoint = new HashMap<String, CWaypoint>();

	static DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();

	@SuppressWarnings("deprecation")
	public static void insertTrackerTable() {
		CTracker[] ctrackerList = new CTracker[] {
				new CTracker("rockerhieu", "hieu", "Hieu Rocker", new Date(
						1987, 3, 9), "0901234567", "A108 chung cu Gia Phu",
						"rockerhieu@gmail.com", 0, 7, "vi", "Vi\u1EC7t Nam",
						true, true, null),
				new CTracker("vo_mita_ov", "tam", "Vo Minh Tam", new Date(1989,
						2, 8), "0921234567", "Quan 6, Sai Gon",
						"vo_mita_ov@gmail.com", 0, 7, "vi", "Vi\u1EC7t Nam",
						true, true, null),
				new CTracker("condorhero01", "duc", "Cao Tien Duc", new Date(
						1989, 4, 10), "0951234567", "Lam Dong",
						"condorhero01@gmail.com", 0, 7, "vi", "Vi\u1EC7t Nam",
						true, true, null),
				new CTracker("trong_nghia89", "nghia", "Le Trong Nghia",
						new Date(1989, 5, 7), "0911234567",
						"Quan 6, Ho Chi Minh", "aizikko@gmail.com", 0, 7, "vi",
						"Vi\u1EC7t Nam", true, true, null),
				new CTracker("locle", "loc", "Le Quang Loc", new Date(1987, 3,
						9), "0971234567", "HCM University of Technology",
						"locle@cse.hcmut.edu.vn", 0, 7, "vi", "Vi\u1EC7t Nam",
						true, true, null) };
		for (final CTracker ctracker : ctrackerList) {
			dbAsync.insertTracker(ctracker,
					new AsyncCallbackAdapter<ServiceResult<CTracker>>());
		}
	}

	public static void removeTracker() {
		dbAsync.removeTracker("rockerhieu",
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}

	public static void insertTrackedTable() {
		Object[][] obj = getTransportIcon();
		CTracked[] ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedRKH1@yahoo.com", "346457768"),
				new CTracked("pass", "trackedRKH2@yahoo.com", "547457663"),
				new CTracked("pass", "trackedRKH3@yahoo.com", "277747474"),
				new CTracked("pass", "trackedRKH4@yahoo.com", "234537567") };
		for (final CTracked ctracked : ctrackedList) {
			ctracked.setIconPath((String)obj[Random.nextInt(obj.length)][2]);
			dbAsync.insertTracked(ctracked, "rockerhieu", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());
		}

		ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedCondorHero1@yahoo.com",
						"987654321"),
				new CTracked("pass", "trackedCondorHero2@yahoo.com",
						"534757453"),
				new CTracked("pass", "trackedCondorHero3@yahoo.com",
						"875676345") };
		for (final CTracked ctracked : ctrackedList) {
			ctracked.setIconPath((String)obj[Random.nextInt(obj.length)][2]);
			dbAsync.insertTracked(ctracked, "condorhero01", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());
		}

		ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedVMT1@yahoo.com", "135790864"),
				new CTracked("pass", "trackedVMT2@yahoo.com", "135790864"),
				new CTracked("pass", "trackedVMT3@yahoo.com", "135790864"),
				new CTracked("pass", "trackedVMT4@yahoo.com", "135790864") };
		for (final CTracked ctracked : ctrackedList) {
			ctracked.setIconPath((String)obj[Random.nextInt(obj.length)][2]);
			dbAsync.insertTracked(ctracked, "vo_mita_ov", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());
		}

		ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedLOCLE1@yahoo.com", "457568433"),
				new CTracked("pass", "trackedLOCLE2@yahoo.com", "135790864") };
		for (final CTracked ctracked : ctrackedList) {
			ctracked.setIconPath((String)obj[Random.nextInt(obj.length)][2]);
			dbAsync.insertTracked(ctracked, "vo_mita_ov", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());
		}

		ctrackedList = new CTracked[] { new CTracked("pass",
				"trackedNghia@yahoo.com", "736973768") };
		for (final CTracked ctracked : ctrackedList) {
			ctracked.setIconPath((String)obj[Random.nextInt(obj.length)][2]);
			dbAsync.insertTracked(ctracked, "trong_nghia89", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());
		}
	}

	public static void insertManageTable() {
		TestEntryPoint.txtResult.setValue(TestEntryPoint.txtResult.getText()
				+ "insertManageTable:Not implemented!");
	}

	public static void insertStaffTable() {
		ArrayList<CTracked> arr = new ArrayList<CTracked>(ctrackedHash.values());
		int size = arr.size();
		CStaff[] cstaffList = new CStaff[] {
				new CStaff("rockerhieu", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("rockerhieu", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("rockerhieu", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("locle", arr.get(Random.nextInt(size)).getUsername()),
				new CStaff("locle", arr.get(Random.nextInt(size)).getUsername()),
				new CStaff("vo_mita_ov", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("condorhero01", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("condorhero01", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("condorhero01", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("trong_nghia89", arr.get(Random.nextInt(size))
						.getUsername()),
				new CStaff("trong_nghia89", arr.get(Random.nextInt(size))
						.getUsername()), };
		for (CStaff cstaff : cstaffList) {
			dbAsync.insertStaff(cstaff.getTrackerUN(), cstaff.getTrackedID(),
					new AsyncCallbackAdapter<ServiceResult<CStaff>>());
		}// end for
	}

	public static void insertTrack() {
		ArrayList<CTracked> arr = new ArrayList<CTracked>(ctrackedHash.values());
		int size = arr.size();
		for (int i = 0; i < 50; i++) {
			dbAsync.newTrack(arr.get(Random.nextInt(size)).getUsername(),
					new AsyncCallbackAdapter<ServiceResult<CTrack>>());
		}
	}

	public static void insertWaypoint() {
		ArrayList<CTrack> arr = new ArrayList<CTrack>(ctrackHash.values());
		int size = arr.size();
		for (int i = 0; i < 500; i++) {
			CWaypoint cwaypoint = new CWaypoint(arr.get(Random.nextInt(size))
					.getTrackID());
			cwaypoint.setLat(10.77234751 + Random.nextDouble());
			cwaypoint.setLng(106.6587469 + Random.nextDouble());
			cwaypoint.setSpeed(10L + Random.nextInt(20));
			dbAsync.insertWaypoint(cwaypoint.getTrackID(), cwaypoint.getLat(),
					cwaypoint.getLng(), cwaypoint.getSpeed(),
					new AsyncCallbackAdapter<ServiceResult<CWaypoint>>());
		}
	}

	public static void getTracksByTracked(Long trackedID) {
		dbAsync.getTracksByTracked(trackedID,
				new AsyncCallbackAdapter<ServiceResult<ArrayList<CTrack>>>());
	}

	public static void login() {
		dbAsync.loginTracker("rockerhieu", "",
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
		dbAsync.loginTracker("rockerhieu", "hieu",
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}

	public static void removeTracker(String trackerUN) {
		dbAsync.removeTracker(trackerUN,
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}

	public static void isLogined() {
		dbAsync.isLogined(new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}

	public static void updateTracker() {
		CTracker ctracker = new CTracker("rockerhieu");
		ctracker.setApiKey("api");
		dbAsync.updateTracker(ctracker,
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}

	public static void resetDatabase(String key) {
		dbAsync.resetDatabase(key,
				new AsyncCallbackAdapter<ServiceResult<Boolean>>());
	}

	public static void insertTrackedRule() {
		ArrayList<CTracked> arr = new ArrayList<CTracked>(ctrackedHash.values());
		int size = arr.size();
		for (int i = 0; i < 100; i++) {
			ArrayList<Double> listLatLng = new ArrayList<Double>();
			for (int j = 0; j < Random.nextInt(20)+3; j++) {
				double lat = 10.77234751 + Random.nextDouble()*3;
				double lng = 106.6587469 + Random.nextDouble()*3;
				listLatLng.add(lat);
				listLatLng.add(lng);
			}
			CTrackedRule rule = new CTrackedRule(Random.nextBoolean(), listLatLng,arr.get(Random.nextInt(size)).getUsername());
			dbAsync.insertTrackedRule(rule, new AsyncCallbackAdapter<ServiceResult<CTrackedRule>>());
		}
	}

	public static void executeQuery(String sql) {
		dbAsync.executeQuery(sql, new AsyncCallbackAdapter<ServiceResult<ArrayList>>());
	}

	private static Object[][] getTransportIcon() {
		return new Object[][] {
				new Object[] { "bus", new Integer(1378),
						"http://bkitpoma.appspot.com/images/marker/bus.png" },
				new Object[] { "cabs", new Integer(1285),
						"http://bkitpoma.appspot.com/images/marker/cabs.png" },
				new Object[] { "cycling", new Integer(1408),
						"http://bkitpoma.appspot.com/images/marker/cycling.png" },
				new Object[] { "ferry", new Integer(1203),
						"http://bkitpoma.appspot.com/images/marker/ferry.png" },
				new Object[] { "helicopter", new Integer(1166),
						"http://bkitpoma.appspot.com/images/marker/helicopter.png" },
				new Object[] { "icon231", new Integer(2451),
						"http://bkitpoma.appspot.com/images/marker/icon231.png" },
				new Object[] { "icon39", new Integer(2445),
						"http://bkitpoma.appspot.com/images/marker/icon39.png" },
				new Object[] { "icon47", new Integer(1303),
						"http://bkitpoma.appspot.com/images/marker/icon47.png" },
				new Object[] { "icon48", new Integer(2593),
						"http://bkitpoma.appspot.com/images/marker/icon48.png" },
				new Object[] { "icon54", new Integer(2466),
						"http://bkitpoma.appspot.com/images/marker/icon54.png" },
				new Object[] { "icon56", new Integer(787),
						"http://bkitpoma.appspot.com/images/marker/icon56.png" },
				new Object[] { "icon621", new Integer(1030),
						"http://bkitpoma.appspot.com/images/marker/icon621.png" },
				new Object[] { "motorcycling", new Integer(1558),
						"http://bkitpoma.appspot.com/images/marker/motorcycling.png" },
				new Object[] { "plane", new Integer(1123),
						"http://bkitpoma.appspot.com/images/marker/plane.png" },
				new Object[] { "rail", new Integer(1311),
						"http://bkitpoma.appspot.com/images/marker/rail.png" },
				new Object[] { "subway", new Integer(2025),
						"http://bkitpoma.appspot.com/images/marker/subway.png" },
				new Object[] { "tram", new Integer(1373),
						"http://bkitpoma.appspot.com/images/marker/tram.png" },
				new Object[] { "truck", new Integer(963),
						"http://bkitpoma.appspot.com/images/marker/truck.png" },
				new Object[] { "wheel_chair_accessible", new Integer(1181),
						"http://bkitpoma.appspot.com/images/marker/wheel_chair_accessible.png" } };
	}
}

class AsyncCallbackAdapter<T> implements AsyncCallback<T> {
	public AsyncCallbackAdapter() {
	}

	@Override
	public void onFailure(Throwable caught) {
		TestEntryPoint.txtResult.setValue(TestEntryPoint.txtResult.getText() + caught.getMessage());
		GWT.log(caught.getMessage(), caught);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(T result) {
		if (result instanceof ServiceResult) {
			TestEntryPoint.txtResult.setValue(TestEntryPoint.txtResult.getText()
					+ ((ServiceResult) result).getMessage() + "\r\n");
			Object obj = ((ServiceResult) result).getResult();
			if (obj instanceof CTracker) {
				CTracker ctracker = (CTracker) obj;
				Test.ctrackerHash.put(ctracker.getUsername(), ctracker);
			}
			if (obj instanceof CTracked) {
				CTracked ctracked = (CTracked) obj;
				Test.ctrackedHash.put(ctracked.getUsername(), ctracked);
			}
			if (obj instanceof CTrack) {
				CTrack ctrack = (CTrack) obj;
				Test.ctrackHash.put(ctrack.getTrackID(), ctrack);
			}
			if (obj instanceof CWaypoint) {
				CWaypoint cwaypoint = (CWaypoint) obj;
				Test.cwaypoint.put(cwaypoint.getWaypointPK(), cwaypoint);
			}
			if (obj instanceof ArrayList) {
				ArrayList arr = (ArrayList) obj;
				for (Object o : arr) {
					String s = "";
					if (o instanceof CTracker) {
						s = ((CTracker)o).toString();
					}
					if (o instanceof CTracked) {
						s = ((CTracked)o).toString();
					}
					if (o instanceof CTrack) {
						s = ((CTrack)o).toString();
					}
					if (o instanceof CManage) {
						s = ((CManage)o).toString();
					}
					if (o instanceof CStaff) {
						s = ((CStaff)o).toString();
					}
					if (o instanceof CWaypoint) {
						s = ((CWaypoint)o).toString();
					}
					if (o instanceof CTrackedRule) {
						s = ((CTrackedRule)o).toString();
					}
					TestEntryPoint.txtResult.setValue(TestEntryPoint.txtResult.getText()
							+ s + "\r\n");
				}
			}
		}
	}
}