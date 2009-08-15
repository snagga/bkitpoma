package com.bkitmobile.poma.client.ui.map;

import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;

public class Icon {
	
	public enum IconSize{
		SMALL, MEDIUM
	} 

	private static final Size SIZE_ICON_MEDIUM = Size.newInstance(39, 39);
	private static final Size SIZE_SHADOW_MEDIUM = Size.newInstance(78, 39);
	private static final Size SIZE_ICON_SMALL = Size.newInstance(26, 26);
	private static final Size SIZE_SHADOW_SMALL = Size.newInstance(52, 26);

	private static void setInfoWindowAnchorToDefault(
			com.google.gwt.maps.client.overlay.Icon icon) {
		icon.setInfoWindowAnchor(Point.newInstance(icon.getIconSize()
				.getWidth() >> 1, 0));
	}

	public static void setIconAttribute(
			com.google.gwt.maps.client.overlay.Icon icon, IconSize size) {
		Size iconSize = null;
		Size shadowSize = null;
		switch (size) {
		case SMALL:
			iconSize = SIZE_ICON_SMALL;
			shadowSize = SIZE_SHADOW_SMALL;
			break;
		case MEDIUM:
			iconSize = SIZE_ICON_MEDIUM;
			shadowSize = SIZE_SHADOW_MEDIUM;
			break;
		}
		
		icon.setIconSize(iconSize);
		if (icon.getShadowURL() != null) {
			icon.setShadowSize(shadowSize);
		}

		icon.setIconAnchor(Point.newInstance(iconSize.getWidth() >> 1, iconSize
				.getHeight()));
		setInfoWindowAnchorToDefault(icon);
	}
}
