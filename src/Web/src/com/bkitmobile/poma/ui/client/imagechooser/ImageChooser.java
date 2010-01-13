/*
 * GWT-Ext Widget Library
 * Copyright 2007 - 2008, GWT-Ext LLC., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.bkitmobile.poma.ui.client.imagechooser;

import java.util.HashMap;

import com.bkitmobile.poma.localization.client.ImageChooserConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.core.XTemplate;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DataView;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DataViewListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

/**
 * Window allow user choose icon image for device to display in map
 * 
 * @author Tam Vo Minh
 * 
 */
public class ImageChooser extends Window {

	private ImageChooserConstants constants = GWT.create(ImageChooserConstants.class);

	private static final int TRANSPORT = 0;
	private static final int DOT = 1;
	private static final int STAR = 2;
	private static final int PLACE = 3;
	private static final int ABC = 4;
	private static final int OTHER = 5;

	private Panel eastPanel;
	private Panel centerPanel;

	private TextField searchField;
	private Button btnOK;

	// the callback once the ok is pressed. This indicates the caller what image
	// was selected
	private ImageChooserCallback callback;

	// the data about the selected image passed to the callback method
	private ImageData imageData;

	private HashMap imageMap;

	/**
	 * Array of view, each view prepresent for one category e.g: Transport, ABC,
	 * ...
	 */
	private DataView[] views;

	/**
	 * Array of field set, each field set to display one category
	 */
	private Panel[] arrPanels;
	private String[] arrFieldSetNames = { constants.arrFieldSetNames_Transport(),
			constants.arrFieldSetNames_Dot(), constants.arrFieldSetNames_Star(),
			constants.arrFieldSetNames_Place(), constants.arrFieldSetNames_ABC(),
			constants.arrFieldSetNames_Other() };

	/**
	 * Array of store
	 */
	private Store[] stores;

	private ComboBox cbCategory;

	private static ImageChooser imageChooser = null;

	/**
	 * @return static instance of this captcha window
	 */
	public static ImageChooser getInstance() {
		if (imageChooser != null)
			return imageChooser;
		return new ImageChooser();
	};

	/**
	 * Constructor: init some stuff
	 */
	public ImageChooser() {
		imageMap = new HashMap();
		setCloseAction(Window.HIDE);
		createStores();
		createView();
		imageChooser = this;
		initMainPanel();
	}

	/**
	 * Call it if you want do other things after apply
	 * 
	 * @param callback
	 */
	public void show(ImageChooserCallback callback) {
		this.callback = callback;
		super.show();
	}

	/**
	 * Initialize for main panel
	 */
	private void initMainPanel() {
		setLayout(new BorderLayout());
		addClass("ychooser-dlg");

		eastPanel = new Panel();
		eastPanel.setId("east-panel");
		eastPanel.setCollapsible(false);
		eastPanel.setWidth(150);
		eastPanel.setPaddings(5);

		centerPanel = new Panel();
		centerPanel.setId("ychooser-view");
		centerPanel.setCollapsible(false);
		centerPanel.setWidth(100);
		centerPanel.setHeight(200);
		centerPanel.setAutoScroll(true);
		centerPanel.setLayout(new AccordionLayout(true));

		arrPanels = new Panel[6];
		for (int i = 0; i < 6; i++) {
			arrPanels[i] = new Panel(arrFieldSetNames[i]);
			arrPanels[i].add(views[i]);
			arrPanels[i].setAutoScroll(true);
			centerPanel.add(arrPanels[i]);
		}

		add(getToolbar(), new BorderLayoutData(RegionPosition.NORTH));
		add(centerPanel, new BorderLayoutData(RegionPosition.CENTER));
		// add(eastPanel, new BorderLayoutData(RegionPosition.EAST));

		addOkButton();
		addCancelButton();
	}

	/**
	 * initialize for btnOK
	 */
	private void addOkButton() {
		btnOK = new Button(constants.btnOK_text());
		btnOK.disable();
		btnOK.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				hide();
				if (callback != null) {
					// pass the image data to the caller
					callback.onImageSelection(imageData);
				}
			}
		});
		addButton(btnOK);
	}

	/**
	 * Initialize for btnCancel
	 */
	private void addCancelButton() {
		Button btnCancel = new Button(constants.btnCancel_text());
		btnCancel.enable();
		btnCancel.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				hide();

			}
		});
		addButton(btnCancel);
		btnCancel.focus();
	}

	/**
	 * This method creates the toolbar for the dialog.
	 * 
	 * @return the toolbar just created to be added into the dialog
	 */
	private Toolbar getToolbar() {
		Toolbar tb = new Toolbar();
		searchField = new TextField();

		searchField.setId("ychooser-toolbar-searchfield");
		searchField.setMaxLength(60);
		searchField.setGrow(false);
		searchField.setSelectOnFocus(true);

		searchField.addListener(new FieldListenerAdapter() {

			/**
			 * This method will be called when special characters are pressed.
			 * This method is only interested in the enter key so that it can
			 * load the images. It simulates pressing the "Find" button.
			 */
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					displayThumbs(searchField.getValueAsString()); // load the
					// images in
					// the view
				}
			}
		});

		tb.addField(searchField);

		ToolbarButton tbBtnFind = new ToolbarButton(constants.tbBtnFind());
		tbBtnFind.setIconCls("search-icon");
		tbBtnFind.addListener(new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				displayThumbs(searchField.getValueAsString());
			}
		});
		tb.addButton(tbBtnFind);

		return tb;
	}

	/**
	 * This method create stores to Get icons detail to display
	 */
	private void createStores() {
		RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("name"), new IntegerFieldDef("size"),
				new StringFieldDef("url") });
		ArrayReader reader = new ArrayReader(recordDef);
		MemoryProxy[] dataProxy = new MemoryProxy[6];
		stores = new Store[6];

		/*
		 * Transport
		 */
		dataProxy[TRANSPORT] = new MemoryProxy(getTransportIcon());
		stores[TRANSPORT] = new Store(dataProxy[TRANSPORT], reader, true);
		stores[TRANSPORT].load();

		/*
		 * Dot
		 */
		dataProxy[DOT] = new MemoryProxy(getDotIcon());
		stores[DOT] = new Store(dataProxy[DOT], reader, true);
		stores[DOT].load();

		/*
		 * Star
		 */
		dataProxy[STAR] = new MemoryProxy(getStarIcon());
		stores[STAR] = new Store(dataProxy[STAR], reader, true);
		stores[STAR].load();

		/*
		 * Place
		 */
		dataProxy[PLACE] = new MemoryProxy(getPlaceIcon());
		stores[PLACE] = new Store(dataProxy[PLACE], reader, true);
		stores[PLACE].load();

		/*
		 * ABC
		 */
		dataProxy[ABC] = new MemoryProxy(getABCIcon());
		stores[ABC] = new Store(dataProxy[ABC], reader, true);
		stores[ABC].load();

		/*
		 * Other
		 */
		dataProxy[OTHER] = new MemoryProxy(getOtherIcon());
		stores[OTHER] = new Store(dataProxy[OTHER], reader, true);
		stores[OTHER].load();
	}

	/**
	 * This method creates the two view for displaying the images. The main view
	 * is the one that displays all the images to select. The second view
	 * displays the selected images with information about the image.
	 */
	private void createView() {
		// the thumb nail template for the main view
		String thumbTemplate[] = new String[] { "<tpl for='.'>",
				"<div class='thumb-wrap' id='{name}'>",
				"<div class='thumb'><img src='{url}' title='{name}'></div>",
				"<span>{shortName}</span></div>", "</tpl>",
				"<div class='x-clear'></div>" };

		// the detail template for the selected image
		String detailTemplate[] = new String[] { "<tpl for='.'>",
				"<div class='details'><img src='{url}'>",
				"<div class='details-info'><b>Image Name:</b>",
				"<span>{name}</span><b>Size:</b>",
				"<span>{sizeString}</span><b>Last Modified:</b>",
				"<span>{dateString}</span></div></div>", "</tpl>",
				"<div class='x-clear'></div>" };

		// compile the templates
		final XTemplate thumbsTemplate = new XTemplate(thumbTemplate);
		final XTemplate detailsTemplate = new XTemplate(detailTemplate);
		thumbsTemplate.compile();
		detailsTemplate.compile();

		views = new DataView[6];
		for (int i = 0; i < 6; i++) {
			// initialize the View using the thumb nail template
			views[i] = new DataView("div.thumb-wrap") {
				public void prepareData(Data data) {
					ImageData newImageData = null;
					String name = data.getProperty("name");
					String sizeString = data.getProperty("size");

					data.setProperty("shortName", Format.ellipsis(data
							.getProperty("name"), 15));
					data.setProperty("sizeString", sizeString);

					if (imageMap.containsKey(name)) {
						newImageData = (ImageData) imageMap.get(name);
					} else {
						newImageData = new ImageData();
						imageMap.put(name, newImageData);
					}

					newImageData.setFileName(name);
					newImageData.setName(name);
					newImageData.setSize(Long.parseLong(data
							.getProperty("size")));
					newImageData.setUrl(data.getProperty("url"));
				}
			};
			views[i].setSingleSelect(true);
			views[i].setTpl(thumbsTemplate);
			views[i].setAutoHeight(true);
			views[i].setStore(stores[i]);
			// view.setOverCls("x-view-over");

			// if there is no images that can be found, just output an message
			views[i]
					.setEmptyText(constants.views_setEmptyText());
			views[i].addListener(new DataViewListenerAdapter() {

				/**
				 * This method is called when a selection is made changing the
				 * previous selection
				 * 
				 * @params view the view that this selection is for
				 * @params selections a list of all selected items. There should
				 *         only be one as we only allow 1 selection.
				 */
				public void onSelectionChange(DataView component,
						Element[] selections) {

					// if there is a selection and show the details
					if (selections != null && selections.length > 0
							&& selections[0] != null) {
						// enable the ok button now there is a selection made
						btnOK.enable();
						String id = DOM
								.getElementAttribute(selections[0], "id");
						imageData = (ImageData) imageMap.get(id);

					} else {
						// no selection means the ok button should be disabled
						// and
						// the detail
						// area should be blanked out
						btnOK.disable();
					}
				}
			});

		}// end for
	}

	/**
	 * Displace thumbail of each image
	 * 
	 * @param findStr
	 */
	private void displayThumbs(String findStr) {
		for (int i = 0; i < 6; i++) {
			if (findStr == null || findStr.equals("")) {
				stores[i].clearFilter(true);
			} else {
				stores[i].filter("name", findStr, true);
			}
			views[i].refresh();
		}
	}

	/**
	 * Get icons related transport
	 * 
	 * @return
	 */
	private Object[][] getTransportIcon() {
		return new Object[][] {
				new Object[] { "bus", new Integer(1378),
						"http://bkitpoma.appspot.com/images/marker/bus.png" },
				new Object[] { "cabs", new Integer(1285),
						"http://bkitpoma.appspot.com/images/marker/cabs.png" },
				new Object[] { "cycling", new Integer(1408),
						"http://bkitpoma.appspot.com/images/marker/cycling.png" },
				new Object[] { "ferry", new Integer(1203),
						"http://bkitpoma.appspot.com/images/marker/ferry.png" },
				new Object[] { "helicopter", new Integer(1166),
						"http://bkitpoma.appspot.com/images/marker/helicopter.png" },
				new Object[] { "icon231", new Integer(2451),
						"http://bkitpoma.appspot.com/images/marker/icon231.png" },
				new Object[] { "icon39", new Integer(2445),
						"http://bkitpoma.appspot.com/images/marker/icon39.png" },
				new Object[] { "icon47", new Integer(1303),
						"http://bkitpoma.appspot.com/images/marker/icon47.png" },
				new Object[] { "icon48", new Integer(2593),
						"http://bkitpoma.appspot.com/images/marker/icon48.png" },
				new Object[] { "icon54", new Integer(2466),
						"http://bkitpoma.appspot.com/images/marker/icon54.png" },
				new Object[] { "icon56", new Integer(787),
						"http://bkitpoma.appspot.com/images/marker/icon56.png" },
				new Object[] { "icon621", new Integer(1030),
						"http://bkitpoma.appspot.com/images/marker/icon621.png" },
				new Object[] { "motorcycling", new Integer(1558),
						"http://bkitpoma.appspot.com/images/marker/motorcycling.png" },
				new Object[] { "plane", new Integer(1123),
						"http://bkitpoma.appspot.com/images/marker/plane.png" },
				new Object[] { "rail", new Integer(1311),
						"http://bkitpoma.appspot.com/images/marker/rail.png" },
				new Object[] { "subway", new Integer(2025),
						"http://bkitpoma.appspot.com/images/marker/subway.png" },
				new Object[] { "tram", new Integer(1373),
						"http://bkitpoma.appspot.com/images/marker/tram.png" },
				new Object[] { "truck", new Integer(963),
						"http://bkitpoma.appspot.com/images/marker/truck.png" },
				new Object[] { "wheel_chair_accessible", new Integer(1181),
						"http://bkitpoma.appspot.com/images/marker/wheel_chair_accessible.png" } };
	}

	/**
	 * Get icons related Star
	 * 
	 * @return
	 */
	private Object[][] getStarIcon() {
		return new Object[][] {
				new Object[] { "icon392", new Integer(2551),
						"http://bkitpoma.appspot.com/images/marker/icon392.png" },
				new Object[] { "icon41", new Integer(1523),
						"http://bkitpoma.appspot.com/images/marker/icon41.png" },
				new Object[] { "icon42", new Integer(1712),
						"http://bkitpoma.appspot.com/images/marker/icon42.png" },
				new Object[] { "icon43", new Integer(887),
						"http://bkitpoma.appspot.com/images/marker/icon43.png" },
				new Object[] { "icon58", new Integer(1560),
						"http://bkitpoma.appspot.com/images/marker/icon58.png" },
				new Object[] { "partly_cloudy", new Integer(1832),
						"http://bkitpoma.appspot.com/images/marker/partly_cloudy.png" },
				new Object[] { "snowflake_simple", new Integer(1507),
						"http://bkitpoma.appspot.com/images/marker/snowflake_simple.png" },
				new Object[] { "star10", new Integer(1699),
						"http://bkitpoma.appspot.com/images/marker/star10.png" },
				new Object[] { "star11", new Integer(1717),
						"http://bkitpoma.appspot.com/images/marker/star11.png" },
				new Object[] { "star2", new Integer(1718),
						"http://bkitpoma.appspot.com/images/marker/star2.png" },
				new Object[] { "star3", new Integer(1721),
						"http://bkitpoma.appspot.com/images/marker/star3.png" },
				new Object[] { "star4", new Integer(1728),
						"http://bkitpoma.appspot.com/images/marker/star4.png" },
				new Object[] { "star5", new Integer(1720),
						"http://bkitpoma.appspot.com/images/marker/star5.png" },
				new Object[] { "star6", new Integer(1711),
						"http://bkitpoma.appspot.com/images/marker/star6.png" },
				new Object[] { "star7", new Integer(1715),
						"http://bkitpoma.appspot.com/images/marker/star7.png" },
				new Object[] { "star8", new Integer(1702),
						"http://bkitpoma.appspot.com/images/marker/star8.png" },
				new Object[] { "star9", new Integer(1709),
						"http://bkitpoma.appspot.com/images/marker/star9.png" },
				new Object[] { "starfinishind", new Integer(1802),
						"http://bkitpoma.appspot.com/images/marker/starfinishind.png" },
				new Object[] { "startstart", new Integer(1886),
						"http://bkitpoma.appspot.com/images/marker/startstart.png" },
				new Object[] { "sunny", new Integer(1700),
						"http://bkitpoma.appspot.com/images/marker/sunny.png" } };
	}

	/**
	 * Get icons related Place
	 * 
	 * @return
	 */
	private Object[][] getPlaceIcon() {
		return new Object[][] {
				new Object[] { "arts", new Integer(1199),
						"http://bkitpoma.appspot.com/images/marker/arts.png" },
				new Object[] { "bar", new Integer(900),
						"http://bkitpoma.appspot.com/images/marker/bar.png" },
				new Object[] { "coffeehouse", new Integer(750),
						"http://bkitpoma.appspot.com/images/marker/coffeehouse.png" },
				new Object[] { "gas", new Integer(1218),
						"http://bkitpoma.appspot.com/images/marker/gas.png" },
				new Object[] { "grocerystore", new Integer(1088),
						"http://bkitpoma.appspot.com/images/marker/grocerystore.png" },
				new Object[] { "hospitals", new Integer(496),
						"http://bkitpoma.appspot.com/images/marker/hospitals.png" },
				new Object[] { "icon19", new Integer(2581),
						"http://bkitpoma.appspot.com/images/marker/icon19.png" },
				new Object[] { "icon20", new Integer(2410),
						"http://bkitpoma.appspot.com/images/marker/icon20.png" },
				new Object[] { "icon21", new Integer(2384),
						"http://bkitpoma.appspot.com/images/marker/icon21.png" },
				new Object[] { "icon34", new Integer(2399),
						"http://bkitpoma.appspot.com/images/marker/icon34.png" },
				new Object[] { "icon46", new Integer(629),
						"http://bkitpoma.appspot.com/images/marker/icon46.png" },
				new Object[] { "icon62", new Integer(1243),
						"http://bkitpoma.appspot.com/images/marker/icon62.png" },
				new Object[] { "movies", new Integer(1751),
						"http://bkitpoma.appspot.com/images/marker/movies.png" },
				new Object[] { "parkinglot", new Integer(933),
						"http://bkitpoma.appspot.com/images/marker/parkinglot.png" },
				new Object[] { "restaurant", new Integer(956),
						"http://bkitpoma.appspot.com/images/marker/restaurant.png" },
				new Object[] { "salon", new Integer(1452),
						"http://bkitpoma.appspot.com/images/marker/salon.png" },
				new Object[] { "shopping", new Integer(1502),
						"http://bkitpoma.appspot.com/images/marker/shopping.png" },
				new Object[] { "snack_bar", new Integer(950),
						"http://bkitpoma.appspot.com/images/marker/snack_bar.png" },
				new Object[] { "toilets", new Integer(1526),
						"http://bkitpoma.appspot.com/images/marker/toilets.png" } };
	}

	/**
	 * Get icons related Dot
	 * 
	 * @return
	 */
	private Object[][] getDotIcon() {
		return new Object[][] {
				new Object[] { "blue-dot", new Integer(1340),
						"http://bkitpoma.appspot.com/images/marker/blue-dot.png" },
				new Object[] { "flag", new Integer(1036),
						"http://bkitpoma.appspot.com/images/marker/flag.png" },
				new Object[] { "green-dot", new Integer(1221),
						"http://bkitpoma.appspot.com/images/marker/green-dot.png" },
				new Object[] { "icon13", new Integer(796),
						"http://bkitpoma.appspot.com/images/marker/icon13.png" },
				new Object[] { "iimm1-blue", new Integer(800),
						"http://bkitpoma.appspot.com/images/marker/iimm1-blue.png" },
				new Object[] { "iimm1-green", new Integer(810),
						"http://bkitpoma.appspot.com/images/marker/iimm1-green.png" },
				new Object[] { "iimm1-orange", new Integer(712),
						"http://bkitpoma.appspot.com/images/marker/iimm1-orange.png" },
				new Object[] { "iimm1-red", new Integer(656),
						"http://bkitpoma.appspot.com/images/marker/iimm1-red.png" },
				new Object[] { "ltblue-dot", new Integer(1301),
						"http://bkitpoma.appspot.com/images/marker/ltblue-dot.png" },
				new Object[] { "pink-dot", new Integer(1349),
						"http://bkitpoma.appspot.com/images/marker/pink-dot.png" },
				new Object[] { "purple-dot", new Integer(1356),
						"http://bkitpoma.appspot.com/images/marker/purple-dot.png" },
				new Object[] { "red-dot", new Integer(1337),
						"http://bkitpoma.appspot.com/images/marker/red-dot.png" },
				new Object[] { "yellow-dot", new Integer(1341),
						"http://bkitpoma.appspot.com/images/marker/yellow-dot.png" } };
	}

	/**
	 * Get other icons
	 * 
	 * @return
	 */
	private Object[][] getOtherIcon() {
		return new Object[][] {
				new Object[] { "campfire", new Integer(1357),
						"http://bkitpoma.appspot.com/images/marker/campfire.png" },
				new Object[] { "caution", new Integer(1035),
						"http://bkitpoma.appspot.com/images/marker/caution.png" },
				new Object[] { "earthquake", new Integer(1915),
						"http://bkitpoma.appspot.com/images/marker/earthquake.png" },
				new Object[] { "electronics", new Integer(1011),
						"http://bkitpoma.appspot.com/images/marker/electronics.png" },
				new Object[] { "fallingrocks", new Integer(1213),
						"http://bkitpoma.appspot.com/images/marker/fallingrocks.png" },
				new Object[] { "firedept", new Integer(1258),
						"http://bkitpoma.appspot.com/images/marker/firedept.png" },
				new Object[] { "homegardenbusiness", new Integer(843),
						"http://bkitpoma.appspot.com/images/marker/homegardenbusiness.png" },
				new Object[] { "icon0", new Integer(2479),
						"http://bkitpoma.appspot.com/images/marker/icon0.png" },
				new Object[] { "icon1", new Integer(2570),
						"http://bkitpoma.appspot.com/images/marker/icon1.png" },
				new Object[] { "icon10", new Integer(702),
						"http://bkitpoma.appspot.com/images/marker/icon10.png" },
				new Object[] { "icon11", new Integer(536),
						"http://bkitpoma.appspot.com/images/marker/icon11.png" },
				new Object[] { "icon12", new Integer(619),
						"http://bkitpoma.appspot.com/images/marker/icon12.png" },
				new Object[] { "icon14", new Integer(1848),
						"http://bkitpoma.appspot.com/images/marker/icon14.png" },
				new Object[] { "icon191", new Integer(2552),
						"http://bkitpoma.appspot.com/images/marker/icon191.png" },
				new Object[] { "icon2", new Integer(2600),
						"http://bkitpoma.appspot.com/images/marker/icon2.png" },
				new Object[] { "icon201", new Integer(2560),
						"http://bkitpoma.appspot.com/images/marker/icon201.png" },
				new Object[] { "icon211", new Integer(2291),
						"http://bkitpoma.appspot.com/images/marker/icon211.png" },
				new Object[] { "icon22", new Integer(2559),
						"http://bkitpoma.appspot.com/images/marker/icon22.png" },
				new Object[] { "icon221", new Integer(2568),
						"http://bkitpoma.appspot.com/images/marker/icon221.png" },
				new Object[] { "icon26", new Integer(867),
						"http://bkitpoma.appspot.com/images/marker/icon26.png" },
				new Object[] { "icon27", new Integer(1490),
						"http://bkitpoma.appspot.com/images/marker/icon27.png" },
				new Object[] { "icon3", new Integer(2524),
						"http://bkitpoma.appspot.com/images/marker/icon3.png" },
				new Object[] { "icon391", new Integer(2396),
						"http://bkitpoma.appspot.com/images/marker/icon391.png" },
				new Object[] { "icon4", new Integer(2524),
						"http://bkitpoma.appspot.com/images/marker/icon4.png" },
				new Object[] { "icon40", new Integer(994),
						"http://bkitpoma.appspot.com/images/marker/icon40.png" },
				new Object[] { "icon44", new Integer(1066),
						"http://bkitpoma.appspot.com/images/marker/icon44.png" },
				new Object[] { "icon45", new Integer(1766),
						"http://bkitpoma.appspot.com/images/marker/icon45.png" },
				new Object[] { "icon461", new Integer(1052),
						"http://bkitpoma.appspot.com/images/marker/icon461.png" },
				new Object[] { "icon49", new Integer(2667),
						"http://bkitpoma.appspot.com/images/marker/icon49.png" },
				new Object[] { "icon5", new Integer(2341),
						"http://bkitpoma.appspot.com/images/marker/icon5.png" },
				new Object[] { "icon50", new Integer(2620),
						"http://bkitpoma.appspot.com/images/marker/icon50.png" },
				new Object[] { "icon541", new Integer(2280),
						"http://bkitpoma.appspot.com/images/marker/icon541.png" },
				new Object[] { "icon55", new Integer(2564),
						"http://bkitpoma.appspot.com/images/marker/icon55.png" },
				new Object[] { "icon57", new Integer(839),
						"http://bkitpoma.appspot.com/images/marker/icon57.png" },
				new Object[] { "icon6", new Integer(2486),
						"http://bkitpoma.appspot.com/images/marker/icon6.png" },
				new Object[] { "icon7", new Integer(1960),
						"http://bkitpoma.appspot.com/images/marker/icon7.png" },
				new Object[] { "icon8", new Integer(1030),
						"http://bkitpoma.appspot.com/images/marker/icon8.png" },
				new Object[] { "icon9", new Integer(1299),
						"http://bkitpoma.appspot.com/images/marker/icon9.png" },
				new Object[] { "lodging", new Integer(998),
						"http://bkitpoma.appspot.com/images/marker/lodging.png" },
				new Object[] { "man", new Integer(994),
						"http://bkitpoma.appspot.com/images/marker/man.png" },
				new Object[] { "marina", new Integer(1117),
						"http://bkitpoma.appspot.com/images/marker/marina.png" },
				new Object[] { "mechanic", new Integer(848),
						"http://bkitpoma.appspot.com/images/marker/mechanic.png" },
				new Object[] { "phone", new Integer(1040),
						"http://bkitpoma.appspot.com/images/marker/phone.png" },
				new Object[] { "POI", new Integer(990),
						"http://bkitpoma.appspot.com/images/marker/POI.png" },
				new Object[] { "police", new Integer(1287),
						"http://bkitpoma.appspot.com/images/marker/police.png" },
				new Object[] { "rainy", new Integer(1372),
						"http://bkitpoma.appspot.com/images/marker/rainy.png" },
				new Object[] { "tree", new Integer(759),
						"http://bkitpoma.appspot.com/images/marker/tree.png" },
				new Object[] { "volcano", new Integer(1304),
						"http://bkitpoma.appspot.com/images/marker/volcano.png" },
				new Object[] { "water", new Integer(1966),
						"http://bkitpoma.appspot.com/images/marker/water.png" },
				new Object[] { "woman", new Integer(1081),
						"http://bkitpoma.appspot.com/images/marker/woman.png" }

		};
	}

	/**
	 * Get icons related Alphabetic
	 * @return
	 */
	private Object[][] getABCIcon() {
		return new Object[][] {
				new Object[] { "a", new Integer(2312),
						"http://bkitpoma.appspot.com/images/marker/a.png" },
				new Object[] { "b", new Integer(2371),
						"http://bkitpoma.appspot.com/images/marker/b.png" },
				new Object[] { "c", new Integer(2416),
						"http://bkitpoma.appspot.com/images/marker/c.png" },
				new Object[] { "d", new Integer(2330),
						"http://bkitpoma.appspot.com/images/marker/d.png" },
				new Object[] { "e", new Integer(2346),
						"http://bkitpoma.appspot.com/images/marker/e.png" },
				new Object[] { "f", new Integer(2341),
						"http://bkitpoma.appspot.com/images/marker/f.png" },
				new Object[] { "g", new Integer(2343),
						"http://bkitpoma.appspot.com/images/marker/g.png" },
				new Object[] { "h", new Integer(2277),
						"http://bkitpoma.appspot.com/images/marker/h.png" },
				new Object[] { "i", new Integer(2206),
						"http://bkitpoma.appspot.com/images/marker/i.png" },
				new Object[] { "j", new Integer(2344),
						"http://bkitpoma.appspot.com/images/marker/j.png" },
				new Object[] { "k", new Integer(2437),
						"http://bkitpoma.appspot.com/images/marker/k.png" },
				new Object[] { "l", new Integer(2285),
						"http://bkitpoma.appspot.com/images/marker/l.png" },
				new Object[] { "m", new Integer(2403),
						"http://bkitpoma.appspot.com/images/marker/m.png" },
				new Object[] { "n", new Integer(2401),
						"http://bkitpoma.appspot.com/images/marker/n.png" },
				new Object[] { "o", new Integer(2374),
						"http://bkitpoma.appspot.com/images/marker/o.png" },
				new Object[] { "p", new Integer(2295),
						"http://bkitpoma.appspot.com/images/marker/p.png" },
				new Object[] { "q", new Integer(1942),
						"http://bkitpoma.appspot.com/images/marker/q.png" },
				new Object[] { "r", new Integer(1955),
						"http://bkitpoma.appspot.com/images/marker/r.png" },
				new Object[] { "s", new Integer(1986),
						"http://bkitpoma.appspot.com/images/marker/s.png" },
				new Object[] { "t", new Integer(1961),
						"http://bkitpoma.appspot.com/images/marker/t.png" },
				new Object[] { "u", new Integer(1955),
						"http://bkitpoma.appspot.com/images/marker/u.png" },
				new Object[] { "v", new Integer(2045),
						"http://bkitpoma.appspot.com/images/marker/v.png" },
				new Object[] { "w", new Integer(1934),
						"http://bkitpoma.appspot.com/images/marker/w.png" },
				new Object[] { "x", new Integer(2152),
						"http://bkitpoma.appspot.com/images/marker/x.png" },
				new Object[] { "y", new Integer(2317),
						"http://bkitpoma.appspot.com/images/marker/y.png" },
				new Object[] { "z", new Integer(2363),
						"http://bkitpoma.appspot.com/images/marker/z.png" } };
	}

}