package com.bkitmobile.poma.client.ui;

import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.Panel;

public class LoadingPanel extends Panel {
	
	private ExtElement e;
	
	public LoadingPanel(String title) {
		super(title);
	}
	
	public LoadingPanel() {
		super();
	}
	
	public void startLoading(String loadingMessage) {
		e = Ext.get(this.getId());
		e.mask(loadingMessage);
	}
	
	public void stopLoading() {
		if (e != null)
			e.unmask();
	}
}
