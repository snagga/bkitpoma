package com.bkitmobile.poma.ui.client;

import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ContainerListenerAdapter;

public class LoadingPanel extends Panel {
	
	private ExtElement e;
	private String loadingMessage;
	private boolean loaded = false;
	
	/**
	 * Constructor
	 * @param title: title of this window
	 */
	public LoadingPanel(String title) {
		this();
		setTitle(title);
	}
	
	/**
	 * Constructor
	 */
	public LoadingPanel() {
		super();
		
		addListener(new ContainerListenerAdapter() {
			
			@Override
			public void onRender(Component component) {
				if (loaded)
					loading(loadingMessage);
			}
		});
	}
	
	/**
	 * Start loading on this panel
	 * @param loadingMessage: message display when this panel loading
	 */
	public void startLoading(String loadingMessage) {
		if (this.isRendered()) {
			loading(loadingMessage);
		}
		else {
			this.loadingMessage = loadingMessage;
			loaded = true;
		}
	}
	
	private void loading(String loadingMessage) {
		e = Ext.get(this.getId());
		e.mask(loadingMessage);
	}
	
	/**
	 * Stop loading on this panel
	 */
	public void stopLoading() {
		if (e != null)
			e.unmask();
	}
}
