package com.bkitmobile.poma.captcha.client;

public class Recaptcha {
	public static final String IMAGE = "image";
	public static final String AUDIO = "audio";

	public static native void create(String key, String div) /*-{
		$wnd.Recaptcha.create(key, "recaptcha_div", {});
	}-*/;

	public static native void reload() /*-{
		$wnd.Recaptcha.reload();
	}-*/;

	public static native void destroy() /*-{
		$wnd.Recaptcha.destroy();
	}-*/;

	public static native String getChallenge() /*-{
		return $wnd.Recaptcha.get_challenge();
	}-*/;

	public static native String getResponse() /*-{
		return $wnd.Recaptcha.get_response();
	}-*/;

	public static native void focusResponseField() /*-{
		return $wnd.Recaptcha.focus_response_field();
	}-*/;

	public static native void showHelp() /*-{
		return $wnd.Recaptcha.showhelp();
	}-*/;

	public static native void switchType(String newType) /*-{
		return $wnd.Recaptcha.switch_type(newType);
	}-*/;

}
