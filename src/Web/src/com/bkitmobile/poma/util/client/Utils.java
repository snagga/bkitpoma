package com.bkitmobile.poma.util.client;

import java.util.ArrayList;

public class Utils {
	private static final String UPPER_ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZqwertyuiopasdfghjklzxcvbnm";

	public static String getUpperAlphaNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int ndx = (int) (Math.random() * UPPER_ALPHA_NUM.length());
			sb.append(ALPHA_NUM.charAt(ndx));
		}
		return sb.toString();
	}
	
	public static String getAlphaNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int ndx = (int) (Math.random() * ALPHA_NUM.length());
			sb.append(ALPHA_NUM.charAt(ndx));
		}
		return sb.toString();
	}
	
	public static String[][] convertArrayList2String2D(
			ArrayList<String> arrList) {
		String[][] arr2D = new String[arrList.size()][];
		for (int i = 0; i < arrList.size(); i++) {
			arr2D[i] = new String[] { arrList.get(i) };
		}

		return arr2D;
	}
}
