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

package org.arakhne.afc.gis.bus.layer;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Constants for bus network layers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class BusLayerConstants {

	/** The default selection color.
	 */
	public static final int DEFAULT_SELECTION_COLOR = 0x00FF00;

	/** Default flag that is indicating if the bus stops
	 * which are not binded to a bus halt are drawn by default.
	 */
	public static final boolean DEFAULT_NO_BUS_HALT_BIND = true;

	/** Default flag that is indicating if the bus stops
	 * which are not binded to a bus halt have their names drawn by default.
	 */
	public static final boolean DEFAULT_NO_BUS_HALT_BIND_NAMES = true;

	/** Default flag that is indicating if the bus stops
	 * are drawn by default.
	 */
	public static final boolean DEFAULT_BUS_STOP_DRAWING = true;

	/** Default flag that is indicating if the bus stops Names
	 * are drawn by default.
	 */
	public static final boolean DEFAULT_BUS_STOP_NAMES_DRAWING = true;

	/** Does the bus layers are exhibiting the attributes of
	 * the displayed bus network components.
	 */
	public static final boolean DEFAULT_ATTRIBUTE_EXHIBITION = true;

	private BusLayerConstants() {
		//
	}

	/** Replies if the bus network should be drawn with the
	 * algorithm.
	 *
	 * @return <code>true</code> if the algorithm may be used,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static BusLayerDrawerType getPreferredLineDrawAlgorithm() {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			final String algo = prefs.get("DRAWING_ALGORITHM", null); //$NON-NLS-1$
			if (algo != null && algo.length() > 0) {
				try {
					return BusLayerDrawerType.valueOf(algo);
				} catch (Throwable exception) {
					//
				}
			}
		}
		return BusLayerDrawerType.OVERLAP;
	}

	/** Set if the bus network should be drawn with the
	 * algorithm.
	 *
	 * @param algorithm indicates if the drawing algorithm.
	 */
	public static void setPreferredLineDrawingAlgorithm(BusLayerDrawerType algorithm) {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			if (algorithm == null) {
				prefs.remove("DRAWING_ALGORITHM"); //$NON-NLS-1$
			} else {
				prefs.put("DRAWING_ALGORITHM", algorithm.name()); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Type of drawer for the bus layers.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public enum BusLayerDrawerType {

		/** Each bus line is drawn on the top of
		 * the other bus lines on the same road segment.
		 */
		OVERLAP,

		/** Each bus line is drawn on the side of the other
		 * bus lines on the same road segment.
		 */
		SHIFT;

	}

	/** Replies if the preferred color for bus stops selection.
	 *
	 * @return Color for selection
	 */
	@Pure
	public static int getSelectionColor() {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			final String str = prefs.get("SELECTION_COLOR", null); //$NON-NLS-1$
			if (str != null) {
				try {
					return Integer.valueOf(str);
				} catch (Throwable exception) {
					//
				}
			}
		}
		return DEFAULT_SELECTION_COLOR;
	}

	/** Set the default color selection.
	 *
	 * @param color is the default color for selection.
	 */
	public static void setSelectionColor(Integer color) {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			if (color == null || color == DEFAULT_SELECTION_COLOR) {
				prefs.remove("SELECTION_COLOR"); //$NON-NLS-1$
			} else {
				prefs.put("SELECTION_COLOR", color.toString()); //$NON-NLS-1$
			}
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}


	/** Replies if the bus stops should be drawn or not.
	 *
	 * @return <code>true</code> if the bus stops are drawable;
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean isBusStopDrawable() {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			return prefs.getBoolean("DRAW_BUS_STOPS", DEFAULT_BUS_STOP_DRAWING); //$NON-NLS-1$
		}
		return DEFAULT_BUS_STOP_DRAWING;
	}

	/** Set if the bus stops should be drawn or not.
	 *
	 * @param isDrawable is <code>true</code> if the bus stops are drawable;
	 *      <code>false</code> if the bus stops are not drawable; or <code>null</code>
	 *      if the default value must be restored.
	 */
	public static void setBusStopDrawable(Boolean isDrawable) {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			if (isDrawable == null) {
				prefs.remove("DRAW_BUS_STOPS"); //$NON-NLS-1$
			} else {
				prefs.putBoolean("DRAW_BUS_STOPS", isDrawable.booleanValue()); //$NON-NLS-1$
			}
			try {
				prefs.sync();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies if the bus stops names should be drawn or not.
	 *
	 * @return <code>true</code> if the bus stops are drawable;
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean isBusStopNamesDrawable() {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			return prefs.getBoolean("DRAW_BUS_STOPS_NAMES", DEFAULT_BUS_STOP_NAMES_DRAWING); //$NON-NLS-1$
		}
		return DEFAULT_BUS_STOP_NAMES_DRAWING;
	}

	/** Set if the bus stops should be drawn or not.
	 *
	 * @param isNamesDrawable is <code>true</code> if the bus stops	names are drawable;
	 *     <code>false</code> if the bus stops names are not drawable; or <code>null</code>
	 *     if the default value must be restored.
	 */
	public static void setBusStopNamesDrawable(Boolean isNamesDrawable) {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			if (isNamesDrawable == null) {
				prefs.remove("DRAW_BUS_STOPS_NAMES"); //$NON-NLS-1$
			} else {
				prefs.putBoolean("DRAW_BUS_STOPS_NAMES", isNamesDrawable.booleanValue()); //$NON-NLS-1$
			}
			try {
				prefs.sync();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies if the bus stops with no binding to bus halt
	 * should be drawn or not.
	 *
	 * @return <code>true</code> if the bus stops name with no binding are drawable;
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean isBusStopNoHaltBindingDrawable() {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			return prefs.getBoolean("DRAW_NO_BUS_HALT_BIND", DEFAULT_NO_BUS_HALT_BIND); //$NON-NLS-1$
		}
		return DEFAULT_NO_BUS_HALT_BIND;
	}

	/** Set if the bus stops stops with no binding to bus halt
	 * should be drawn or not.
	 *
	 * @param isDrawable is <code>true</code> if the bus stops name with no binding are drawable;
	 *     <code>false</code> if the bus stops name with no binding are not drawable; or <code>null</code>
	 *     if the default value must be restored.
	 */
	public static void setBusStopNoHaltBindingDrawable(Boolean isDrawable) {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			if (isDrawable == null) {
				prefs.remove("DRAW_NO_BUS_HALT_BIND"); //$NON-NLS-1$
			} else {
				prefs.putBoolean("DRAW_NO_BUS_HALT_BIND", isDrawable.booleanValue()); //$NON-NLS-1$
			}
			try {
				prefs.sync();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies if the bus stops with no binding to bus halt
	 * should have their names drawn or not.
	 *
	 * @return <code>true</code> if the bus stops name with no binding are drawable;
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean isBusStopNoHaltBindingNamesDrawable() {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			return prefs.getBoolean("DRAW_NO_BUS_HALT_BIND_NAMES", DEFAULT_NO_BUS_HALT_BIND_NAMES); //$NON-NLS-1$
		}
		return DEFAULT_NO_BUS_HALT_BIND_NAMES;
	}

	/** Set if the bus stops stops with no binding to bus halt
	 * should have their names drawn or not.
	 *
	 * @param isDrawable is <code>true</code> if the bus stops name with no binding are drawable;
	 *     <code>false</code> if the bus stops name with no binding are not drawable; or <code>null</code>
	 *     if the default value must be restored.
	 */
	public static void setBusStopNoHaltBindingNamesDrawable(Boolean isDrawable) {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			if (isDrawable == null) {
				prefs.remove("DRAW_NO_BUS_HALT_BIND_NAMES"); //$NON-NLS-1$
			} else {
				prefs.putBoolean("DRAW_NO_BUS_HALT_BIND_NAMES", isDrawable.booleanValue()); //$NON-NLS-1$
			}
			try {
				prefs.sync();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies if the attributes of the bus network elements
	 * should be exhibited by the layers that are displaying
	 * them.
	 * For example, a BusLineLayer may exhibit the BusLine
	 * attributes.
	 *
	 * @return <code>true</code> if the bus stops are drawable;
	 *     otherwise <code>false</code>
	 */
	@Pure
	public static boolean isAttributeExhibitable() {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			return prefs.getBoolean("EXHIBIT_ATTRIBUTES", DEFAULT_ATTRIBUTE_EXHIBITION); //$NON-NLS-1$
		}
		return DEFAULT_ATTRIBUTE_EXHIBITION;
	}

	/** Set if the attributes of the bus network elements
	 * should be exhibited by the layers that are displaying
	 * them.
	 * For example, a BusLineLayer may exhibit the BusLine
	 * attributes.
	 *
	 * @param isExhibit is <code>true</code> if the attributes are exhibited by the layers;
	 *     <code>false</code> if the attributes are not exhibited; or <code>null</code>
	 *     if the default value must be restored.
	 */
	public static void setAttributeExhibitable(Boolean isExhibit) {
		final Preferences prefs = Preferences.userNodeForPackage(BusLayerConstants.class);
		if (prefs != null) {
			if (isExhibit == null) {
				prefs.remove("EXHIBIT_ATTRIBUTES"); //$NON-NLS-1$
			} else {
				prefs.putBoolean("EXHIBIT_ATTRIBUTES", isExhibit.booleanValue()); //$NON-NLS-1$
			}
			try {
				prefs.sync();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

}
