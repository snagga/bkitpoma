package com.bkitmobile.poma.client.ui.map;

import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;

public class Icon {
	private static final Size SIZE_ICON_MEDIUM = Size.newInstance(32, 32);
	private static final Size SIZE_SHADOW_MEDIUM = Size.newInstance(64, 32);
	private static final Size SIZE_ICON_SMALL = Size.newInstance(20, 20);
	private static final Size SIZE_SHADOW_SMALL = Size.newInstance(40, 20);

	private static void setInfoWindowAnchorToDefault(
			com.google.gwt.maps.client.overlay.Icon icon) {
		icon.setInfoWindowAnchor(Point.newInstance(
				icon.getIconSize().getWidth() >> 1, 0));
	}

	public static void setIconAttributeToDefault(
			com.google.gwt.maps.client.overlay.Icon icon) {
		icon.setIconSize(SIZE_ICON_SMALL);
		if (icon.getShadowURL() != null) {
			icon.setShadowSize(SIZE_SHADOW_SMALL);
		}
		Size iconSize = icon.getIconSize();

		icon.setIconAnchor(Point.newInstance(iconSize.getWidth() >> 1, iconSize
				.getHeight()));
		setInfoWindowAnchorToDefault(icon);
	}

}
