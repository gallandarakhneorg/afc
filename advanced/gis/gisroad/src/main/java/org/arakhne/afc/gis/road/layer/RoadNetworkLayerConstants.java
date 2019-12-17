/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.gis.road.layer;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.primitive.RoadType;

/**
 * Constants for the road network layer API.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public final class RoadNetworkLayerConstants {

	/** Defaults colors for the different types of roads. The array
	 * contains pairs of colors: one for the inner part, and one for the borders.
	 */
	public static final int[] DEFAULT_ROAD_COLORS;

	/** Default flag indicating if the internal data structures
	 * of the road networks may be drawn on the displayers.
	 */
	public static final boolean DEFAULT_ROAD_INTERN_DRAWING = false;

	/** Color of the internal data structures
	 * of the road networks when they are drawn on the displayers.
	 */
	public static final int DEFAULT_ROAD_INTERN_COLOR = 0xFF00FF;

	/** Default size of the road borders (in pixels).
	 */
	public static final int DEFAULT_BORDER_SIZE = 1;

	static {
		DEFAULT_ROAD_COLORS = new int[RoadType.values().length * 2];
		DEFAULT_ROAD_COLORS[2 * RoadType.FREEWAY.ordinal()] = 0xffc345;
		DEFAULT_ROAD_COLORS[2 * RoadType.FREEWAY.ordinal() + 1] = 0xe3a339;
		DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal()] = 0xfffd8b;
		DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal() + 1] = 0xe6d8a3;
		DEFAULT_ROAD_COLORS[2 * RoadType.LOCAL_ROAD.ordinal()] = 0xffffff;
		DEFAULT_ROAD_COLORS[2 * RoadType.LOCAL_ROAD.ordinal() + 1] = 0x505050;
		DEFAULT_ROAD_COLORS[2 * RoadType.BIKEWAY.ordinal()] = 0xfaf9f6;
		DEFAULT_ROAD_COLORS[2 * RoadType.BIKEWAY.ordinal() + 1] = 0xe3e3e3;
		DEFAULT_ROAD_COLORS[2 * RoadType.INTERCHANGE_RAMP.ordinal()] = DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal()];
		DEFAULT_ROAD_COLORS[2 * RoadType.INTERCHANGE_RAMP.ordinal() + 1] =
				DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal() + 1];
		DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_URBAN_AXIS.ordinal()] = DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal()];
		DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_URBAN_AXIS.ordinal() + 1] =
				DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal() + 1];
		DEFAULT_ROAD_COLORS[2 * RoadType.OTHER.ordinal()] = DEFAULT_ROAD_COLORS[2 * RoadType.LOCAL_ROAD.ordinal()];
		DEFAULT_ROAD_COLORS[2 * RoadType.OTHER.ordinal() + 1] = DEFAULT_ROAD_COLORS[2 * RoadType.LOCAL_ROAD.ordinal() + 1];
		DEFAULT_ROAD_COLORS[2 * RoadType.PRIVACY_PATH.ordinal()] = DEFAULT_ROAD_COLORS[2 * RoadType.BIKEWAY.ordinal()];
		DEFAULT_ROAD_COLORS[2 * RoadType.PRIVACY_PATH.ordinal() + 1] = DEFAULT_ROAD_COLORS[2 * RoadType.BIKEWAY.ordinal() + 1];
		DEFAULT_ROAD_COLORS[2 * RoadType.SECONDARY_ROAD.ordinal()] = DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal()];
		DEFAULT_ROAD_COLORS[2 * RoadType.SECONDARY_ROAD.ordinal() + 1] =
				DEFAULT_ROAD_COLORS[2 * RoadType.MAJOR_ROAD.ordinal() + 1];
		DEFAULT_ROAD_COLORS[2 * RoadType.TRACK.ordinal()] = DEFAULT_ROAD_COLORS[2 * RoadType.BIKEWAY.ordinal()];
		DEFAULT_ROAD_COLORS[2 * RoadType.TRACK.ordinal() + 1] = DEFAULT_ROAD_COLORS[2 * RoadType.BIKEWAY.ordinal() + 1];
	}

	private RoadNetworkLayerConstants() {
		//
	}

	/** Replies the preferences for the road network layer.
	 *
	 * @return the preferences.
	 */
	@Pure
	public static Preferences getPreferences() {
		return Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
	}

	/** Replies the preferred size of the roazd borders (in pixels).
	 *
	 * @return size of the road borders.
	 */
	@Pure
	public static int getPreferredRoadBorderSize() {
		return getPreferredRoadBorderSize(true);
	}

	/** Replies the preferred size of the roazd borders (in pixels).
	 *
	 * @param useSystemValue indicates if the <code>null</code> value
	 *     must be replaced by the default system value:
	 * {@link #DEFAULT_BORDER_SIZE}.
	 * @return size of the road borders.
	 */
	@Pure
	public static Integer getPreferredRoadBorderSize(boolean useSystemValue) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			final int size = prefs.getInt("ROAD_BORDER_SIZE", -1); //$NON-NLS-1$
			if (size >= 0) {
				return size;
			}
		}
		if (useSystemValue) {
			return DEFAULT_BORDER_SIZE;
		}
		return null;
	}

	/** Set the preferred size of the roazd borders (in pixels).
	 *
	 * @param size is the preferred size of the roazd borders (in pixels).
	 */
	public static void setPreferredRoadBorderSize(Integer size) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			if (size == null || size.intValue() == DEFAULT_BORDER_SIZE) {
				prefs.remove("ROAD_BORDER_SIZE"); //$NON-NLS-1$
			} else {
				prefs.putInt("ROAD_BORDER_SIZE", size.intValue()); //$NON-NLS-1$
			}
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies the preferred color to draw the content of the roads of the given type.
	 *
	 * @param roadType is the type of road for which the color should be replied.
	 * @return the color of the inner parts of the roads.
	 */
	@Pure
	public static int getPreferredRoadColor(RoadType roadType) {
		return getPreferredRoadColor(roadType, true);
	}

	/** Replies the preferred color to draw the content of the roads of the given type.
	 *
	 * @param roadType is the type of road for which the color should be replied.
	 * @param useSystemValue indicates if the <code>null</code> value
	 *     must be replaced by the default system value in
	 * {@link #DEFAULT_ROAD_COLORS} (if <code>true</code>).
	 * @return the color of the inner parts of the roads.
	 */
	@Pure
	public static int  getPreferredRoadColor(RoadType roadType, boolean useSystemValue) {
		final RoadType rt = roadType == null ? RoadType.OTHER : roadType;
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			final String str = prefs.get("ROAD_COLOR_" + rt.name().toUpperCase(), null); //$NON-NLS-1$
			if (str != null) {
				try {
					return Integer.valueOf(str);
				} catch (Throwable exception) {
					//
				}
			}
		}
		if (useSystemValue) {
			return DEFAULT_ROAD_COLORS[rt.ordinal() * 2];
		}
		return 0;
	}

	/** Set the preferred color to draw the content of the roads of the given type.
	 *
	 * @param roadType is the type of road for which the color should be replied.
	 * @param color is the color or <code>null</code> to restore the default value.
	 */
	public static void setPreferredRoadColor(RoadType roadType, Integer color) {
		final RoadType rt = roadType == null ? RoadType.OTHER : roadType;
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			if (color == null || color.intValue() == DEFAULT_ROAD_COLORS[rt.ordinal() * 2]) {
				prefs.remove("ROAD_COLOR_" + rt.name().toUpperCase()); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_COLOR_" + rt.name().toUpperCase(), Integer.toString(color.intValue())); //$NON-NLS-1$
			}
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies the preferred color to draw the borders of the roads of the given type.
	 *
	 * @param roadType is the type of road for which the color should be replied.
	 * @return the color of the inner parts of the roads.
	 */
	@Pure
	public static int getPreferredBorderColor(RoadType roadType) {
		return getPreferredBorderColor(roadType, true);
	}

	/** Replies the preferred color to draw the borders of the roads of the given type.
	 *
	 * @param roadType is the type of road for which the color should be replied.
	 * @param useSystemValue indicates if the <code>null</code> value
	 *     must be replaced by the default system value in
	 * {@link #DEFAULT_ROAD_COLORS} (if <code>true</code>).
	 * @return the color of the inner parts of the roads.
	 */
	@Pure
	public static int getPreferredBorderColor(RoadType roadType, boolean useSystemValue) {
		final RoadType rt = roadType == null ? RoadType.OTHER : roadType;
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			final String str = prefs.get("BORDER_COLOR_" + rt.name().toUpperCase(), null); //$NON-NLS-1$
			if (str != null) {
				try {
					return Integer.valueOf(str);
				} catch (Throwable exception) {
					//
				}
			}
		}
		if (useSystemValue) {
			return DEFAULT_ROAD_COLORS[rt.ordinal() * 2 + 1];
		}
		return 0;
	}

	/** Set the preferred color to draw the borders of the roads of the given type.
	 *
	 * @param roadType is the type of road for which the color should be replied.
	 * @param color is the color or <code>null</code> to restore the default value.
	 */
	public static void setPreferredBorderColor(RoadType roadType, Integer color) {
		final RoadType rt = roadType == null ? RoadType.OTHER : roadType;
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			if (color == null || color.intValue() == DEFAULT_ROAD_COLORS[rt.ordinal() * 2 + 1]) {
				prefs.remove("BORDER_COLOR_" + rt.name().toUpperCase()); //$NON-NLS-1$
			} else {
				prefs.put("BORDER_COLOR_" + rt.name().toUpperCase(), Integer.toString(color.intValue())); //$NON-NLS-1$
			}
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies if the internal data structure used to store the road network
	 * may be drawn on the displayers.
	 *
	 * @return <code>true</code> if the internal data structures may be
	 *     drawn on the displayers, otherwise <code>false</code>.
	 */
	@Pure
	public static boolean getPreferredRoadInternDrawing() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			return prefs.getBoolean("ROAD_INTERN_DRAWING", DEFAULT_ROAD_INTERN_DRAWING); //$NON-NLS-1$
		}
		return DEFAULT_ROAD_INTERN_DRAWING;
	}

	/** Set if the internal data structure used to store the road network
	 * may be drawn on the displayers.
	 *
	 * @param draw is <code>true</code> if the internal data structures may be
	 *     drawn on the displayers, <code>false</code> if the internal data structures
	 *     may not be drawn on the displayers, <code>null</code> to restore
	 *     the default value given by {@link #DEFAULT_ROAD_INTERN_DRAWING}.
	 */
	public static void setPreferredRoadInternDrawing(Boolean draw) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			if (draw == null) {
				prefs.remove("ROAD_INTERN_DRAWING"); //$NON-NLS-1$
			} else {
				prefs.putBoolean("ROAD_INTERN_DRAWING", draw); //$NON-NLS-1$
			}
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies the color of the internal data structures used when they
	 * are drawn on the displayers.
	 *
	 * @return the color of the internal data structures, never <code>null</code>.
	 */
	@Pure
	public static int getPreferredRoadInternColor() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			final String color = prefs.get("ROAD_INTERN_COLOR", null); //$NON-NLS-1$
			if (color != null) {
				try {
					return Integer.valueOf(color);
				} catch (Throwable exception) {
					//
				}
			}
		}
		return DEFAULT_ROAD_INTERN_COLOR;
	}

	/** Set the color of the internal data structures used when they
	 * are drawn on the displayers.
	 *
	 * @param color is the color of the internal data structures, never <code>null</code>.
	 */
	public static void setPreferredRoadInternColor(Integer color) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkLayerConstants.class);
		if (prefs != null) {
			if (color == null) {
				prefs.remove("ROAD_INTERN_COLOR"); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_INTERN_COLOR", Integer.toString(color.intValue())); //$NON-NLS-1$
			}
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}
}
