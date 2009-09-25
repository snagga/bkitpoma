package com.bkitmobile.poma.mobile.api.util;

import java.util.Vector;

public class StringUtils {
	public static String[] split(String s, char ch) {
		String tmp = "";
		Vector v = new Vector();
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ch || i >= s.length() - 1) {
				if (i >= s.length() - 1) {
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

	public static String strReplace(String inString, String inPattern,
			String inVal) {
		int index = inString.indexOf(inPattern);
		if (index != -1)
			return inString.substring(0, index) + inVal
					+ inString.substring(index + inPattern.length());

		return inString;
	}

	public static String strReplace(String inString, String inPattern,
			float infloat) {
		return strReplace(inString, inPattern, infloat + "");
	}

	public static String strReplace(String inString, String inPattern,
			double indouble) {
		return strReplace(inString, inPattern, indouble + "");
	}

	public static String strReplace(String inString, String inPattern,
			long inlong) {
		return strReplace(inString, inPattern, inlong + "");
	}

	public static String strReplace(String inString, String inPattern, int inVal) {
		return strReplace(inString, inPattern, inVal + "");
	}
}
