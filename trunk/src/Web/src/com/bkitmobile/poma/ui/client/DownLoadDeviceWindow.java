package com.bkitmobile.poma.ui.client;

import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class DownLoadDeviceWindow extends Window {

	private static String PHONE_IMAGE = "/images/02-Nokia-E72.jpg";

	public DownLoadDeviceWindow() {
		setSize(500, 330);
		setResizable(false);
		setCloseAction(HIDE);
		setLayout(new BorderLayout());
		setModal(true);

		Label phoneLabel = new Label();
		phoneLabel.setHtml("<center><img src='" + PHONE_IMAGE
				+ "' width=\"151\"  height=\"274\" /></center>");
		Label textLabel = new Label();

		String s = "";
		s += "POMA Client h\u1ED7 tr\u1EE3 g\u1EDFi t\u00EDn hi\u1EC7u t\u1EEB c\u00E1c \u0111i\u1EC7n tho\u1EA1i di \u0111\u1ED9ng c\u00F3 h\u1ED7 tr\u1EE3 GPRS v\u00E0 GPS.<br />";
		s += "<br />\u0110\u1EC3 s\u1EED d\u1EE5ng POMA Client, \u0111i\u1EC7n tho\u1EA1i c\u1EE7a b\u1EA1n c\u1EA7n h\u1ED7 tr\u1EE3 c\u00E1c th\u00F4ng s\u1ED1 sau:<br />";
		s += "\u2022&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MIDP 2.0+ CLDC 1.1+.<br />";
		s += "\u2022&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Location API  for J2ME.<br />";
		s += "\u2022&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;K\u00EDch ho\u1EA1t GPRS.<br />";
		s += "\u2022&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;GPS/A-GPS internal ho\u1EB7c GPS receiver th\u00F4ng qua k\u1EBFt n\u1ED1i Bluetooth.<br />";
		s += "<br /><b><u>C\u00E1ch c\u00E0i \u0111\u1EB7t:</u></b><br />";
		s += "\u2022&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tr\u1EF1c ti\u1EBFp:<br />";
		s += "C\u00E0i \u0111\u1EB7t <a href='http://bkitpoma.appspot.com/download/PomaMobile.jad'>http://bkitpoma.appspot.com/download/PomaMobile.jad</a> tr\u1EF1c ti\u1EBFp t\u1EEB \u0111i\u1EC7n tho\u1EA1i c\u1EE7a b\u1EA1n th\u00F4ng qua tr\u00ECnh duy\u1EC7t wap.<br />";
		s += "\u2022&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Gi\u00E1n ti\u1EBFp:<br />";
		s += "Nh\u1EA5n v\u00E0o icon d\u01B0\u1EDBi \u0111\u00E2y \u0111\u1EC3 t\u1EA3i file PomaMobile.jar, sau \u0111\u00F3 ch\u00E9p v\u00E0o \u0111i\u1EC7n tho\u1EA1i v\u00E0 c\u00E0i \u0111\u1EB7t b\u00ECnh th\u01B0\u1EDDng.<br />";
		s += "<center><a href='/download/PomaMobile.jar'><img src='/images/downloader.png' width='80' height='80'></a></center>";
		
		textLabel.setHtml(s);

		add(phoneLabel, new BorderLayoutData(RegionPosition.WEST));
		add(textLabel, new BorderLayoutData(RegionPosition.CENTER));
	}
}
