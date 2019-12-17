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

package org.arakhne.afc.gis.bus.network;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Describes all the possible reasons of the invalidity
 * of a bus primitive.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum BusPrimitiveInvalidityType {

	/** The validity is not checked.
	 */
	VALIDITY_NOT_CHECKED,

	/** This is a generic error.
	 */
	GENERIC_ERROR,

	/** The position of a bus stop is not set.
	 */
	NO_STOP_POSITION,

	/** The stop is outside the bounds of the map.
	 */
	OUTSIDE_MAP_BOUNDS,

	/** The bus stop is not inside a bus network.
	 */
	STOP_NOT_IN_NETWORK,

	/** No bus stop in a bus hub.
	 */
	NO_STOP_IN_HUB,

	/** Associated bus stop is invalid.
	 */
	INVALID_LINKED_STOP,

	/** Two primitives are not stored inside the same
	 * bus network.
	 */
	NOT_IN_SAME_NETWORK,

	/** A bus itinerary halt is not inside an bus itinerary.
	 */
	HALT_NOT_IN_ITINERARY,

	/** No bus stop associated to a bus halt.
	 */
	NO_STOP_IN_HALT,

	/** The bus itinerary halt is not mapped to a
	 * road segment.
	 */
	HALT_NOT_ON_ROAD_SEGMENT,

	/** Invalid curviline position of the bus halt.
	 */
	INVALID_CURVILINE_POSITION,

	/** No road segment attached to the itinerary.
	 */
	NO_ROAD_SEGMENT_IN_ITINERARY,

	/** The bus itinerary has a discontinuous path.
	 */
	DISCONTINUOUS_PATH_IN_ITINERARY,

	/** Not enough valid bus halts.
	 */
	NOT_ENOUGH_VALID_BUS_HALTS,

	/** The bus itinerary contains an invalid halt.
	 */
	INVALID_HALT_IN_ITINERARY,

	/** The order of the bus halts is valid.
	 */
	INVALID_HALT_ORDER,

	/** The itinerary contains road segment unused
	 * beyond terminus halts.
	 */
	UNUSED_ROAD_SEGMENT_BEYOND_TERMINUS,

	/** No bus itinerary in a bus line.
	 */
	NO_ITINERARY_IN_LINE,

	/** Invalid bus itinerary in the bus line.
	 */
	INVALID_ITINERARY_IN_LINE,

	/** Not enough valid bus stops.
	 */
	NOT_ENOUGH_VALID_BUS_STOPS,

	/** Invalid stop in the bus network.
	 */
	INVALID_STOP_IN_NETWORK,

	/** Invalid hub in the bus network.
	 */
	INVALID_HUB_IN_NETWORK,

	/** No bus line in a bus network.
	 */
	NO_LINE_IN_NETWORK,

	/** Invalid bus line in the bus network.
	 */
	INVALID_LINE_IN_NETWORK,

	/** The primitive is not inside a bus network.
	 */
	NO_BUS_NETWORK,

	/** The bus network is not linked to a road network.
	 */
	NO_ROAD_NETWORK,

	/** The segment is outside the road network linked
	 * to the bus network.
	 */
	SEGMENT_OUTSIDE_ROAD_NETWORK;


	/** Replies the localized message of the reason.
	 *
	 * @param complementaryInformation contains additional information to put in
	 *     the error message when possible.
	 * @return the localized message of the reason.
	 */
	@Pure
	public String getLocalizedMessage(String complementaryInformation) {
		return Locale.getString(name(), ordinal(), complementaryInformation);
	}

}
