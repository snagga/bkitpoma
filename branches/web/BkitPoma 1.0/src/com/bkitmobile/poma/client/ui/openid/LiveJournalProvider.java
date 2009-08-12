package com.bkitmobile.poma.client.ui.openid;

public class LiveJournalProvider extends Provider{

	public LiveJournalProvider() {
		setLogo("images/openid/livejournal.png");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "LiveJournal";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://username.livejournal.com";
	}
}
