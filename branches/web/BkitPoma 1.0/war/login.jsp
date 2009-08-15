<%--
This page is a sample for consumers to use, but also serves as a testing page for running the server.
--%>
<%@ page import="org.verisign.joid.consumer.OpenIdFilter"%>
<%@ page import="org.verisign.joid.util.UrlUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
	String loggedInAs = OpenIdFilter.getCurrentUser(session);
	if (loggedInAs == null) {
%>
<p align="center"><span
	style="font-size: 20px; background-color: black; color: white; padding: 5px;">You
are logged in as: <%=OpenIdFilter.getCurrentUser(session)%></span></p>
<p align="center"><a href="javascript:window.close();">Close
this window...</a></p>
<script>
	setTimeout("close", delaytime);
	function close() {
		window.close();
	}
</script>
<%
	} else {
		String returnTo = UrlUtils.getBaseUrl(request);
		try {
			String id = request.getParameter("openid");
			if (!id.startsWith("http")) {
				id = "http://" + id;
			}
			String trustRoot = returnTo;
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
%>