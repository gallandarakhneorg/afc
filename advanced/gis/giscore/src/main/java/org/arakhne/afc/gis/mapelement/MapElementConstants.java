/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.gis.mapelement;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.ColorNames;

/**
 * Set of global constants for map elements.
 *
 * <p>* The constants may be changed according to user preferences.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class MapElementConstants {

	/** Distance for considering two points as at same location.
	 */
	public static final double POINT_FUSION_DISTANCE = 1e-2;

	/** Squared distance for considering two points as at same location.
	 */
	public static final double POINT_FUSION_SQUARED_DISTANCE = POINT_FUSION_DISTANCE * POINT_FUSION_DISTANCE;

	/** Default radius of a map circle.
	 */
	public static final double DEFAULT_RADIUS = 5.;

	/** Default color of a map elements.
	 */
	public static final int DEFAULT_COLOR = ColorNames.getColorFromName("darkgray"); //$NON-NLS-1$

	private MapElementConstants() {
		//
	}

	/** Replies the default radius for map elements.
	 *
	 * @return the default radius for map elements.
	 */
	@Pure
	public static double getPreferredRadius() {
		final Preferences prefs = Preferences.userNodeForPackage(MapElementConstants.class);
		if (prefs != null) {
			return prefs.getDouble("RADIUS", DEFAULT_RADIUS); //$NON-NLS-1$
		}
		return DEFAULT_RADIUS;
	}

	/** Set the default radius for map elements.
	 *
	 * @param radius is the default radius for map elements.
	 */
	public static void setPreferredRadius(double radius) {
		assert !Double.isNaN(radius);
		final Preferences prefs = Preferences.userNodeForPackage(MapElementConstants.class);
		if (prefs != null) {
			prefs.putDouble("RADIUS", radius); //$NON-NLS-1$
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

	/** Replies the default color for map elements.
	 *
	 * @return the default color for map elements.
	 */
	@Pure
	public static int getPreferredColor() {
		final Preferences prefs = Preferences.userNodeForPackage(MapElementConstants.class);
		if (prefs != null) {
			return prefs.getInt("COLOR", DEFAULT_COLOR); //$NON-NLS-1$
		}
		return DEFAULT_COLOR;
	}

	/** Set the default color for map elements.
	 *
	 * @param color is the default color for map elements.
	 */
	public static void setPreferredColor(int color) {
		final Preferences prefs = Preferences.userNodeForPackage(MapElementConstants.class);
		if (prefs != null) {
			prefs.putInt("COLOR", color); //$NON-NLS-1$
			try {
				prefs.flush();
			} catch (BackingStoreException exception) {
				//
			}
		}
	}

}
