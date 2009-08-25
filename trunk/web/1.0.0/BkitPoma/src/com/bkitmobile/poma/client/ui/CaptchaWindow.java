package com.bkitmobile.poma.client.ui;

import com.bkitmobile.poma.client.captcha.RecaptchaService;
import com.bkitmobile.poma.client.captcha.RecaptchaWidget;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;

public class CaptchaWindow extends Window {
	
	private RecaptchaWidget wgCaptcha;
	private boolean value = false;
	private static boolean firstShow = true;
	private static CaptchaWindow captchaWindow = null;
	
	public static CaptchaWindow getInstance() {
//		if (captchaWindow != null) return captchaWindow;
		return new CaptchaWindow();
	}
	
	private CaptchaWindow() {
		captchaWindow = this;
		/*
		 * init
		 */
		setButtonAlign(Position.CENTER);
		setModal(true);
//		setSize(350, 300);
		setWidth(350);
		setClosable(true);
		setCloseAction(Window.HIDE);
		
		setTitle("Enter captcha message");
		setPlain(true);
		setFrame(true);
		setDraggable(false);
		setResizable(false);
		addEvent("validate");
		
		addListener(new PanelListenerAdapter() {
			@Override
			public void onHide(Component component) {
				// TODO Auto-generated method stub
//				value = false;
				fireEvent("validate");
			}
		});
		
		
		/*
		 * Captcha
		 */
		wgCaptcha = new RecaptchaWidget("6LdakQcAAAAAALX2JUFtsjbPTV0TcAkMhQY8iMkS");
		
		add(wgCaptcha);
		
		
		/*
		 * Validate button
		 */
		addButton(new Button("Validate captcha", new ButtonListenerAdapter() {
			
			@Override
			public void onClick(Button button, EventObject e) {
				RecaptchaService.Util.getInstance().verifyChallenge(
					wgCaptcha.getChallenge(), 
					wgCaptcha.getResponse(), 
					new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
							MessageBox.alert("Validate captcha error", caught.toString());
							wgCaptcha.reload();
						}

						@Override
						public void onSuccess(Boolean result) {
							
							if (result) {
								value = true;
								hide();
							} else {
								value = false;
								MessageBox.alert("Wrong captcha message typed");
								wgCaptcha.reload();
							}
							
							fireEvent("validate");
						}
				});
			}
		}));
		
		/*
		 * Show this window
		 */
		center();
//		show();
	}
	
	public boolean getValue() {
		return value;
	}
	
	public void show() {
		if (firstShow) {
			super.show();
			firstShow = false;
		} else {
			wgCaptcha.reload();
			super.show();
		}
	}
}
