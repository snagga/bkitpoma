package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.ui.client.MapToolbar.TrackedViewMode;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * @author hieu.hua
 */
public class MenuPanel extends LoadingPanel {
	private TrackedDetailPanel trackedDetailPanel;
	private TrackedPanel trackedPanel;

	private static MenuPanel menuPanel;

	public static MenuPanel getInstance() {
		return menuPanel == null ? new MenuPanel() : menuPanel;
	}

	public static final int WIDTH = 330;
	public static final int LABEL_HEIGHT = 20;
	private Panel noTrackedPanel;

	private MenuPanel() {
		super();
		menuPanel = this;
		init();
		addListener();
		layout();
	}

	private void init() {
		trackedPanel = TrackedPanel.getInstance();
		trackedDetailPanel = TrackedDetailPanel.getInstance();
		
		// a panel show when current user has no tracked
		noTrackedPanel = new Panel();
		noTrackedPanel.setBorder(false);
		noTrackedPanel.setBodyBorder(false);
		noTrackedPanel.setLayout(new VerticalLayout(5));
		noTrackedPanel.setButtonAlign(Position.CENTER);
		
		final Image imgNewTracked = new Image();
		imgNewTracked.setUrl("images/add_user.png");
		
		// hover effect for image
		imgNewTracked.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				imgNewTracked.addStyleName("button-newTracked-hover");
			}
		});
		imgNewTracked.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				BkitPoma.displayItemID(RegisterTrackedForm.getInstance().getId());
			}
		});
		imgNewTracked.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				imgNewTracked.removeStyleName("button-newTracked-hover");
			}
		});
		
		noTrackedPanel.add(new Label("Click the below picture to create new device"));
		noTrackedPanel.add(imgNewTracked);
	}
	
	private void addListener() {
		this.addListener(new PanelListenerAdapter() {
			@Override
			public void onShow(Component component) {
				super.onShow(component);
				_show();
			}
			
			@Override
			public void onRender(Component component) {
				// TODO Auto-generated method stub
				super.onRender(component);
				_show();
			}
		});
	}
	
	private void _show() {
		BkitPoma.startLoading("Loading ...");
		DatabaseService.Util.getInstance().getTrackedsByTracker(UserSettings.ctracker.getUsername(), new AsyncCallback<ServiceResult<ArrayList<CTracked>>>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				setActiveItemID(noTrackedPanel.getId());
				BkitPoma.stopLoading();
			}

			@Override
			public void onSuccess(ServiceResult<ArrayList<CTracked>> result) {
				if (result.isOK()) {
					if (result.getResult().size() != 0) {
						setActiveItemID(trackedPanel.getId());
					} else {
						setActiveItemID(noTrackedPanel.getId());
					} 
				} else {
					MessageBox.alert(result.getMessage());
				}
				BkitPoma.stopLoading();
			}
		});
	}

	private void layout() {
		this.setWidth(WIDTH);
		this.setTitle("Menu");
		this.setCollapsible(true);
		this.setLayout(new CardLayout());
		
		this.add(noTrackedPanel);
		this.add(trackedPanel);
		this.add(trackedDetailPanel);
	}

	public void detailPage() {
		this.setActiveItemID(trackedDetailPanel.getId());
		trackedDetailPanel.showDetail(
				TrackedPanel.getCurrentTrackedID());
		MapPanel.getInstance().getToolbar().setTrackedViewMode(
				TrackedViewMode.TRACK);
	}

	public void trackedPage() {
		this.setActiveItemID(trackedPanel.getId());
		BkitPoma.stopLoading();
		MapPanel.getInstance().getToolbar().setTrackedViewMode(
				TrackedViewMode.MULTI_TRACK);
		trackedPanel.addTrackedsToMap();
	}
	
	public void noTrackedPage() {
		this.setActiveItemID(noTrackedPanel.getId());
	}
}
