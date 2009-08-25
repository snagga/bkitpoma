<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="org.verisign.joid.consumer.OpenIdFilter"%>
<%@ page import="org.verisign.joid.util.UrlUtils"%>

<%
	String loggedInAs = null;
	try {
		OpenIdFilter.getCurrentUser(session);
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	if (loggedInAs != null) {
		session.setAttribute("tracker", loggedInAs);
	}
	session.setAttribute("isopenid", loggedInAs != null);
%>
<head>
<meta name="verify-v1"
	content="4EPCx5yy22va7pC2b/Y/1ekaTyEv32sHd6q3DpSPb2A=" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Author" content="HieuRocker" />
<meta name="gwt:property" content="locale=vi" />

<title>POMA - Position Manager</title>
<script type="text/javascript">
	var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl."
			: "http://www.");
	document
			.write(unescape("%3Cscript src='"
					+ gaJsHost
					+ "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
	try {
		var pageTracker = _gat._getTracker("UA-9953276-1");
		pageTracker._trackPageview();
	} catch (err) {
	}
</script>
<link rel="shortcut icon" href="favicon.ico" />
<link href="css/style.css" type="text/css" rel="stylesheet" />
</head>
<body>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
	style="position: absolute; width: 0; height: 0; border: 0"></iframe>
<div id="loading">
<div class="loading-indicator"><img
	src="js/ext/resources/images/default/shared/large-loading.gif"
	width="32" height="32"
	style="margin-right: 8px; float: left; vertical-align: top;" />Hello
^_^<br />
<span id="loading-msg">Loading styles and images...</span></div>
</div>
<!--include the Ext CSS, and use the gray theme-->
<link rel="stylesheet" type="text/css"
	href="js/ext/resources/css/ext-all.css" />
<link id="theme" rel="stylesheet" type="text/css"
	href="js/ext/resources/css/xtheme-gray.css" />
<script type="text/javascript">
	document.getElementById('loading-msg').innerHTML = 'Loading Core API...';
</script>

<!--include the Ext Core API-->
<script type="text/javascript" src="js/ext/adapter/ext/ext-base.js"></script>

<!--include Ext -->
<script type="text/javascript">
	document.getElementById('loading-msg').innerHTML = 'Loading UI Components...';
</script>
<script type="text/javascript" src="js/ext/ext-all.js"></script>

<!--include the application JS-->
<script type="text/javascript">
	document.getElementById('loading-msg').innerHTML = 'Initializing POMA...';
</script>

<!-- The GWT js file generated at run time -->
<script type="text/javascript" src='bkitpoma/bkitpoma.nocache.js'></script>

<script type="text/javascript">
	Ext.get('loading').fadeOut( {
		remove :true,
		duration :.25
	});
</script>
</body>
</html>