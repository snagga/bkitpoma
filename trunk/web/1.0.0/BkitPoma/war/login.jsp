<%--
This page is a sample for consumers to use, but also serves as a testing page for running the server.
--%>
<%@ page import="org.verisign.joid.consumer.OpenIdFilter"%>
<%@ page import="org.verisign.joid.util.UrlUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
	String loggedInAs = OpenIdFilter.getCurrentUser(session);
	if (loggedInAs == null) {
		String returnTo = "http://bkitpoma.appspot.com/home.jsp";//UrlUtils.getBaseUrl(request);
		try {
			String id = request.getParameter("openid");
			if (!id.startsWith("http")) {
				id = "http://" + id;
			}
			String trustRoot = "http://bkitpoma.appspot.com";
			String s = OpenIdFilter.joid().getAuthUrl(id, returnTo,
					trustRoot);
			response.sendRedirect(s);
		} catch (Throwable e) {
%>
An error occurred! Please press back and try again.
<%
		}
		return;
	}
	response.sendRedirect("/home.jsp");
%>