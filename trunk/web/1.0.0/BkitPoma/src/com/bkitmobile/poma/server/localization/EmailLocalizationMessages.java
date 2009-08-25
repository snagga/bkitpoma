package com.bkitmobile.poma.server.localization;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource.Generate;

@Generate(format = "com.google.gwt.i18n.rebind.format.PropertiesFormat")
@DefaultLocale("en_US")
public interface EmailLocalizationMessages extends Messages {
	@DefaultMessage("[BkitPOMA] Verify your email")
	String title_verify_email_forgot_password();

	String content_verify_email_forgot_password(String name,String url);

	@DefaultMessage("[BkitPOMA] Reset your password")
	String title_get_new_password();

	String content_get_new_password(String name, String trackerUN,
			String password);
			
	String title_verify_email_new_tracker();
	
	String content_verify_email_new_tracker(String name, String trackerUN,String url);
}
