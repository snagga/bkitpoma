package com.bkitmobile.poma.client;

import java.util.Vector;

public class StringUtils {
	public static String[] split(String s, char ch) {
		String tmp = "";
		Vector v = new Vector();
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ch || i >= s.length() - 1) {
				if (i>=s.length() - 1) {
					tmp += s.charAt(i);
				}
				v.addElement(tmp);
				tmp = "";
				count++;
				continue;
			}
			tmp += s.charAt(i);
		}
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = (String) v.elementAt(i);
		}
		return result;
	}

	public static String strReplace(String inString, String inPattern, int inVal) {
		int index = inString.indexOf(inPattern);
		if (index != -1)
			return inString.substring(0, index) + Integer.toString(inVal)
					+ inString.substring(index + inPattern.length());

		return inString;
	}

	public static String strReplace(String inString, String inPattern,
			String inVal) {
		int index = inString.indexOf(inPattern);
		if (index != -1)
			return inString.substring(0, index) + inVal
					+ inString.substring(index + inPattern.length());

		return inString;
	}

	public static String strReplace(String inString, String inPattern,
			Float infloat) {
		int index = inString.indexOf(inPattern);
		if (index != -1) {
			return inString.substring(0, index) + infloat
					+ inString.substring(index + inPattern.length());
		}

		return inString;
	}
}
