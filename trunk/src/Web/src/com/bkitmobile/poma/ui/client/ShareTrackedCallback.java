package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

/**
 * <b>ShareTrackedCallback</b> Share tracked callback. See TrackedProfileForm for usage
 * @author Tam Vo Minh
 *
 */
public interface ShareTrackedCallback {
	/**
	 * This method is called when user click apply
	 * @param <code>arrListManage</code>: arrManage after insert into database
	 * @param <code>arrListStaff</code>: arrStaff after insert into database
	 */
	public void onApplyOperation(ArrayList<String> arrListManage,ArrayList<String> arrListStaff);
}
