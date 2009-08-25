<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fbtag" uri="tiny.tld"%>
<%@ page import="com.socialjava.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	String sessionKey = request.getParameter("fb_sig_session_key");
	TinyFBClient fb = new TinyFBClient(
			"a8d694517496758f43e57022b77b01c0",
			"bd106866600cedff2b91829ad481543e", sessionKey);
	request.setAttribute("tinyFBClient", fb);
%>
<fbtag:getFriends id="" selector="true"/>
</body>
</html>