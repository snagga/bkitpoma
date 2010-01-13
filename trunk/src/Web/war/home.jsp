<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.bkitmobile.poma.database.server.User"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="com.bkitmobile.poma.facebook.server.FacebookApi"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.bkitmobile.poma.database.server.User"%>
<%@page import="org.json.*"%>
<%@page import="javax.servlet.http.Cookie"%>

<%
	User user = new User(request);
	user.setSession(session);
	Logger log = Logger.getLogger("home_jsp");

	if (user.determine()) {
		log.warning("tracker_username: " + user.getTrackerUsername());
		log.warning("tracker_type: " + user.getTrackerType());
		log.warning("tracker_language: " + user.getTrackerLanguage());

		if (user.isFacebook) {
			FacebookApi fb = new FacebookApi(User.API_KEY,
					User.SECRET_KEY);

			TreeMap<String, String> tm = new TreeMap<String, String>();
			tm.put("method", "users.getInfo");
			tm.put("uids", user.getTrackerUsername() + "");
			tm.put("fields", "name,locale");

			String fb_user_detail = fb.call(tm);

			try {
				JSONArray result = new JSONArray(fb_user_detail);
				JSONObject obj = result.getJSONObject(0);
				session.setAttribute("tracker_name", obj
						.getString("name"));
				session.setAttribute("tracker_language", obj.getString(
						"locale").substring(0,2));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (user.facebook_session_key != null) {
				fb.setSession(user.facebook_session_key);
				tm.clear();
				tm.put("method", "friends.get");

				// Send a notification
				tm.clear();
				tm.put("to_ids", "");
				tm.put("notification", "Thanks for trying POMA.");
				fb.getResponse("notifications.send", tm);

				// Publish a feed
				JSONArray imageArray = new JSONArray();
				JSONObject templateData = new JSONObject();
				JSONObject obj1 = new JSONObject();
				JSONObject imageData = new JSONObject();
				imageData.put("href", "http://bkitpoma.appspot.com");
				imageData
						.put(
								"src",
								"http://photos-a.ak.fbcdn.net/photos-ak-sf2p/v43/4/123253217392/app_2_123253217392_1842.gif");
				imageArray.put(imageData);
				templateData.put("images", imageArray); // to populate template_data
				tm.clear();
				tm.put("template_data", templateData.toString());
				tm.put("template_bundle_id", "135967302444");
				tm.put("story_size", "2");

				fb.getResponse("feed.publishUserAction", tm);
			}
		}
	}
	
	String language = user.getTrackerLanguage();	
	
	boolean in_facebook_frame = user.in_facebook_frame;
	log.warning("in_facebook_frame: " + in_facebook_frame);
	response.addCookie(new Cookie("in_facebook_frame", in_facebook_frame+""));
	
	log.warning(language);
	
	if (language == null)
	language = "vi";
%>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
<meta name="verify-v1"
	content="4EPCx5yy22va7pC2b/Y/1ekaTyEv32sHd6q3DpSPb2A=" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Content-Author" content="HieuRocker" />
<meta name="gwt:property" content="locale=<%=language%>" />
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
<script type="text/javascript" src="js/tools.js"></script>
</head>
<body>
<!--<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'-->
<!--	style="position: absolute; width: 0; height: 0; border: 0"></iframe>-->
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

<!--include Ext -->
<script type="text/javascript">
	document.getElementById('loading-msg').innerHTML = 'Loading GoogleMap API...';
</script>

<script
	src="http://maps.google.com/maps?hl=<%=language%>&amp;gwt=1&amp;file=api&amp;v=2&amp;sensor=false&amp;key=ABQIAAAAXqEHQfJuF82t_ZEOQ94VCBSm7gIuvKTAbvj75gTzcgVr0mGA8BTbDVlIpNWa9wacDbFOBL2LHj5_EQ">
<!-- ABQIAAAAXqEHQfJuF82t_ZEOQ94VCBTvAv_lJH_V34Cez6bXSeA2hcCZdBR6DaYNdiPCziM9SJDeewFX-FYa1A -->
</script>

<!--include the application JS-->
<script type="text/javascript">
	document.getElementById('loading-msg').innerHTML = 'Initializing POMA...';
</script>

<!-- The GWT js file generated at run time -->
<script type="text/javascript" src='/home/home.nocache.js'></script>

<% if (!in_facebook_frame) { %>
<script
	src="http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php/vi_VN"
	type="text/javascript"></script>
<% } %>

<script type="text/javascript">
	FB.init("<%=User.API_KEY%>", "/facebook/connect/xd_receiver.htm");
<% if (user.isFacebook) { %>
	FB.ensureInit( function() {
		FB.Connect.showPermissionDialog("email,publish_stream");
	});
<% } %>
	
</script>
</body>
</html>