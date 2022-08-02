/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.gis.road.primitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Constants for the road network API.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class RoadNetworkConstants {

	/** By default the road network API disallows cycles during graph iterations.
	 */
	public static final boolean ENABLE_GRAPH_ITERATION_CYCLES = false;

	/** By default the road network API supports oriented graph segments.
	 */
	public static final boolean ENABLE_ORIENTED_SEGMENTS = true;

	/** This is the name of the attribute describing the width of a road.
	 */
	public static final String DEFAULT_ATTR_ROAD_WIDTH = "largeur"; //$NON-NLS-1$

	/** This is the name of the attribute describing the count of lanes of a road.
	 */
	public static final String DEFAULT_ATTR_LANE_COUNT = "nb_voies"; //$NON-NLS-1$

	/** This is the name of the attribute describing the description of the traffic direction.
	 */
	public static final String DEFAULT_ATTR_TRAFFIC_DIRECTION = "sens"; //$NON-NLS-1$

	/** This is the name of the attribute describing the type of the road.
	 */
	public static final String DEFAULT_ATTR_ROAD_TYPE = "vocation"; //$NON-NLS-1$

	/** This is the name of the attribute describing the name of the road.
	 */
	public static final String DEFAULT_ATTR_ROAD_NAME = "nom_rue_d"; //$NON-NLS-1$

	/** This is the name of the attribute describing the number of the road.
	 */
	public static final String DEFAULT_ATTR_ROAD_NUMBER = "numero"; //$NON-NLS-1$

	/** Default lane count in a road segment.
	 */
	public static final int DEFAULT_LANE_COUNT = 2;

	/** Default lane width (in meters).
	 */
	public static final double DEFAULT_LANE_WIDTH = 3.3;

	/** Color of the internal data structures
	 * of the road networks when they are drawn on the displayers.
	 */
	public static final int DEFAULT_ROAD_INTERN_COLOR = 0xFF00FF;

	/** Balue for the double-way road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_DOUBLE_WAY_TRAFFIC_DIRECTION_0 = "00"; //$NON-NLS-1$

	/** Value for the double-way road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_DOUBLE_WAY_TRAFFIC_DIRECTION_1 = "Double"; //$NON-NLS-1$

	/** Value for the no-entry road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_NO_ENTRY_TRAFFIC_DIRECTION_0 = "16"; //$NON-NLS-1$

	/** Value for the no-entry road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_NO_ENTRY_TRAFFIC_DIRECTION_1 = "Inverse"; //$NON-NLS-1$

	/** Value for the no-way road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_NO_WAY_TRAFFIC_DIRECTION_0 = "14"; //$NON-NLS-1$

	/** Value for the no-way road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_NO_WAY_TRAFFIC_DIRECTION_1 = "Aucun"; //$NON-NLS-1$

	/** Value for the one-way road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_ONE_WAY_TRAFFIC_DIRECTION_0 = "09"; //$NON-NLS-1$

	/** Value for the one-way road (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_ONE_WAY_TRAFFIC_DIRECTION_1 = "Direct"; //$NON-NLS-1$

	/** Value for the pedestrian/other roads (undefined in the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_OTHER_ROAD_TYPE = "99"; //$NON-NLS-1$

	/** Value for the track roads (undefined in the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_TRACK_ROAD_TYPE = "98"; //$NON-NLS-1$

	/** Value for the privacy paths or roads (undefined in the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_PRIVACY_ROAD_TYPE = "95"; //$NON-NLS-1$

	/** Value for the bikeways (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_BIKEWAY_ROAD_TYPE = "10"; //$NON-NLS-1$

	/** Value for the local roads (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_LOCAL_ROAD_ROAD_TYPE = "07"; //$NON-NLS-1$

	/** Value for the interchange ramps (from the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_INTERCHANGE_RAMP_ROAD_TYPE = "08"; //$NON-NLS-1$

	/** Value for the major urban roads (undefined in the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_MAJOR_URBAN_ROAD_TYPE = "97"; //$NON-NLS-1$

	/** Value for the secondary roads (undefined in the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_SECONDARY_ROAD_TYPE = "06"; //$NON-NLS-1$

	/** Value for the major roads (undefined in the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_MAJOR_ROAD_TYPE = "02"; //$NON-NLS-1$

	/** Value for the freeways (undefined in the French
	 * DBCarto standards).
	 */
	public static final String DEFAULT_FREEWAY_ROAD_TYPE = "01"; //$NON-NLS-1$

	/** This is the hard-coded distance below which roads may be connected (in meters).
	 */
	public static final double DEFAULT_ROAD_CONNECTION_DISTANCE = .5;

	private RoadNetworkConstants() {
		//
	}

	/** Replies the preferences for the road network.
	 *
	 * @return the preferences.
	 */
	@Pure
	public static Preferences getPreferences() {
		return Preferences.userNodeForPackage(RoadNetworkConstants.class);
	}

	/** Replies the preferred name for the width of the roads.
	 *
	 * @return the preferred name for the width of the roads.
	 * @see #DEFAULT_ATTR_ROAD_WIDTH
	 */
	@Pure
	public static String getPreferredAttributeNameForRoadWidth() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.get("ROAD_WIDTH_ATTR_NAME", DEFAULT_ATTR_ROAD_WIDTH); //$NON-NLS-1$
		}
		return DEFAULT_ATTR_ROAD_WIDTH;
	}

	/** Set the preferred name for the width of the roads.
	 *
	 * @param name is the preferred name for the width of the roads.
	 * @see #DEFAULT_ATTR_ROAD_WIDTH
	 */
	public static void setPreferredAttributeNameForRoadWidth(String name) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (name == null || "".equals(name) || DEFAULT_ATTR_ROAD_WIDTH.equalsIgnoreCase(name)) { //$NON-NLS-1$
				prefs.remove("ROAD_WIDTH_ATTR_NAME"); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_WIDTH_ATTR_NAME", name); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred name for the number of lanes of the roads.
	 *
	 * @return the preferred name for the number of lanes of the roads.
	 * @see #DEFAULT_ATTR_LANE_COUNT
	 */
	@Pure
	public static String getPreferredAttributeNameForLaneCount() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.get("LANE_COUNT_ATTR_NAME", DEFAULT_ATTR_LANE_COUNT); //$NON-NLS-1$
		}
		return DEFAULT_ATTR_LANE_COUNT;
	}

	/** Set the preferred name for the number of lanes of the roads.
	 *
	 * @param name is the preferred name for the number of lanes of the roads.
	 * @see #DEFAULT_ATTR_LANE_COUNT
	 */
	public static void setPreferredAttributeNameForLaneCount(String name) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (name == null || "".equals(name) || DEFAULT_ATTR_LANE_COUNT.equalsIgnoreCase(name)) { //$NON-NLS-1$
				prefs.remove("LANE_COUNT_ATTR_NAME"); //$NON-NLS-1$
			} else {
				prefs.put("LANE_COUNT_ATTR_NAME", name); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred number of lanes for a road segment.
	 *
	 * @return the preferred number of lanes for a road segment.
	 * @see #DEFAULT_LANE_COUNT
	 */
	@Pure
	public static int getPreferredLaneCount() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.getInt("LANE_COUNT", DEFAULT_LANE_COUNT); //$NON-NLS-1$
		}
		return DEFAULT_LANE_COUNT;
	}

	/** Set the preferred number of lanes for a road segment.
	 *
	 * @param count is the preferred number of lanes for a road segment.
	 * @see #DEFAULT_LANE_COUNT
	 */
	public static void setPreferredLaneCount(int count) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (count < 0) {
				prefs.remove("LANE_COUNT"); //$NON-NLS-1$
			} else {
				prefs.putInt("LANE_COUNT", count); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred width of a lane for a road segment.
	 *
	 * @return the preferred width of a lane for a road segment, in meters.
	 * @see #DEFAULT_LANE_WIDTH
	 */
	@Pure
	public static double getPreferredLaneWidth() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.getDouble("LANE_WIDTH", DEFAULT_LANE_WIDTH); //$NON-NLS-1$
		}
		return DEFAULT_LANE_WIDTH;
	}

	/** Set the preferred width of a lane for a road segment.
	 *
	 * @param width is the preferred width of a lane for a road segment, in meters.
	 * @see #DEFAULT_LANE_WIDTH
	 */
	public static void setPreferredLaneWidth(double width) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (width <= 0) {
				prefs.remove("LANE_WIDTH"); //$NON-NLS-1$
			} else {
				prefs.putDouble("LANE_WIDTH", width); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred side of traffic for vehicles on road segments.
	 *
	 * @return the preferred side of traffic for vehicles on road segments.
	 */
	@Pure
	public static LegalTrafficSide getPreferredLegalTrafficSide() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		String name = null;
		if (prefs != null) {
			name = prefs.get("LEGAL_TRAFFIC_SIDE", null); //$NON-NLS-1$
		}
		if (name != null) {
			LegalTrafficSide side;
			try {
				side = LegalTrafficSide.valueOf(name);
			} catch (Throwable exception) {
				side = null;
			}
			if (side != null) {
				return side;
			}
		}
		return LegalTrafficSide.getCurrent();
	}

	/** Set the preferred side of traffic for vehicles on road segments.
	 *
	 * @param side is the preferred side of traffic for vehicles on road segments.
	 */
	public static void setPreferredLegalTrafficSide(LegalTrafficSide side) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (side == null) {
				prefs.remove("LEGAL_TRAFFIC_SIDE"); //$NON-NLS-1$
			} else {
				prefs.put("LEGAL_TRAFFIC_SIDE", side.name()); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred name for the traffic direction on the roads.
	 *
	 * @return the preferred name for the traffic direction on the roads.
	 * @see #DEFAULT_ATTR_TRAFFIC_DIRECTION
	 */
	@Pure
	public static String getPreferredAttributeNameForTrafficDirection() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.get("TRAFFIC_DIRECTION_ATTR_NAME", DEFAULT_ATTR_TRAFFIC_DIRECTION); //$NON-NLS-1$
		}
		return DEFAULT_ATTR_TRAFFIC_DIRECTION;
	}

	/** Set the preferred name for the traffic direction on the roads.
	 *
	 * @param name is the preferred name for the traffic direction on the roads.
	 * @see #DEFAULT_ATTR_TRAFFIC_DIRECTION
	 */
	public static void setPreferredAttributeNameForTrafficDirection(String name) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (name == null || "".equals(name) || DEFAULT_ATTR_TRAFFIC_DIRECTION.equalsIgnoreCase(name)) { //$NON-NLS-1$
				prefs.remove("TRAFFIC_DIRECTION_ATTR_NAME"); //$NON-NLS-1$
			} else {
				prefs.put("TRAFFIC_DIRECTION_ATTR_NAME", name); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred value of traffic direction
	 * used in the attributes for the traffic direction on the roads.
	 *
	 * @param direction a direction.
	 * @param index is the index of the supported string to reply
	 * @return the preferred name for the traffic direction on the roads,
	 *     or <code>null</code> if there is no string value for the given index.
	 */
	@Pure
	public static String getPreferredAttributeValueForTrafficDirection(TrafficDirection direction, int index) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			final StringBuilder b = new StringBuilder();
			b.append("TRAFFIC_DIRECTION_VALUE_"); //$NON-NLS-1$
			b.append(direction.name());
			b.append("_"); //$NON-NLS-1$
			b.append(index);
			final String v = prefs.get(b.toString(), null);
			if (v != null && !"".equals(v)) { //$NON-NLS-1$
				return v;
			}
		}
		try {
			return getSystemDefault(direction, index);
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			return null;
		}
	}

	/** Replies the preferred values of traffic direction
	 * used in the attributes for the traffic direction on the roads.
	 *
	 * @param direction aa direction
	 * @return the preferred names for the traffic direction on the roads, never <code>null</code>.
	 */
	@Pure
	public static List<String> getPreferredAttributeValuesForTrafficDirection(TrafficDirection direction) {
		final List<String> array = new ArrayList<>();
		int i = 0;
		String value = getPreferredAttributeValueForTrafficDirection(direction, i);
		while (value != null) {
			array.add(value);
			value = getPreferredAttributeValueForTrafficDirection(direction, ++i);
		}
		return array;
	}

	/** Replies the system default string-value for the traffic direction, at the
	 * given index in the list of system default values.
	 *
	 * @param direction is the direction for which the string value should be replied
	 * @param valueIndex is the position of the string-value.
	 * @return the string-value at the given position, never <code>null</code>
	 * @throws IndexOutOfBoundsException is the given index is wrong.
	 * @throws IllegalArgumentException is the given direction is wrong.
	 */
	@Pure
	public static String getSystemDefault(TrafficDirection direction, int valueIndex) {
		// Values from the IGN-BDCarto standard
		switch (direction) {
		case DOUBLE_WAY:
			switch (valueIndex) {
			case 0:
				return DEFAULT_DOUBLE_WAY_TRAFFIC_DIRECTION_0;
			case 1:
				return DEFAULT_DOUBLE_WAY_TRAFFIC_DIRECTION_1;
			default:
				throw new IndexOutOfBoundsException();
			}
		case NO_ENTRY:
			switch (valueIndex) {
			case 0:
				return DEFAULT_NO_ENTRY_TRAFFIC_DIRECTION_0;
			case 1:
				return DEFAULT_NO_ENTRY_TRAFFIC_DIRECTION_1;
			default:
				throw new IndexOutOfBoundsException();
			}
		case NO_WAY:
			switch (valueIndex) {
			case 0:
				return DEFAULT_NO_WAY_TRAFFIC_DIRECTION_0;
			case 1:
				return DEFAULT_NO_WAY_TRAFFIC_DIRECTION_1;
			default:
				throw new IndexOutOfBoundsException();
			}
		case ONE_WAY:
			switch (valueIndex) {
			case 0:
				return DEFAULT_ONE_WAY_TRAFFIC_DIRECTION_0;
			case 1:
				return DEFAULT_ONE_WAY_TRAFFIC_DIRECTION_1;
			default:
				throw new IndexOutOfBoundsException();
			}
		default:
		}
		throw new IllegalArgumentException();
	}

	/** Replies the system-default value for the type of
	 * road.
	 *
	 * @param type a type
	 * @return the system-default value.
	 * @throws IllegalArgumentException if bad type.
	 */
	@Pure
	public static String getSystemDefault(RoadType type) {
		switch (type) {
		case OTHER:
			return DEFAULT_OTHER_ROAD_TYPE;
		case PRIVACY_PATH:
			return DEFAULT_PRIVACY_ROAD_TYPE;
		case TRACK:
			return DEFAULT_TRACK_ROAD_TYPE;
		case BIKEWAY:
			return DEFAULT_BIKEWAY_ROAD_TYPE;
		case LOCAL_ROAD:
			return DEFAULT_LOCAL_ROAD_ROAD_TYPE;
		case INTERCHANGE_RAMP:
			return DEFAULT_INTERCHANGE_RAMP_ROAD_TYPE;
		case MAJOR_URBAN_AXIS:
			return DEFAULT_MAJOR_URBAN_ROAD_TYPE;
		case SECONDARY_ROAD:
			return DEFAULT_SECONDARY_ROAD_TYPE;
		case MAJOR_ROAD:
			return DEFAULT_MAJOR_ROAD_TYPE;
		case FREEWAY:
			return DEFAULT_FREEWAY_ROAD_TYPE;
		default:
		}
		throw new IllegalArgumentException();
	}

	/** Replies the system default string-values for the traffic direction.
	 *
	 * @param direction is the direction for which the string values should be replied
	 * @return the string-values, never <code>null</code>
	 */
	@Pure
	public static List<String> getSystemDefaults(TrafficDirection direction) {
		final List<String> array = new ArrayList<>();
		try {
			int i = 0;
			while (true) {
				array.add(getSystemDefault(direction, i));
				++i;
			}
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable exception) {
			//
		}
		return array;
	}

	/** Set the preferred value of traffic direction
	 * used in the attributes for the traffic direction on the roads.
	 *
	 * @param direction a direction.
	 * @param index is the index of the supported string to reply
	 * @param value is the preferred name for the traffic direction on the roads.
	 */
	public static void setPreferredAttributeValueForTrafficDirection(TrafficDirection direction, int index, String value) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			final StringBuilder keyName = new StringBuilder();
			keyName.append("TRAFFIC_DIRECTION_VALUE_"); //$NON-NLS-1$
			keyName.append(direction.name());
			keyName.append("_"); //$NON-NLS-1$
			keyName.append(index);
			String sysDef;
			try {
				sysDef = getSystemDefault(direction, index);
			} catch (IndexOutOfBoundsException exception) {
				sysDef = null;
			}
			if (value == null || "".equals(value) //$NON-NLS-1$
					|| (sysDef != null && sysDef.equalsIgnoreCase(value))) {
				prefs.remove(keyName.toString());
				return;
			}
			prefs.put(keyName.toString(), value);
		}
	}

	/** Set the preferred values of traffic direction
	 * used in the attributes for the traffic direction on the roads.
	 *
	 * @param direction a direction
	 * @param values are the values for the given direction.
	 */
	public static void setPreferredAttributeValuesForTrafficDirection(TrafficDirection direction, String... values) {
		setPreferredAttributeValuesForTrafficDirection(direction, Arrays.asList(values));
	}

	/** Set the preferred values of traffic direction
	 * used in the attributes for the traffic direction on the roads.
	 *
	 * @param direction a direction
	 * @param values are the values for the given direction.
	 */
	public static void setPreferredAttributeValuesForTrafficDirection(TrafficDirection direction, List<String> values) {
		int i = 0;
		for (final String value : values) {
			setPreferredAttributeValueForTrafficDirection(direction, i, value);
			++i;
		}
		setPreferredAttributeValueForTrafficDirection(direction, i, null);
	}

	/** Replies the preferred distance below which roads may be connected.
	 *
	 * @return the preferred distance (in meters) below which roads may be connected.
	 */
	@Pure
	public static double getPreferredRoadConnectionDistance() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.getDouble("ROAD_CONNECTION_DISTANCE", DEFAULT_ROAD_CONNECTION_DISTANCE); //$NON-NLS-1$
		}
		return DEFAULT_ROAD_CONNECTION_DISTANCE;
	}

	/** Set the preferred distance below which roads may be connected.
	 *
	 * @param distance is the preferred distance (in meters) below which roads may be connected.
	 */
	public static void setPreferredRoadConnectionDistance(double distance) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (distance <= 0.) {
				prefs.remove("ROAD_CONNECTION_DISTANCE"); //$NON-NLS-1$
			} else {
				prefs.putDouble("ROAD_CONNECTION_DISTANCE", distance); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred type of road segment.
	 *
	 * @return the preferred type of road segment.
	 */
	@Pure
	public static RoadType getPreferredRoadType() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		String name = null;
		if (prefs != null) {
			name = prefs.get("ROAD_TYPE", null); //$NON-NLS-1$
		}
		if (name != null) {
			RoadType type;
			try {
				type = RoadType.valueOf(name);
			} catch (Throwable exception) {
				type = null;
			}
			if (type != null) {
				return type;
			}
		}
		return RoadType.OTHER;
	}

	/** Set the preferred type of road segment.
	 *
	 * @param type is the preferred type of road segment.
	 */
	public static void setPreferredRoadType(RoadType type) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (type == null) {
				prefs.remove("ROAD_TYPE"); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_TYPE", type.name()); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred name for the types of the roads.
	 *
	 * @return the preferred name for the types of the roads.
	 * @see #DEFAULT_ATTR_ROAD_TYPE
	 */
	@Pure
	public static String getPreferredAttributeNameForRoadType() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.get("ROAD_TYPE_ATTR_NAME", DEFAULT_ATTR_ROAD_TYPE); //$NON-NLS-1$
		}
		return DEFAULT_ATTR_ROAD_TYPE;
	}

	/** Set the preferred name for the types of the roads.
	 *
	 * @param name is the preferred name for the types of the roads.
	 * @see #DEFAULT_ATTR_ROAD_TYPE
	 */
	public static void setPreferredAttributeNameForRoadType(String name) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (name == null || "".equals(name) || DEFAULT_ATTR_ROAD_TYPE.equalsIgnoreCase(name)) { //$NON-NLS-1$
				prefs.remove("ROAD_TYPE_ATTR_NAME"); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_TYPE_ATTR_NAME", name); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred value of road type
	 * used in the attributes for the types of the roads.
	 *
	 * @param type a type
	 * @return the preferred name for the given type of the roads.
	 */
	@Pure
	public static String getPreferredAttributeValueForRoadType(RoadType type) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			final String v = prefs.get("ROAD_TYPE_VALUE_" + type.name(), null); //$NON-NLS-1$
			if (v != null && !"".equals(v)) { //$NON-NLS-1$
				return v;
			}
		}
		return getSystemDefault(type);
	}

	/** Set the preferred value of road type
	 * used in the attributes for the types of the roads.
	 *
	 * @param type a type
	 * @param value is the preferred name for the types of the roads.
	 */
	public static void setPreferredAttributeValueForRoadType(RoadType type, String value) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			final String sysDef = getSystemDefault(type);
			if (value == null || "".equals(value) || sysDef.equalsIgnoreCase(value)) { //$NON-NLS-1$
				prefs.remove("ROAD_TYPE_VALUE_" + type.name()); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_TYPE_VALUE_" + type.name(), value); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred name for the name of the roads.
	 *
	 * @return the preferred name for the name of the roads.
	 * @see #DEFAULT_ATTR_ROAD_NAME
	 */
	@Pure
	public static String getPreferredAttributeNameForRoadName() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.get("ROAD_NAME_ATTR_NAME", DEFAULT_ATTR_ROAD_NAME); //$NON-NLS-1$
		}
		return DEFAULT_ATTR_ROAD_NAME;
	}

	/** Set the preferred name for the name of the roads.
	 *
	 * @param name is the preferred name for the name of the roads.
	 * @see #DEFAULT_ATTR_ROAD_NAME
	 */
	public static void setPreferredAttributeNameForRoadName(String name) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (name == null || "".equals(name) || DEFAULT_ATTR_ROAD_NAME.equalsIgnoreCase(name)) { //$NON-NLS-1$
				prefs.remove("ROAD_NAME_ATTR_NAME"); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_NAME_ATTR_NAME", name); //$NON-NLS-1$
			}
		}
	}

	/** Replies the preferred name for the numbers of the roads.
	 *
	 * @return the preferred name for the numbers of the roads.
	 * @see #DEFAULT_ATTR_ROAD_NUMBER
	 */
	@Pure
	public static String getPreferredAttributeNameForRoadNumber() {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			return prefs.get("ROAD_NUMBER_ATTR_NAME", DEFAULT_ATTR_ROAD_NUMBER); //$NON-NLS-1$
		}
		return DEFAULT_ATTR_ROAD_NUMBER;
	}

	/** Set the preferred name for the numbers of the roads.
	 *
	 * @param name is the preferred name for the numbers of the roads.
	 * @see #DEFAULT_ATTR_ROAD_NUMBER
	 */
	public static void setPreferredAttributeNameForRoadNumber(String name) {
		final Preferences prefs = Preferences.userNodeForPackage(RoadNetworkConstants.class);
		if (prefs != null) {
			if (name == null || "".equals(name) || DEFAULT_ATTR_ROAD_NUMBER.equalsIgnoreCase(name)) { //$NON-NLS-1$
				prefs.remove("ROAD_NUMBER_ATTR_NAME"); //$NON-NLS-1$
			} else {
				prefs.put("ROAD_NUMBER_ATTR_NAME", name); //$NON-NLS-1$
			}
		}
	}

}
