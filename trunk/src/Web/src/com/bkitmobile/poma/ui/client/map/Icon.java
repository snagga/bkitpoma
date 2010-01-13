package com.bkitmobile.poma.ui.client.map;

import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;

public class Icon {

	public enum IconSize {
		SMALL, MEDIUM
	}

	public enum IconAnchor {
		TOP_LEFT, TOP_RIGHT, TOP_CENTER, BASELINE_LEFT, BASELINE_RIGHT, BASELINE_CENTER, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
	}

	private static final Size SIZE_ICON_MEDIUM = Size.newInstance(26, 26);
	private static final Size SIZE_SHADOW_MEDIUM = Size.newInstance(52, 26);
	private static final Size SIZE_ICON_SMALL = Size.newInstance(18, 18);
	private static final Size SIZE_SHADOW_SMALL = Size.newInstance(32, 18);

	private static void setInfoWindowAnchorToDefault(
			com.google.gwt.maps.client.overlay.Icon icon) {
		icon.setInfoWindowAnchor(Point.newInstance(icon.getIconSize()
				.getWidth() >> 1, 0));
	}

	/**
	 * Set icon anchor and size
	 * 
	 * @param icon
	 * @param size
	 * @param anchor
	 */
	public static void setIconAttribute(
			com.google.gwt.maps.client.overlay.Icon icon, IconSize size,
			IconAnchor anchor) {
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

		Point anchorPoint = null;
		switch (anchor) {
		case BASELINE_CENTER:
			anchorPoint = Point.newInstance(iconSize.getWidth() >> 1, iconSize
					.getHeight() >> 1);
			break;
		case BASELINE_LEFT:
			anchorPoint = Point.newInstance(0, iconSize.getHeight() >> 1);
			break;
		case BASELINE_RIGHT:
			anchorPoint = Point.newInstance(iconSize.getWidth(), iconSize
					.getHeight() >> 1);
			break;
		case BOTTOM_CENTER:
			anchorPoint = Point.newInstance(iconSize.getWidth() >> 1, iconSize
					.getHeight());
			break;
		case BOTTOM_LEFT:
			anchorPoint = Point.newInstance(0, iconSize.getHeight());
			break;
		case BOTTOM_RIGHT:
			anchorPoint = Point.newInstance(iconSize.getWidth(), iconSize
					.getHeight());
			break;
		case TOP_CENTER:
			anchorPoint = Point.newInstance(iconSize.getWidth() >> 1, 0);
			break;
		case TOP_LEFT:
			anchorPoint = Point.newInstance(0, 0);
			break;
		case TOP_RIGHT:
			anchorPoint = Point.newInstance(iconSize.getWidth(), 0);
			break;
		}

		icon.setIconAnchor(anchorPoint);
		setInfoWindowAnchorToDefault(icon);
	}
}
