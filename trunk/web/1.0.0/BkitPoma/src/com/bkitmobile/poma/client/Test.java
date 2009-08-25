package com.bkitmobile.poma.client;

import java.util.ArrayList;
import java.util.Date;

import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CManage;
import com.bkitmobile.poma.client.database.entity.CStaff;
import com.bkitmobile.poma.client.database.entity.CTrack;
import com.bkitmobile.poma.client.database.entity.CTracked;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.client.database.entity.CWaypoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;

public class Test {
	static int i = 1;

	static DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();

	public static void insertTrackerTable() {
		CTracker[] ctrackerList = new CTracker[] {
				new CTracker("rockerhieu", "hieu", "Hieu Rocker", new Date(
						1987, 3, 9), "0901234567", "A108 chung cu Gia Phu",
						"rockerhieu@gmail.com", 0, 7, "vi", "Vietnam", true,
						true),
				new CTracker("vo_mita_ov", "tam", "Vo Minh Tam", new Date(1989,
						2, 8), "0921234567", "Quan 6, Sai Gon",
						"vo_mita_ov@gmail.com", 0, 7, "vi", "Vietnam", true,
						true),
				new CTracker("condorhero01", "duc", "Cao Tien Duc", new Date(
						1989, 4, 10), "0951234567", "Lam Dong",
						"condorhero01@gmail.com", 0, 7, "vi", "Vietnam", true,
						true),
				new CTracker("trong_nghia89", "nghia", "Le Trong Nghia",
						new Date(1989, 5, 7), "0911234567",
						"Quan 6, Ho Chi Minh", "aizikko@gmail.com", 0, 7, "vi",
						"Vietnam", true, true),
				new CTracker("locle", "loc", "Le Quang Loc", new Date(1987, 3,
						9), "0971234567", "HCM University of Technology",
						"locle@cse.hcmut.edu.vn", 0, 7, "vi", "Vietnam", true,
						true) };
		for (final CTracker ctracker : ctrackerList) {
			dbAsync.insertTracker(ctracker,
					new AsyncCallbackAdapter<ServiceResult<CTracker>>() {
						@Override
						public void onSuccess(ServiceResult<CTracker> arg0) {
							// TODO Auto-generated method stub
							System.out.println(arg0.getMessage()
									+ ctracker.getUsername());
						}
					});
		}
	}

	public static void removeTracker() {
		dbAsync.removeTracker("rockerhieu",
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}

	public static void insertTrackedTable() {
		CTracked[] ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedRKH1@yahoo.com", "346457768"),
				new CTracked("pass", "trackedRKH2@yahoo.com", "547457663"),
				new CTracked("pass", "trackedRKH3@yahoo.com", "277747474"),
				new CTracked("pass", "trackedRKH4@yahoo.com", "234537567") };
		for (final CTracked ctracked : ctrackedList)
			dbAsync.insertTracked(ctracked, "rockerhieu", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());

		ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedCondorHero1@yahoo.com",
						"987654321"),
				new CTracked("pass", "trackedCondorHero2@yahoo.com",
						"534757453"),
				new CTracked("pass", "trackedCondorHero3@yahoo.com",
						"875676345") };
		for (final CTracked ctracked : ctrackedList)
			dbAsync.insertTracked(ctracked, "condorhero01", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());

		ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedVMT1@yahoo.com", "135790864"),
				new CTracked("pass", "trackedVMT2@yahoo.com", "135790864"),
				new CTracked("pass", "trackedVMT3@yahoo.com", "135790864"),
				new CTracked("pass", "trackedVMT4@yahoo.com", "135790864") };
		for (final CTracked ctracked : ctrackedList)
			dbAsync.insertTracked(ctracked, "vo_mita_ov", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());

		ctrackedList = new CTracked[] {
				new CTracked("pass", "trackedLOCLE1@yahoo.com", "457568433"),
				new CTracked("pass", "trackedLOCLE2@yahoo.com", "135790864") };
		for (final CTracked ctracked : ctrackedList)
			dbAsync.insertTracked(ctracked, "vo_mita_ov", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());

		ctrackedList = new CTracked[] { new CTracked("pass",
				"trackedNghia@yahoo.com", "736973768") };
		for (final CTracked ctracked : ctrackedList)
			dbAsync.insertTracked(ctracked, "trong_nghia89", true,
					new AsyncCallbackAdapter<ServiceResult<CTracked>>());
	}

	public static void insertManageTable() {
		System.out.println("insertManageTable:Not implemented!");
	}

	public static void insertStaffTable() {
		CStaff[] cstaffList = new CStaff[] { new CStaff("rockerhieu", 5L),
				new CStaff("rockerhieu", 6L), new CStaff("rockerhieu", 8L),
				new CStaff("locle", 1L), new CStaff("locle", 2L),
				new CStaff("vo_mita_ov", 1L), new CStaff("condorhero01", 10L),
				new CStaff("condorhero01", 11L),
				new CStaff("condorhero01", 2L),
				new CStaff("trong_nghia89", 5L),
				new CStaff("trong_nghia89", 7L), };
		for (CStaff cstaff : cstaffList) {
			dbAsync.insertStaff(cstaff.getTrackerUN(), cstaff.getTrackedID(),
					new AsyncCallbackAdapter<ServiceResult<CStaff>>());
		}// end for
	}

	public static void insertTrack() {
		for (i = 0; i< 50; i++) {
			dbAsync.newTrack(Random.nextInt(14) + 1L,
					new AsyncCallbackAdapter<ServiceResult<CTrack>>());
		}
	}

	public static void insertWaypoint() {
		for (i = 1; i < 500; i++) {
			CWaypoint cwaypoint = new CWaypoint(Random.nextInt(50) + 15L);
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
		dbAsync.loginTracker("rockerhieu", "blabla",
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
		dbAsync.loginTracker("rockerhieu", "hieu",
				new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}

	public static void removeTracker(String trackerUN) {
		dbAsync.removeTracker(trackerUN, new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}
	
	public static void isLogined() {
		dbAsync.isLogined(new AsyncCallbackAdapter<ServiceResult<CTracker>>());
	}
}

class AsyncCallbackAdapter<T> implements AsyncCallback<T> {
	public AsyncCallbackAdapter() {
	}

	@Override
	public void onFailure(Throwable caught) {
		MessageBox.alert("WHAT");
		caught.printStackTrace();
	}

	@Override
	public void onSuccess(T result) {
		if (result instanceof ServiceResult) {
			System.out.println(((ServiceResult) result).getMessage());
		}
	}
}